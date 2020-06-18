package com.example.auction.Dao;

import com.example.auction.Model.acutionListInfo;
import com.example.auction.Model.auctioninfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface auctioninfoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(auctioninfo record);

    int insertSelective(auctioninfo record);

    auctioninfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(auctioninfo record);

    int updateByPrimaryKey(auctioninfo record);

   List<acutionListInfo> listauctioninfo(Map map);

    List<auctioninfo> listacutionList(Map map);
}