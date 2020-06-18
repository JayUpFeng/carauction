package com.example.auction.Service;

import com.alibaba.fastjson.JSON;
import com.example.auction.Configuration.H_Format;
import com.example.auction.Dao.Userdao;

import com.example.auction.Dao.auctioninfoDao;
import com.example.auction.Dao.biddercompanyDao;
import com.example.auction.Dao.carDao;
import com.example.auction.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private Userdao userdao;
    @Autowired
    private auctioninfoDao auctionDao;
    @Autowired
    private carDao carDao;
    @Autowired
    private biddercompanyDao biddercompanyDao;


    public Map addUser(User user) {
        Map map=new HashMap();
        map.put("msg",0);
      Integer code=  userdao.addUser(user);
      if(code==1){
          map.put("msg",1);
      }
        return map;
    }

    public Map listUser(Map map) {
        Map m=new HashMap();
        m.put("msg",0);
        List<Map> list=userdao.listUser(map);
        if(list.size()>0 && list!=null){
            m.put("msg",1);
            m.put("bidderList",list);
        }
        return m;
    }

    public Map addauctioninfo(auctioninfo auctioninfo) {
        Map m=new HashMap();
        m.put("msg",0);
       String date= String.valueOf(new Date().getTime());
        auctioninfo.setAuctionnumber(date);
        Date d=new Date();
        SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=formatter.format(d);
        auctioninfo.setOther(time);
       int count= auctionDao.insert(auctioninfo);
       if(count>0){
           m.put("msg",1);
       }
        return m;
    }

    public Map listauctioninfo(Map map) {
        Map m=new HashMap();
        m.put("msg",0);
        List<auctioninfo> auctioninfoList=auctionDao.listacutionList(map);
        List list=new ArrayList();
        for(auctioninfo a:auctioninfoList){

           Map auctionMap= JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str=a.getCarid().split(",");
            List carList=new ArrayList();
            for(String s:str){
               car car= carDao.selectByPrimaryKey(Integer.parseInt(s));
               carList.add(car);
            }
            auctionMap.put("carList",carList);
            //查询参与的竞拍人
           List<Map> bidderList= userdao.selectbidderList(a.getAuctionnumber());
           auctionMap.put("bidderList",bidderList);

           list.add(auctionMap);
        }
        //List<acutionListInfo> list=auctionDao.listauctioninfo(map);
        if(list.size()>0 && list !=null){
            m.put("msg",1);
            m.put("list",list);
        }

        return m;
    }

    public Map addCar(car c) {
        Map m=new HashMap();
        m.put("msg",0);
        c.setCarnumber(String.valueOf(H_Format.getRandomNum()));
        int count=carDao.insert(c);
        if(count>0){
            m.put("msg",1);
        }


        return m;
    }

    public Map carlist(car c) {
        Map m=new HashMap();
        m.put("msg",0);
       List<car> carList= carDao.carlist(c);
        if(carList.size()>0 && carList !=null){
            m.put("msg",1);
            m.put("carlist",carList);
        }
        return m;
    }

    public Map updatacar(car c) {
        Map map=new HashMap();
        map.put("msg",0);
        Integer code=  carDao.updateByPrimaryKeySelective(c);
        if(code==1){
            map.put("msg",1);
        }
        return map;
    }

    public Map delcar(car c) {
        Map map=new HashMap();
        map.put("msg",0);
        Integer code=  carDao.deleteByPrimaryKey(c.getId());
        if(code==1){
            map.put("msg",1);
        }
        return map;
    }

    public Map bondList(Map map) {
        Map m=new HashMap();
        m.put("msg",0);
       List<Map> biddercompanyList =  userdao.bondList(map);
        if(biddercompanyList.size()>0 && biddercompanyList !=null){
            m.put("msg",1);
            m.put("biddercompanyList",biddercompanyList);
        }
        return m;
    }

    public Map updateList(biddercompany biddercompany) {
        Map m=new HashMap();
        m.put("msg",0);
        int count= biddercompanyDao.updateByPrimaryKeySelective(biddercompany);
        if(count>0){
            m.put("msg",1);
        }

        return m;
    }
}
