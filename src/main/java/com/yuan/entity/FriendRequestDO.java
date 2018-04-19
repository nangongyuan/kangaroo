package com.yuan.entity;

import java.util.Date;

public class FriendRequestDO {
    private Integer id;
    private Integer sender;
    private Integer receiver;
    private Integer status;
    private Date createTime;

    public FriendRequestDO() {
    }

    public FriendRequestDO(Integer id, Integer sender, Integer receiver, Integer status, Date createTime) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
