package com.yuan.constant;

/**
 * @program: kangaroo
 * @description: socket消息常量
 * @author: yuan
 * @create: 2018-03-31 15:26
 **/

public interface SocketMsgConstant {


    //好友模块   0发送好友请求 1好友请求同意  2消息  3图片   4预定提醒
    int FRIEND_REQUEST = 0;
    int FRIEND_AGREE = 1;
    int FRIEND_MESSAGE = 2;
    int FRIEND_PIC = 3;
    int SUBSCRIBLE_REMIND = 4;

    //房间模块 0普通消息  1推送图片 2展示海报 3大屏幕显示签到  4发起抽奖  5发起投票  6发起红包 7发起评分  8向用户推送投票 9用户投票
    //10用户进入房间  11大屏幕显示奖结果   12修改发言模式  13推送发言模式  14大屏幕显示投票结果  15推送评分 16用户评分   17已评分
    //18推送红包  19用户抢红包  20用户是否抢到红包 21推送当前人数  22获取在房间的所有用户  23图片消息  24取消推送图片  25发起签到
    //26用户签到 27广播签到

    int ROOM_MESSAGE = 0;        //普通弹幕
    int ROOM__PUSH_PIC = 1;      //推送图片  房主发起  大屏幕显示
    int ROOM_SHOW_POSTER = 2;     //2展示海报   房主发起  大屏幕显示
    int ROOM_SHOW_SIGN_IN = 3;    //大屏幕显示签到      房主发起  大屏幕显示
    int ROOM_SPONSOR_LOTTERY = 4;  //发起抽奖   房主发起
    int ROOM_SPONSOR_VOTE = 5;     //发起投票    房主发起
    int ROOM_SPONSOR_RED_PACKET = 6;  //发起红包  房主发起
    int ROOM_SPONSOR_GRADE = 7;       //发起评分   房主发起
    int ROOM_PUSH_VOTE = 8;    //8向用户推送投票
    int ROOM_USER_VOTE = 9;                //用户投票
    int ROOM_USER_JOIN = 10;                //10用户进入房间
    int ROOM_SHOW_LOTTERY_SCREEN = 11;
    int ROOM_UPDE_SPEAK_MODE = 12;
    int ROOM_PUSH_SPEAK_MODE = 13;
    int ROOM_PUSH_SHOW_VOTE_SCREEN = 14;
    int ROOM_PUSH_GRADE = 15;
    int ROOM_USER_GRADE = 16;
    int ROOM_PUSH_GRADE_ERR = 17;
    int ROOM_PUSH_RED_PACKET = 18;
    int ROOM_USER_RED_PACKET = 19;
    int ROOM_USER_RED_PACKET_RESULT = 20;
    int ROOM_PUSH_ONLINE_NUMBER = 21;
    int ROOM_GET_ALL_USERS = 22;
    int ROOM_PIC_MESSAGE = 23;
    int ROOM__PUSH_PIC_CANCEL = 24;
    int ROOM_PUSH_SIGN = 25;
    int ROOM_USER_SIGN = 26;
    int ROOM_BROADCAST_SIGN = 27;



    int HEART_BEAT_PACKET= 9999;
}
