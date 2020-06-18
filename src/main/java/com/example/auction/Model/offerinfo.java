package com.example.auction.Model;

public class offerinfo {
    private Integer id;

    private String offer;

    private Integer bidderid;

    private Integer carid;

    private String state;

    public offerinfo(Integer id, String offer, Integer bidderid, Integer carid, String state) {
        this.id = id;
        this.offer = offer;
        this.bidderid = bidderid;
        this.carid = carid;
        this.state = state;
    }

    public offerinfo() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer == null ? null : offer.trim();
    }

    public Integer getBidderid() {
        return bidderid;
    }

    public void setBidderid(Integer bidderid) {
        this.bidderid = bidderid;
    }

    public Integer getCarid() {
        return carid;
    }

    public void setCarid(Integer carid) {
        this.carid = carid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }
}