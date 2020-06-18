package com.example.auction.Model;

public class logistics {
    private Integer id;

    private String addresseename;

    private String addresseetel;

    private Integer userid;

    private String startaddress;

    private String endaddress;

    private String state;

    private String pay;

    private String other;

    public logistics(Integer id, String addresseename, String addresseetel, Integer userid, String startaddress, String endaddress, String state, String pay, String other) {
        this.id = id;
        this.addresseename = addresseename;
        this.addresseetel = addresseetel;
        this.userid = userid;
        this.startaddress = startaddress;
        this.endaddress = endaddress;
        this.state = state;
        this.pay = pay;
        this.other = other;
    }

    public logistics() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddresseename() {
        return addresseename;
    }

    public void setAddresseename(String addresseename) {
        this.addresseename = addresseename == null ? null : addresseename.trim();
    }

    public String getAddresseetel() {
        return addresseetel;
    }

    public void setAddresseetel(String addresseetel) {
        this.addresseetel = addresseetel == null ? null : addresseetel.trim();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getStartaddress() {
        return startaddress;
    }

    public void setStartaddress(String startaddress) {
        this.startaddress = startaddress == null ? null : startaddress.trim();
    }

    public String getEndaddress() {
        return endaddress;
    }

    public void setEndaddress(String endaddress) {
        this.endaddress = endaddress == null ? null : endaddress.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay == null ? null : pay.trim();
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }
}