package com.example.auction.Model;

import javax.persistence.*;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/22 15:10
 * @描述 : 公众号信息
 */
@Table(name = "public_num")
public class PublicUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",length = 20,nullable = false)
    private Integer id;
    @Column(name = "unionid" ,length = 50 , nullable = false,columnDefinition = "微信全局唯一id")
    private String unionid;
    @Column(name = "openid" ,length = 50 , nullable = false,columnDefinition = "每个微信平台不同")
    private String openid;
    @Column(name = "username" ,length = 50 , nullable = false,columnDefinition = "用户名")
    private String username;
    @Column(name = "tel" ,length = 50 , nullable = false,columnDefinition = "电话")
    private String tel;
    @Column(name = "sex" ,length = 50 , nullable = false,columnDefinition = "性别")
    private String sex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "PublicUser{" +
                "id=" + id +
                ", unionId='" + unionid + '\'' +
                ", openid='" + openid + '\'' +
                ", username='" + username + '\'' +
                ", tel='" + tel + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
