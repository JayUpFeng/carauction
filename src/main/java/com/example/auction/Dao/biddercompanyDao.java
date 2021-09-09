package com.example.auction.Dao;

import com.example.auction.Model.biddercompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface biddercompanyDao {
    int deleteByPrimaryKey(Integer id);

    int insert(biddercompany record);

    int insertSelective(biddercompany record);

    biddercompany selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(biddercompany record);

    int updateByPrimaryKey(biddercompany record);

    biddercompany selectByUserid(@Param("userid") Integer userid);
}