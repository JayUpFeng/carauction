package com.example.auction.Dao;

import com.example.auction.Model.bidder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface bidderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(bidder record);

    int insertSelective(bidder record);

    bidder selectByPrimaryKey(Integer id);
    //获取标书id
    Integer getAuctionInfoId(Integer id);

    Integer getOtherById(Integer id);

    int updateByPrimaryKeySelective(bidder record);

    int updateByPrimaryKey(bidder record);

    bidder selectByUserid(@Param("userid") String userid,@Param("aucnumber") String aucnumber);

    String selectCarIdById(Integer id);
    //app端中标/成交/失败列表
    List<bidder> commonBidder(Map map);
    int updateCarImg(bidder bidder);
    //删除流拍的标书
    int deleteByAuctionNumber(String aucnumber);
    //查询最大流拍bidderId
    int getMaxPrice(String auctionnumber);
}