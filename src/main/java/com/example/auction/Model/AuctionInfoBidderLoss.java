package com.example.auction.Model;

/**
 * @作者：zhanghe
 * @时间：2021-09-08 17:33:10
 * @注释：流拍中间表
 */
public class AuctionInfoBidderLoss {
    private Integer id;
    private Integer auctioninfoid;
    private Integer bidderid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
}
