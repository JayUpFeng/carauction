package com.example.auction.Dao;

import com.example.auction.Model.Userinfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface Userdao {

    Integer addUser(Userinfo userinfo);

    List<Map> listUser(Map map);

    List<Map> bondList(Map map);
    //查询总条数
    int bondListCount(Map map);

    List<Map> selectbidderList(Map map);
    List<Map> selectbidderList2(Map map);

    int updateByPrimaryKeySelective(Userinfo record);

    List<Map> selectNoacution(@Param("auctionnumber") String auctionnumber);
    Userinfo selectByPrimaryKey(@Param("id") Integer id);

    Userinfo selectuser2(@Param("userqyid") String userqyid, @Param("auctionnumber") String auctionnumber , @Param("auctionstate") String auctionstate);
   List<Userinfo> selectuser(@Param("auctionnumber") String auctionnumber);

    List<Userinfo> selectUser2(@Param("auctionnumber") String auctionnumber);

    Userinfo selectUserid(@Param("userids") String userid);

    List<Map> listuser2(Map map);

    List<Userinfo> selectauctionuser(@Param("auctionnumber")String auctionnumber, @Param("auctionstate") String auctionstate);
    List<Map> selectauctionuser2(@Param("auctionnumber")String auctionnumber, @Param("auctionstate") String auctionstate);
    List<Map> selectauctionuser3(@Param("auctionnumber")String auctionnumber, @Param("auctionstate") String auctionstate,@Param("isviolations") String isviolations);

    List<Map>  selectAllUser(Map map);


    //  acutionListInfo listauctioninfo(Map map);

//    String getOtherByTel(@Param("tel")String tel);
    //根据openid获取用户信息
    Userinfo getByOpenId(@Param("openid")String openid);
    //更新用户手机号信息
    Integer updateTelAndOtherById(@Param("id")Integer id,@Param("tel")String tel);
    //根据id更新openid
    Integer updateOpenIdAndStateById(@Param("id")Integer id,@Param("openid")String openid,@Param("state")Integer state);
    Integer updateOtherAndStateById(@Param("id")Integer id,@Param("username")String username,@Param("other")String other,@Param("state")Integer state);
    //根据id更新openid
    Integer updateOpenIdById(@Param("id")Integer id,@Param("openid")String openid);
    //获取所有公众号用户
    List<String> getByStates(List<Integer> list);
    //根据other获取openid
    List<String> getOpenIdByOthers(List<String> list);
    //根据手机号获取用户
    Userinfo getByTel(@Param("tel")String tel);
    //更新用户状态
    Integer updateStateById(@Param("state")Integer state,@Param("id")Integer id);
    //删除只保存公众号的用户
    void delById(Integer id);

    List<Map> selectAllUserInfo(Map map);

    Long countUserInfo(Map map);

    Integer getMaxUserNumber();
    List<Userinfo> selectAllUsers();
    Integer updateAllUser(Userinfo userinfo);
}
