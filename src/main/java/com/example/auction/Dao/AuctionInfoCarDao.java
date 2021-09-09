package com.example.auction.Dao;

import com.example.auction.Model.AuctionInfoCar;
import org.apache.ibatis.annotations.Mapper;

/**
 * @作者：zhanghe
 * @时间：2021-09-03 14:12:34
 * @注释：
 */
@Mapper
public interface AuctionInfoCarDao {
    int save(AuctionInfoCar auctionInfoCar);
    int getByInfoIdAndCarId(AuctionInfoCar auctionInfoCar);
    int delete(AuctionInfoCar auctionInfoCar);
}
