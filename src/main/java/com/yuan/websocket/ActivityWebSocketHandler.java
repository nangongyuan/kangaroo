package com.yuan.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yuan.dao.ActivityDao;
import com.yuan.dao.BarrageDao;
import com.yuan.dao.UserDao;
import com.yuan.dto.SocketDTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.ActivityDO;
import com.yuan.entity.ActivitySocket;
import com.yuan.entity.BarrageDO;
import com.yuan.entity.UserDO;
import com.yuan.service.IGradeService;
import com.yuan.service.ILotteryService;
import com.yuan.service.IRedPacketService;
import com.yuan.service.ISignService;
import com.yuan.service.IVoteService;
import com.yuan.shiro.ShiroUser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;


import static com.yuan.constant.OtherConstant.*;
import static com.yuan.constant.SessionConstant.*;
import static com.yuan.constant.SocketMsgConstant.*;


/**
 * @program: kangaroo
 * @description: 处理活动的webSocket
 * @author: yuan
 * @create: 2018-03-29 14:06
 **/

@Component
public class ActivityWebSocketHandler implements WebSocketHandler {

    @Autowired
    private BarrageDao barrageDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private IVoteService voteService;

    @Autowired
    private ILotteryService lotteryService;

    @Autowired
    private IRedPacketService redPacketService;

    @Autowired
    private IGradeService gradeService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ISignService signService;

    //活动id，对应活动信息
    private static final Map<Integer, ActivitySocket> activityRooms;

    static {
        activityRooms = new HashMap<Integer, ActivitySocket>();
    }

    /**
     * @Description: 连接成功
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        Boolean screen = (Boolean) webSocketSession.getAttributes().get(SESSION_SCREEN);
        Integer aid = (Integer) webSocketSession.getAttributes().get(SESSION_AID);
        if (screen == null) {     //用户连接
            //获取用户信息
            ShiroUser shiroUser = (ShiroUser) webSocketSession.getAttributes().get(SESSION_USER);

            System.out.println(shiroUser+"进入房间");

            //获取活动信息
            ActivitySocket activitySocket = activityRooms.get(aid);
            //将用户的socket保存到活动信息中
            activitySocket.getUserWebSocketSessionMap().put(shiroUser.getId(), webSocketSession);
            //推送当前用户进入房间
            broadcastJson(aid, new SocketDTO(ROOM_USER_JOIN, null, null, shiroUser.getName()));
            //推送当前人数
            pushOnlineNumber(aid);
            //推送签到
            joinRoomPushSign(aid,shiroUser.getId(),webSocketSession);
            //推送当前发言模式
            sendJson(webSocketSession,new SocketDTO(ROOM_PUSH_SPEAK_MODE,null,null,activitySocket.getSpeakMode()));
        } else {
            //大屏幕连接
            ActivitySocket activitySocket = activityRooms.get(aid);
            activitySocket.setWebSocketSession(webSocketSession);
        }
    }

    /**
     * @Description: 收到消息
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) {
        if (webSocketMessage.getPayloadLength() == 0) return;

        System.out.println(webSocketMessage.getPayload().toString());

        String json = webSocketMessage.getPayload().toString();
        try {
            SocketDTO socketDTO = new ObjectMapper().readValue(json, SocketDTO.class);
            //获取发送人的信息
            ShiroUser shiroUser = (ShiroUser) webSocketSession.getAttributes().get(SESSION_USER);
            if (shiroUser!=null){
                UserDTO userDTO = new UserDTO();
                userDTO.setId(shiroUser.getId());
                userDTO.setName(shiroUser.getName());
                userDTO.setHeadpic(shiroUser.getHeadpic());
                socketDTO.setSender(userDTO);
            }

            switch (socketDTO.getType()) {
                case ROOM_MESSAGE:
                    ordinaryBarrage(socketDTO);           //处理弹幕
                    break;
                case ROOM__PUSH_PIC:                                 //1推送图片
                    picPush(socketDTO);
                    break;
                case ROOM_SHOW_POSTER:                                  //2展示海报
                    showPoster(socketDTO);
                    break;
                case ROOM_SHOW_SIGN_IN:                                 //3大屏幕签到页面
                    showSignIn(socketDTO);
                    break;
                case ROOM_SPONSOR_LOTTERY:                                 //4发起抽奖
                    startLottery(socketDTO);
                    break;
                case ROOM_SPONSOR_VOTE:                                 //5发起投票
                    startVote(socketDTO);
                    break;
                case ROOM_SPONSOR_RED_PACKET:                                 //6发起红包
                    startRedPacket(socketDTO);
                    break;
                case ROOM_SPONSOR_GRADE:                                 //7发起评分
                    startGrade(socketDTO);
                    break;
                case ROOM_USER_VOTE:                                 //用户投票
                    userVote(socketDTO);
                    break;
                case ROOM_UPDE_SPEAK_MODE:                                //发起修改发言模式
                    updateSpeakMode(socketDTO);
                    break;
                case ROOM_USER_GRADE:                             //用户评分
                    userGrade(socketDTO);
                    break;
                case ROOM_USER_RED_PACKET:                          //用户抢红包
                    userRedPacket(socketDTO);
                    break;
                case ROOM_SHOW_LOTTERY_SCREEN:                      //大屏幕播完抽奖动画
                    pushLotteryResult(socketDTO);
                    break;
                case ROOM_PUSH_SHOW_VOTE_SCREEN:                  //大屏幕显示投票结果
                    showVoteResult(socketDTO);
                    break;
                case ROOM_GET_ALL_USERS:                        //获取所有在线的用户
                    listOnlineUser(socketDTO);
                    break;
                case ROOM_PIC_MESSAGE:                          //发送图片消息
                    picBarrage(socketDTO);
                    break;
                case ROOM__PUSH_PIC_CANCEL:               //房主取消推送图片
                    pushPicCancel(socketDTO);
                    break;
                case ROOM_PUSH_SIGN:                    //房主发起签到
                    pushSign(socketDTO);
                    break;
                case ROOM_USER_SIGN:                //用户签到
                    userSign(socketDTO);
                    break;
            }
        } catch (IOException e) {
            sendJson(webSocketSession,"未知错误");
            e.printStackTrace();
        }
    }

    /**
     * @Description: 连接出错
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
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
        Boolean screen = (Boolean) webSocketSession.getAttributes().get(SESSION_SCREEN);
        Integer aid = (Integer) webSocketSession.getAttributes().get(SESSION_AID);
        if (screen == null) {     //用户关闭连接
            ShiroUser shiroUser = (ShiroUser) webSocketSession.getAttributes().get(SESSION_USER);
            activityRooms.get(aid).getUserWebSocketSessionMap().remove(shiroUser.getId());
            //推送当前人数
            pushOnlineNumber(aid);
        } else {        //大屏幕关闭连接
            activityRooms.get(aid).setWebSocketSession(null);
        }
    }

    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * @Description: 发送消息给单个session
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/30 0030
     */
    public Boolean sendJson(WebSocketSession session, Object o) {
        if (session == null || !session.isOpen()) {
            return false;
        }
        try {
            String msg = new ObjectMapper().writeValueAsString(o);
            session.sendMessage(new TextMessage(msg));
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description: 广播消息
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/30 0030
     */
    public void broadcastJson(Integer aid, Object o) {
        Map<Integer, WebSocketSession> map = activityRooms.get(aid).getUserWebSocketSessionMap();
        Collection<WebSocketSession> collection = map.values();
        Iterator<WebSocketSession> iterator = collection.iterator();
        while (iterator.hasNext()) {
            WebSocketSession webSocketSession = iterator.next();
            if (webSocketSession != null && webSocketSession.isOpen()) {
                sendJson(webSocketSession, o);
            }
        }
    }

    /**
     * @Description: 获取活动的当前人数
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public int getActivityPeopleNumber(int aid) {
        ActivitySocket activitySocket = activityRooms.get(aid);
        if (activitySocket.getUserWebSocketSessionMap() != null) {
            return activitySocket.getUserWebSocketSessionMap().size();
        }
        return 0;
    }

    /**
     * @Description: 创建活动
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029
     */
    public void createActivity(int aid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", aid);
        List<ActivityDO> list = activityDao.listActivityDO(map, 1, 1);
        if (list != null && list.size() > 0) {
            ActivityDO activityDO = list.get(0);
            ActivitySocket activitySocket = new ActivitySocket(new HashMap<Integer, WebSocketSession>(),
                    0, activityDO.getUid(), 0);
            activityRooms.put(aid, activitySocket);
            String corrd = activityDO.getCoords();
            if (corrd!=null && corrd.equals("")){
                Double latitude = Double.valueOf(corrd.substring(0,corrd.indexOf("-")));
                Double longitude =Double.valueOf(corrd.substring(corrd.indexOf("-")+1));
                activitySocket.setLongitude(longitude);
                activitySocket.setLatitude(latitude);
            }
        }
    }

    /**
     * @Description: 处理普通弹幕
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/29 0029B
     */
    private void ordinaryBarrage(SocketDTO socketDTO) {
        //判断当前的发言模式
        ActivitySocket activitySocket = activityRooms.get(socketDTO.getReceiver());
        if (activitySocket.getSpeakMode() == SPEAK_MODE_ONE) {
            UserDTO userDTO = (UserDTO) socketDTO.getSender();
            if (!userDTO.getId().equals(activitySocket.getCreater())) {
                return;
            }
        }

        //发送到大屏幕上
        sendJson(activitySocket.getWebSocketSession(), socketDTO);

        //将弹幕写入数据库
        if (barrageDao.saveBarrage(new BarrageDO(socketDTO.getReceiver(),
                ((UserDTO) socketDTO.getSender()).getId(),0,(String) socketDTO.getData())) > 0) {
            //给所有人发送弹幕
            broadcastJson(socketDTO.getReceiver(), socketDTO);
        }
    }

    /**
     * @Description: 投票处理
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/30 0030
     */
    private void startVote(SocketDTO socketDTOt) {
        voteService.startVote(socketDTOt, activityRooms.get(socketDTOt.getReceiver()));
    }

    /**
     * @Description: 用户投票
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/30 0030
     */
    private void userVote(SocketDTO socketDTO) {
        voteService.userVote(socketDTO);
    }

    /**
     * @Description: 向大屏幕推送图片
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    private void picPush(SocketDTO socketDTO) {
        //获得大屏幕的socket
        WebSocketSession webSocketSession = activityRooms.get(socketDTO.getReceiver()).getWebSocketSession();
        sendJson(webSocketSession, socketDTO);
    }

    /**
     * @Description: 展示海报
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    private void showPoster(SocketDTO socketDTO) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", socketDTO.getReceiver());
        List<ActivityDO> list = activityDao.listActivityDO(map, 1, 1);
        if (list != null && list.size() > 0) {
            socketDTO.setData(list.get(0).getPic());
            sendJson(activityRooms.get(socketDTO.getReceiver()).getWebSocketSession(), socketDTO);
        }
    }

    /**
     * @Description: 显示签到页面
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    private void showSignIn(SocketDTO socketDTO) {
        sendJson(activityRooms.get(socketDTO.getReceiver()).getWebSocketSession(), socketDTO);
    }

    /**
     * @Description: 发起抽奖
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    private void startLottery(SocketDTO socketDTO) {
        lotteryService.startLottery(socketDTO, activityRooms.get(socketDTO.getReceiver()));
    }

    /**
     * @Description: 房主修改发言模式
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    private void updateSpeakMode(SocketDTO socketDTO) {
        ActivitySocket activitySocket = activityRooms.get(socketDTO.getReceiver());
        activitySocket.setSpeakMode((Integer) socketDTO.getData());
        socketDTO.setType(ROOM_PUSH_SPEAK_MODE);
        broadcastJson(socketDTO.getReceiver(), socketDTO);
    }

    /**
     * @Description: 发起红包
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    private void startRedPacket(SocketDTO socketDTO) {
        redPacketService.startRedPacket(socketDTO, activityRooms.get(socketDTO.getReceiver()));
    }

    /**
     * @Description: 发起评分
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    private void startGrade(SocketDTO socketDTO) {
        gradeService.startGrade(socketDTO, activityRooms.get(socketDTO.getReceiver()));
    }

    /**
     * @Description: 用户评分
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    private void userGrade(SocketDTO socketDTO) {
        gradeService.userGrade(socketDTO);
    }

    /**
     * @Description: 用户抢红包
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/1 0001
     */
    private void userRedPacket(SocketDTO socketDTO) {
        redPacketService.userRedPacket(socketDTO, activityRooms.get(socketDTO.getReceiver()));
    }

    /**
     * @Description: 推送在线人数
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/3 0003
     */
    private void pushOnlineNumber(int aid) {
        Integer number = activityRooms.get(aid).getUserWebSocketSessionMap().size();
        SocketDTO socketDTO = new SocketDTO(ROOM_PUSH_ONLINE_NUMBER, null, null, number);
        broadcastJson(aid, socketDTO);
    }

    /**
     * @Description: 移除活动房间
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/3 0003
     */
    public Boolean removeActivityRoom(Integer aid) {
        if (activityRooms.get(aid) != null && activityRooms.get(aid).getUserWebSocketSessionMap().size() == 0) {
            activityRooms.remove(aid);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Description: 大屏幕显示投票结果
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/4 0004
     */
    private void showVoteResult(SocketDTO socketDTO) {
        voteService.showVoteResult(socketDTO);
    }

    public WebSocketSession getScreenSession(Integer aid) {
        ActivitySocket activitySocket = activityRooms.get(aid);
        if (activitySocket!=null){
            return activitySocket.getWebSocketSession();
        }
        return null;
    }


    /**
    * @Description: 推送抽奖的结果
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/5 0005
    */
    private void pushLotteryResult(SocketDTO socketDTO){
        lotteryService.pushLotteryResult(socketDTO);
    }

    /**
    * @Description: 图片弹幕
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/6 0006
    */
    private void picBarrage(SocketDTO socketDTO){
        //判断当前的发言模式
        ActivitySocket activitySocket = activityRooms.get(socketDTO.getReceiver());
        if (activitySocket.getSpeakMode() == SPEAK_MODE_ONE) {
            UserDTO userDTO = (UserDTO) socketDTO.getSender();
            if (!userDTO.getId().equals(activitySocket.getCreater())) {
                return;
            }
        }
        //发送到大屏幕上
        sendJson(activitySocket.getWebSocketSession(), socketDTO);

        //将弹幕写入数据库
        if (barrageDao.saveBarrage(new BarrageDO(socketDTO.getReceiver(),
                ((UserDTO) socketDTO.getSender()).getId(),1,(String) socketDTO.getData())) > 0) {
            //给所有人发送弹幕
            broadcastJson(socketDTO.getReceiver(), socketDTO);
        }
    }

    /**
    * @Description: 获取房间里的所有用户
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/6 0006
    */
    private void listOnlineUser(SocketDTO socketDTO){
        ActivitySocket activitySocket = activityRooms.get(socketDTO.getReceiver());
        Set<Integer> set = activitySocket.getUserWebSocketSessionMap().keySet();
        Iterator<Integer> iterator = set.iterator();
        List<UserDTO> list = new ArrayList<UserDTO>();
        while (iterator.hasNext()){
            Integer id = iterator.next();
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("id",id);
            UserDTO user=  userDao.listUserDTO(map,1,1).get(0);
            list.add(user);
        }
        socketDTO.setData(list);
        sendJson(activitySocket.getWebSocketSession(),socketDTO);
    }

    private void pushPicCancel(SocketDTO socketDTO){
        ActivitySocket activitySocket = activityRooms.get(socketDTO.getReceiver());
        sendJson(activitySocket.getWebSocketSession(),socketDTO);
    }


    /**
    * @Description: 房主发起签到
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/6 0006
    */
    private void pushSign(SocketDTO socketDTO){
        ActivitySocket activitySocket = activityRooms.get(socketDTO.getReceiver());
        signService.pushSign(socketDTO,activitySocket);
    }

    /**
    * @Description:  用户签到
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/6 0006
    */
    private void userSign(SocketDTO socketDTO){
        signService.userSign(socketDTO,activityRooms.get(socketDTO.getReceiver()));
    }

    /**
    * @Description: 进入房间后推送签到
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/7 0007
    */
    private void joinRoomPushSign(Integer aid,Integer uid,WebSocketSession webSocketSession){
        ActivitySocket activitySocket = activityRooms.get(aid);
        if (activitySocket.getLongitude()!=null && activitySocket.getLatitude()!=null && !signService.judgeSign(aid,uid)){
            sendJson(webSocketSession,new SocketDTO(ROOM_BROADCAST_SIGN,null,null,null));
        }
    }

    public ActivitySocket getActivitySocket(Integer aid){
        return activityRooms.get(aid);
    }

    public Collection<ActivitySocket> getAllActivitySocket(){
        return activityRooms.values();
    }
}
