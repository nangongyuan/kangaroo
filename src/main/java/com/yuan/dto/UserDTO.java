package com.yuan.dto;

public class UserDTO {
    private Integer id;
    private String name;
    private String sex;
    private String area;
    private String phone;
    private String headpic;

    public UserDTO() {
    }

    public UserDTO(Integer id, String name, String sex, String area, String phone, String headpic) {
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.area = area;
        this.phone = phone;
        this.headpic = headpic;
    }

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

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", area='" + area + '\'' +
                ", phone='" + phone + '\'' +
                ", headpic='" + headpic + '\'' +
                '}';
    }
}
