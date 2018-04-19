package com.yuan.shiro;

import com.yuan.dao.ResourceDao;
import com.yuan.entity.ResourceDO;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.config.Ini;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: kangaroo
 * @description: Shiro对url的权限管理
 * @author: yuan
 * @create: 2018-04-05 12:21
 **/

public class ShiroPermissionFactory extends ShiroFilterFactoryBean {

    @Autowired
    private ResourceDao resourceDao;
    /**
     * 记录配置中的过滤链
     */
    public static String definition = "";


    /**
     * 初始化设置过滤链
     */
    @Override
    public void setFilterChainDefinitions(String definitions) {
//        String token =  manageUserService.getAdminToken(0);

        List<ResourceDO> list = resourceDao.listResource();
        Map<String, String> otherChains = new LinkedHashMap<String, String>();    //保留插入的顺序
        for (ResourceDO resouce : list){
            if (resouce.getPrivilege()==0){
                otherChains.put(resouce.getUrl(), "aotuLoginAndRedirectFilter,roles[0]"); //    [0,1]同时拥有0 1角色才能访问
            }else if(resouce.getPrivilege()==1){
                otherChains.put(resouce.getUrl(), "aotuLoginAndRedirectFilter,roles[1]");
            }
        }
        //加载配置默认的过滤链
        Ini ini = new Ini();
        ini.load(definitions);
        Ini.Section section = ini.getSection(IniFilterChainResolverFactory.URLS);
        if (CollectionUtils.isEmpty(section)) {
            section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
        }
        //加上数据库中过滤链
        section.putAll(otherChains);
        section.put("/**","anon");
        setFilterChainDefinitionMap(section);
    }

}