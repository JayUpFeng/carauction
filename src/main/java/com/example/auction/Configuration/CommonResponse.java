package com.example.auction.Configuration;

import com.example.auction.Model.PublicUser;
import com.example.auction.Model.Userinfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/23 21:38
 * @描述 :
 */
public class CommonResponse {
    public static Map<String,Object> fail(String msg){
        User user =new User();
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("msg",msg);
        map.put("user",user);
        return map;
    }
    public static Map<String,Object> publicSuccess(PublicUser publicUser){
        User user =new User();
        user.setId(publicUser.getId());
        user.setOpenid(publicUser.getOpenid());
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("msg",2);
        map.put("user",user);
        return map;
    }
    public static Map<String,Object> success1(Userinfo userinfo){
        User user =new User();
        user.setId(userinfo.getId());
        user.setOpenid(userinfo.getOpenid());
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("msg",1);
        map.put("user",user);
        return map;
    }
    public static Map<String,Object> success2(Userinfo userinfo){
        User user =new User();
        user.setId(userinfo.getId());
        user.setOpenid(userinfo.getOpenid());
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("msg",2);
        map.put("user",user);
        return map;
    }
}
class User{
    private Integer id;
    private String openid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
class Test{
    private Integer id;
    private String name;
    private Integer age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
