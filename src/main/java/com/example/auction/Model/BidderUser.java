package com.example.auction.Model;

public class BidderUser {
    private String paymentvoucher;

    private String paymentstate;

    private String applicationtime;

    private String banumber;

    private String totalamount;

    private String Auctionstate;
    private String ispaybond;

    //private Integer userid;

    private String username;
    private String tel;
    private String certificates;
    private String address;

    public BidderUser() {
        super();
    }

    public BidderUser(String paymentvoucher, String paymentstate, String applicationtime, String banumber, String totalamount, String auctionstate, String ispaybond, String username, String tel, String certificates, String address) {
        this.paymentvoucher = paymentvoucher;
        this.paymentstate = paymentstate;
        this.applicationtime = applicationtime;
        this.banumber = banumber;
        this.totalamount = totalamount;
        Auctionstate = auctionstate;
        this.ispaybond = ispaybond;
        this.username = username;
        this.tel = tel;
        this.certificates = certificates;
        this.address = address;
    }

    public String getPaymentvoucher() {
        return paymentvoucher;
    }

    public void setPaymentvoucher(String paymentvoucher) {
        this.paymentvoucher = paymentvoucher;
    }

    public String getPaymentstate() {
        return paymentstate;
    }

    public void setPaymentstate(String paymentstate) {
        this.paymentstate = paymentstate;
    }

    public String getApplicationtime() {
        return applicationtime;
    }

    public void setApplicationtime(String applicationtime) {
        this.applicationtime = applicationtime;
    }

    public String getBanumber() {
        return banumber;
    }

    public void setBanumber(String banumber) {
        this.banumber = banumber;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }



    public String getIspaybond() {
        return ispaybond;
    }

    public void setIspaybond(String ispaybond) {
        this.ispaybond = ispaybond;
    }

    public String getAuctionstate() {
        return Auctionstate;
    }

    public void setAuctionstate(String auctionstate) {
        Auctionstate = auctionstate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
