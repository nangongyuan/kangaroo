package com.yuan.entity;

import java.util.Date;
import java.util.List;
import org.omg.CORBA.INTERNAL;

/**
 * @program: kangaroo
 * @description: 投票
 * @author: yuan
 * @create: 2018-03-30 21:08
 **/

public class VoteDO {
    private Integer id;
    private Integer aid;
    private String name;
    private Integer multiselect;
    private Date addtime;
    private String remark;
    private List<Integer> voteIndexs;
    private Integer voteSum;
    private List<VoteGroup> voteGroups;
    public VoteDO() {
    }

    public VoteDO(Integer aid, String name, Integer multiselect, String remark) {
        this.aid = aid;
        this.name = name;
        this.multiselect = multiselect;
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

    public Integer getMultiselect() {
        return multiselect;
    }

    public void setMultiselect(Integer multiselect) {
        this.multiselect = multiselect;
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

    public List<VoteGroup> getVoteGroups() {
        return voteGroups;
    }

    public void setVoteGroups(List<VoteGroup> voteGroups) {
        this.voteGroups = voteGroups;
    }

    public List<Integer> getVoteIndexs() {
        return voteIndexs;
    }

    public void setVoteIndexs(List<Integer> voteIndexs) {
        this.voteIndexs = voteIndexs;
    }

    public Integer getVoteSum() {
        return voteSum;
    }

    public void setVoteSum(Integer voteSum) {
        this.voteSum = voteSum;
    }
}