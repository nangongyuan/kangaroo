package com.yuan.service.impl;

import com.yuan.dao.VoteDao;
import com.yuan.dto.DTO;
import com.yuan.dto.SocketDTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.ActivitySocket;
import com.yuan.entity.VoteDO;
import com.yuan.entity.VoteGroup;
import com.yuan.entity.VoteRecord;
import com.yuan.service.IVoteService;
import com.yuan.websocket.ActivityWebSocketHandler;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.ws.ServiceMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;


import static com.yuan.constant.SocketMsgConstant.ROOM_PUSH_VOTE;


/**
 * @program: kangaroo
 * @description: 投票服务
 * @author: yuan
 * @create: 2018-03-30 21:20
 **/
@Service
public class VoteServiceImpl implements IVoteService {

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    /**
     * @Description: 房主发起投票
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    public void startVote(SocketDTO socketDTO, ActivitySocket activitySocket) {
        Map<String, Object> map = (Map<String, Object>) socketDTO.getData();
        String voteName = (String) map.get("name");
        Integer multiselect = (Integer) map.get("multiselect");
        List<String> groups = new ArrayList<String>();
        List<LinkedHashMap<String, Object>> list = (List<LinkedHashMap<String, Object>>) map.get("voteGroups");
        for (int i = 0; i < list.size(); i++) {
            LinkedHashMap<String, Object> link = list.get(i);
            groups.add((String) link.get("name"));
        }
        //添加投票
        VoteDO voteDO = new VoteDO(socketDTO.getReceiver(), voteName, multiselect, null);
        voteDao.saveVote(voteDO);
        voteDO.setVoteGroups(new ArrayList<VoteGroup>());
        //添加分组
        for (String name : groups) {
            VoteGroup voteGroup = new VoteGroup(voteDO.getId(), name, 0, null);
            voteDO.getVoteGroups().add(voteGroup);
            voteDao.saveVoteGroup(voteGroup);
        }

        //推送投票
        SocketDTO data = new SocketDTO(ROOM_PUSH_VOTE, null, null, voteDO);
        activityWebSocketHandler.broadcastJson(socketDTO.getReceiver(), data);

    }

    /**
     * @Description: 用户投票
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/3/31 0031
     */
    public void userVote(SocketDTO socketDTO) {
        List<Integer> list = (List<Integer>) socketDTO.getData();
        UserDTO userDTO = (UserDTO) socketDTO.getSender();
        for (Integer i : list) {
            VoteRecord voteRecord = new VoteRecord(i, userDTO.getId(), null);
            try {
                voteDao.saveVoteRecord(voteRecord);
                voteDao.addVoteGroupNum(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * @Description: 大屏幕显示投票结果
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/4 0004
     */
    public void showVoteResult(SocketDTO socketDTO) {
        Integer voteid = (Integer) socketDTO.getData();
        VoteDO voteDO = voteDao.getVoteDOById(voteid);
        List<VoteGroup> groups = voteDao.listVoteGroupByVid(voteid);
        voteDO.setVoteSum(0);
        for (VoteGroup group : groups){
            voteDO.setVoteSum(voteDO.getVoteSum()+group.getNumber());
        }
        voteDO.setVoteGroups(groups);
        socketDTO.setData(voteDO);
        WebSocketSession webSocketSession = activityWebSocketHandler.getScreenSession(socketDTO.getReceiver());
        activityWebSocketHandler.sendJson(webSocketSession, socketDTO);
    }

}