package com.example.auction.Dao;

import com.example.auction.Model.offerinfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface offerinfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(offerinfo record);

    int insertSelective(offerinfo record);

    offerinfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(offerinfo record);

    int updateByPrimaryKey(offerinfo record);
}