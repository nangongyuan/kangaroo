package com.yuan.entity;

import java.util.Date;

public class BarrageDO {
    private Integer id;
    private Integer aid;
    private Integer sender;
    private Integer type;
    private String content;
    private Date createtime;
    private String remark;

    public BarrageDO() {
    }

    public BarrageDO(Integer aid, Integer sender, Integer type, String content) {
        this.aid = aid;
        this.sender = sender;
        this.type = type;
        this.content = content;
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

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
