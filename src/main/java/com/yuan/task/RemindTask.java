package com.yuan.task;

import com.yuan.dao.ActivityDao;
import com.yuan.dao.SubscribleDao;
import com.yuan.dto.RemindDTO;
import com.yuan.dto.UserDTO;
import com.yuan.entity.ActivityDO;
import com.yuan.websocket.ActivityWebSocketHandler;
import com.yuan.websocket.FriendWebSocketHandler;
import java.rmi.activation.ActivationID;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;


import static com.yuan.constant.SocketMsgConstant.SUBSCRIBLE_REMIND;

@Component
public class RemindTask {

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ActivityWebSocketHandler activityWebSocketHandler;

    @Autowired
    private FriendWebSocketHandler friendWebSocketHandler;

    @Autowired
    private SubscribleDao subscribleDao;

    //已结束的活动但是没有关闭房间
    private List<Integer> finishedActivities = new ArrayList<Integer>();

    private int f = 0;


    /**
     * @Description: 每50秒运行一次     提醒预约用户
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/3 0003
     */

    @Scheduled(cron = "0/50 * * * * ?")
    public void remindSubscribleUser() {
        //刚启动程序把正在进行的活动房间创建
        if (f == 0) {
            createActivitying();
            f = 1;
        }
        //修改状态为进行中   创建房间
        List<Integer> list = activityDao.listStartActivity();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                //修改状态为进行中
                activityDao.updateStatus(list.get(i), 0);
                //创建活动房间
                activityWebSocketHandler.createActivity(list.get(i));
                //提醒预定的用户
                subscribleActivityRemind(list.get(i));
            }
        }

        //修改状态为已结束   移除房间
        list = activityDao.listEndActivity();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                activityDao.updateStatus(list.get(i), 2);  //修改状态为已结束
                if (!activityWebSocketHandler.removeActivityRoom(list.get(i))) {
                    finishedActivities.add(list.get(i));
                }
            }
        }

        //将结束的活动的房间关闭
        Iterator<Integer> iterator = finishedActivities.iterator();
        while (iterator.hasNext()) {
            Integer removeid = iterator.next();
            if (activityWebSocketHandler.removeActivityRoom(removeid)) {
                iterator.remove();
            }
        }
    }

    /**
     * @Description: 预定活动提醒
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/3 0003
     */
    private void subscribleActivityRemind(Integer aid) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", aid);
        List<ActivityDO> list = activityDao.listActivityDO(map, 1, 1);
        ActivityDO activityDO = list.get(0);
        String msg = "小袋提醒您，您预定的活动：" + activityDO.getName() + "已开始，点击直接进入活动房间(#"+aid+")";
        List<UserDTO> users = subscribleDao.listSubscribleByAid(aid);
        if (users != null && users.size() > 0) {
            for (UserDTO userDTO : users) {
                friendWebSocketHandler.sysNotification(SUBSCRIBLE_REMIND, msg, userDTO.getId());
                subscribleDao.updateSubscribleValid(aid, userDTO.getId(), 0);
            }
        }
    }

    /**
     * @Description: 启动时创建正在进行中的活动
     * @Param:
     * @return:
     * @Author: yuan
     * @Date: 2018/4/3 0003
     */
    private void createActivitying() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", 0);
        List<ActivityDO> list = activityDao.listActivityDO(map, 1, 100000);
        if (list != null && list.size() > 0) {
            for (ActivityDO activityDO : list) {
                activityWebSocketHandler.createActivity(activityDO.getId());
            }
        }
    }
}
