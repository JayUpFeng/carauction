package com.example.auction.Configuration;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class TemplateUtils {
    /**
     * 获取token
     * @return token
     */


    public String getToken(String secret,String appid){
        //授予形式
        String grant_type = "client_credential";
        // appid = appid;
        //String secret = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        //接口地址拼接参数
        String getTokenApi = "https://api.weixin.qq.com/cgi-bin/token?grant_type="+grant_type+"&appid="+appid+"&secret="+secret;

        String tokenJsonStr =  doGetPost(getTokenApi,"GET",null);
        JSONObject tokenJson = JSONObject.parseObject(tokenJsonStr);
        String token = tokenJson.get("access_token").toString();
        System.out.println("获取到的TOKEN : "+token);

        return token;
    }

    public String getcorpToken(String corpsecret,String corpid){
        //授予形式access_token===access_token===access_token===
        String grant_type = "client_credential";
        // appid = appid;
        //String secret = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
        //接口地址拼接参数
        String getTokenApi = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+corpid+"&corpsecret="+corpsecret;

        String tokenJsonStr =  doGetPost(getTokenApi,"GET",null);
        JSONObject tokenJson = JSONObject.parseObject(tokenJsonStr);
        String token = tokenJson.get("access_token").toString();
        System.out.println("获取到的TOKEN : "+token);

        return token;
    }


    public String getOpenId(String code,String secret,String appid){
        System.out.println("1code=="+code);
        System.out.println("1secret=="+secret);
        System.out.println("1appid=="+appid);
        Map params = new HashMap();
        params.put("secret", secret);
        params.put("appid", appid);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
//        String result = HttpGetUtil.httpRequestToString(
//                "https://api.weixin.qq.com/sns/oauth2/access_token", params);
        String url="https://api.weixin.qq.com/sns/oauth2/access_token?appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
        String doGetPost = doGetPost(url, "GET", null);
        return doGetPost;
    }
    public String getOpenId2(String code,String secret,String appid){
        System.out.println("2code=="+code);
        System.out.println("2secret=="+secret);
        System.out.println("2appid=="+appid);
        Map params = new HashMap();
        params.put("secret", secret);
        params.put("appid", appid);
        params.put("grant_type", "authorization_code");
        params.put("js_code", code);
//        String result = HttpGetUtil.httpRequestToString(
//                "https://api.weixin.qq.com/sns/oauth2/access_token", params);
        String doGetPost = doGetPost("https://api.weixin.qq.com/sns/jscode2session", "POST", params);
        JSONObject jsonObject = JSONObject.parseObject(doGetPost);
        System.out.println("jsonObject=============="+jsonObject);
        Object openid = jsonObject.get("openid");
        if (openid!=null){
            return (String)openid;
        }
        return "";
    }


//
    public String doGetPost(String apiPath, String type, Map<String,Object> paramMap){

        OutputStreamWriter out = null;
        InputStream is = null;
        String result = null;
        try{
            URL url = new URL(apiPath);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod(type) ; // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();

            if(type.equals("POST")){
                out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); // utf-8编码
                out.append(JSON.toJSONString(paramMap));
                out.flush();
                out.close();
            }


            // 读取响应
            is = connection.getInputStream();
            int length = (int) connection.getContentLength();// 获取长度
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                result = new String(data, "UTF-8"); // utf-8编码

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  result;
    }
    public  String CODE_TO_USERINFO = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token=ACCESS_TOKEN&code=CODE&agentid=AGENTID";

    public  String getUserID(String access_token, String code, String agentid) {
        String UserId = "";
        CODE_TO_USERINFO = CODE_TO_USERINFO.replace("ACCESS_TOKEN", access_token).replace("CODE", code).replace("AGENTID", agentid);
        String js = doGetPost(CODE_TO_USERINFO, "GET", null);
        JSONObject jsonobject = JSONObject.parseObject(js);
        if (null != jsonobject) {

            if (jsonobject.containsKey("errcode") ){ //是否有错误
                if (jsonobject.getString("errcode").equals("40029")){  //是否为code错误

                    String newstr=   CODE_TO_USERINFO.substring(0, CODE_TO_USERINFO.indexOf("&code=")).concat("&code=").concat(code); //去除错误的code加入正确的

                    String st = doGetPost(newstr, "GET", null);//重新发起请求
                     jsonobject = JSONObject.parseObject(st);

                }

            }

            UserId = jsonobject.getString("UserId");

            if (!"".equals(UserId)) {
                System.out.println("获取信息成功，o(∩_∩)o ————UserID:" + UserId);
            }

        }
        return UserId;
    }

    public  String CODE_TO_USERINFO_U = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID";

    public JSONObject getUserinfo(String token, String userid) {
        CODE_TO_USERINFO_U = CODE_TO_USERINFO_U.replace("ACCESS_TOKEN", token).replace("USERID", userid);
        String js = doGetPost(CODE_TO_USERINFO_U, "GET", null);
        JSONObject jsonobject = JSONObject.parseObject(js);
        return jsonobject;
    }
}
