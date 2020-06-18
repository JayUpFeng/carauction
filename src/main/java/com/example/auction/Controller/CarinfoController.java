package com.example.auction.Controller;

import com.example.auction.Model.User;
import com.example.auction.Model.auctioninfo;
import com.example.auction.Model.biddercompany;
import com.example.auction.Model.car;
import com.example.auction.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/car")
public class CarinfoController {

    @Autowired
    private  UserService userService;

    //添加二手车
        @RequestMapping(value = "/addCar")
        @ResponseBody
        public Map addCar(@RequestBody car c) {
            Map map=new HashMap();

               map =  userService.addCar(c);

            return map;
        }

        //查询车辆
    @RequestMapping(value = "/carlist")
    @ResponseBody
    public Map carlist(@RequestBody car c) {
        Map map=new HashMap();

        map =  userService.carlist(c);

        return map;
    }

    //修改车辆信息
    @RequestMapping(value = "/updatacar")
    @ResponseBody
    public Map updatacar(@RequestBody car c) {
        Map map=new HashMap();

        map =  userService.updatacar(c);

        return map;
    }

    //删除车辆
    @RequestMapping(value = "/delcar")
    @ResponseBody
    public Map delcar(@RequestBody car c) {
        Map map=new HashMap();

        map =  userService.delcar(c);

        return map;
    }

    //查询竞拍者列表(可根据手机号和竞拍编号查询)
    @RequestMapping(value = "/listBidder")
    @ResponseBody
    public Map listUser(@RequestBody Map map) {
            Map m =  userService.listUser(map);
            return m;
    }

    //添加竞拍
    @RequestMapping(value = "/addauctioninfo")
    @ResponseBody
    public Map addauctioninfo(@RequestBody auctioninfo auctioninfo) {
        Map m =  userService.addauctioninfo(auctioninfo);
        return m;
    }

    //查询竞拍列表
    @RequestMapping(value = "/listauctioninfo")
    @ResponseBody
    public Map listauctioninfo(@RequestBody Map map) {
        Map m =  userService.listauctioninfo(map);
        return m;
    }

    //查询保证金列表
    @RequestMapping(value = "/bondList")
    @ResponseBody
    public Map bondList(@RequestBody Map map) {
        Map m =  userService.bondList(map);
        return m;
    }

    //修改保证金的状态
    @RequestMapping(value = "/updateList")
    @ResponseBody
    public Map updateList(@RequestBody biddercompany biddercompany) {
        Map m =  userService.updateList(biddercompany);
        return m;
    }
}
