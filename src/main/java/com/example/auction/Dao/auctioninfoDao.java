package com.example.auction.Dao;

import com.example.auction.Model.AuctionInfoUserLoss;
import com.example.auction.Model.acutionListInfo;
import com.example.auction.Model.auctioninfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    auctioninfo selectByauctionnumber(@Param("auctionnumber") String auctionnumber);

    List<Map> selectNobidder(@Param("userid") String userid,@Param("auctionnumber") String aucid);

    List<auctioninfo> selectList(Map map);

    List<auctioninfo> selectListk(Map map);
    List<auctioninfo> selectListk2(Map map);
    List<auctioninfo> selectListk3(Map map);
    List<auctioninfo> selectListk4(Map map);
    Integer selectListPageCount(Map map);

    List<Map> selectauction(Map map);

    List<auctioninfo> selectAllAuc();

    List<Map> selectListk5(Map map);
    //根据number查询
    List<auctioninfo> listByNumber(@Param("set")Set<String> set);
    //更新车辆图片地址
    int updateCarImg(auctioninfo auctioninfo);
    //更新流拍车辆图片
    int updateCarLossImg(auctioninfo auctioninfo);
    List<auctioninfo> selectList6(Map map);
    int selectList6Count(Map map);
    List<auctioninfo> selectList7(Map map);
    int selectList7Count(Map map);
    //流拍列表
    List<auctioninfo> lossAuction(Map map);
    int lossAuctionCount(Map map);
    //修改最高出价
    int updateLossAuction(auctioninfo auctioninfo);
}