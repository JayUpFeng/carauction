package com.example.auction.Model;

import java.util.List;

public class acutionListInfo {
    private Integer id;

    private String auctiontime;

    private String cycle;

    private String carid;

    private String floor;

    private String assessmentprice;

    private String other;

    private String auctionnumber;

    private String state;
    private String auctionendtime;
    private String cardealers;
    private String issesscar;
    private String auctionjurisdiction;
    private String transactionamount;

    private Integer zjtime;
    private String userstr;
    //是否推送公众号
    private String issend;
    private List<car> carList;

    public acutionListInfo() {
        super();
    }

    public String getUserstr() {
        return userstr;
    }

    public void setUserstr(String userstr) {
        this.userstr = userstr;
    }

    public String getTransactionamount() {
        return transactionamount;
    }

    public void setTransactionamount(String transactionamount) {
        this.transactionamount = transactionamount;
    }

    public Integer getZjtime() {
        return zjtime;
    }

    public void setZjtime(Integer zjtime) {
        this.zjtime = zjtime;
    }

    public String getAuctionjurisdiction() {
        return auctionjurisdiction;
    }

    public void setAuctionjurisdiction(String auctionjurisdiction) {
        this.auctionjurisdiction = auctionjurisdiction;
    }

    public String getCardealers() {
        return cardealers;
    }

    public void setCardealers(String cardealers) {
        this.cardealers = cardealers;
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

    public String getAuctionendtime() {
        return auctionendtime;
    }

    public void setAuctionendtime(String auctionendtime) {
        this.auctionendtime = auctionendtime;
    }

    public String getIssesscar() {
        return issesscar;
    }

    public void setIssesscar(String issesscar) {
        this.issesscar = issesscar;
    }

    public String getIssend() {
        return issend;
    }

    public void setIssend(String issend) {
        this.issend = issend;
    }

    public List<car> getCarList() {
        return carList;
    }

    public void setCarList(List<car> carList) {
        this.carList = carList;
    }

    @Override
    public String toString() {
        return "acutionListInfo{" +
                "id=" + id +
                ", auctiontime='" + auctiontime + '\'' +
                ", cycle='" + cycle + '\'' +
                ", carid='" + carid + '\'' +
                ", floor='" + floor + '\'' +
                ", assessmentprice='" + assessmentprice + '\'' +
                ", other='" + other + '\'' +
                ", auctionnumber='" + auctionnumber + '\'' +
                ", state='" + state + '\'' +
                ", auctionendtime='" + auctionendtime + '\'' +
                ", cardealers='" + cardealers + '\'' +
                ", issesscar='" + issesscar + '\'' +
                ", auctionjurisdiction='" + auctionjurisdiction + '\'' +
                ", transactionamount='" + transactionamount + '\'' +
                ", zjtime=" + zjtime +
                ", userstr='" + userstr + '\'' +
                ", issend=" + issend +
                '}';
    }
}