package com.yuan.entity;

import java.util.Date;
import java.util.List;

/**
 * @program: kangaroo
 * @description: 红包
 * @author: yuan
 * @create: 2018-03-31 22:05
 **/

public class RedPacketDO {
    private Integer id;
    private Integer aid;
    private String rptitle;
    private Float money;
    private Integer number;
    private Integer random;
    private Date addtime;
    private String remark;
    private List<RedPacketRecordDO> redPacketRecords;

    public RedPacketDO() {
    }

    public RedPacketDO(Integer aid, String rptitle, Float money, Integer number, Integer random, String remark) {
        this.aid = aid;
        this.rptitle = rptitle;
        this.money = money;
        this.number = number;
        this.random = random;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getRptitle() {
        return rptitle;
    }

    public void setRptitle(String rptitle) {
        this.rptitle = rptitle;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getRandom() {
        return random;
    }

    public void setRandom(Integer random) {
        this.random = random;
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

    @Override
    public String toString() {
        return "RedPacketDO{" +
                "id=" + id +
                ", aid=" + aid +
                ", rptitle='" + rptitle + '\'' +
                ", money=" + money +
                ", number=" + number +
                ", random=" + random +
                ", addtime=" + addtime +
                ", remark='" + remark + '\'' +
                '}';
    }

    public List<RedPacketRecordDO> getRedPacketRecords() {
        return redPacketRecords;
    }

    public void setRedPacketRecords(List<RedPacketRecordDO> redPacketRecords) {
        this.redPacketRecords = redPacketRecords;
    }

}