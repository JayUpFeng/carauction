package com.example.auction.Controller;

import com.example.auction.Configuration.CommonResponse;
import com.example.auction.Configuration.Utils;
import com.example.auction.Model.SmsInfo;
import com.example.auction.Model.Userinfo;
import com.example.auction.Service.SmsInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/23 12:34
 * @描述 :
 */
@RestController
@RequestMapping("/car/sms")
public class SmsInfoController {
    private Logger logger = LoggerFactory.getLogger(SmsInfoController.class);
    @Autowired
    private SmsInfoService smsInfoService;

    @GetMapping("/getSmsCode")
    public Map<String,Object> getSmsCode(String phone){
        Map<String,Object> map=new HashMap<>();
        map.put("code",0);
        map.put("msg",1);
        try {
            return smsInfoService.getSmsCodeTencent(phone,map);
        }catch (Exception e){
            logger.error(Utils.getStackMsg(e));
        }
        return map;
    }
    @PostMapping("/checkSms")
    public Map checkSms(@RequestBody SmsInfo smsInfo){
        try {
            return smsInfoService.checkSms(smsInfo);
        }catch (Exception e){
            logger.error(Utils.getStackMsg(e));
        }
        return CommonResponse.success1(new Userinfo());
    }
}
