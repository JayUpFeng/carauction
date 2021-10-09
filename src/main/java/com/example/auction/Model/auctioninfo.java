package com.example.auction.Model;

import java.util.List;

public class auctioninfo {//标书表
    private Integer id;

    private String auctiontime;

    private String cycle;

    private String carid;

    private String floor;

    private String assessmentprice;

    private String other;

    private String auctionnumber;
    //4：流拍
    private String state;
    private String auctionendtime;
    private String cardealers;
    private String issesscar;
    private String auctionjurisdiction;
    //成交金额、流拍最高出价者
    private String transactionamount;
    //成交、违规、流拍更新时间
    private String updatetime;
    private List<car> carList;
    private List<bidder> bidderList;
    //成交、流拍车辆凭证
    private String carimg;
    private Integer bidderid;
    private String usernumber;
    private String imgBase64;

    public auctioninfo(Integer id, String auctiontime, String cycle, String carid, String floor, String assessmentprice,
                       String other, String auctionnumber, String state, String auctionendtime, String cardealers,
                       String issesscar, String auctionjurisdiction, String transactionamount,
                       String carimg,String updatetime) {
        this.id = id;
        this.auctiontime = auctiontime;
        this.cycle = cycle;
        this.carid = carid;
        this.floor = floor;
        this.assessmentprice = assessmentprice;
        this.other = other;
        this.auctionnumber = auctionnumber;
        this.state = state;
        this.auctionendtime = auctionendtime;
        this.cardealers = cardealers;
        this.issesscar = issesscar;
        this.auctionjurisdiction = auctionjurisdiction;
        this.transactionamount = transactionamount;
        this.carimg = carimg;
        this.updatetime = updatetime;
    }

    public String getTransactionamount() {
        return transactionamount;
    }

    public void setTransactionamount(String transactionamount) {
        this.transactionamount = transactionamount;
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

    public List<car> getCarList() {
        return carList;
    }

    public void setCarList(List<car> carList) {
        this.carList = carList;
    }

    public List<bidder> getBidderList() {
        return bidderList;
    }

    public void setBidderList(List<bidder> bidderList) {
        this.bidderList = bidderList;
    }

    public String getCarimg() {
        return carimg;
    }

    public void setCarimg(String carimg) {
        this.carimg = carimg;
    }

    public Integer getBidderid() {
        return bidderid;
    }

    public void setBidderid(Integer bidderid) {
        this.bidderid = bidderid;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getImgBase64() {
        return imgBase64;
    }

    public void setImgBase64(String imgBase64) {
        this.imgBase64 = imgBase64;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    @Override
    public String toString() {
        return "auctioninfo{" +
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
                '}';
    }
}