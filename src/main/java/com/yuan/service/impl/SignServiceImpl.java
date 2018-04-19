package com.yuan.service.impl;

import com.yuan.dao.ActivityDao;
import com.yuan.dao.SignDao;
import com.yuan.dto.SocketDTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.ActivityDO;
import com.yuan.entity.ActivitySocket;
import com.yuan.entity.SignDO;
import com.yuan.service.ISignService;
import com.yuan.websocket.ActivityWebSocketHandler;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.yuan.constant.SocketMsgConstant.ROOM_BROADCAST_SIGN;
import static com.yuan.constant.SocketMsgConstant.ROOM_USER_SIGN;

/**
 * @program: kangaroo
 * @description: 签到
 * @author: yuan
 * @create: 2018-04-07 11:29
 **/
@Service
public class SignServiceImpl implements ISignService {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    @Autowired
    private SignDao signDao;

    /**
    * @Description: 房主发起签到
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/7 0007
    */
    public void pushSign(SocketDTO socketDTO, ActivitySocket activitySocket) {
        if (signDao.listSignUserByAid(socketDTO.getReceiver()).size()>0)
            return;
        Map<String, Object> map = (Map<String, Object>) socketDTO.getData();
        Double latitude = (Double) map.get("latitude");
        Double longitude = (Double) map.get("longitude");
        //存进数据库
        activityDao.updateActivityCoords(socketDTO.getReceiver(),String.valueOf(latitude)+"-"+String.valueOf(longitude));
        activitySocket.setLatitude(latitude);
        activitySocket.setLongitude(longitude);

        SocketDTO msg = new SocketDTO(ROOM_BROADCAST_SIGN,null,null,null);
        activityWebSocketHandler.broadcastJson(socketDTO.getReceiver(),msg);
    }

    public void userSign(SocketDTO socketDTO, ActivitySocket activitySocket) {
        Map<String, Object> map = (Map<String, Object>) socketDTO.getData();
        Double latitude = (Double) map.get("latitude");
        Double longitude = (Double) map.get("longitude");
        UserDTO userDTO = (UserDTO) socketDTO.getSender();
        int lati1 = (int) (latitude * 100);
        int longi1 = (int) (longitude*100);
        int lati2 = (int) (activitySocket.getLatitude()*100);
        int longi2 = (int) (activitySocket.getLongitude()*100);
        SocketDTO result = new SocketDTO(ROOM_USER_SIGN,null,null,null);
        if (lati1==lati2 && longi1==longi2){
            result.setData("success");
            signDao.saveSignDO(new SignDO(socketDTO.getReceiver(),userDTO.getId()));
        }else{
            result.setData("您不在附近无法签到");
        }
        activityWebSocketHandler.sendJson(activitySocket.getUserWebSocketSessionMap().get(userDTO.getId()),result);
    }

    public Boolean judgeSign(Integer aid,Integer uid){
        return signDao.getSign(aid,uid)!=null;
    }
}