package com.example.auction.Dao;

import com.example.auction.Model.SmsInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/22 15:25
 * @描述 :
 */
@Mapper
public interface SmsInfoDao {
    /**
     * 新增短信记录
     * @param smsInfo
     * @return
     */
    Integer addSmsInfo(SmsInfo smsInfo);

    /**
     * 根据电话和code查询数据
     * @param phone
     * @param code
     * @return
     */
    Integer countByPhoneAndCode(@Param("phone") String phone, @Param("code") String code);

    /**
     * 根据手机号查询数据
     * @param phone
     * @return
     */
    List<SmsInfo> selectByPhone(@Param("phone") String phone);

    /**
     * 更新状态
     * @param id
     * @param isDel
     * @return
     */
    Integer updateIsdel(@Param("id") Integer id, @Param("isdel") Integer isdel);

}
