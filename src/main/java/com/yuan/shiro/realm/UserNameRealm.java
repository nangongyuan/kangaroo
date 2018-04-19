package com.yuan.shiro.realm;

import com.yuan.dao.UserDao;
import com.yuan.entity.UserDO;
import com.yuan.shiro.ShiroUser;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.sound.midi.Soundbank;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.MemorySessionDAO;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;

public class UserNameRealm extends AuthorizingRealm {

    @Autowired
    private UserDao userDao;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(
            PrincipalCollection principals) {
        ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",shiroUser.getId());
        UserDO user =  userDao.listUserDO(map,1,1).get(0);
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> set = new HashSet<String>();
        if (user.getType()==1){
            set.add("1");
        }else if (user.getType()==0){
            set.add("0");
            set.add("1");
        }
        authorizationInfo.setRoles(set);
        return authorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", username);
        List<UserDO> users = userDao.listUserDO(map, 1, 1);
        if (users == null || users.size() <= 0) {
            try {
                int uid = Integer.parseInt(username);
                map.remove("name");
                map.put("id", uid);
                users = userDao.listUserDO(map, 1, 1);
            } catch (NumberFormatException e) {
                throw new UnknownAccountException();
            }
            if (users == null || users.size() <= 0) {
                throw new UnknownAccountException();
            }
        }
        UserDO userDO = users.get(0);
        if (Boolean.TRUE.equals(userDO.getLocked() == 1)) {
            throw new LockedAccountException(); // 帐号锁定
        }


        ShiroUser shiroUser = new ShiroUser(userDO.getId(), userDO.getType(), userDO.getName(), userDO.getHeadpic());



        //清除该用户在其他地方登录的session
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        DefaultWebSessionManager sessionManager = (DefaultWebSessionManager)securityManager.getSessionManager();


        //获取当前已登录的用户session列表
        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();
        for(Session session:sessions){
            String temp = String.valueOf(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
            if (temp.indexOf(shiroUser.getName())>=0){
                sessionManager.getSessionDAO().delete(session);
            }
        }

        // 认证缓存信息
        return new SimpleAuthenticationInfo(shiroUser, userDO.getPassword(), ByteSource.Util.bytes(userDO.getSalt()), getName());
    }

}
