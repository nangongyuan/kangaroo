package com.yuan.dto;

/**
 * @program: kangaroo
 * @description: 图灵机器人
 * @author: yuan
 * @create: 2018-04-02 15:35
 **/

public class RobotMsgDTO {
    private Integer code;
    private String text;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "RobotMsgDTO{" +
                "code=" + code +
                ", text='" + text + '\'' +
                '}';
    }
}