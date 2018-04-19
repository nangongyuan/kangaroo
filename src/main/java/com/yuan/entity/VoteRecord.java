package com.yuan.entity;

import java.util.Date;

/**
 * @program: kangaroo
 * @description: 投票记录
 * @author: yuan
 * @create: 2018-03-30 21:12
 **/

public class VoteRecord {
    private Integer id;
    private Integer groupid;
    private String name;
    private Integer uid;
    private Date addtime;
    private String remark;

    public VoteRecord() {
    }

    public VoteRecord(Integer groupid, Integer uid, String remark) {
        this.groupid = groupid;
        this.uid = uid;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupid() {
        return groupid;
    }

    public void setGroupid(Integer groupid) {
        this.groupid = groupid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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