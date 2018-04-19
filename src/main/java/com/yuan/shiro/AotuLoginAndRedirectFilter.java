package com.yuan.shiro;

import com.yuan.dao.UserDao;
import com.yuan.entity.UserDO;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: kangaroo
 * @description: 自定义FormAuthenticationFilter
 * @author: yuan
 * @create: 2018-04-06 13:44
 **/

public class AotuLoginAndRedirectFilter extends AccessControlFilter {

    @Autowired
    private UserDao userDao;

    //判断是否允许访问      如果允许访问返回 true，否则 false
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()){
            return true;
        }else if (subject.isRemembered()){
            try {
                ShiroUser shiroUser = (ShiroUser) subject.getPrincipal();
                if (null != shiroUser) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("id", shiroUser.getId());
                    List<UserDO> users = userDao.listUserDO(map, 1, 1);
                    if (users!=null && users.size()>0){
                        String password = users.get(0).getPassword();
                        UsernamePasswordToken token = new UsernamePasswordToken(shiroUser.getName(), password);
                        token.setRememberMe(true);
                        subject.login(token);//登录
                        return true;
                    }
                }
            }catch (Exception e){
                return false;
            }
        }
        return false;
    }


    // 表示当访问拒绝时是否已经处理了；如果返回 true 表示需要继续处理；如果返回 false 表示该拦截器实例已经处理了
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String requestType = ((HttpServletRequest) servletRequest).getHeader("X-Requested-With");
        if("XMLHttpRequest".equals(requestType)){
            try {
                servletResponse.setCharacterEncoding("utf-8");
                PrintWriter out = servletResponse.getWriter();
                servletResponse.setContentType("application/json; charset=utf-8");
                out.print("{\"status\":302}");
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            WebUtils.redirectToSavedRequest(servletRequest,servletResponse,"/kangaroo/Mobile-terminal/signin-reg/login.html");
        }
        return true;
    }
}