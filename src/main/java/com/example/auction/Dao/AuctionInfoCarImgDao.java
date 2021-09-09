package com.example.auction.Dao;

import com.example.auction.Model.AuctionInfoCarImg;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @作者：zhanghe
 * @时间：2021-08-04 17:10:21
 * @注释：
 */
@Mapper
public interface AuctionInfoCarImgDao {
    AuctionInfoCarImg getById(Integer auctioninfoid);
    int insertInfo(AuctionInfoCarImg auctionInfoCarImg);
    int updateInfo(AuctionInfoCarImg auctionInfoCarImg);
}
