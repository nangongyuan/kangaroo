package com.yuan.entity;

import com.yuan.dto.UserDTO;
import java.util.Date;
import java.util.List;

/**
 * @program: kangaroo
 * @description: 抽奖
 * @author: yuan
 * @create: 2018-03-31 13:59
 **/

public class LotteryDO {
    private Integer id;
    private Integer aid;
    private String name;
    private String prizename;
    private Integer number;
    private Date addtime;
    private String remark;
    private List<UserDTO> lotteryRecords;

    public LotteryDO() {
    }

    public LotteryDO(Integer aid, String name, String prizename, Integer number, String remark) {
        this.aid = aid;
        this.name = name;
        this.prizename = prizename;
        this.number = number;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrizename() {
        return prizename;
    }

    public void setPrizename(String prizename) {
        this.prizename = prizename;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public List<UserDTO> getLotteryRecords() {
        return lotteryRecords;
    }

    public void setLotteryRecords(List<UserDTO> lotteryRecords) {
        this.lotteryRecords = lotteryRecords;
    }
}