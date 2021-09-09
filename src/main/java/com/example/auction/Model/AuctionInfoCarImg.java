package com.example.auction.Model;

/**
 * @作者：zhanghe
 * @时间：2021-08-04 17:06:37
 * @注释：
 */
public class AuctionInfoCarImg {
    private Integer auctioninfoid;
    private Integer bidderid;
    private Integer carid;
    private String carimg;
    private String usernumber;

    public Integer getAuctioninfoid() {
        return auctioninfoid;
    }

    public void setAuctioninfoid(Integer auctioninfoid) {
        this.auctioninfoid = auctioninfoid;
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

    public String getCarimg() {
        return carimg;
    }

    public void setCarimg(String carimg) {
        this.carimg = carimg;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }
}
