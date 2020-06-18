package com.example.auction.Dao;

import com.example.auction.Model.bidder;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface bidderDao {
    int deleteByPrimaryKey(Integer id);

    int insert(bidder record);

    int insertSelective(bidder record);

    bidder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(bidder record);

    int updateByPrimaryKey(bidder record);
}