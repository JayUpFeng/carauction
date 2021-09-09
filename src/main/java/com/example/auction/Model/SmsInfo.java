package com.example.auction.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/23 10:02
 * @描述 :
 */
@Table(name = "sms_info")
public class SmsInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",length = 20,nullable = false)
    private Integer id;
    @Column(name = "phone" ,length = 50 ,columnDefinition = "微信全局唯一id")
    private String phone;
    @Column(name = "code" ,length = 50 ,columnDefinition = "验证码")
    private String code;
    @Column(name = "createtime" ,length = 50 ,columnDefinition = "添加时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createtime;
    @Column(name = "isdel",length = 2,nullable = false,columnDefinition = "是否删除：0：未删除1：已删除")
    private Integer isdel;
    private String openid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
