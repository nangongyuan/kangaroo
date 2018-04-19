package com.yuan.entity;

import java.util.List;

/**
 * @program: kangaroo
 * @description: 投票分组
 * @author: yuan
 * @create: 2018-03-30 21:10
 **/

public class VoteGroup {
    private Integer id;
    private Integer voteid;
    private String name;
    private Integer number;
    private String remark;
    private List<VoteRecord> voteRecords;

    public VoteGroup() {
    }

    public VoteGroup(Integer voteid, String name, Integer number, String remark) {
        this.voteid = voteid;
        this.name = name;
        this.number = number;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVoteid() {
        return voteid;
    }

    public void setVoteid(Integer voteid) {
        this.voteid = voteid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<VoteRecord> getVoteRecords() {
        return voteRecords;
    }

    public void setVoteRecords(List<VoteRecord> voteRecords) {
        this.voteRecords = voteRecords;
    }

    @Override
    public String toString() {
        return "VoteGroup{" +
                "id=" + id +
                ", voteid=" + voteid +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", remark='" + remark + '\'' +
                '}';
    }
}