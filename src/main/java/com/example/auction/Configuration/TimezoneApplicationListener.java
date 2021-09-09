package com.example.auction.Configuration;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.TimeZone;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/24 10:07
 * @描述 :
 */
@Component
public class TimezoneApplicationListener implements ApplicationListener<ContextRefreshedEvent>  {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
}
