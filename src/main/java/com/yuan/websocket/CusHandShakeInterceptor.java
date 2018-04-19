package com.yuan.websocket;

import com.yuan.shiro.ShiroUser;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;


import static com.yuan.constant.SessionConstant.*;

/**
 * @program: kangaroo
 * @description: websocket握手前和握手后的处理
 * @author: yuan
 * @create: 2018-03-29 14:06
 **/

public class CusHandShakeInterceptor implements HandshakeInterceptor {

    public static final int SOCKET_TYPE_FRIEND = 1;
    public static final int SOCKET_TYPE_ACTIVITY = 2;

    //判断握手请求是好友还是活动房间
    private int type;


    public CusHandShakeInterceptor(int type) {
        this.type = type;
    }

    //握手前
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (type == SOCKET_TYPE_FRIEND) {       //好友socket
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            if (shiroUser==null)
                return false;
            map.put(SESSION_USER, shiroUser);
        } else {               //活动socket
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            HttpSession session = servletRequest.getSession();
            Integer aid = (Integer) session.getAttribute(SESSION_AID);
            Boolean screen = (Boolean) session.getAttribute(SESSION_SCREEN);
            if (aid==null)
                return false;
            map.put(SESSION_AID, aid);
            if (screen != null) {      //大屏幕连接
                map.put(SESSION_SCREEN, screen);
            } else {
                ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
                if (shiroUser==null)
                    return false;
                map.put(SESSION_USER, shiroUser);
            }
        }
        return true;
    }

    //握手后
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
