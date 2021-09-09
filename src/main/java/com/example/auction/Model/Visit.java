package com.example.auction.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

public class Visit {
    /*
    * 数据库入库数据
    * */
    private Integer id;
    private Integer userid;
    private Integer auctionid;
    private Integer carid;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    /*
    * 前端回显数据
    * */
    //用户名称
    private String username;
    //用户手机号
    private String tel;

    //标书编号
    private String auctionnumber;
    //车库名称
    private String cardealers;

    //列表最后访问时间/每个车辆访问最后访问时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date lastvisittime;
    //列表访问次数总次数/每个车辆访问总次数
    private Long vistcount;
    //0：删除1未删除
    private Integer state;
    //车辆信息
    private car car;


    public Integer getAuctionid() {
        return auctionid;
    }

    public void setAuctionid(Integer auctionid) {
        this.auctionid = auctionid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAuctionnumber() {
        return auctionnumber;
    }

    public void setAuctionnumber(String auctionnumber) {
        this.auctionnumber = auctionnumber;
    }

    public Date getLastvisittime() {
        return lastvisittime;
    }

    public void setLastvisittime(Date lastvisittime) {
        this.lastvisittime = lastvisittime;
    }

    public Long getVistcount() {
        return vistcount;
    }

    public void setVistcount(Long vistcount) {
        this.vistcount = vistcount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }


    public Integer getCarid() {
        return carid;
    }

    public void setCarid(Integer carid) {
        this.carid = carid;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public com.example.auction.Model.car getCar() {
        return car;
    }

    public void setCar(com.example.auction.Model.car car) {
        this.car = car;
    }

    public String getCardealers() {
        return cardealers;
    }

    public void setCardealers(String cardealers) {
        this.cardealers = cardealers;
    }
}
