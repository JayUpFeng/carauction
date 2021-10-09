package com.example.auction.Dao;

import com.example.auction.Model.AuctionInfoUserLoss;
import com.example.auction.Model.Userinfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @作者：zhanghe
 * @时间：2021-09-08 17:35:02
 * @注释：
 */
@Mapper
public interface AuctionInfoUserLossDao {
    int save(AuctionInfoUserLoss auctionInfoUserLoss);
    Userinfo getUserInfo(Integer auctioninfoid);
}
