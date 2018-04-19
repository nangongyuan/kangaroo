package com.yuan.task;

import com.yuan.dto.SocketDTO;
import com.yuan.entity.ActivitySocket;
import com.yuan.websocket.ActivityWebSocketHandler;
import com.yuan.websocket.FriendWebSocketHandler;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;


import static com.yuan.constant.SocketMsgConstant.HEART_BEAT_PACKET;

/**
 * @program: kangaroo
 * @description:
 * @author: yuan
 * @create: 2018-04-11 12:19
 **/
@Component
public class HeartBeatTask {

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    @Autowired
    private FriendWebSocketHandler friendWebSocketHandler;

    @Scheduled(cron = "0/55 * * * * ?")
    public void heartBeat(){
        Collection<ActivitySocket> list = activityWebSocketHandler.getAllActivitySocket();
        for (ActivitySocket activitySocket:list){
            activityWebSocketHandler.sendJson(activitySocket.getWebSocketSession(),new SocketDTO(HEART_BEAT_PACKET,null,null,null));
            for (WebSocketSession webSocketSession : activitySocket.getUserWebSocketSessionMap().values()){
                activityWebSocketHandler.sendJson(webSocketSession,new SocketDTO(HEART_BEAT_PACKET,null,null,null));
            }
        }
        for (WebSocketSession webSocketSession: friendWebSocketHandler.getAllWebSockets()){
            activityWebSocketHandler.sendJson(webSocketSession,new SocketDTO(HEART_BEAT_PACKET,null,null,null));
        }
    }
}