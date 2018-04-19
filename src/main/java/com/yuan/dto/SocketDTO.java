package com.yuan.dto;

//房间模块   0普通消息  1推送图片 2展示海报 3大屏幕显示签到  4发起抽奖  5发起投票  6红包 7发起评分  8推送投票 9用户投票
//10用户进入房间  11向大屏幕推送抽奖结果   12修改发言模式  13推送发言模式

public class SocketDTO {
    private Integer type;    //好友模块   0发送好友请求 1好友请求同意  2消息  3图片
    private Object sender;
    private Integer receiver;     //房间模块   表示活动的id
    private Object data;

    public SocketDTO() {

    }

    public SocketDTO(Integer type, Object sender, Integer receiver, Object data) {
        this.type = type;
        this.sender = sender;
        this.receiver = receiver;
        this.data = data;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SocketDTO{" +
                "type=" + type +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", data=" + data +
                '}';
    }
}
