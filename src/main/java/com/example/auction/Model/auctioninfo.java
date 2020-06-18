package com.example.auction.Model;

public class auctioninfo {
    private Integer id;

    private String auctiontime;

    private String cycle;

    private String carid;

    private String floor;

    private String assessmentprice;

    private String other;

    private String auctionnumber;

    private String state;

    public auctioninfo(Integer id, String auctiontime, String cycle, String carid, String floor, String assessmentprice, String other, String auctionnumber, String state) {
        this.id = id;
        this.auctiontime = auctiontime;
        this.cycle = cycle;
        this.carid = carid;
        this.floor = floor;
        this.assessmentprice = assessmentprice;
        this.other = other;
        this.auctionnumber = auctionnumber;
        this.state = state;
    }

    public auctioninfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuctiontime() {
        return auctiontime;
    }

    public void setAuctiontime(String auctiontime) {
        this.auctiontime = auctiontime == null ? null : auctiontime.trim();
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle == null ? null : cycle.trim();
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid == null ? null : carid.trim();
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    public String getAssessmentprice() {
        return assessmentprice;
    }

    public void setAssessmentprice(String assessmentprice) {
        this.assessmentprice = assessmentprice == null ? null : assessmentprice.trim();
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }

    public String getAuctionnumber() {
        return auctionnumber;
    }

    public void setAuctionnumber(String auctionnumber) {
        this.auctionnumber = auctionnumber == null ? null : auctionnumber.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}