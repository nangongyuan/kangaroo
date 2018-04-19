package com.yuan.dto;

/**
 * @program: kangaroo
 * @description: 提醒
 * @author: yuan
 * @create: 2018-04-05 21:18
 **/

public class RemindDTO {
    private Integer aid;
    private String content;

    public RemindDTO(Integer aid, String content) {
        this.aid = aid;
        this.content = content;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}