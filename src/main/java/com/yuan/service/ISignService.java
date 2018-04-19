package com.yuan.service;

import com.yuan.dto.SocketDTO;
import com.yuan.entity.ActivitySocket;
import org.apache.ibatis.annotations.Param;

/**
 * @program: kangaroo
 * @description: 签到
 * @author: yuan
 * @create: 2018-04-07 11:26
 **/

public interface ISignService {
    void pushSign(SocketDTO socketDTO, ActivitySocket activitySocket);
    void userSign(SocketDTO socketDTO, ActivitySocket activitySocket);
    Boolean judgeSign(Integer aid,Integer uid);
}
