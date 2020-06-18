package com.example.auction.Dao;

import com.example.auction.Model.logistics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface logisticsDao {
    int deleteByPrimaryKey(Integer id);

    int insert(logistics record);

    int insertSelective(logistics record);

    logistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(logistics record);

    int updateByPrimaryKey(logistics record);
}