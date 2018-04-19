package com.yuan.service.impl;

import com.yuan.dao.LotteryDao;
import com.yuan.dto.DTO;
import com.yuan.dto.SocketDTO;
import com.yuan.dto.WinningDTO;
import com.yuan.entity.ActivitySocket;
import com.yuan.entity.LotteryDO;
import com.yuan.entity.LotteryRecordDO;
import com.yuan.service.ILotteryService;
import com.yuan.websocket.ActivityWebSocketHandler;
import com.yuan.websocket.FriendWebSocketHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;


import static com.yuan.constant.SocketMsgConstant.FRIEND_MESSAGE;
import static com.yuan.constant.SocketMsgConstant.ROOM_SHOW_LOTTERY_SCREEN;

/**
 * @program: kangaroo
 * @description: 抽奖
 * @author: yuan
 * @create: 2018-03-31 12:14
 **/

@Service
public class LotteryServiceImpl implements ILotteryService {

    @Autowired
    private LotteryDao lotteryDao;

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    @Autowired
    private FriendWebSocketHandler friendWebSocketHandler;

    public void startLottery(SocketDTO socketDTO, ActivitySocket activitySocket) {
        Map<String, Object> map = (Map<String, Object>) socketDTO.getData();
        String name = (String) map.get("name");
        String prizename = (String) map.get("prizename");
        Integer number = (Integer) map.get("number");
        LotteryDO lotteryDO = new LotteryDO(socketDTO.getReceiver(), name, prizename, number, null);
        lotteryDao.saveLottery(lotteryDO);

        //抽奖
        Set<Integer> set = activitySocket.getUserWebSocketSessionMap().keySet();
        List<Integer> list = new ArrayList<Integer>(set);
        List<Integer> users = new ArrayList<Integer>();
        for (int i = 0; i < number; i++) {
            int r = (int) (Math.random() * list.size());
            users.add(r);
            lotteryDao.saveLotteryRecord(new LotteryRecordDO(lotteryDO.getId(), list.get(r), null));
            list.remove(r);
            if (list.size() <= 0)
                break;
        }

        //向大屏幕推送抽奖结果
        WinningDTO winningDTO = new WinningDTO();
        winningDTO.setName(name);
        winningDTO.setNumber(number);
        winningDTO.setPrizename(prizename);
        winningDTO.setUsers(lotteryDao.listWinningUser(lotteryDO.getId()));
        SocketDTO data = new SocketDTO(ROOM_SHOW_LOTTERY_SCREEN, socketDTO.getSender(), socketDTO.getReceiver(), winningDTO);
        activityWebSocketHandler.sendJson(activitySocket.getWebSocketSession(), data);

    }

    public void pushLotteryResult(SocketDTO socketDTO) {
        //推送给在房间的所有人
        activityWebSocketHandler.broadcastJson(socketDTO.getReceiver(), socketDTO);

        //通过小袋通知中奖人
        Map<String, Object> map = (Map<String, Object>) socketDTO.getData();
        if (map != null) {
            String prizename = (String) map.get("prizename");
            List<Map<String, Object>> users = (List<Map<String, Object>>) map.get("users");
            if (users != null) {
                for (Map<String, Object> user : users) {
                    Integer uid = (Integer) user.get("id");
                    if (uid != null) {
                        friendWebSocketHandler.sysNotification(FRIEND_MESSAGE, "主人恭喜您抽中了：" + prizename, uid);
                    }
                }
            }
        }
    }

    public DTO listLotteryByAid(int aid) {
        DTO result = new DTO();
        List<LotteryDO> lotteryDOs = lotteryDao.listLotteryDOByAid(aid);
        for (LotteryDO lotteryDO:lotteryDOs){
            lotteryDO.setLotteryRecords(lotteryDao.listWinningUser(lotteryDO.getId()));
        }
        result.setSuccess(true);
        result.setData(lotteryDOs);
        return result;
    }


}