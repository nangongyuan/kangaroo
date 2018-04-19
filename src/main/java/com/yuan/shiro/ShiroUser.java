package com.yuan.shiro;

import java.util.List;

public class ShiroUser {
    private Integer id;
    private Integer type;
    private Integer loginType;
    private String name;
    private String headpic;

    public ShiroUser() {
    }

    public ShiroUser(int id, int type, String name, String headpic) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.headpic = headpic;
    }

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

    public Integer getLoginType() {
        return loginType;
    }

    public void setLoginType(Integer loginType) {
        this.loginType = loginType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadpic() {
        return headpic;
    }

    public void setHeadpic(String headpic) {
        this.headpic = headpic;
    }

    @Override
    public boolean equals(Object obj) {
        ShiroUser o = (ShiroUser) obj;
        if (o.getId().equals(this.getId())){
            return true;
        }else if (o.getName().equals(this.getName())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ShiroUser{" +
                "id=" + id +
                ", type=" + type +
                ", loginType=" + loginType +
                ", name='" + name + '\'' +
                ", headpic='" + headpic + '\'' +
                '}';
    }
}
