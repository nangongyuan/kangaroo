package com.yuan.dto;

import java.util.Date;

/**
 * @program: kangaroo
 * @description: 后台获取弹幕信息
 * @author: yuan
 * @create: 2018-03-29 21:56
 **/

public class BarrageDTO {
    private Integer id;
    private String sender;
    private String content;
    private Date createtime;
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
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
}