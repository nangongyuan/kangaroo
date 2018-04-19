package com.yuan.entity;

import com.yuan.dto.SocketDTO;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.web.socket.WebSocketSession;

/**
 * @program: kangaroo
 * @description: 在websocket中保存活动的信息
 * @author: yuan
 * @create: 2018-03-29 14:06
 **/

public class ActivitySocket {
    //保存用户的id和对应的WebSocketSession
    private Map<Integer, WebSocketSession> userWebSocketSessionMap;
    //保存活动的弹幕主题
    private Integer theme = 0;
    //活动的创建人
    private Integer creater;
    //活动的发言模式   0全部人可以发言   1只有房主可以发言
    private Integer speakMode = 0;
    //大屏幕的WebSocketSession
    private WebSocketSession webSocketSession;
    //所有红包包括金额
    private Map<Integer, List<Float>> redPackets = new HashMap<Integer, List<Float>>();
    //位置经纬度
    private Double latitude;
    private Double longitude;

    public ActivitySocket(Map<Integer, WebSocketSession> userWebSocketSessionMap, Integer theme, Integer creater, Integer speakMode) {
        this.userWebSocketSessionMap = userWebSocketSessionMap;
        this.theme = theme;
        this.creater = creater;
        this.speakMode = speakMode;
    }

    public Map<Integer, WebSocketSession> getUserWebSocketSessionMap() {
        return userWebSocketSessionMap;
    }

    public void setUserWebSocketSessionMap(Map<Integer, WebSocketSession> userWebSocketSessionMap) {
        this.userWebSocketSessionMap = userWebSocketSessionMap;
    }

    public Integer getTheme() {
        return theme;
    }

    public void setTheme(Integer theme) {
        this.theme = theme;
    }

    public Integer getCreater() {
        return creater;
    }

    public void setCreater(Integer creater) {
        this.creater = creater;
    }

    public Integer getSpeakMode() {
        return speakMode;
    }

    public void setSpeakMode(Integer speakMode) {
        this.speakMode = speakMode;
    }

    public WebSocketSession getWebSocketSession() {
        return webSocketSession;
    }

    public void setWebSocketSession(WebSocketSession webSocketSession) {
        this.webSocketSession = webSocketSession;
    }


    public Map<Integer, List<Float>> getRedPackets() {
        return redPackets;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}