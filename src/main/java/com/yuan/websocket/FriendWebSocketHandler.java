package com.yuan.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.dao.ChattingRecordDao;
import com.yuan.dao.FriendDao;
import com.yuan.dao.FriendRequestDao;
import com.yuan.dao.UserDao;
import com.yuan.dto.RemindDTO;
import com.yuan.dto.RobotMsgDTO;
import com.yuan.dto.SocketDTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.ChattingRecordDO;
import com.yuan.entity.FriendRequestDO;
import com.yuan.shiro.ShiroUser;
import com.yuan.util.OtherUtil;
import com.yuan.web.UserController;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


import static com.yuan.constant.OtherConstant.TU_LING_API;
import static com.yuan.constant.OtherConstant.TU_LING_KEY;
import static com.yuan.constant.OtherConstant.XIAO_DAI_USER_ID;
import static com.yuan.constant.SessionConstant.*;
import static com.yuan.constant.SocketMsgConstant.FRIEND_AGREE;
import static com.yuan.constant.SocketMsgConstant.FRIEND_MESSAGE;
import static com.yuan.constant.SocketMsgConstant.FRIEND_REQUEST;

/**
 * @program: kangaroo
 * @description: 处理好友的webSocket聊天
 * @author: yuan
 * @create: 2018-03-29 14:06
 **/

@Component
public class FriendWebSocketHandler implements WebSocketHandler {

    @Autowired
    private UserDao userDao;

    @Autowired
    private ChattingRecordDao chattingRecordDao;

    @Autowired
    private FriendDao friendDao;

    //保存所有用户的session
    private static final Map<Integer, WebSocketSession> userSocketSessionMap;

    static {
        userSocketSessionMap = new HashMap<Integer, WebSocketSession>();
    }

    /**
     * @Description: 成功连接 ，获取用户信息，将用户与webSocketSession记录
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        ShiroUser shiroUser = (ShiroUser) webSocketSession.getAttributes().get(SESSION_USER);
        userSocketSessionMap.put(shiroUser.getId(), webSocketSession);
    }

    /**
     * @Description: 接受消息
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        if (webSocketMessage.getPayloadLength() == 0) return;

        String json = webSocketMessage.getPayload().toString();
        
        System.out.println(json);
        
        try {
            SocketDTO socketDTO = new ObjectMapper().readValue(json, SocketDTO.class);
            //查询发送人的信息
            ShiroUser shiroUser = (ShiroUser) webSocketSession.getAttributes().get(SESSION_USER);
            UserDTO userDTO = new UserDTO();
            userDTO.setId(shiroUser.getId());
            userDTO.setName(shiroUser.getName());
            userDTO.setHeadpic(shiroUser.getHeadpic());

            //发给系统号
            if (socketDTO.getReceiver() == XIAO_DAI_USER_ID && socketDTO.getType() == FRIEND_MESSAGE) {
                //保存聊天记录
                chattingRecordDao.saveChattingRecord(new ChattingRecordDO(userDTO.getId(),
                        socketDTO.getReceiver(), socketDTO.getType(), (String) socketDTO.getData(), 1));
                //更新最近的聊天时间
                friendDao.updatRecentlychat(userDTO.getId(), socketDTO.getReceiver());
                socketDTO.setSender(userDTO);
                if (!robotHandler(socketDTO))
                    return;
            } else {
                //转发消息
                SocketDTO data = new SocketDTO(socketDTO.getType(), userDTO, socketDTO.getReceiver(), socketDTO.getData());
                sendMessageToUser(socketDTO.getReceiver(), data);
            }

            //在数据库中保存
            chattingRecordDao.saveChattingRecord(new ChattingRecordDO(userDTO.getId(),
                    socketDTO.getReceiver(), socketDTO.getType(), (String) socketDTO.getData(), 0));
            friendDao.updatRecentlychat(userDTO.getId(), socketDTO.getReceiver());
        } catch (IOException e) {
            sendJson(webSocketSession, "数据格式错误");
            e.printStackTrace();
        }

    }

    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {
    }

    /**
     * 在此刷新页面就相当于断开WebSocket连接,原本在静态变量userSocketSessionMap中的
     * WebSocketSession会变成关闭状态(close)，但是刷新后的第二次连接服务器创建的
     * 新WebSocketSession(open状态)又不会加入到userSocketSessionMap中,所以这样就无法发送消息
     * 因此应当在关闭连接这个切面增加去除userSocketSessionMap中当前处于close状态的WebSocketSession，
     * 让新创建的WebSocketSession(open状态)可以加入到userSocketSessionMap中
     *
     * @param webSocketSession
     * @param closeStatus
     * @throws Exception
     */
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {
        ShiroUser shiroUser = (ShiroUser) webSocketSession.getAttributes().get(SESSION_USER);
        userSocketSessionMap.remove(shiroUser.getId());

    }

    public boolean supportsPartialMessages() {
        return false;
    }

    private Boolean sendJson(WebSocketSession session, Object o) {
        try {
            String msg = new ObjectMapper().writeValueAsString(o);
            session.sendMessage(new TextMessage(msg));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //发送信息的实现
    public Boolean sendMessageToUser(int receiver, SocketDTO socketDTO) {
        WebSocketSession session = userSocketSessionMap.get(receiver);
        if (session != null && session.isOpen()) {
            if (sendJson(session, socketDTO)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 发送好友请求
     * @Param: 好友id
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public Boolean sendFriendRequest(int uid) {
        ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
        WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", shiroUser.getId());
            List<UserDTO> list = userDao.listUserDTO(map, 1, 1);
            if (list.size()>0 && sendJson(session, new SocketDTO(FRIEND_REQUEST, list.get(0), uid, null))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 好友请求被同意的通知
     * @Param: 被通知的id
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public Boolean sendAgreeFriend(int uid) {
        Boolean ret = false;
        WebSocketSession session = userSocketSessionMap.get(uid);
        if (session != null && session.isOpen()) {
            if (sendJson(session, new SocketDTO(FRIEND_AGREE, 0, uid, null))) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * @Description: 系统通知消息
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/2 0002
     */
    public void sysNotification(Integer type, String msg, Integer receiver) {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("id",XIAO_DAI_USER_ID);
        List<UserDTO> list = userDao.listUserDTO(map,1,1);
        if (list.size()!=1)
            return;
        UserDTO userDTO = list.get(0);
        
        //转发消息
        SocketDTO data = new SocketDTO(type, userDTO, receiver, msg);
        sendMessageToUser(receiver, data);

        //在数据库中保存
        chattingRecordDao.saveChattingRecord(new ChattingRecordDO(userDTO.getId(),
                receiver, type, msg, 0));
        friendDao.updatRecentlychat(userDTO.getId(), receiver);
    }

    /** 
    * @Description: 调用图灵机器人智能回复
    * @Param:  
    * @return:  
    * @Author: yuan 
    * @Date: 2018/4/11 0011 
    */ 
    private boolean robotHandler(SocketDTO socketDTO) {
        String msg = (String) socketDTO.getData();
        String result = OtherUtil.sendPost(TU_LING_API, TU_LING_KEY+"&info=" + msg);
        System.out.println(result);
        int code = Integer.parseInt(result.substring(8, 14));
        if (code == 100000) {
            try {
                RobotMsgDTO readValue = new ObjectMapper().readValue(result, RobotMsgDTO.class);
                UserDTO userDTO = (UserDTO) socketDTO.getSender();
                socketDTO.setReceiver(userDTO.getId());
                Map<String,Object> map = new HashMap<String, Object>();
                map.put("id",XIAO_DAI_USER_ID);
                userDTO = userDao.listUserDTO(map,1,1).get(0);
                socketDTO.setSender(userDTO);
                socketDTO.setData(readValue.getText());
                sendMessageToUser(socketDTO.getReceiver(), socketDTO);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Collection<WebSocketSession> getAllWebSockets(){
        return userSocketSessionMap.values();
    }
}
