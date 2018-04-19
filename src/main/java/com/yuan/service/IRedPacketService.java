package com.yuan.service;

import com.yuan.dto.SocketDTO;
import com.yuan.entity.ActivitySocket;

/**
 * @program: kangaroo
 * @description: 红包
 * @author: yuan
 * @create: 2018-03-31 22:12
 **/

public interface IRedPacketService {
    void startRedPacket(SocketDTO socketDTO, ActivitySocket activitySocket);

    void userRedPacket(SocketDTO socketDTO, ActivitySocket activitySocket);
}
