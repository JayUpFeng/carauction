package com.example.auction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
@ComponentScan(basePackages = "com.example.auction.*")
public class CarauctionApplication  {



    public static void main(String[] args) {

        SpringApplication.run(CarauctionApplication.class, args);

    }




}

