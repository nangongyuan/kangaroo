package com.yuan.service.impl;

import com.yuan.dao.GradeDao;
import com.yuan.dto.SocketDTO;
import com.yuan.entity.ActivitySocket;
import com.yuan.entity.GradeDO;
import com.yuan.service.IGradeService;
import com.yuan.websocket.ActivityWebSocketHandler;
import java.lang.management.GarbageCollectorMXBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import static com.yuan.constant.SocketMsgConstant.ROOM_PUSH_GRADE;
import static com.yuan.constant.SocketMsgConstant.ROOM_PUSH_GRADE_ERR;

/**
 * @program: kangaroo
 * @description: 评分
 * @author: yuan
 * @create: 2018-03-31 22:38
 **/
@Service
public class GradeServiceImpl implements IGradeService {
    
    @Autowired
    private GradeDao gradeDao;
    
    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;
    
    /** 
    * @Description: 发起评分
    * @Param:  
    * @return:  
    * @Author: yuan 
    * @Date: 2018/3/31 0031 
    */ 
    public void startGrade(SocketDTO socketDTO,ActivitySocket activitySocket) {
        //已评分
        if (gradeDao.getGradeDOByAid(socketDTO.getReceiver())!=null){
            socketDTO.setType(ROOM_PUSH_GRADE_ERR);
            activityWebSocketHandler.sendJson(activitySocket.getUserWebSocketSessionMap().get(activitySocket.getCreater()),socketDTO);
            return;
        }

        GradeDO gradeDO = new GradeDO(socketDTO.getReceiver(),0,0,null);
        gradeDao.saveGrade(gradeDO);
        //推送评分
        socketDTO.setType(ROOM_PUSH_GRADE);
        socketDTO.setData(gradeDO.getId());
        activityWebSocketHandler.broadcastJson(socketDTO.getReceiver(),socketDTO);

    }

    /** 
    * @Description: 用户投票 
    * @Param:  
    * @return:  
    * @Author: yuan 
    * @Date: 2018/3/31 0031 
    */ 
    public void userGrade(SocketDTO socketDTO) {
        Integer grade = (Integer) socketDTO.getData();
        gradeDao.addGrade(socketDTO.getReceiver(),grade);
    }

}