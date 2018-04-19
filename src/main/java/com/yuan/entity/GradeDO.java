package com.yuan.entity;

/**
 * @program: kangaroo
 * @description: 评分
 * @author: yuan
 * @create: 2018-03-31 22:19
 **/

public class GradeDO {
    private Integer id;
    private Integer aid;
    private Integer sum;
    private Integer number;
    private String remark;

    public GradeDO() {
    }

    public GradeDO(Integer aid, Integer sum, Integer number, String remark) {
        this.aid = aid;
        this.sum = sum;
        this.number = number;
        this.remark = remark;
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

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}