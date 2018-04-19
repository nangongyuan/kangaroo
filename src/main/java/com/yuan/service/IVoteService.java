package com.yuan.service;

import com.yuan.dto.DTO;
import com.yuan.dto.SocketDTO;
import com.yuan.entity.ActivitySocket;
import com.yuan.entity.VoteDO;
import com.yuan.entity.VoteGroup;
import org.springframework.web.socket.WebSocketSession;

/**
 * @program: kangaroo
 * @description: 投票服务
 * @author: yuan
 * @create: 2018-03-30 21:19
 **/

public interface IVoteService {
    void startVote(SocketDTO socketDTO, ActivitySocket activitySocket);

    void userVote(SocketDTO socketDTO);

    void showVoteResult(SocketDTO socketDTO);
}
