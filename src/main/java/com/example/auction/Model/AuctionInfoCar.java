package com.example.auction.Model;

/**
 * @作者：zhanghe
 * @时间：2021-09-03 14:08:40
 * @注释： 中间表，保存竞拍和汽车数据，auctioninfo/car
 */
public class AuctionInfoCar {
    Integer auctioninfoid;
    Integer carid;

    public Integer getCarid() {
        return carid;
    }

    public Integer getAuctioninfoid() {
        return auctioninfoid;
    }

    public void setAuctioninfoid(Integer auctioninfoid) {
        this.auctioninfoid = auctioninfoid;
    }

    public void setCarid(Integer carid) {
        this.carid = carid;
    }
}
