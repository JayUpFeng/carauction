package com.example.auction.Dao;

import com.example.auction.Model.Visit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/22 15:25
 * @描述 :
 */
@Mapper
public interface VisitDao {

    Integer addVisit(Visit visit);

    List<Visit> list(Map map);

    List<Visit> getByUserId(@Param("userid") Integer userid);

    List<Visit> getByUserIdAuctionIdCarId(@Param("userid") Integer userid,@Param("auctionid") Integer auctionid,@Param("carid") Integer carid);

    Integer updateState(@Param("userid") Integer userId,@Param("state") Integer state);

}
