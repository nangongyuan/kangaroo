package com.yuan.entity;

import java.util.Date;

public class FriendDO {
    private Integer id;
    private Integer uid1;
    private Integer uid2;
    private String friendremark;
    private Date recentlychat;
    private Date createTime;
    private String remark;

    public FriendDO() {
    }

    public FriendDO(Integer uid1, Integer uid2, String friendremark, Date recentlychat) {
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.friendremark = friendremark;
        this.recentlychat = recentlychat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid1() {
        return uid1;
    }

    public void setUid1(Integer uid1) {
        this.uid1 = uid1;
    }

    public Integer getUid2() {
        return uid2;
    }

    public void setUid2(Integer uid2) {
        this.uid2 = uid2;
    }

    public String getFriendremark() {
        return friendremark;
    }

    public void setFriendremark(String friendremark) {
        this.friendremark = friendremark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getRecentlychat() {
        return recentlychat;
    }

    public void setRecentlychat(Date recentlychat) {
        this.recentlychat = recentlychat;
    }
}
