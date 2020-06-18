package com.example.auction.Dao;

import com.example.auction.Model.car;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface carDao {
    int deleteByPrimaryKey(Integer id);

    int insert(car record);

    int insertSelective(car record);

    car selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(car record);

    int updateByPrimaryKey(car record);

    List<car> carlist(car c);
}