package com.yuan.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Component
@EnableWebSocket
public class CusWebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {

    @Autowired
    private FriendWebSocketHandler friendWebSocketHandler;

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    //握手请求
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {

        //添加websocket处理器，添加握手拦截器  .setAllowedOrigins("*")    注册一个握手接口
        webSocketHandlerRegistry.addHandler(friendWebSocketHandler, "/fws").setAllowedOrigins("*").addInterceptors(new CusHandShakeInterceptor(CusHandShakeInterceptor.SOCKET_TYPE_FRIEND));

        webSocketHandlerRegistry.addHandler(activityWebSocketHandler, "/aws").setAllowedOrigins("*").addInterceptors(new CusHandShakeInterceptor(CusHandShakeInterceptor.SOCKET_TYPE_ACTIVITY));


    }


}
