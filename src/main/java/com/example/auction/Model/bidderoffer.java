package com.example.auction.Model;

import java.util.List;

public class bidderoffer {
    private Integer id;

    private Integer userid;

    private String carid;

    private String paymentvoucher;

    private String paymentstate;

    private String applicationtime;

    private String auctionnumber;

    private String other;

    private String auctionstate;

    private String offerid;

    private Integer auctioninfoid;

    private String ispaybond;

    private String appointmentseecarname;

    private String appointmentseecartel;

    private String seecartime;

    private String seecarmancertificates;

    private List<offerinfo> offerinfoList;

    public bidderoffer(Integer id, Integer userid, String carid, String paymentvoucher, String paymentstate, String applicationtime, String auctionnumber, String other, String auctionstate, String offerid, Integer auctioninfoid, String ispaybond, String appointmentseecarname, String appointmentseecartel, String seecartime, String seecarmancertificates, List<offerinfo> offerinfoList) {
        this.id = id;
        this.userid = userid;
        this.carid = carid;
        this.paymentvoucher = paymentvoucher;
        this.paymentstate = paymentstate;
        this.applicationtime = applicationtime;
        this.auctionnumber = auctionnumber;
        this.other = other;
        this.auctionstate = auctionstate;
        this.offerid = offerid;
        this.auctioninfoid = auctioninfoid;
        this.ispaybond = ispaybond;
        this.appointmentseecarname = appointmentseecarname;
        this.appointmentseecartel = appointmentseecartel;
        this.seecartime = seecartime;
        this.seecarmancertificates = seecarmancertificates;
        this.offerinfoList = offerinfoList;
    }

    public List<offerinfo> getOfferinfoList() {
        return offerinfoList;
    }

    public void setOfferinfoList(List<offerinfo> offerinfoList) {
        this.offerinfoList = offerinfoList;
    }

    public bidderoffer() {
        super();
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


    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getPaymentvoucher() {
        return paymentvoucher;
    }

    public void setPaymentvoucher(String paymentvoucher) {
        this.paymentvoucher = paymentvoucher == null ? null : paymentvoucher.trim();
    }

    public String getPaymentstate() {
        return paymentstate;
    }

    public void setPaymentstate(String paymentstate) {
        this.paymentstate = paymentstate == null ? null : paymentstate.trim();
    }

    public String getApplicationtime() {
        return applicationtime;
    }

    public void setApplicationtime(String applicationtime) {
        this.applicationtime = applicationtime == null ? null : applicationtime.trim();
    }

    public String getAuctionnumber() {
        return auctionnumber;
    }

    public void setAuctionnumber(String auctionnumber) {
        this.auctionnumber = auctionnumber == null ? null : auctionnumber.trim();
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }

    public String getAuctionstate() {
        return auctionstate;
    }

    public void setAuctionstate(String auctionstate) {
        this.auctionstate = auctionstate == null ? null : auctionstate.trim();
    }

    public String getOfferid() {
        return offerid;
    }

    public void setOfferid(String offerid) {
        this.offerid = offerid == null ? null : offerid.trim();
    }

    public Integer getAuctioninfoid() {
        return auctioninfoid;
    }

    public void setAuctioninfoid(Integer auctioninfoid) {
        this.auctioninfoid = auctioninfoid;
    }

    public String getIspaybond() {
        return ispaybond;
    }

    public void setIspaybond(String ispaybond) {
        this.ispaybond = ispaybond == null ? null : ispaybond.trim();
    }

    public String getAppointmentseecarname() {
        return appointmentseecarname;
    }

    public void setAppointmentseecarname(String appointmentseecarname) {
        this.appointmentseecarname = appointmentseecarname == null ? null : appointmentseecarname.trim();
    }

    public String getAppointmentseecartel() {
        return appointmentseecartel;
    }

    public void setAppointmentseecartel(String appointmentseecartel) {
        this.appointmentseecartel = appointmentseecartel == null ? null : appointmentseecartel.trim();
    }

    public String getSeecartime() {
        return seecartime;
    }

    public void setSeecartime(String seecartime) {
        this.seecartime = seecartime == null ? null : seecartime.trim();
    }

    public String getSeecarmancertificates() {
        return seecarmancertificates;
    }

    public void setSeecarmancertificates(String seecarmancertificates) {
        this.seecarmancertificates = seecarmancertificates == null ? null : seecarmancertificates.trim();
    }


}