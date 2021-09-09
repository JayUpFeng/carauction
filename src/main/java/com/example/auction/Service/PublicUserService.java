package com.example.auction.Service;

import com.alibaba.fastjson.JSONObject;
import com.example.auction.Configuration.TemplateUtils;
import com.example.auction.Dao.PublicUserDao;
import com.example.auction.Model.PublicUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/22 15:37
 * @描述 :
 */
@Service
@Transactional
public class PublicUserService {
    @Autowired
    private PublicUserDao publicUserDao;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.appSecret}")
    private String secret;

    public Map<String, Object> addPublicUser(String code) {
        TemplateUtils templateUtils = new TemplateUtils();
        String result = templateUtils.getOpenId(code, secret, appid);
        String token = templateUtils.getToken(secret, appid);
        JSONObject resultJson = JSONObject.parseObject(result);
        System.out.println("jsonObject=============="+resultJson);
        Object openid = resultJson.get("openid");
        Object access_token = resultJson.get("access_token");
        String openId="";
        if (openid!=null){
            System.out.println("openid===="+openid);
            openId= (String) openid;
        }
        if (access_token!=null){
            System.out.println("access_token==="+access_token);
        }
        PublicUser publicUser = publicUserDao.getByOpenId(openId);
        Map<String, Object> map = new HashMap<>();
        if (publicUser != null) {
            map.put("msg", "用户已经存在");
            map.put("code", 0);
            map.put("user", publicUser);
        } else {
            String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openId + "&lang=zh_CN";
            String get = templateUtils.doGetPost(url, "GET", null);
            System.out.println("get==========" + get);
            JSONObject jsonObject = JSONObject.parseObject(get);
            System.out.println("jsonObject========" + jsonObject);
            String username = jsonObject.getString("nickname");
            String unionid = jsonObject.getString("unionid");
            String sex = jsonObject.getString("sex");
            PublicUser user = new PublicUser();
            user.setUnionid(unionid);
            user.setOpenid(openId);
            user.setUsername(username);
            user.setSex(sex);
            System.out.println("tostring===========" + user.toString());
            Integer count = publicUserDao.addPublicUser(user);
            System.out.println("count==="+count);
            if (count!=null&&count == 1) {
                map.put("openid", openId);
                map.put("user", user);
                map.put("msg", 1);
                map.put("code", 0);
            }else {
                map.put("openid", openId);
                map.put("msg", "入库失败！");
                map.put("code", 0);
            }
        }
        return map;
    }
}
