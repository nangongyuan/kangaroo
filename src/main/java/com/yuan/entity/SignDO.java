package com.yuan.entity;

import java.util.Date;

/**
 * @program: kangaroo
 * @description: 签到
 * @author: yuan
 * @create: 2018-04-01 16:52
 **/

public class SignDO {
    private Integer id;
    private Integer aid;
    private Integer uid;
    private String name;
    private Date addtime;
    private String remark;

    public SignDO() {
    }

    public SignDO(Integer aid, Integer uid) {
        this.aid = aid;
        this.uid = uid;
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

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}