package com.yuan.service;

import com.yuan.dto.DTO;
import com.yuan.dto.SocketDTO;
import com.yuan.entity.ActivitySocket;
import org.springframework.web.socket.WebSocketSession;

/**
 * @program: kangaroo
 * @description: 抽奖
 * @author: yuan
 * @create: 2018-03-31 12:13
 **/

public interface ILotteryService {
    void startLottery(SocketDTO socketDTO, ActivitySocket activitySocket);

    void pushLotteryResult(SocketDTO socketDTO);

    DTO listLotteryByAid(int aid);
}
