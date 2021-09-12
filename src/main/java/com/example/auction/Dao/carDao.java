package com.example.auction.Dao;

import com.example.auction.Model.car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface carDao {
    int deleteByPrimaryKey(Integer id);

    int insert(car record);

    int insertSelective(car record);

    car selectByPrimaryKey(Integer id);
    //根据id集合获取数据
    List<car> selectAllByPrimaryKey(List<String> list);

    int updateByPrimaryKeySelective(car record);

    int updateByPrimaryKey(car record);

    List<car> carlist(Map map);

    Long countCarList(Map map);

   Map offercarList(@Param("carid") Integer carid, @Param("bidderid") String bidderid);

    int getByIdAndCarFrameNumberCount(@Param("carframenumber")String carframenumber);
}