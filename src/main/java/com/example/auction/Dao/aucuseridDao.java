package com.example.auction.Dao;

import com.example.auction.Model.auctioninfo;
import com.example.auction.Model.aucuserid;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface aucuseridDao {
    int deleteByPrimaryKey(Integer id);

    int insert(aucuserid record);

    int insertSelective(aucuserid record);

    aucuserid selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(aucuserid record);

    int updateByPrimaryKey(aucuserid record);

    List<auctioninfo> selectAllAuc();

    List<aucuserid> selectAll();

    aucuserid selectByaucid(aucuserid aucuserid);
    aucuserid selectByAucId(@Param("aucuserid") Integer aucuserid);
    int updateIssend(@Param("issend") String  issend,@Param("id") Integer id);
}