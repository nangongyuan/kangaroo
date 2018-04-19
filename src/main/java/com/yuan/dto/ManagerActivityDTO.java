package com.yuan.dto;

import com.yuan.entity.ActivityDO;
import com.yuan.entity.LotteryDO;
import com.yuan.entity.RedPacketDO;
import com.yuan.entity.SignDO;
import com.yuan.entity.VoteDO;
import java.util.List;

/**
 * @program: kangaroo
 * @description: 后团根据id获取活动详情
 * @author: yuan
 * @create: 2018-04-01 16:03
 **/

public class ManagerActivityDTO {
    private ActivityDO activityDO;
    private List<RedPacketDO> redPackets;
    private List<VoteDO> votes;
    private List<UserDTO> signs;
    private Float grade;
    private List<LotteryDO> lotteries;
    private List<UserDTO> subscrible;

    public ActivityDO getActivityDO() {
        return activityDO;
    }

    public void setActivityDO(ActivityDO activityDO) {
        this.activityDO = activityDO;
    }

    public List<RedPacketDO> getRedPackets() {
        return redPackets;
    }

    public void setRedPackets(List<RedPacketDO> redPackets) {
        this.redPackets = redPackets;
    }

    public List<VoteDO> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteDO> votes) {
        this.votes = votes;
    }

    public List<UserDTO> getSigns() {
        return signs;
    }

    public void setSigns(List<UserDTO> signs) {
        this.signs = signs;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public List<LotteryDO> getLotteries() {
        return lotteries;
    }

    public void setLotteries(List<LotteryDO> lotteries) {
        this.lotteries = lotteries;
    }

    public List<UserDTO> getSubscrible() {
        return subscrible;
    }

    public void setSubscrible(List<UserDTO> subscrible) {
        this.subscrible = subscrible;
    }
}