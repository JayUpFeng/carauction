package com.example.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@SpringBootApplication//(exclude = {DataSourceAutoConfiguration.class})
//@ComponentScan(basePackages ="com.example.auction.*")
public class CarauctionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarauctionApplication.class, args);
    }

   /* @RequestMapping
    public String hello() {
        return "hello spring boot!";
    }*/
}

