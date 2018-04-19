package com.yuan.entity;

import java.util.Date;

/**
 * @program: kangaroo
 * @description: 红包记录
 * @author: yuan
 * @create: 2018-03-31 22:08
 **/

public class RedPacketRecordDO {
    private Integer id;
    private Integer rpid;
    private Integer uid;
    private Float money;
    private Date addtime;
    private String remark;
    private String name;

    public RedPacketRecordDO() {
    }

    public RedPacketRecordDO(Integer rpid, Integer uid, Float money, String remark) {
        this.rpid = rpid;
        this.uid = uid;
        this.money = money;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRpid() {
        return rpid;
    }

    public void setRpid(Integer rpid) {
        this.rpid = rpid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}