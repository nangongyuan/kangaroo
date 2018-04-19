package com.yuan.entity;

import java.util.Date;

/**
 * @program: kangaroo
 * @description: URL资源
 * @author: yuan
 * @create: 2018-04-05 22:23
 **/

public class ResourceDO {
    private Integer id;
    private String name;
    private String url;
    private String desctiption;
    private Integer status;
    private Integer Privilege;
    private Date createtime;
    private String remark;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesctiption() {
        return desctiption;
    }

    public void setDesctiption(String desctiption) {
        this.desctiption = desctiption;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPrivilege() {
        return Privilege;
    }

    public void setPrivilege(Integer privilege) {
        Privilege = privilege;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ResourceDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", desctiption='" + desctiption + '\'' +
                ", status=" + status +
                ", Privilege=" + Privilege +
                ", createtime=" + createtime +
                ", remark='" + remark + '\'' +
                '}';
    }
}