package com.example.auction.Dao;

import com.example.auction.Model.User;
import com.example.auction.Model.acutionListInfo;
import com.example.auction.Model.biddercompany;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface Userdao {

    Integer addUser(User user);

    List<Map> listUser(Map map);

    List<Map> bondList(Map map);

    List<Map> selectbidderList(@Param("auctionnumber") String auctionnumber);


    //  acutionListInfo listauctioninfo(Map map);
}
