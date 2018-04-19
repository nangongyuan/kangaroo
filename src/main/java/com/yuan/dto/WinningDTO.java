package com.yuan.dto;

import java.util.List;

/**
 * @program: kangaroo
 * @description: 中奖信息
 * @author: yuan
 * @create: 2018-03-31 14:44
 **/

public class WinningDTO {
    private String name;
    private String prizename;
    private Integer number;
    private List<UserDTO> users;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrizename() {
        return prizename;
    }

    public void setPrizename(String prizename) {
        this.prizename = prizename;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "WinningDTO{" +
                "name='" + name + '\'' +
                ", prizename='" + prizename + '\'' +
                ", number=" + number +
                ", users=" + users +
                '}';
    }
}