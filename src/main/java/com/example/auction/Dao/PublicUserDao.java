package com.example.auction.Dao;

import com.example.auction.Model.PublicUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/22 15:25
 * @描述 :
 */
@Mapper
public interface PublicUserDao {
    /**
     * 新增公众号用户
     * @param publicUser
     * @return
     */
    Integer addPublicUser(PublicUser publicUser);


    /**
     * 根据openid更新手机号
     * @param openId
     * @param phone
     * @return
     */
    Integer updatePhone(@Param("openid")String openid,@Param("phone")String phone);

    /**
     * 根据openid获取公众号用户
     * @param openId
     * @return
     */
    PublicUser getByOpenId(@Param("openid") String openid);

    /**
     * 获取全部用户信息
     * @param tel
     * @return
     */
    List<PublicUser> publicUserList(@Param("tel") String tel);


    /**
     * 获取公众号的所有openid
     * @return
     */
    List<String> getAllPublicUser();

    /**
     * 获取中间表的所有openid
     * @return
     */
    List<String> getAllUserPublic();

    /**
     * 根据企业微信用户获取openid
     * @param list
     * @return
     */
    List<String> getOpenIdByOthers(List<String> list);


}
