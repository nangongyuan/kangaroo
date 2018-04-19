package com.yuan.dto;

public class FriendDTO {
    private Integer id;
    private String name;
    private String sex;
    private String friendremark;
    private String headpic;
    private Boolean unread = false;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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


    public String getFriendremark() {
        return friendremark;
    }

    public void setFriendremark(String friendremark) {
        this.friendremark = friendremark;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }


}
