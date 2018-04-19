package com.yuan.entity;

/**
 * @program: kangaroo
 * @description: 抽奖记录
 * @author: yuan
 * @create: 2018-03-31 14:02
 **/

public class LotteryRecordDO {
    private Integer id;
    private Integer lotteryid;
    private Integer uid;
    private String remark;

    public LotteryRecordDO() {
    }

    public LotteryRecordDO(Integer lotteryid, Integer uid, String remark) {
        this.lotteryid = lotteryid;
        this.uid = uid;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLotteryid() {
        return lotteryid;
    }

    public void setLotteryid(Integer lotteryid) {
        this.lotteryid = lotteryid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}