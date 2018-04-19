package com.yuan.service.impl;

import com.yuan.dao.RedPacketDao;
import com.yuan.dto.SocketDTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.ActivitySocket;
import com.yuan.entity.RedPacketDO;
import com.yuan.entity.RedPacketRecordDO;
import com.yuan.service.IRedPacketService;
import com.yuan.websocket.ActivityWebSocketHandler;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.yuan.constant.SocketMsgConstant.ROOM_PUSH_RED_PACKET;
import static com.yuan.constant.SocketMsgConstant.ROOM_USER_RED_PACKET_RESULT;

/**
 * @program: kangaroo
 * @description: 红包
 * @author: yuan
 * @create: 2018-03-31 22:13
 **/
@Service
public class RedPacketServiceImpl implements IRedPacketService {
    @Autowired
    private RedPacketDao redPacketDao;

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    public void startRedPacket(SocketDTO socketDTO,ActivitySocket activitySocket) {
        Map<String, Object> map = (Map<String, Object>) socketDTO.getData();
        String rptitle = (String) map.get("rptitle");
        Float money = 0F ;
        Object object = map.get("money");
        if (object instanceof Integer){
            int i = (Integer) object;
            money = (float) i;
        }else{
            money = (Float) object;
        }

        Integer number = (Integer) map.get("number");
        Integer random = (Integer) map.get("random");

        RedPacketDO redPacketDO = new RedPacketDO(socketDTO.getReceiver(),rptitle,money,number,random,null);
        redPacketDao.saveRedPacket(redPacketDO);
        //红包金额
        List<Float> moneys = null;
        if (random==1){
            moneys = rndRedPacket(money,number);
        }else{
            moneys = bisectRedPacket(money,number);
        }
        activitySocket.getRedPackets().put(redPacketDO.getId(),moneys);
        //推送红包
        socketDTO.setType(ROOM_PUSH_RED_PACKET);
        socketDTO.setData(redPacketDO.getId());
        activityWebSocketHandler.broadcastJson(socketDTO.getReceiver(),socketDTO);

    }
    /**
    * @Description: 用户抢红包
    * @Param:
    * @return:
    * @Author: yuan
    * @Date: 2018/4/1 0001
    */
    public synchronized  void userRedPacket(SocketDTO socketDTO, ActivitySocket activitySocket) {
        Integer rpid = (Integer) socketDTO.getData();
        List<Float> list = activitySocket.getRedPackets().get(rpid);
        UserDTO userDTO = (UserDTO) socketDTO.getSender();
        //红包已被抢完
        if (list.size()<=0){
            socketDTO.setType(ROOM_USER_RED_PACKET_RESULT);
            socketDTO.setData(0F);
        }else{
            Float money = list.get(0);   //红包金额
            list.remove(0);

            RedPacketRecordDO redPacketRecordDO = new RedPacketRecordDO(rpid,userDTO.getId(),money,null);
            redPacketDao.saveRedPacketRecord(redPacketRecordDO);

            socketDTO.setType(ROOM_USER_RED_PACKET_RESULT);
            socketDTO.setData(money);
        }
        activityWebSocketHandler.sendJson(activitySocket.getUserWebSocketSessionMap().get(userDTO.getId()),socketDTO);
    }


    private List<Float> rndRedPacket(float money,int number){
        List<Float> list = new LinkedList<Float>();
        int s = (int) (money*100);
        int t=s;
        s = s - number;
        int sum =0;
        for (int i=0; i<number-1; i++){
            int r = 1+(int) (Math.random()*s);
            s-=r;
            sum +=r;
            list.add(r/100F);
        }
        list.add((t-sum)/100F);
        return list;
    }

    private List<Float> bisectRedPacket(float money,int number){
        List<Float> list = new LinkedList<Float>();
        int s = (int) (money*100);
        int num = s/number;
        for (int i=0; i<number; i++){
            list.add((num)/100F);
        }
        return list;
    }
}