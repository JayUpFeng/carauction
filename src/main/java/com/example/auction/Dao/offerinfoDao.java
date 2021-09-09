package com.example.auction.Dao;

import com.example.auction.Model.offerinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface offerinfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(offerinfo record);

    int insertSelective(offerinfo record);

    offerinfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(offerinfo record);

    int updateByPrimaryKey(offerinfo record);

    List<offerinfo> selectBybidderid(@Param("bidderid") Integer bidderid);

    offerinfo selectBybidderidandcarid(offerinfo offerinfo);

    int del(offerinfo o);
}