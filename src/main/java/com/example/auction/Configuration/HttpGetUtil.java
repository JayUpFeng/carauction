package com.example.auction.Configuration;

import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class HttpGetUtil {

        public static String httpRequestToString(String url,
                                                 Map<String,String> params) {
            String result = null;
            try {
                InputStream is = httpRequestToStream(url,  params);
                BufferedReader in = new BufferedReader(new InputStreamReader(is,
                        "UTF-8"));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }
                result = buffer.toString();
            } catch (Exception e) {
                return null;
            }
            return result;
        }

        private static InputStream httpRequestToStream(String url,
                                                       Map<String, String> params) {
            InputStream is = null;
            try {
                String parameters = "";
                boolean hasParams = false;
                for(String key : params.keySet()){
                    String value = null;
                    try {
                        value = URLEncoder.encode(params.get(key), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    parameters += key +"="+ value +"&";
                    hasParams = true;
                }
                if(hasParams){
                    parameters = parameters.substring(0, parameters.length()-1);
                }


                url += "?"+ parameters;

                URL u = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.setRequestProperty("contentType", "utf-8");
                conn.setConnectTimeout(50000);
                conn.setReadTimeout(50000);
                conn.setDoInput(true);
                //设置请求方式，默认为GET
                conn.setRequestMethod("GET");


                is = conn.getInputStream();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return is;
        }

        public static String  GetCodeRequest1 = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect";
        public static String getCodeRequest(String appid){
            HttpClient client = null;
            String result = null;
            String appId = appid;
            String REDIRECT_URI= "";//回调请求地址
            String SCOPE="snsapi_base";

            GetCodeRequest1  = GetCodeRequest1.replace("APPID", urlEnodeUTF8(appId));
            GetCodeRequest1  = GetCodeRequest1.replace("REDIRECT_URI",urlEnodeUTF8(REDIRECT_URI));
            GetCodeRequest1 = GetCodeRequest1.replace("SCOPE", SCOPE);
            result = GetCodeRequest1;

            System.out.println(REDIRECT_URI);

            return result;
        }
        public static String urlEnodeUTF8(String str){
            String result = str;
            try {
                result = URLEncoder.encode(str,"UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

}
