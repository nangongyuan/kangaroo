package com.yuan.dto;

public class FriendInfoDTO {
    private Integer id;
    private String name;
    private String sex;
    private String area;
    private String phone;
    private String friendremark;
    private String headpic;

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

    public String getFriendremark() {
        return friendremark;
    }

    public void setFriendremark(String friendremark) {
        this.friendremark = friendremark;
    }
}
