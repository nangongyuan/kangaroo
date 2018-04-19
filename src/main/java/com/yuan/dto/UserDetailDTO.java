package com.yuan.dto;

import java.util.Date;
import java.util.List;

public class UserDetailDTO {
    private Integer id;
    private Integer type;
    private String name;
    private String sex;
    private String area;
    private String phone;
    private String headpic;
    private Integer locked;
    private Date create_time;
    private String remark;
    private List<FriendDTO> friends;
    private List<ActivityDTO> myActivities;
    private List<ActivityDTO> subscribleActivities;
    private List<ActivityDTO> participatingActivities;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public Integer getLocked() {
        return locked;
    }

    public void setLocked(Integer locked) {
        this.locked = locked;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<FriendDTO> getFriends() {
        return friends;
    }

    public void setFriends(List<FriendDTO> friends) {
        this.friends = friends;
    }

    public List<ActivityDTO> getMyActivities() {
        return myActivities;
    }

    public void setMyActivities(List<ActivityDTO> myActivities) {
        this.myActivities = myActivities;
    }

    public List<ActivityDTO> getSubscribleActivities() {
        return subscribleActivities;
    }

    public void setSubscribleActivities(List<ActivityDTO> subscribleActivities) {
        this.subscribleActivities = subscribleActivities;
    }

    public List<ActivityDTO> getParticipatingActivities() {
        return participatingActivities;
    }

    public void setParticipatingActivities(List<ActivityDTO> participatingActivities) {
        this.participatingActivities = participatingActivities;
    }
}
