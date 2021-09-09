package com.example.auction.Service;

import com.alibaba.fastjson.JSONException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.example.auction.Configuration.CommonResponse;
import com.example.auction.Dao.PublicUserDao;
import com.example.auction.Dao.SmsInfoDao;
import com.example.auction.Dao.Userdao;
import com.example.auction.Model.PublicUser;
import com.example.auction.Model.SmsInfo;
import com.example.auction.Model.Userinfo;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/22 15:37
 * @描述 :
 */
@Service
@Transactional
public class SmsInfoService {
    @Value("${ali.accesskeyid}")
    private String accessKeyId;
    @Value("${ali.accesskeysecret}")
    private String accessKeySecret;
    @Value("${ali.signname}")
    private String signName;
    @Value("${ali.templatecode}")
    private String templateCode;
    @Value("${tencent.appid}")
    private String appId;
    @Value("${tencent.appkey}")
    private String appkey;
    @Value("${tencent.templateId}")
    private String templateId;
    @Value("${tencent.smsSign}")
    private String smsSign;
    @Autowired
    private SmsInfoDao smsInfoDao;
    @Autowired
    private Userdao userdao;
    @Autowired
    private PublicUserDao publicUserDao;


    public Map<String,Object> getSmsCodeAli(String phone, Map<String,Object> map) {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Dysmsapi", "dysmsapi.aliyuncs.com");
        } catch (ClientException e1) {
            e1.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //随机生成六位验证码
        int code = (int) ((Math.random() * 9 + 1) * 100000);
        //把上一个设置为已删除
        List<SmsInfo> list = smsInfoDao.selectByPhone(phone);
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                SmsInfo info = list.get(i);
                smsInfoDao.updateIsdel(info.getId(), 1);
            }
        }
        /**
         * 验证码与号码存入数据库
         */
        SmsInfo smsInfo = new SmsInfo();
        smsInfo.setPhone(phone);
        smsInfo.setCode(code + "");
        smsInfo.setIsdel(0);
        smsInfoDao.addSmsInfo(smsInfo);
        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到，你在签名管理里的内容
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，你模板管理里的模板编号
        request.setTemplateCode(templateCode);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//        request.setOutId("yourOutId");
        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);
            String cod = sendSmsResponse.getCode();
            System.out.println("cod=====" + cod);
            //获取发送状态
            map.put("smsCode",code);
            return map;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return map;
    }

    public Map<String,Object> getSmsCodeTencent(String phone, Map<String,Object> map) {
        // 短信应用SDK AppID   // 1400开头
        int appid = Integer.parseInt(appId);
        // 短信应用SDK AppKey
//        String appkey = appkey;
        // 短信模板ID，需要在短信应用中申请
//        int templateId = Integer.parseInt(templateId) ;
        // 签名，使用的是`签名内容`，而不是`签名ID`
//        String smsSign = "";
        try {
            //参数，一定要对应短信模板中的参数顺序和个数，
            int code = (int) ((Math.random() * 9 + 1) * 100000);
            String[] params = {code+""};
            //创建ssender对象
            SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
            //发送
            SmsSingleSenderResult result = ssender.sendWithParam("86", phone,Integer.parseInt(templateId), params, smsSign, "", "");
            // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.println(result.toString());
            if(result.result==0){
                //把上一个设置为已删除
                List<SmsInfo> list = smsInfoDao.selectByPhone(phone);
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        SmsInfo info = list.get(i);
                        smsInfoDao.updateIsdel(info.getId(), 1);
                    }
                }
                /**
                 * 验证码与号码存入数据库
                 */
                SmsInfo smsInfo = new SmsInfo();
                smsInfo.setPhone(phone);
                smsInfo.setCode(code + "");
                smsInfo.setIsdel(0);
                smsInfoDao.addSmsInfo(smsInfo);
                map.put("smsCode",code);
                return map;
            }
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }catch (Exception e) {
            // 网络IO错误
            e.printStackTrace();
        }
        return map;
    }

    public Map<String, Object> checkSms(SmsInfo smsInfo) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "请重新获取验证码！");
        //1、校验接收的短信是否正确
        Integer count = smsInfoDao.countByPhoneAndCode(smsInfo.getPhone(), smsInfo.getCode());
        if (count != null && count > 0) {
            Userinfo byTel = userdao.getByTel(smsInfo.getPhone());
            if (byTel != null) {
                //更新企业微信用户的openid
                Integer state = byTel.getState();
                if (state!=null&&state==0){
                    userdao.updateOpenIdAndStateById(byTel.getId(), smsInfo.getOpenid(), 2);
                }else{
                    userdao.updateOpenIdById(byTel.getId(),smsInfo.getOpenid());
                }
                byTel.setOpenid(smsInfo.getOpenid());
                return CommonResponse.success1(byTel);
            } else {
                if (!StringUtils.isEmpty(smsInfo.getOpenid())){
                    //根据openid获取用户的username
                    PublicUser byOpenId = publicUserDao.getByOpenId(smsInfo.getOpenid());
                    if (byOpenId!=null){
                        Userinfo userinfo = new Userinfo();
                        userinfo.setUsername(byOpenId.getUsername());
                        userinfo.setOpenid(smsInfo.getOpenid());
                        userinfo.setOther(smsInfo.getPhone());
                        userinfo.setTel(smsInfo.getPhone());
                        userinfo.setState(1);
                        userinfo.setParticipate("2");
                        Integer maxUserNumber = userdao.getMaxUserNumber();
                        if (maxUserNumber!=null){
                            maxUserNumber+=1;
                        }else{
                            maxUserNumber=100000;
                        }
                        userinfo.setUsernumber(maxUserNumber);
                        userdao.addUser(userinfo);
                        return CommonResponse.success1(userinfo);
                    }else {
                        map.put("msg", "请重新关注公众号！");
                    }
                }else {
                    map.put("msg", "请重新关注公众号！");
                }
            }
        }
        return map;
    }
}
