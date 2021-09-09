package com.example.auction.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.auction.Configuration.CommonResponse;
import com.example.auction.Configuration.H_Format;
import com.example.auction.Configuration.TemplateUtils;
import com.example.auction.Configuration.uploadPic;
import com.example.auction.Dao.*;

import com.example.auction.Model.*;
import com.example.auction.util.ImgUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private offerinfoDao offerinfoDao;
    @Autowired
    private bidderDao bidderDao;
    @Autowired
    private aucuseridDao aucuseridDao;
    @Autowired
    private PublicUserDao publicUserDao;
    @Autowired
    private VisitDao visitDao;
    @Autowired
    private AuctionInfoCarImgDao auctionInfoCarImgDao;
    @Autowired
    private AuctionInfoCarDao auctionInfoCarDao;
    @Autowired
    private AuctionInfoBidderLossDao auctionInfoBidderLossDao;
    @Value("${customer.email}")
    private String email;
    @Value("${wx.appid}")
    private String appid;
    @Value("${wx.appSecret}")
    private String secret;

    @Value("${qwx.corpid}")
    private String corpid;
    @Value("${qwx.corpsecret}")
    private String corpsecret;

    @Value("${qwx.agentid}")
    private String agentid;
    @Value("${qy.url}")
    private String qyurl;
    @Value("${gzh.url}")
    private String gzhUrl;

    public int PAGE_SIZE = 30;

    public static Calendar c;
    //public static long endTime;
    public static Date date;
    public static long startTime;
    public static long midTime;
    String token2 = null;
    String template_id = null;

    public Map addUser(Userinfo userinfo) {
        System.out.println("userinfo=" + userinfo);
        TemplateUtils templateUtils = new TemplateUtils();
        //String openid=  templateUtils.getOpenId(userinfo.getOther(),secret,appid);
        Map map = new HashMap();
        map.put("msg", 0);
        String token = templateUtils.getcorpToken(corpsecret, corpid);//"4ogx6QHNSA48HGQfX6ReOEaArlsV3jSaWEUI6hVi56sk6d450NJ-lS2LqYR8Q9HH3JiaSkhzihr3E0HcAsa04cQbVZ6nZxSgWUrfW46Bggm1_BowzC4P0JIDZAjuKof0_fb45tAsKYvKG88DskDFAyTpBKvAExfiU_69_QR4GhOhp1QMd5P7gl3GwStgWMrXAysvmWRzfMceT4IpJ_KHdg";//
        String userid = templateUtils.getUserID(token, userinfo.getOther(), agentid);

        JSONObject jsonObject = templateUtils.getUserinfo(token, userid);
        System.out.println("jsonObject=" + jsonObject);
        if ("".equals(jsonObject.getString("name"))) {
            userinfo.setUsername(userid);
        } else {
            userinfo.setUsername(jsonObject.getString("name"));
        }
        userinfo.setOther(userid);
        String tel = jsonObject.getString("mobile");
        if (!StringUtils.isEmpty(tel)) {
            Userinfo byTel = userdao.getByTel(tel);
            System.out.println("byTel==============" + byTel);
            //公众号用户已经注册
            if (byTel != null) {
                Integer state = byTel.getState();
                if (state == 1) {
                    //更新other和state
                    userdao.updateOtherAndStateById(byTel.getId(), userinfo.getUsername(), userid, 2);
                    return CommonResponse.success1(byTel);
                }
                return CommonResponse.success1(byTel);
            } else {
                //企业微信第一次登陆
                userinfo.setTel(tel);
                userinfo.setParticipate("2");
                userinfo.setState(0);
                Integer maxUserNumber = userdao.getMaxUserNumber();
                if (maxUserNumber != null) {
                    maxUserNumber += 1;
                } else {
                    maxUserNumber = 100000;
                }
                userinfo.setUsernumber(maxUserNumber);
                Integer code = userdao.addUser(userinfo);
                if (code == 1) {
                    return CommonResponse.success1(userinfo);
                }
            }
        }
//        Userinfo u = userdao.selectUserid(userid);
//        System.out.println("u=============="+u);
//        if (u != null) {
//            Map<String, Object> stringObjectMap = CommonResponse.success1(u);
//            System.out.println("stringObjectMap=============="+stringObjectMap);
//            return stringObjectMap;
//        } else {
//        }
        return map;
    }

    public Map addPublicUser(String code) {
        Map map = new HashMap();
        map.put("msg", 0);
        TemplateUtils templateUtils = new TemplateUtils();
        String result = templateUtils.getOpenId(code, secret, appid);
        String token = templateUtils.getToken(secret, appid);
        JSONObject resultJson = JSONObject.parseObject(result);
        System.out.println("jsonObject==============" + resultJson);
        Object openid = resultJson.get("openid");
        Object access_token = resultJson.get("access_token");
        String openId = "";
        if (openid != null) {
            System.out.println("openid====" + openid);
            openId = (String) openid;
        }
        if (access_token != null) {
            System.out.println("access_token===" + access_token);
        }
        Userinfo userinfo = userdao.getByOpenId(openId);
        if (userinfo != null) {
            return CommonResponse.success1(userinfo);
        } else {
            if (StringUtils.isEmpty(openId)) {
                return CommonResponse.fail("openId为空！请从新登陆！");
            }
            PublicUser userDb = publicUserDao.getByOpenId(openId);
            System.out.println("userDb==" + userDb);
            if (userDb != null) {
                return CommonResponse.publicSuccess(userDb);
            } else {
                String url = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + token + "&openid=" + openId + "&lang=zh_CN";
                String get = templateUtils.doGetPost(url, "GET", null);
                JSONObject jsonObject = JSONObject.parseObject(get);
                String username = jsonObject.getString("nickname");
                String sex = jsonObject.getString("sex");
                PublicUser publicUser = new PublicUser();
                publicUser.setUsername(username);
                publicUser.setOpenid(openId);
                publicUser.setSex(sex);
                Integer count = publicUserDao.addPublicUser(publicUser);
                return CommonResponse.publicSuccess(publicUser);
            }
        }
    }

    public Map listUser(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        map.put("code", 0);


        PageHelper.startPage(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("limit").toString()));
        List<Map> list = userdao.listUser(map);
        PageInfo<Map> pageInfo = new PageInfo<>(list);
        if (list.size() > 0 && list != null) {
            m.put("msg", 1);

            m.put("code", 0);

            m.put("total", pageInfo.getTotal());
            m.put("data", pageInfo.getList());
            m.put("pageNum", pageInfo.getPageNum());
            m.put("pageSize", pageInfo.getPageSize());
            m.put("size", pageInfo.getSize());
            m.put("pages", pageInfo.getPages());
        }
        return m;
    }

    public Map addauctioninfo(auctioninfo auctioninfo) {
        Map m = new HashMap();
        m.put("msg", 0);
        car car = new car();
        bidder b = new bidder();
        if (auctioninfo.getCarid() != null) {
            String date = String.valueOf(new Date().getTime());
            auctioninfo.setAuctionnumber(date);
            Date d = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = formatter.format(d);
            auctioninfo.setOther(time);
            auctioninfo.setState("2");
            //auctioninfo.setIssesscar("1");
            int count = auctionDao.insertSelective(auctioninfo);
            String[] carstr = auctioninfo.getCarid().split(",");
            for (String carid : carstr) {
                car.setId(Integer.parseInt(carid));
                car.setOther("1");
                carDao.updateByPrimaryKeySelective(car);
                //保存数据到中间表
                AuctionInfoCar auctionInfoCar=new AuctionInfoCar();
                auctionInfoCar.setAuctioninfoid(auctioninfo.getId());
                auctionInfoCar.setCarid(Integer.parseInt(carid));
                auctionInfoCarDao.save(auctionInfoCar);
            }

            if (count > 0) {
                m.put("auctionid", auctioninfo.getId());
                m.put("endtime", auctioninfo.getAuctionendtime());
                m.put("msg", 1);
                m.put("code", 0);
            }
        } else {
            m.put("msg", "0");
            m.put("tit", "汽车id不能为空");
        }


        return m;
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public Map listauctioninfo0(Map map) throws ParseException {
        Map m = new HashMap();
        m.put("msg", 0);


        Map biddermap = new HashMap();
        List list = null;

        if (map.get("auctiontime") != null) {
            if ("other".equals(map.get("auctiontime").toString())) {
                map.remove("auctiontime");
                map.put("other", "other");
            } else if ("auctionendtime".equals(map.get("auctiontime").toString())) {
                map.remove("auctiontime");
                map.put("auctionendtime", "auctionendtime");
            }

        } else {
            map.put("id", "id");
        }


        // List<auctioninfo> auctioninfoList = auctionDao.selectList(map);
        List<acutionListInfo> acutionListInfoList = new ArrayList<>();

        if ("2".equals(map.get("state")) && map.get("userid2") != null && !"".equals(map.get("userid2"))) {
            //根据id查询userinfo表中用户
            Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid2").toString()));
            //根据map中字段state=2查询auctioninfo和aucuserid表中数据
            List<Map> auctioninfoList1 = auctionDao.selectListk5(map);
            List<Map> auctioninfoList3 = new ArrayList<>();//state=2保存当前竞标全部可见的userid
            List<Map> auctioninfoList4 = new ArrayList<>();//state=3保存当前竞标全部可见的userid
            List<acutionListInfo> auctioninfoList5 = new ArrayList<>();//state=2保存当前竞标全部可见的userid转为list
            for (int j = 0; j < auctioninfoList1.size(); j++) {
                String[] userotherstr = auctioninfoList1.get(j).get("userstr").toString().split("\\|");
                for (String userother : userotherstr) {
                    if (userinfo.getOther().equals(userother)) {
                        auctioninfoList3.add(auctioninfoList1.get(j));
                    }
                }

            }

            HashSet h = new HashSet(auctioninfoList3);

            auctioninfoList3.clear();
            auctioninfoList3.addAll(h);


            map.put("state", "3");
            List<Map> auctioninfoList2 = auctionDao.selectListk5(map); //根据map中字段state=3查询auctioninfo和aucuserid表中数据
            for (int j = 0; j < auctioninfoList2.size(); j++) {
                String[] userotherstr = auctioninfoList2.get(j).get("userstr").toString().split("\\|");
                for (String userother : userotherstr) {
                    if (userinfo.getOther().equals(userother)) {
                        auctioninfoList4.add(auctioninfoList2.get(j));
                    }
                }

            }

            HashSet h2 = new HashSet(auctioninfoList4);
            auctioninfoList4.clear();
            auctioninfoList4.addAll(h2);

            for (Map a : auctioninfoList3) {
                acutionListInfo acuti = JSON.parseObject(JSON.toJSONString(a), acutionListInfo.class);
                auctioninfoList5.add(acuti);
            }
            //acutionListInfoList添加state=2保存当前竞标全部可见的userid转为list
            gettimecl2(auctioninfoList5, acutionListInfoList);

            for (Map a : auctioninfoList4) {
                // acutionListInfo auctioninfo = new acutionListInfo();
                acutionListInfo acuti = JSON.parseObject(JSON.toJSONString(a), acutionListInfo.class);
                // BeanUtils.copyProperties(acuti, auctioninfo);
                acutionListInfoList.add(acuti);//acutionListInfoList添加state=3保存当前竞标全部可见的userid转为list
            }

            list = new ArrayList();
            for (acutionListInfo a : acutionListInfoList) {

                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                if (map.get("totalpay") != null) {
                    biddermap.put("totalpay", "totalpay");
                }
                // biddermap.put("other",a.getUserstr());
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.getCarid().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    carList.add(car);
                }
                auctionMap.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                auctionMap.put("bidderList", bidderList);
                // }
                Integer state = userinfo.getState();
                if (state != null) {
                    //如果为单公众号用户
                    if (state == 1) {

                    }
                    //单企业微信用户
                    if (state == 0) {
                        list.add(auctionMap);
                    }
                    //如果为双重身份
                    if (state == 2) {
                        //userstr中是否选择了当前用户
                        //issend按钮是否打开

                    }
                }
//                    list.add(auctionMap);


            }

        } else if ("2".equals(map.get("state"))) {
            List<Map> auctioninfoList1 = new ArrayList<>();
            List<auctioninfo> auc = auctionDao.selectListk(map);
            for (auctioninfo a : auc) {
                aucuserid au = new aucuserid();
                au.setAucid(a.getId());
                aucuserid ai = aucuseridDao.selectByaucid(au);
                // for(Map ap:auctioninfoList1){
                Map ap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                ap.put("userstr", ai.getUserstr());
                auctioninfoList1.add(ap);
                // }
            }
            //map.put("state","3");
            // List<auctioninfo> auctioninfoList2 = auctionDao.selectListk(map);

         /*   for(Map ma:auctioninfoList1){

            }*/
            //gettimecl(auctioninfoList1, acutionListInfoList);

          /*  for(auctioninfo a:auctioninfoList2){
                acutionListInfo auctioninfo = new acutionListInfo();
                BeanUtils.copyProperties(a, auctioninfo);
                acutionListInfoList.add(auctioninfo);
            }*/

            list = new ArrayList();
            for (Map a : auctioninfoList1) {

                biddermap.put("auctionnumber", a.get("auctionnumber").toString());
                /*if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }*/
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                // Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.get("carid").toString().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    carList.add(car);
                }
                a.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                a.put("biddercount", bidderList.size());
                a.put("bidderList", bidderList);
                // }
                list.add(a);

            }
        } else if ("1".equals(map.get("state").toString()) && "1".equals(map.get("auctionstate").toString()) && "2".equals(map.get("paymentstate").toString())) {
            List<auctioninfo> auctioninfoList = auctionDao.selectList(map);
            gettimecl(auctioninfoList, acutionListInfoList);
            list = new ArrayList();

            getAuctionInfo(map, list, biddermap, acutionListInfoList);
        } else if ("1".equals(map.get("state").toString()) && "1".equals(map.get("paymentstate").toString()) && "1".equals(map.get("auctionstate").toString())) {
            map.put("auctionendtime", "auctionendtime");
            List<auctioninfo> auctioninfoList = auctionDao.selectList(map);

            gettimecl(auctioninfoList, acutionListInfoList);
            list = new ArrayList();
            for (acutionListInfo a : acutionListInfoList) {
                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.getCarid().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    carList.add(car);
                }
                auctionMap.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                if (bidderList.size() > 0 && bidderList != null) {
                    auctionMap.put("bidderList", bidderList);
                    list.add(auctionMap);
                }

            }
        } else if ("1".equals(map.get("state").toString()) && "4".equals(map.get("auctionstate").toString())) {
            List<auctioninfo> auctioninfoList2 = auctionDao.selectListk2(map);
            gettimecl(auctioninfoList2, acutionListInfoList);
            list = new ArrayList();
            getAuctionInfo(map, list, biddermap, acutionListInfoList);
        } else if ("1".equals(map.get("state").toString())) {
            List<auctioninfo> auctioninfoListk = auctionDao.selectListk(map);
            list = new ArrayList();
            for (auctioninfo a : auctioninfoListk) {
                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.getCarid().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    carList.add(car);
                }
                auctionMap.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                if (bidderList.size() > 0 && bidderList != null) {
                    auctionMap.put("bidderList", bidderList);
                    list.add(auctionMap);
                }

            }
        } else if ("4".equals(map.get("state").toString()) && "2".equals(map.get("paymentstate").toString()) && "4".equals(map.get("auctionstate").toString()) && "1".equals(map.get("isviolations").toString())) {
            List<auctioninfo> auctioninfoListk = auctionDao.selectListk4(map);
            list = new ArrayList();
            for (auctioninfo a : auctioninfoListk) {
                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                if ("".equals(map.get("isviolations").toString())) {
                    m.put("msg", "isviolations 不能为空");
                    m.put("code", 0);
                    return m;
                }
               /* biddermap.put("pageIndex",pageIndex);
                biddermap.put("endPageIndex",endPageIndex);*/
                biddermap.put("isviolations", map.get("isviolations").toString());
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.getCarid().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    carList.add(car);
                }
                auctionMap.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                if (bidderList.size() > 0 && bidderList != null) {
                    auctionMap.put("bidderList", bidderList);
                    list.add(auctionMap);
                }

            }
        } else {
            List<auctioninfo> auctioninfoListk = auctionDao.selectListk3(map);
            list = new ArrayList();
            for (auctioninfo a : auctioninfoListk) {
                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.getCarid().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    carList.add(car);
                }
                auctionMap.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                auctionMap.put("biddercount", bidderList.size());
                // if (bidderList.size() > 0 && bidderList != null) {
                auctionMap.put("bidderList", bidderList);
                // }
                list.add(auctionMap);

            }

        }
        //List<acutionListInfo> list=auctionDao.listauctioninfo(map);
        if (list.size() > 0 && list != null) {
            m.put("msg", 1);
            m.put("list", list);
            m.put("code", 0);
        }

        return m;
    }

    public void buildList(List<acutionListInfo> acutionListInfoList) {
        if (!acutionListInfoList.isEmpty()) {
            List<String> carIds = new ArrayList<>();
            acutionListInfoList.forEach(a -> {
                String[] str = a.getCarid().split(",");
                Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
                carIds.addAll(collect);
            });
            List<car> allCars = new ArrayList<>();
            if (!carIds.isEmpty()) {
                allCars = carDao.selectAllByPrimaryKey(carIds);
            }
            List<car> finalAllCars = allCars;
            //查询要竞拍的车辆
            acutionListInfoList.forEach(a -> {
                String[] str = a.getCarid().split(",");
                Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
                List<car> resultCar = new ArrayList<>();
                collect.forEach(c -> {
                    List<car> carList = finalAllCars.stream().filter(s -> s.getId() == (Integer.parseInt(c))).collect(Collectors.toList());
                    resultCar.addAll(carList);
                });
                a.setCarList(resultCar);
            });
        }
    }
    public void buildMap(List<Map> mapList) {
        if (!mapList.isEmpty()) {
            List<String> carIds = new ArrayList<>();
            mapList.forEach(a -> {
                String carid = a.get("carid").toString();
                String[] str = carid.split(",");
                Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
                carIds.addAll(collect);
            });
            List<car> allCars = new ArrayList<>();
            if (!carIds.isEmpty()) {
                allCars = carDao.selectAllByPrimaryKey(carIds);
            }
            List<car> finalAllCars = allCars;
            //查询要竞拍的车辆
            mapList.forEach(a -> {
                String carid = a.get("carid").toString();
                String[] str = carid.split(",");
                Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
                List<car> resultCar = new ArrayList<>();
                collect.forEach(c -> {
                    List<car> carList = finalAllCars.stream().filter(s -> s.getId() == (Integer.parseInt(c))).collect(Collectors.toList());
                    resultCar.addAll(carList);
                });
                a.put("catList",resultCar);
            });
        }
    }
    //竞拍中和竞拍结束
    public List new1(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) throws ParseException {
        //根据id查询userinfo表中用户
        Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid2").toString()));
        //state=2查询竞拍中的标书
        List<Map> auctioninfoList1 = auctionDao.selectListk5(map);
        List<Map> auctioninfoList3 = new ArrayList<>();//state=2保存当前竞标全部可见的userid
        List<Map> auctioninfoList4 = new ArrayList<>();//state=3保存当前竞标全部可见的userid
        List<acutionListInfo> auctioninfoList5 = new ArrayList<>();//state=2保存当前竞标全部可见的userid转为list
        if (auctioninfoList1 != null && !auctioninfoList1.isEmpty()) {
            for (Map value : auctioninfoList1) {
                if (userinfo != null) {
                    Integer state = userinfo.getState();
                    if (state != null && state.equals(1)) {
                        auctioninfoList3.add(value);
                    } else {
                        String[] userotherstr = value.get("userstr").toString().split("\\|");
                        if (userotherstr.length > 0) {
                            for (String userother : userotherstr) {
                                if (userinfo.getOther().equals(userother)) {
                                    auctioninfoList3.add(value);
                                }
                            }
                        }
                    }
                }
            }
        }
        HashSet h = new HashSet(auctioninfoList3);
        auctioninfoList3.clear();
        auctioninfoList3.addAll(h);
        map.put("state", "3");
        //查询已经结束的标书state=3
        List<Map> auctioninfoList2 = auctionDao.selectListk5(map); //根据map中字段state=3查询auctioninfo和aucuserid表中数据
        for (int j = 0; j < auctioninfoList2.size(); j++) {
            String[] userotherstr = auctioninfoList2.get(j).get("userstr").toString().split("\\|");
            for (String userother : userotherstr) {
                if (userinfo.getOther().equals(userother)) {
                    auctioninfoList4.add(auctioninfoList2.get(j));
                }
            }
        }
        HashSet h2 = new HashSet(auctioninfoList4);
        auctioninfoList4.clear();
        auctioninfoList4.addAll(h2);
        //竞拍中的
        for (Map a : auctioninfoList3) {
            acutionListInfo acuti = JSON.parseObject(JSON.toJSONString(a), acutionListInfo.class);
            auctioninfoList5.add(acuti);
        }
        //acutionListInfoList添加state=2保存当前竞标全部可见的userid转为list
        if (!auctioninfoList5.isEmpty()) {
            gettimecl2(auctioninfoList5, acutionListInfoList);
        }
        //竞拍结束的
        for (Map a : auctioninfoList4) {
            // acutionListInfo auctioninfo = new acutionListInfo();
            acutionListInfo acuti = JSON.parseObject(JSON.toJSONString(a), acutionListInfo.class);
            // BeanUtils.copyProperties(acuti, auctioninfo);
            acutionListInfoList.add(acuti);//acutionListInfoList添加state=3保存当前竞标全部可见的userid转为list
        }
        //查询issend字段
        list = new ArrayList();
        long start = System.currentTimeMillis();
        buildList(acutionListInfoList);
        for (acutionListInfo a : acutionListInfoList) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            if (map.get("totalpay") != null) {
                biddermap.put("totalpay", "totalpay");
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询参与的竞拍人
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            auctionMap.put("bidderList", bidderList);
            Integer state = userinfo.getState();
            if (state != null) {
                //如果为单公众号用户
                if (state == 1) {
                    String issend = a.getIssend();
                    if (issend != null && issend.equals("1")) {
                        list.add(auctionMap);
                    }
                }
                //单企业微信用户
                if (state == 0 || state == 2) {
                    list.add(auctionMap);
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("biddingAndEnd===for时间====" + (end - start));
        return list;
    }

    public List old1(Map map, List list, Map biddermap) throws ParseException {
        List<acutionListInfo> acutionListInfoList = new ArrayList<>();
        //根据id查询userinfo表中用户
        Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid2").toString()));
        //根据map中字段state=2查询auctioninfo和aucuserid表中数据
        List<Map> auctioninfoList1 = auctionDao.selectListk5(map);
        List<Map> auctioninfoList3 = new ArrayList<>();//state=2保存当前竞标全部可见的userid
        List<Map> auctioninfoList4 = new ArrayList<>();//state=3保存当前竞标全部可见的userid
        List<acutionListInfo> auctioninfoList5 = new ArrayList<>();//state=2保存当前竞标全部可见的userid转为list
        for (int j = 0; j < auctioninfoList1.size(); j++) {
            String[] userotherstr = auctioninfoList1.get(j).get("userstr").toString().split("\\|");
            if (userinfo != null) {
                Integer state = userinfo.getState();
                if (state != null && state.equals(1)) {
                    auctioninfoList3.add(auctioninfoList1.get(j));
                } else {
                    for (String userother : userotherstr) {
                        if (userinfo.getOther().equals(userother)) {
                            auctioninfoList3.add(auctioninfoList1.get(j));
                        }
                    }
                }
            }
        }

        HashSet h = new HashSet(auctioninfoList3);

        auctioninfoList3.clear();
        auctioninfoList3.addAll(h);

        map.put("state", "3");
        List<Map> auctioninfoList2 = auctionDao.selectListk5(map); //根据map中字段state=3查询auctioninfo和aucuserid表中数据
        for (int j = 0; j < auctioninfoList2.size(); j++) {
            String[] userotherstr = auctioninfoList2.get(j).get("userstr").toString().split("\\|");
            for (String userother : userotherstr) {
                if (userinfo.getOther().equals(userother)) {
                    auctioninfoList4.add(auctioninfoList2.get(j));
                }
            }

        }
        HashSet h2 = new HashSet(auctioninfoList4);
        auctioninfoList4.clear();
        auctioninfoList4.addAll(h2);
        for (Map a : auctioninfoList3) {
            acutionListInfo acuti = JSON.parseObject(JSON.toJSONString(a), acutionListInfo.class);
            auctioninfoList5.add(acuti);
        }
        //acutionListInfoList添加state=2保存当前竞标全部可见的userid转为list
        gettimecl2(auctioninfoList5, acutionListInfoList);
        for (Map a : auctioninfoList4) {
            // acutionListInfo auctioninfo = new acutionListInfo();
            acutionListInfo acuti = JSON.parseObject(JSON.toJSONString(a), acutionListInfo.class);
            // BeanUtils.copyProperties(acuti, auctioninfo);
            acutionListInfoList.add(acuti);//acutionListInfoList添加state=3保存当前竞标全部可见的userid转为list
        }
        //查询issend字段
        list = new ArrayList();
        long start = System.currentTimeMillis();
        for (acutionListInfo a : acutionListInfoList) {

            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            if (map.get("totalpay") != null) {
                biddermap.put("totalpay", "totalpay");
            }
            // biddermap.put("other",a.getUserstr());
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str = a.getCarid().split(",");
            List carList = new ArrayList();
            for (String s : str) {
                car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                carList.add(car);
            }
            auctionMap.put("carList", carList);
            //查询参与的竞拍人
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            auctionMap.put("bidderList", bidderList);
            Integer state = userinfo.getState();
            if (state != null) {
                //如果为单公众号用户
                if (state == 1) {
                    String issend = a.getIssend();
                    if (issend != null && issend.equals("1")) {
                        System.out.println("issend != null    ");
                        list.add(auctionMap);
                    }
                }
                //单企业微信用户
                if (state == 0 || state == 2) {
                    list.add(auctionMap);
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("old1===for时间====" + (end - start));
        return list;
    }

    public List new2(Map map, List list, Map biddermap) {
        List<Map> auctioninfoList1 = new ArrayList<>();
        long start = System.currentTimeMillis();
        List<auctioninfo> auc = auctionDao.selectListk(map);
        if (auc != null && !auc.isEmpty()) {
            for (auctioninfo a : auc) {
                aucuserid ai = aucuseridDao.selectByAucId(a.getId());
                if (ai != null) {
                    Map ap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                    ap.put("userstr", ai.getUserstr());
                    ap.put("issend", ai.getIssend());
                    auctioninfoList1.add(ap);
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("bidding:for时间====" + (end - start));
        list = new ArrayList();
        List<String> carIds = new ArrayList<>();
        auctioninfoList1.forEach(a -> {
            String[] str = a.get("carid").toString().split(",");
            Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
            carIds.addAll(collect);
        });
        List<car> allCars = new ArrayList<>();
        if (!carIds.isEmpty()) {
            allCars = carDao.selectAllByPrimaryKey(carIds);
        }
        //查询要竞拍的车辆
        List<car> finalAllCars = allCars;
        auctioninfoList1.forEach(a -> {
            String[] str = a.get("carid").toString().split(",");
            Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
            List<car> resultCar = new ArrayList<>();
            collect.forEach(c -> {
                List<car> carList = finalAllCars.stream().filter(s -> s.getId() == (Integer.parseInt(c))).collect(Collectors.toList());
                resultCar.addAll(carList);
            });
            a.put("carList", resultCar);
        });
        for (Map a : auctioninfoList1) {
            biddermap.put("auctionnumber", a.get("auctionnumber").toString());
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            a.put("biddercount", bidderList.size());
            a.put("bidderList", bidderList);
            list.add(a);
        }
        return list;
    }

    public List old2(Map map, List list, Map biddermap) {
        List<Map> auctioninfoList1 = new ArrayList<>();
        List<auctioninfo> auc = auctionDao.selectListk(map);
        for (auctioninfo a : auc) {
            aucuserid ai = aucuseridDao.selectByAucId(a.getId());
            if (ai != null) {
                // for(Map ap:auctioninfoList1){
                Map ap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                ap.put("userstr", ai.getUserstr());
                ap.put("issend", ai.getIssend());
                auctioninfoList1.add(ap);
            }
        }
        list = new ArrayList();
        long start = System.currentTimeMillis();
        for (Map a : auctioninfoList1) {
            biddermap.put("auctionnumber", a.get("auctionnumber").toString());
                /*if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }*/
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            // Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str = a.get("carid").toString().split(",");
            List carList = new ArrayList();
            for (String s : str) {
                car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                carList.add(car);
            }
            a.put("carList", carList);
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            a.put("biddercount", bidderList.size());
            a.put("bidderList", bidderList);
            list.add(a);
        }
        long end = System.currentTimeMillis();
        System.out.println("old2:for时间====" + (end - start));
        return list;
    }

    public List new3(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) throws ParseException {
//        List<auctioninfo> auctioninfoList = auctionDao.selectList(map);
        List<auctioninfo> auctioninfoList = auctionDao.selectList6(map);
        gettimecl(auctioninfoList, acutionListInfoList);
        buildList(acutionListInfoList);
        list = new ArrayList();
        for (acutionListInfo a : acutionListInfoList) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList != null && bidderList.size() > 0) {
                auctionMap.put("bidderList", bidderList);
                //后台管理系统没有传userid
                String userid2 = map.get("userid2").toString();
                if (userid2 == null || "".equals(userid2)) {
                    list.add(auctionMap);
                } else {
                    Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(userid2));
                    if (userinfo != null) {
                        Integer state = userinfo.getState();
                        if (state != null) {
                            //如果为单公众号用户
                            if (state == 1) {
                            }
                            //单企业微信用户
                            if (state == 0 || state == 2) {
                                list.add(auctionMap);
                            }
                        }
                    }
                }
            }
        }
      /*  list = new ArrayList();
        List<Map> maps = auctionDao.selectList6(map);
        buildMap(maps);
        list.add(maps);*/
        return list;
    }

    public List old3(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) throws ParseException {
        List<auctioninfo> auctioninfoList = auctionDao.selectList(map);
        gettimecl(auctioninfoList, acutionListInfoList);
        list = new ArrayList();
        getAuctionInfo(map, list, biddermap, acutionListInfoList);//公众号，企业微信进入：成交列表
        return list;
    }

    public List old4(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) throws ParseException {
        map.put("auctionendtime", "auctionendtime");
        List<auctioninfo> auctioninfoList = auctionDao.selectList(map);
        gettimecl(auctioninfoList, acutionListInfoList);
        list = new ArrayList();
        for (acutionListInfo a : acutionListInfoList) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str = a.getCarid().split(",");
            List carList = new ArrayList();
            for (String s : str) {
                car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                carList.add(car);
            }
            auctionMap.put("carList", carList);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList.size() > 0 && bidderList != null) {
                auctionMap.put("bidderList", bidderList);
                Object userid2 = map.get("userid2");
                if (userid2 == null) {
                    list.add(auctionMap);
                } else {
                    Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid2").toString()));
                    Integer state = userinfo.getState();
                    if (state != null) {
                        //如果为单公众号用户
                        if (state == 1) {
                        }
                        //单企业微信用户
                        if (state == 0 || state == 2) {
                            list.add(auctionMap);
                        }
                    }
                }
            }
        }//企业微信公众号：失败列表
        return list;
    }

    public List new4(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) throws ParseException {
        map.put("auctionendtime", "auctionendtime");
        List<auctioninfo> auctioninfoList = auctionDao.selectList(map);
        gettimecl(auctioninfoList, acutionListInfoList);
        list = new ArrayList();
        buildList(acutionListInfoList);
        for (acutionListInfo a : acutionListInfoList) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList.size() > 0 && bidderList != null) {
                auctionMap.put("bidderList", bidderList);
                Object userid2 = map.get("userid2");
                if (userid2 == null) {
                    list.add(auctionMap);
                } else {
                    Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid2").toString()));
                    Integer state = userinfo.getState();
                    if (state != null) {
                        //如果为单公众号用户
                        if (state == 1) {
                        }
                        //单企业微信用户
                        if (state == 0 || state == 2) {
                            list.add(auctionMap);
                        }
                    }
                }
            }
        }//企业微信公众号：失败列表
        return list;
    }

    public List old5(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) throws ParseException {
        List<auctioninfo> auctioninfoList2 = auctionDao.selectListk2(map);
        gettimecl(auctioninfoList2, acutionListInfoList);
        list = new ArrayList();
        getAuctionInfo(map, list, biddermap, acutionListInfoList);
        return list;
    }

    public List new5(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) throws ParseException {
        List<auctioninfo> auctioninfoList2 = auctionDao.selectListk2(map);
        gettimecl(auctioninfoList2, acutionListInfoList);
        list = new ArrayList();
        buildList(acutionListInfoList);
        long start = System.currentTimeMillis();
        //后台管理系统没有传userid
        String userid2 = map.get("userid2").toString();
        Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(userid2));
        for (acutionListInfo a : acutionListInfoList) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList != null && bidderList.size() > 0) {
                auctionMap.put("bidderList", bidderList);
                if (userid2 == null || "".equals(userid2)) {
                    list.add(auctionMap);
                } else {
                    if (userinfo != null) {
                        Integer state = userinfo.getState();
                        if (state != null) {
                            //如果为单公众号用户
                            if (state == 1) {
                            }
                            //单企业微信用户
                            if (state == 0 || state == 2) {
                                list.add(auctionMap);
                            }
                        }
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("时间======" + (end - start));
        return list;
    }

    public List old6(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) {
        List<auctioninfo> auctioninfoListk = auctionDao.selectListk(map);
        list = new ArrayList();
        for (auctioninfo a : auctioninfoListk) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str = a.getCarid().split(",");
            List carList = new ArrayList();
            for (String s : str) {
                car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                carList.add(car);
            }
            auctionMap.put("carList", carList);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList.size() > 0 && bidderList != null) {
                auctionMap.put("bidderList", bidderList);
                list.add(auctionMap);
            }
        }
        return list;
    }

    public List new6(Map map, List list, Map biddermap) {
        List<auctioninfo> auctioninfoListk = auctionDao.selectListk(map);
        List<String> carIds = new ArrayList<>();
        if (!auctioninfoListk.isEmpty()) {
            auctioninfoListk.forEach(a -> {
                String[] str = a.getCarid().split(",");
                Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
                carIds.addAll(collect);
            });
            List<car> allCars = new ArrayList<>();
            if (!carIds.isEmpty()) {
                allCars = carDao.selectAllByPrimaryKey(carIds);
            }
            List<car> finalAllCars = allCars;
            //查询要竞拍的车辆
            auctioninfoListk.forEach(a -> {
                String[] str = a.getCarid().split(",");
                Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
                List<car> resultCar = new ArrayList<>();
                collect.forEach(c -> {
                    List<car> carList = finalAllCars.stream().filter(s -> s.getId() == (Integer.parseInt(c))).collect(Collectors.toList());
                    resultCar.addAll(carList);
                });
                a.setCarList(resultCar);
            });
        }
        list = new ArrayList();
        for (auctioninfo a : auctioninfoListk) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList.size() > 0 && bidderList != null) {
                auctionMap.put("bidderList", bidderList);
                list.add(auctionMap);
            }
        }
        return list;
    }

    public Map old7(Map m, Map map, List list, Map biddermap) {
        List<auctioninfo> auctioninfoListk = auctionDao.selectListk4(map);
        list = new ArrayList();
        for (auctioninfo a : auctioninfoListk) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            if ("".equals(map.get("isviolations").toString())) {
                m.put("msg", "isviolations 不能为空");
                m.put("code", 0);
                return m;
            }
               /* biddermap.put("pageIndex",pageIndex);
                biddermap.put("endPageIndex",endPageIndex);*/
            biddermap.put("isviolations", map.get("isviolations").toString());
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str = a.getCarid().split(",");
            List carList = new ArrayList();
            for (String s : str) {
                car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                carList.add(car);
            }
            auctionMap.put("carList", carList);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList.size() > 0 && bidderList != null) {
                auctionMap.put("bidderList", bidderList);
                list.add(auctionMap);
            }

        }//竞拍结束列表
        return m;
    }

    public Map new7(Map m, Map map, List list, Map biddermap) {
        List<auctioninfo> auctioninfoListk = auctionDao.selectListk4(map);
        list = new ArrayList();
        for (auctioninfo a : auctioninfoListk) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            if ("".equals(map.get("isviolations").toString())) {
                m.put("msg", "isviolations 不能为空");
                m.put("code", 0);
                return m;
            }
               /* biddermap.put("pageIndex",pageIndex);
                biddermap.put("endPageIndex",endPageIndex);*/
            biddermap.put("isviolations", map.get("isviolations").toString());
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList.size() > 0 && bidderList != null) {
                auctionMap.put("bidderList", bidderList);
                list.add(auctionMap);
            }
        }
        return m;
    }

    public List old8(Map map, List list, List<acutionListInfo> acutionListInfoList, Map biddermap) {
        List<auctioninfo> auctioninfoListk = auctionDao.selectListk3(map);
        list = new ArrayList();
        for (auctioninfo a : auctioninfoListk) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str = a.getCarid().split(",");
            List carList = new ArrayList();
            for (String s : str) {
                car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                carList.add(car);
            }
            auctionMap.put("carList", carList);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            auctionMap.put("biddercount", bidderList.size());
            // if (bidderList.size() > 0 && bidderList != null) {
            auctionMap.put("bidderList", bidderList);
            // }
            list.add(auctionMap);
        }
        return list;
    }

    public List new8(Map map, List list, Map biddermap) {
        List<auctioninfo> auctioninfoListk = auctionDao.selectListk3(map);
        list = new ArrayList();
        List<String> carIds = new ArrayList<>();
        if (!auctioninfoListk.isEmpty()) {
            auctioninfoListk.forEach(a -> {
                String[] str = a.getCarid().split(",");
                Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
                carIds.addAll(collect);
            });
            List<car> allCars = new ArrayList<>();
            if (!carIds.isEmpty()) {
                allCars = carDao.selectAllByPrimaryKey(carIds);
            }
            List<car> finalAllCars = allCars;
            //查询要竞拍的车辆
            auctioninfoListk.forEach(a -> {
                String[] str = a.getCarid().split(",");
                Set<String> collect = Arrays.stream(str).collect(Collectors.toSet());
                List<car> resultCar = new ArrayList<>();
                collect.forEach(c -> {
                    List<car> carList = finalAllCars.stream().filter(s -> s.getId() == (Integer.parseInt(c))).collect(Collectors.toList());
                    resultCar.addAll(carList);
                });
                a.setCarList(resultCar);
            });
        }
        for (auctioninfo a : auctioninfoListk) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str = a.getCarid().split(",");
            List carList = new ArrayList();
            for (String s : str) {
                car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                carList.add(car);
            }
            auctionMap.put("carList", carList);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            auctionMap.put("biddercount", bidderList.size());
            // if (bidderList.size() > 0 && bidderList != null) {
            auctionMap.put("bidderList", bidderList);
            // }
            list.add(auctionMap);
        }
        return list;
    }

    public Map listauctioninfo(Map map) throws ParseException {
        Map m = new HashMap();
        m.put("msg", 0);
        m.put("code", 0);
        Map biddermap = new HashMap();
        List list = null;
        if (map.get("auctiontime") != null) {
            if ("other".equals(map.get("auctiontime").toString())) {
                map.remove("auctiontime");
                map.put("other", "other");
            } else if ("auctionendtime".equals(map.get("auctiontime").toString())) {
                map.remove("auctiontime");
                map.put("auctionendtime", "auctionendtime");
            }
        } else {
            map.put("id", "id");
        }
        // List<auctioninfo> auctioninfoList = auctionDao.selectList(map);
        List<acutionListInfo> acutionListInfoList = new ArrayList<>();
        //公众号，企业微信进入：竞拍列表
        if ("2".equals(map.get("state")) && map.get("userid2") != null && !"".equals(map.get("userid2"))) {
            long start = System.currentTimeMillis();
//          list = new1(map,list,acutionListInfoList,biddermap);
            list = old1(map, list, biddermap);
            long end = System.currentTimeMillis();
            System.out.println("old1时间======" + (end - start));
            //后台管理系统：竞拍列表
        } else if ("2".equals(map.get("state"))) {
            long start = System.currentTimeMillis();
//            list = old2(map,list,biddermap);
            list = new2(map, list, biddermap);
            long end = System.currentTimeMillis();
            System.out.println("new2时间======" + (end - start));
            //后台管理系统：中标列表，企业微信公众号：中标列表(慢)
        } else if ("1".equals(map.get("state").toString()) && "1".equals(map.get("auctionstate").toString()) && "2".equals(map.get("paymentstate").toString())) {
            long start = System.currentTimeMillis();
//            list=old3(map,list,acutionListInfoList,biddermap);
            list = new3(map, list, acutionListInfoList, biddermap);
            long end = System.currentTimeMillis();
            System.out.println("new3时间======" + (end - start));//个人成交列表（慢）
        } else if ("1".equals(map.get("state").toString()) && "1".equals(map.get("paymentstate").toString()) && "1".equals(map.get("auctionstate").toString())) {
            long start = System.currentTimeMillis();
//            list=old4(map,list,acutionListInfoList,biddermap);
            list = new4(map, list, acutionListInfoList, biddermap);
            long end = System.currentTimeMillis();
            System.out.println("new4时间======" + (end - start));//个人失败列表（慢）
        } else if ("1".equals(map.get("state").toString()) && "4".equals(map.get("auctionstate").toString())) {
            long start = System.currentTimeMillis();
//            list=old5(map,list,acutionListInfoList,biddermap);
            list = new5(map, list, acutionListInfoList, biddermap);
            long end = System.currentTimeMillis();
            System.out.println("new5时间======" + (end - start));
        } else if ("1".equals(map.get("state").toString())) {
            long start = System.currentTimeMillis();
//            list=old6(map,list,acutionListInfoList,biddermap);
            list = new6(map, list, biddermap);
            long end = System.currentTimeMillis();
            System.out.println("new6时间======" + (end - start));
        } else if ("4".equals(map.get("state").toString()) && "2".equals(map.get("paymentstate").toString()) && "4".equals(map.get("auctionstate").toString()) && "1".equals(map.get("isviolations").toString())) {
            long start = System.currentTimeMillis();
//            m = old7(m,map,list,biddermap);
            m = new7(m, map, list, biddermap);
            long end = System.currentTimeMillis();
            System.out.println("new7时间======" + (end - start));
        } else {
            long start = System.currentTimeMillis();
//            list=old8(map,list,acutionListInfoList,biddermap);
            list = new8(map, list, biddermap);
            long end = System.currentTimeMillis();
            System.out.println("new8时间======" + (end - start));
        }
        //List<acutionListInfo> list=auctionDao.listauctioninfo(map);
        if (list.size() > 0) {
            m.put("msg", 1);
            m.put("list", list);
            m.put("code", 0);
        }
        return m;
    }

    private void gettimecl(List<auctioninfo> auctioninfoList, List<acutionListInfo> acutionListInfoList) throws ParseException {
        for (auctioninfo auction : auctioninfoList) {
            acutionListInfo auctioninfo = new acutionListInfo();
            BeanUtils.copyProperties(auction, auctioninfo);


            //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            TemplateUtils templateUtils = new TemplateUtils();
            Calendar calendar = new GregorianCalendar();

            Date d = new Date();
            long newtime = (d.getTime()) / 1000;
            Date auctime = simpleDateFormat.parse(auction.getAuctionendtime());
            calendar.setTime(auctime);
            calendar.add(Calendar.DATE, 1);

            long endtime = (auctime.getTime() / 1000) + 86400;
            long zjtime = 0;
            if (newtime > endtime) {
                zjtime = endtime - newtime;
                zjtime = 1440 + zjtime;
            } else {
                zjtime = endtime - newtime;
            }


            auctioninfo.setZjtime((int) zjtime);
            acutionListInfoList.add(auctioninfo);
        }
        H_Format.sortList(acutionListInfoList, "zjtime", "ASC");
    }


    private void gettimecl2(List<acutionListInfo> auctioninfoList, List<acutionListInfo> acutionListInfoList) throws ParseException {
        for (acutionListInfo auction : auctioninfoList) {
            acutionListInfo auctioninfo = new acutionListInfo();
            //Map auctioninfo=new HashMap();
            BeanUtils.copyProperties(auction, auctioninfo);


            //SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            TemplateUtils templateUtils = new TemplateUtils();
            Calendar calendar = new GregorianCalendar();

            Date d = new Date();
            long newtime = (d.getTime()) / 1000;
            Date auctime = simpleDateFormat.parse(auction.getAuctionendtime());
            calendar.setTime(auctime);
            calendar.add(Calendar.DATE, 1);

            long endtime = (auctime.getTime() / 1000) + 86400;
            long zjtime = 0;
            if (newtime > endtime) {
                zjtime = endtime - newtime;
                zjtime = 1440 + zjtime;
            } else {
                zjtime = endtime - newtime;
            }


            auctioninfo.setZjtime((int) zjtime);
            acutionListInfoList.add(auctioninfo);
        }
        H_Format.sortList(acutionListInfoList, "zjtime", "ASC");
    }

    private void getAuctionInfo(Map map, List list, Map biddermap, List<acutionListInfo> auctioninfoList) {
        for (acutionListInfo a : auctioninfoList) {
            biddermap.put("auctionnumber", a.getAuctionnumber());
            if (!"".equals(map.get("userid2").toString())) {
                biddermap.put("userid2", map.get("userid2").toString());
            }
            if (!"".equals(map.get("auctionstate").toString())) {
                biddermap.put("auctionstate", map.get("auctionstate").toString());
            }
            if (!"".equals(map.get("paymentstate").toString())) {
                biddermap.put("paymentstate", map.get("paymentstate").toString());
            }
            Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
            //查询要竞拍的车辆
            String[] str = a.getCarid().split(",");
            List carList = new ArrayList();
            for (String s : str) {
                car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                carList.add(car);
            }
            auctionMap.put("carList", carList);
            //查询参与的竞拍人
            //if(map.get("userid") ==null){
            List<Map> bidderList = userdao.selectbidderList(biddermap);
            if (bidderList != null && bidderList.size() > 0) {
                auctionMap.put("bidderList", bidderList);
                //后台管理系统没有传userid
                String userid2 = map.get("userid2").toString();
                if (userid2 == null || "".equals(userid2)) {
                    list.add(auctionMap);
                } else {
                    Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(userid2));
                    if (userinfo != null) {
                        Integer state = userinfo.getState();
                        if (state != null) {
                            //如果为单公众号用户
                            if (state == 1) {
                            }
                            //单企业微信用户
                            if (state == 0 || state == 2) {
                                list.add(auctionMap);
                            }
                        }
                    }
                }
            }
        }
    }

    public Map addCar(car c) {
        Map m = new HashMap();
        m.put("msg", 0);
        c.setOther("2");
        c.setCarnumber(String.valueOf(H_Format.getRandomNum()));
        int count = carDao.insertSelective(c);
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }


        return m;
    }

    public Map carlist(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        map.put("other", "2");
        Integer pageInt = 1;
        Integer limitInt = 10;
        Object page = map.get("page");
        if (page != null) {
            pageInt = Integer.parseInt(page.toString());
        }
        Object limit = map.get("limit");
        if (limit != null) {
            limitInt = Integer.parseInt(limit.toString());
        }
        map.put("page", (pageInt - 1) * limitInt);
        map.put("limit", limitInt);
        Long countCarList = carDao.countCarList(map);
        List<car> carList = carDao.carlist(map);
        //if(carList.size()>0 && carList !=null){
        m.put("msg", 1);
        m.put("carlist", carList);
        m.put("code", 0);
        m.put("total", countCarList);
        //}
        return m;
    }

    public Map updatacar(car c) {
        Map map = new HashMap();
        map.put("msg", 0);

        if (c.getId() == null) {
            map.put("msg", "车辆id不能为空");
            map.put("code", 0);
        }
        Integer code = carDao.updateByPrimaryKeySelective(c);
        if (code == 1) {
            map.put("msg", 1);
            map.put("code", 0);
        }
        return map;
    }

    public Map delcar(car c) {
        Map map = new HashMap();
        map.put("msg", 0);

        Integer code = carDao.deleteByPrimaryKey(c.getId());
        if (code == 1) {
            map.put("msg", 1);
            map.put("code", 0);
        }
        return map;
    }

    public Map bondList(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);

        map.put("other", "1");
        map.putIfAbsent("limit", 10);
        if (null != map.get("page")) {
            int page = Integer.parseInt(map.get("page").toString());
            map.put("page", (page - 1) * Integer.parseInt(map.get("limit").toString()));
        } else {
            map.put("page", 0);
        }
        int total = userdao.bondListCount(map);
        List<Map> biddercompanyList = userdao.bondList(map);
        if (biddercompanyList.size() > 0 && biddercompanyList != null) {
            m.put("msg", 1);
            m.put("biddercompanyList", biddercompanyList);
            m.put("code", 0);
            m.put("total", total);
        }
        return m;
    }

    public Map updateList(biddercompany biddercompany) {
        Map m = new HashMap();
        m.put("msg", 0);
        biddercompany b = biddercompanyDao.selectByPrimaryKey(biddercompany.getId());
        Userinfo u = new Userinfo();
        u.setId(b.getUserid());
        if ("4".equals(biddercompany.getState().toString())) {
            u.setParticipate("1");
            userdao.updateByPrimaryKeySelective(u);
        } else {
            u.setParticipate("2");
            userdao.updateByPrimaryKeySelective(u);
        }
        int count = biddercompanyDao.updateByPrimaryKeySelective(biddercompany);
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }

        return m;
    }

    public Map addbond(biddercompany biddercompany) {
        Map m = new HashMap();
        m.put("msg", 0);

        TemplateUtils templateUtils = new TemplateUtils();
        Userinfo userinfo = userdao.selectByPrimaryKey(biddercompany.getUserid());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String time = sdf.format(new Date());
        Map paramMap = new HashMap();
        Map taskcardMap = new HashMap();

        //发送通知
        Map btnMap1 = new HashMap();
        List btnList1 = new ArrayList();
        btnMap1.put("key", "key" + H_Format.getRandomNum());
        btnMap1.put("name", "详情");
        btnMap1.put("replace_name", "详情");
        btnList1.add(btnMap1);
        taskcardMap.put("title", "保证金协议签署成功");
        taskcardMap.put("description", time + "<br>【神易好车】尊敬的用户您好，《缴纳保证金协议》签署成功，点击详情查看更多内容。");
        taskcardMap.put("url", qyurl + "/#/information?userid=" + userinfo.getId());
        taskcardMap.put("task_id", "taskid" + H_Format.getRandomNum());
        taskcardMap.put("btn", btnList1);
        paramMap.put("touser", userinfo.getOther());
        paramMap.put("msgtype", "taskcard");
        paramMap.put("agentid", agentid);
        paramMap.put("taskcard", taskcardMap);


        //发送模板消息
        sendMessage2(m, templateUtils, paramMap);

        int count = 0;
        biddercompany b = biddercompanyDao.selectByUserid(biddercompany.getUserid());
        if (b == null) {
            biddercompany.setState("2");
            biddercompany.setOther("1");
            count = biddercompanyDao.insertSelective(biddercompany);
            m.put("bondid", biddercompany.getId());
        } else {
            biddercompanyDao.deleteByPrimaryKey(b.getId());
            biddercompany.setState("2");
            biddercompany.setOther("1");
            count = biddercompanyDao.insertSelective(biddercompany);
            m.put("bondid", biddercompany.getId());
        }


        if (count > 0) {

            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map updatebond(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(new Date());
        biddercompany b = new biddercompany();

        b.setId(Integer.parseInt(map.get("id").toString()));
        b.setPayvoucher(map.get("payvoucher").toString());
        b.setUpdateday(time);
        b.setState("1");
        int count = biddercompanyDao.updateByPrimaryKeySelective(b);
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map applyrefundbond(Integer id, Integer userid) {
        Map m = new HashMap();
        m.put("msg", 0);
        Userinfo userinfo = new Userinfo();
        userinfo.setId(userid);
        userinfo.setParticipate("2");
        userdao.updateByPrimaryKeySelective(userinfo);
        biddercompany b = new biddercompany();
        b.setId(id);
        b.setState("3");
        int count = biddercompanyDao.updateByPrimaryKeySelective(b);
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map bidderoffer(offerinfo offerinfo) {


        Map m = new HashMap();
        m.put("msg", 0);
        int count = 0;
        offerinfo.setState("2");
        bidder bidder = new bidder();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(new Date());
        bidder.setApplicationtime(date);
        bidder.setUpdatetime(date);
        Integer result = bidderDao.getAuctionInfoId(offerinfo.getBidderid());
        bidder.setAuctioninfoid(result);
        if (offerinfo.getId() == null) {
            offerinfo list = offerinfoDao.selectBybidderidandcarid(offerinfo);
            if (list != null) {
                offerinfo.setId(list.getId());
                count = offerinfoDao.updateByPrimaryKeySelective(offerinfo);
                //更新bidder表中other字段=所有offer表中offer字段之和
                List<offerinfo> offerinfos = offerinfoDao.selectBybidderid(offerinfo.getBidderid());
                if (offerinfos != null && !offerinfos.isEmpty()) {
                    Integer reduce = offerinfos.stream().map(o -> {
                        String offer = o.getOffer();
                        if (offer == null || "".equals(offer)) {
                            offer = "0";
                        }
                        return Integer.parseInt(offer);
                    }).reduce(0, Integer::sum);
                    bidder bi = new bidder();
                    bi.setId(offerinfo.getBidderid());
                    bi.setOther(reduce);
                    bi.setUpdatetime(date);
                    String carId = bidderDao.selectCarIdById(offerinfo.getBidderid());
                    if (!StringUtils.isEmpty(carId)) {
                        int size = carId.split(",").length;
                        if (offerinfos.size() == size) {
                            bi.setAuctionstate("2");
                        }
                    }
                    bidderDao.updateByPrimaryKeySelective(bi);
                }
            } else {
                count = getCountBidder(offerinfo, bidder, result);
            }
        } else {
            count = offerinfoDao.updateByPrimaryKeySelective(offerinfo);
            //更新bidder表中other字段=所有offer表中offer字段之和
            List<offerinfo> offerinfos = offerinfoDao.selectBybidderid(offerinfo.getBidderid());
            if (offerinfos != null && !offerinfos.isEmpty()) {
                Integer reduce = offerinfos.stream().map(o -> {
                    String offer = o.getOffer();
                    if (offer == null || "".equals(offer)) {
                        offer = "0";
                    }
                    return Integer.parseInt(offer);
                }).reduce(0, Integer::sum);
                bidder bi = new bidder();
                bi.setId(offerinfo.getBidderid());
                bi.setOther(reduce);
                bi.setUpdatetime(date);
                String carId = bidderDao.selectCarIdById(offerinfo.getBidderid());
                if (!StringUtils.isEmpty(carId)) {
                    int size = carId.split(",").length;
                    if (offerinfos.size() == size) {
                        bi.setAuctionstate("2");
                    }
                }
                bidderDao.updateByPrimaryKeySelective(bi);
            }
        }

        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    private int getCountBidder(offerinfo offerinfo, bidder bidder, Integer result) {
        int count;
        bidder.setId(offerinfo.getBidderid());
        bidder.setAuctionstate("5");
        count = offerinfoDao.insertSelective(offerinfo);
        List<offerinfo> offerinfos = offerinfoDao.selectBybidderid(offerinfo.getBidderid());
        if (offerinfos != null && !offerinfos.isEmpty()) {
            Integer reduce = offerinfos.stream().map(o -> {
                String offer = o.getOffer();
                if (offer == null || "".equals(offer)) {
                    offer = "0";
                }
                return Integer.parseInt(offer);
            }).reduce(0, Integer::sum);
            bidder.setOther(reduce);
            String carId = bidderDao.selectCarIdById(offerinfo.getBidderid());
            if (!StringUtils.isEmpty(carId)) {
                int size = carId.split(",").length;
                if (offerinfos.size() == size) {
                    bidder.setAuctionstate("2");
                }
            }
        }
        bidderDao.updateByPrimaryKeySelective(bidder);
        return count;
    }

    public Map auctioncarList(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        List carofferList = new ArrayList();
        auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("id").toString()));
        String[] str = auctioninfo.getCarid().split(",");

        //List<Map> carList=new ArrayList();
        for (String s : str) {
            Map caroffer = carDao.offercarList(Integer.parseInt(s), map.get("bidderid").toString());
            carofferList.add(caroffer);
        }
        if (carofferList.size() > 0 && carofferList != null) {
            m.put("carList", carofferList);
            m.put("msg", 1);
            m.put("code", 0);
        }
        //保存用户访问记录
        Object iscar = map.get("iscar");
        if (iscar != null) {
            Integer carIndex = Integer.parseInt(map.get("iscar").toString());
            if (carIndex != null && carofferList.size() > 0) {
                Map caroffer = (Map) carofferList.get(carIndex);
                Integer carId = (Integer) caroffer.get("id");
                Object userid = map.get("userid");
                if (userid != null) {
                    Integer userId = Integer.parseInt(map.get("userid").toString());
                    Userinfo userinfo = userdao.selectByPrimaryKey(userId);
                    if (userinfo != null) {
                        Integer state = userinfo.getState();
                        //公众号用户=1
                        if (state != null && state == 1) {
                            Integer auctionId = auctioninfo.getId();
                            Visit visit = new Visit();
                            visit.setUserid(userId);
                            visit.setAuctionid(auctionId);
                            visit.setCarid(carId);
                            visitDao.addVisit(visit);
                        }
                    }
                }
            }
        }
        return m;

    }

    public Map updatebidder(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);

        bidder bidder = new bidder();
        bidder.setOther(Integer.parseInt(map.get("totalpay").toString()));
        bidder.setAuctionstate("2");
        bidder.setId(Integer.parseInt(map.get("id").toString()));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = formatter.format(new Date());
        //bidder.setApplicationtime(time);
        int count = bidderDao.updateByPrimaryKeySelective(bidder);

        bidder b = bidderDao.selectByPrimaryKey(Integer.parseInt(map.get("id").toString()));
        //经过和史周浩讨论，Transactionamount没有使用，去掉逻辑
        auctioninfo auctioninfo = new auctioninfo();
        auctioninfo.setId(b.getAuctioninfoid());
        auctioninfo.setTransactionamount(map.get("totalpay").toString());
        auctionDao.updateByPrimaryKeySelective(auctioninfo);

        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map addappointmentseecar(bidder bidder) {
        Map m = new HashMap();
        m.put("msg", 0);
        //String s= uploadPic.singleFileUpload(file);
        //bidder.setSeecarmancertificates(s);
        int count = bidderDao.updateByPrimaryKeySelective(bidder);
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map addpayvoucher(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);

        //String s= uploadPic.singleFileUpload(file);
        bidder bidder = new bidder();
        bidder.setId(Integer.parseInt(map.get("id").toString()));
        bidder.setPaymentvoucher(map.get("paymentvoucher").toString());
        bidder.setPaymentstate("2");
        int count = bidderDao.updateByPrimaryKeySelective(bidder);
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }

        return m;
    }

    @Value("${pic.address}")
    private String a;
    @Value("${car.address}")
    private String carAddress;

    public Map uploadImg(MultipartFile file) {
        Map m = new HashMap();
        m.put("msg", 0);
        try {
            String s = uploadPic.singleFileUpload(file, a, "1");
            m.put("imgaddress", s);
            m.put("msg", 1);
            m.put("code", 0);
        } catch (IOException e) {
            e.printStackTrace();
            m.put("Tips", "上传图片失败!");
            return m;
        }
        return m;
    }

    public Map uploadImg2(MultipartFile file) {
        Map m = new HashMap();
        m.put("msg", 0);
        try {
            String s = uploadPic.singleFileUpload(file, a, "2");
            m.put("imgaddress", s);
            m.put("msg", 1);
            m.put("code", 0);
        } catch (IOException e) {
            e.printStackTrace();
            m.put("Tips", "上传图片失败!");
            return m;
        }
        return m;
    }


    public Map sendWinningbid(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        m.put("code", 0);
        TemplateUtils templateUtils = new TemplateUtils();
        //整体参数map
        // Map<String, Object> paramMap = null;
        Map<String, String> miniprogramMap = null;
        Map<String, Object> dataMap = null;
        Map infoMap = null;
        String openid = null;

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String time = formatter.format(new Date());
        if (!"".equals(map.get("bidderid").toString())) {
            auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
            Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid").toString()));
            //bidder bidder=bidderDao.selectByPrimaryKey(Integer.parseInt(map.get("bidderid").toString()));
            //点击消息跳转相关参数map
            miniprogramMap = new HashMap<String, String>();
            //消息主题显示相关map
            dataMap = new HashMap<String, Object>();
            infoMap = new HashMap();
            infoMap.put("value", time + "\r\n【神易好车】尊敬的" + userinfo.getUsername() + ",您参与的竞拍结果为投标成功。详情页增加收款账户，请在3个工作日内打款，打款凭证发送至邮箱" + email + "\r\n \r\n \r\n \r\n \r\n 编号:    " + auctioninfo.getAuctionnumber() +
                    "\r\n 发标时间:    " + auctioninfo.getAuctiontime() +
                    "\r\n 截止时间:    " + auctioninfo.getAuctionendtime() +
                    "\r\n 车库名称:    " + auctioninfo.getCardealers());
            infoMap.put("color", "#ffff");


            dataMap.put("first", infoMap);

            Map key1 = new HashMap();
            key1.put("value", "XXXXXX");
            key1.put("color", "#173177");
            dataMap.put("keyword1", key1);
            Map key2 = new HashMap();
            key2.put("value", "XXXXXX");
            key2.put("color", "#173177");
            dataMap.put("keyword2", key2);
            Map key3 = new HashMap();
            key3.put("value", "XXXXXX");
            key3.put("color", "#173177");
            dataMap.put("keyword3", key3);

            Map key4 = new HashMap();
            key4.put("value", "XXXXXX");
            key4.put("color", "#173177");
            dataMap.put("keyword4", key4);


           /* Map key5=new HashMap();
            key5.put("value","XXXXXX");
            key5.put("color","#173177");
           // dataMap.put("keyword4",key5);
           dataMap.put("remark", key5);*/


            //消息模板ID 竞拍成功
            template_id = "XhJ1uhpVS6JA6PBmk6YFX5hYYzUqeI2x3VTqNDVTsuQ";
            miniprogramMap.put("appid", "");
            miniprogramMap.put("pagepath", "");


            //发送模板消息
            sendMessage(userinfo.getOther(), map, m, templateUtils, miniprogramMap, dataMap);

            //修改中标者的状态
            bidder bidder = new bidder();
            bidder.setId(Integer.parseInt(map.get("bidderid").toString()));
            bidder.setAuctionstate("1");
            bidderDao.updateByPrimaryKeySelective(bidder);
            //修改竞拍车辆
            auctioninfo auctioninfo2 = new auctioninfo();
            auctioninfo2.setId(Integer.parseInt(map.get("aucid").toString()));
            auctioninfo2.setState("1");
            auctionDao.updateByPrimaryKeySelective(auctioninfo2);

        } else {
            //竞拍失败
            template_id = "FlXpFHrEqoq_U-VI3d8UuMr-_7pGSQUZle497EiRwbg";
            List<Map> userList = userdao.selectNoacution(map.get("auctionnumber").toString());
            for (int i = 0; userList.size() > 0; i++) {
                if (Integer.parseInt(userList.get(i).get("id").toString()) == Integer.parseInt(map.get("userid").toString())) {
                    userList.remove(userList.get(i));
                }
            }


            for (Map u : userList) {
                dataMap.put("params1", infoMap.put("从不超田", "#173177"));
                dataMap.put("params2", infoMap.put("喜欢发癫", "#173177"));
                dataMap.put("params3", infoMap.put("七彩螺旋", "#173177"));
                dataMap.put("params4", infoMap.put("憨牛上天", "#173177"));

                miniprogramMap.put("appid", "");
                miniprogramMap.put("pagepath", "");

                //发送模板消息
                sendMessage(u.get("other").toString(), map, m, templateUtils, miniprogramMap, dataMap);

                bidder bidder = new bidder();
                bidder.setId(Integer.parseInt(u.get("bidderid").toString()));
                bidder.setAuctionstate("4");
                bidderDao.updateByPrimaryKeySelective(bidder);
            }


        }

        return m;
    }

    public Map addbidder(bidder bidder) {
        Map m = new HashMap();
        m.put("msg", 0);

        bidder.setAuctionstate("3");
        bidder.setIsviolations("2");

        // userdao.updateByPrimaryKeySelective(user);
        int count = bidderDao.insertSelective(bidder);
        if (count > 0) {
            m.put("bidderid", bidder.getId());
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    private Map sendMessage(String openid, Map map, Map m, TemplateUtils templateUtils, Map<String, String> miniprogramMap, Map<String, Object> dataMap) {
        //openId
        //String toUser=templateUtils.getOpenId(map.get("code").toString());
        String token2 = "34_WT7FHHI_ydZM4U2WOipcijHCkqc8pDEfvXFOZ0tCzbIVWyhSUE4KrRDuyRLQbxXzAp3CLcpUVXeL13WOT_KoWhwa89G6sTn47McBY2CwipXSpFnLeI3U2HTxkjcU_2nKZhpe2M-tLXGeAPULJGXbABASPG";
        Map paramMap = new HashMap();
        String sendMsgApi = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token2;// templateUtils.getToken(secret,appid);
        String toUrl = "http://weixin.qq.com/download";
//        Map<String,String> miniprogramMap =new HashMap<>();
//        miniprogramMap.put("appid", appid);
//        //所需跳转到小程序的具体页面路径
//        miniprogramMap.put("pagepath", "www.baidu.com");
        paramMap.put("touser", openid);
        paramMap.put("template_id", template_id);
        paramMap.put("url", toUrl);
        paramMap.put("miniprogram", miniprogramMap);
        paramMap.put("data", dataMap);
        String temmsg = templateUtils.doGetPost(sendMsgApi, "POST", paramMap);
        if (temmsg != null) {
            m.put("temmsg", temmsg);
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    private Map sendMessage(String openid, String toUrl, Map m, TemplateUtils templateUtils, Map<String, Object> dataMap) {
        //openId
        //String toUser=templateUtils.getOpenId(map.get("code").toString());
//        String token2 = "34_WT7FHHI_ydZM4U2WOipcijHCkqc8pDEfvXFOZ0tCzbIVWyhSUE4KrRDuyRLQbxXzAp3CLcpUVXeL13WOT_KoWhwa89G6sTn47McBY2CwipXSpFnLeI3U2HTxkjcU_2nKZhpe2M-tLXGeAPULJGXbABASPG";
        String token = templateUtils.getToken(secret, appid);
        Map paramMap = new HashMap();
        String sendMsgApi = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + token;// templateUtils.getToken(secret,appid);
        Map<String, String> miniprogramMap = new HashMap<>();
        miniprogramMap.put("appid", appid);
        //所需跳转到小程序的具体页面路径
        miniprogramMap.put("page", "http://carwx.baomanyi.net/");
        paramMap.put("touser", openid);
        paramMap.put("template_id", template_id);
        paramMap.put("url", toUrl);
//        paramMap.put("miniprogram", miniprogramMap);
        paramMap.put("data", dataMap);
        String temmsg = templateUtils.doGetPost(sendMsgApi, "POST", paramMap);
        System.out.println("temmsg====" + temmsg);
        if (temmsg != null) {
            m.put("temmsg", temmsg);
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }


    public Map successfulcontract(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        TemplateUtils templateUtils = new TemplateUtils();
        Map dataMap = new HashMap();
        Map infoMap = new HashMap();
        Map<String, String> miniprogramMap = new HashMap<>();
        dataMap.put("params1", infoMap.put("从不超田", "#173177"));
        dataMap.put("params2", infoMap.put("喜欢发癫", "#173177"));
        dataMap.put("params3", infoMap.put("七彩螺旋", "#173177"));
        dataMap.put("params4", infoMap.put("憨牛上天", "#173177"));

        miniprogramMap.put("appid", "");
        miniprogramMap.put("pagepath", "");
        Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid").toString()));
        //发送模板消息
        sendMessage(userinfo.getOther(), map, m, templateUtils, miniprogramMap, dataMap);
        return m;
    }

    public Map auctionstart(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        TemplateUtils templateUtils = new TemplateUtils();
        Map dataMap = new HashMap();
        Map infoMap = new HashMap();
        List<Userinfo> userinfoList = userdao.selectuser(map.get("auctionnumber").toString());
        for (Userinfo u : userinfoList) {


            Map<String, String> miniprogramMap = new HashMap<>();
            dataMap.put("params1", infoMap.put("从不超田", "#173177"));
            dataMap.put("params2", infoMap.put("喜欢发癫", "#173177"));
            dataMap.put("params3", infoMap.put("七彩螺旋", "#173177"));
            dataMap.put("params4", infoMap.put("憨牛上天", "#173177"));

            miniprogramMap.put("appid", "");
            miniprogramMap.put("pagepath", "");
            sendMessage(u.getOther(), map, m, templateUtils, miniprogramMap, dataMap);


        }

        return m;
    }


    public Map auctionend(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        try {
            auctioninfo auctioninfo = auctionDao.selectByauctionnumber(map.get("auctionnumber").toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String da = sdf.format(auctioninfo.getAuctiontime());
            Date endtime = null;

            endtime = sdf.parse(da);

            Calendar cal = Calendar.getInstance();
            cal.setTime(endtime);
            cal.add(Calendar.DATE, Integer.parseInt(auctioninfo.getCycle()));
            endtime = cal.getTime();
            date = new Date();
            startTime = date.getTime();
            midTime = (endtime.getTime() - startTime) / 1000;
            //定时
            //m =time2(m,map,templateUtils,paramMap);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return m;
    }


    /**
     * 方式二： 设定时间戳，倒计时
     */
    private Map time2(Map m, TemplateUtils templateUtils, Map paramMap, Long longtime, auctioninfo a) {
        if (midTime > longtime) {
            while (midTime > 0) {
                midTime--;
                if (midTime == longtime) {
                    //发送模板消息
                    sendMessage2(m, templateUtils, paramMap);
                    auctioninfo auc = new auctioninfo();
                    auc.setId(a.getId());
                    auc.setState("3");
                    auctionDao.updateByPrimaryKeySelective(auc);
                    m.put("msg", 1);
                    m.put("code", 0);
                    break;
                }
                try {
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        return m;
    }


    public Map auctionend2(Map map) {

        Map m = new HashMap();
        m.put("msg", 0);

        try {
            auctioninfo auctioninfo = auctionDao.selectByauctionnumber(map.get("auctionnumber").toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String da = sdf.format(auctioninfo.getAuctiontime());
            Date endtime = null;

            endtime = sdf.parse(da);

            Calendar cal = Calendar.getInstance();
            cal.setTime(endtime);
            cal.add(Calendar.DATE, Integer.parseInt(auctioninfo.getCycle()));
            endtime = cal.getTime();
            date = new Date();
            startTime = date.getTime();
            midTime = (endtime.getTime() - startTime) / 1000;
            //定时
            //m =time3(m,map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return m;
    }


    public Map auctionend3(Map map) {

        Map m = new HashMap();
        m.put("msg", 0);

        try {
            auctioninfo auctioninfo = auctionDao.selectByauctionnumber(map.get("auctionnumber").toString());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String da = sdf.format(auctioninfo.getAuctiontime());
            Date endtime = null;

            endtime = sdf.parse(da);

            Calendar cal = Calendar.getInstance();
            cal.setTime(endtime);
            cal.add(Calendar.DATE, Integer.parseInt(auctioninfo.getCycle()));
            endtime = cal.getTime();
            date = new Date();
            startTime = date.getTime();
            midTime = (endtime.getTime() - startTime) / 1000;
            //定时
            m = time4(m, map);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return m;
    }


    private Map time4(Map m, Map map) {

        while (midTime > 0) {
            midTime--;
            if (midTime == 0) {
                TemplateUtils templateUtils = new TemplateUtils();
                Map dataMap = new HashMap();
                Map infoMap = new HashMap();
                List<Userinfo> userinfoList = userdao.selectUser2(map.get("auctionnumber").toString());
                for (Userinfo u : userinfoList) {

                    Map<String, String> miniprogramMap = new HashMap<>();
                    dataMap.put("params1", infoMap.put("从不超田", "#173177"));
                    dataMap.put("params2", infoMap.put("喜欢发癫", "#173177"));
                    dataMap.put("params3", infoMap.put("七彩螺旋", "#173177"));
                    dataMap.put("params4", infoMap.put("憨牛上天", "#173177"));

                    miniprogramMap.put("appid", "");
                    miniprogramMap.put("pagepath", "");
                    m = sendMessage(u.getOther(), map, m, templateUtils, miniprogramMap, dataMap);
                    break;

                }
            /*long hh = midTime / 60 / 60 % 60;
            long mm = midTime / 60 % 60;
            long ss = midTime % 60;
            System.out.println("还剩" + hh + "小时" + mm + "分钟" + ss + "秒");*/
                try {
                    Thread.sleep(5 * 1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return m;
    }

    private String GOODS_IMG_PATH = "/home/installPackage/imgs/";

    public void IoReadImage(String imgUrl, HttpServletResponse response) throws IOException {

        ServletOutputStream out = null;
        FileInputStream ips = null;
        String upload = null;

        try {
            //获取图片存放路径
            //String imgPath = GOODS_IMG_PATH + "/" + imgUrl;
            ips = new FileInputStream(new File(imgUrl));
            String type = imgUrl.substring(imgUrl.indexOf(".") + 1);
            if (type.equals("png")) {
                response.setContentType("image/png");
            }
            if (type.equals("jpeg")) {
                response.setContentType("image/jpeg");
            }

            out = response.getOutputStream();
            //读取文件流
            int len = 0;
            byte[] buffer = new byte[1024 * 10];
            while ((len = ips.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
            ips.close();
        }

    }

    public void IoReadImage2(byte[] img, String path) {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(path);
            //将图片输处到流中
            outputStream.write(img);
            //刷新
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public Map corpsendWinningbid(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        TemplateUtils templateUtils = new TemplateUtils();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String time = sdf.format(new Date());
        Map paramMap = new HashMap();
        Map taskcardMap = new HashMap();
        Map btnMap = new HashMap();
        List btnList = new ArrayList();
        if (!"".equals(map.get("bidderid").toString())) {
            //中标人修改,发送通知
            getSuccessfulbidder(map, m, templateUtils, time, paramMap, taskcardMap, btnMap, btnList);
        }
        //查询所有参与竞拍的人,除中标人
        List<Map> list = auctionDao.selectNobidder(map.get("userid").toString(), map.get("auctionnumber").toString());
        if (list != null && !list.isEmpty()) {
            StringBuffer userBuffer = new StringBuffer();
            String type = null;
            auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
            type = "see";
            for (Map bidderMap : list) {
                userBuffer.append(bidderMap.get("openid"));
                userBuffer.append("|");
                bidder bidder = new bidder();
                bidder.setId(Integer.parseInt(bidderMap.get("id").toString()));
                bidder.setAuctionstate("4");
                bidderDao.updateByPrimaryKeySelective(bidder);
            }
            String touser = userBuffer.toString();
            Map btnMap1 = new HashMap();
            List btnList1 = new ArrayList();
            btnMap1.put("key", "key" + H_Format.getRandomNum());
            btnMap1.put("name", "详情");
            btnMap1.put("replace_name", "详情");
            btnList1.add(btnMap1);
            taskcardMap.put("title", "竞拍结果");
            taskcardMap.put("description", time + "<br>【神易好车】尊敬的用户,您参与的投标结果为投标失败。" +
                    "<br>编号:" + auctioninfo.getAuctionnumber() + "<br>发标时间:" + auctioninfo.getAuctiontime() + "<br>截止时间:" + auctioninfo.getAuctionendtime() +
                    "<br>车库名称:" + auctioninfo.getCardealers() + "<br>投标结果: 投标失败");
            taskcardMap.put("url", qyurl + "/#/bidingDocDetails?aboutId=" + auctioninfo.getId() + "&type=" + type);
            taskcardMap.put("task_id", "taskid" + H_Format.getRandomNum());
            taskcardMap.put("btn", btnList1);
            paramMap.put("touser", touser);
            paramMap.put("msgtype", "taskcard");
            paramMap.put("agentid", agentid);
            paramMap.put("taskcard", taskcardMap);
            //发送模板消息
            sendMessage2(m, templateUtils, paramMap);
        }
        return m;
    }

    //中标人修改,发送通知
    private void getSuccessfulbidder(Map map, Map m, TemplateUtils templateUtils, String time, Map paramMap, Map taskcardMap, Map btnMap, List btnList) {

        String type = null;

        auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
        if ("1".equals(auctioninfo.getIssesscar())) {
            type = "see";
        }
        Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid").toString()));
        bidder b = bidderDao.selectByPrimaryKey(Integer.parseInt(map.get("bidderid").toString()));
        btnMap.put("key", "key" + H_Format.getRandomNum());
        btnMap.put("name", "详情");
        btnMap.put("replace_name", "详情");
        btnList.add(btnMap);
        taskcardMap.put("title", "竞拍结果");
        taskcardMap.put("description", time + "<br>【神易好车】尊敬的用户,您参与的投标结果为投标成功。" +
                "<br>编号:" + auctioninfo.getAuctionnumber() + "<br>发标时间:" + auctioninfo.getAuctiontime() + "<br>截止时间:" + auctioninfo.getAuctionendtime() +
                "<br>车库名称:" + auctioninfo.getCardealers() + "<br>投标结果: 中标");
        taskcardMap.put("url", qyurl + "/#/detapage?aboutId=" + auctioninfo.getId() + "&paymentvoucher=" + b.getPaymentvoucher() + "&auctionnumber=" + auctioninfo.getAuctionnumber() + "&id=" + map.get("bidderid").toString() + "&userid=" + userinfo.getId());
        taskcardMap.put("task_id", "taskid" + H_Format.getRandomNum());
        taskcardMap.put("btn", btnList);
        paramMap.put("touser", userinfo.getOther());
        paramMap.put("msgtype", "taskcard");
        paramMap.put("agentid", agentid);
        paramMap.put("taskcard", taskcardMap);

        //发送模板消息
        sendMessage2(m, templateUtils, paramMap);

        //修改中标者的状态
        bidder bidder = new bidder();
        bidder.setId(Integer.parseInt(map.get("bidderid").toString()));
        bidder.setAuctionstate("1");
        bidderDao.updateByPrimaryKeySelective(bidder);
        //修改竞拍
        auctioninfo auctioninfo2 = new auctioninfo();
        auctioninfo2.setId(Integer.parseInt(map.get("aucid").toString()));
        auctioninfo2.setState("1");
        auctionDao.updateByPrimaryKeySelective(auctioninfo2);
    }


    private Map sendMessage2(Map m, TemplateUtils templateUtils, Map<String, Object> dataMap) {
        //openId
        //String toUser=templateUtils.getOpenId(map.get("code").toString());
        String token2 = "TBV5X_-OF8uu8AyJAPChLM7N69VIfjZYQC8qnfuL5MEjL43H7gabTS2rySnb7R3RetiIUTqvZUH0B6M1E3kuWDb3R_cOEA-sTzdl9mBO-6IE1kyIZqlmJZE8YssrAYpk447rh8kSKG1NEGikowsHp-RgmXxikpoeOOJqKDDRRgMWlIX-4VO0TMuNUbcNjAM1DZT2WQd0AWRJoEvTPxhPqw";
        String sendMsgApi = " https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + templateUtils.getcorpToken(corpsecret, corpid);
        /*paramMap.put("data", dataMap);*/
        String temmsg = templateUtils.doGetPost(sendMsgApi, "POST", dataMap);
        System.out.println("temmsg==" + temmsg);
        if (temmsg != null) {
            m.put("temmsg", temmsg);
            m.put("msg", 1);
            m.put("code", 0);
        }
        System.out.println("map====" + m);
        return m;
    }


    public Map corpauctionstart(Map map) {
        Map m = new HashMap();
        m.put("code", 0);
        m.put("msg", 0);
        if (map.get("useridstr") == null) {
            m.put("msg", "请传useridstr参数名！");
            return m;
        }
        if (map.get("aucid") == null) {
            m.put("msg", "请传aucid参数名！");
            return m;
        }
        String userstr = map.get("useridstr").toString();
        String issend = "";
        Object objIssend = map.get("issend");
        if ("".equals(userstr) && (objIssend == null || "".equals(objIssend))) {
            m.put("msg", "请选择需要发布的用户！");
            return m;
        }
        if (objIssend != null && !"".equals(objIssend)) {
            issend = objIssend.toString();
        }
        boolean flag = true;
        if ("".equals(userstr) || null == userstr) {
//            m.put("msg", "useridstr不能为空");
            flag = false;
        }
        auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
        if (auctioninfo != null) {
            aucuserid aucuserid = new aucuserid();
            aucuserid.setAucid(auctioninfo.getId());
            aucuserid.setUserstr(userstr);
            aucuserid.setState(auctioninfo.getState());
            //插入公众号是否推送字段
            if (!"".equals(issend)) {
                aucuserid.setIssend(issend);
            }
            if (map.get("ident") == null) {
                int selective = aucuseridDao.insertSelective(aucuserid);
            } else {
                aucuserid selectByAucId = aucuseridDao.selectByAucId(auctioninfo.getId());
                if (selectByAucId != null && !"".equals(issend)) {
                    selectByAucId.setIssend(issend);
                    int update = aucuseridDao.updateIssend(issend, selectByAucId.getId());
                }
            }
            if (flag) {
                if (!"".equals(issend)) {
                    String a = issend;
                    new Thread(() -> sendQiYe(userstr, a, m, auctioninfo)).start();
                } else {
                    new Thread(() -> sendQiYe(userstr, "0", m, auctioninfo)).start();
                }
            } else {
                if (!"".equals(issend)) {
                    String a = issend;
                    new Thread(() -> sendGzh(userstr, a, auctioninfo)).start();
                }
            }
        }
        return m;
    }

    public void sendQiYe(String userstr, String issend, Map m, auctioninfo auctioninfo) {
        TemplateUtils templateUtils = new TemplateUtils();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String time = sdf.format(new Date());
        Map paramMap = new HashMap();
        Map taskcardMap = new HashMap();
        Map btnMap = new HashMap();
        List btnList = new ArrayList();
        btnMap.put("key", "key" + H_Format.getRandomNum());
        btnMap.put("name", "详情");
        btnMap.put("replace_name", "详情");
        btnList.add(btnMap);
        taskcardMap.put("title", "投标通知");
        taskcardMap.put("description", time + "<br>【神易好车】尊敬的用户,您收到了一份新的标书。点击“详情”查看。" +
                "<br>编号:" + auctioninfo.getAuctionnumber() + "<br>发标时间:" + auctioninfo.getAuctiontime() + "<br>截止时间:" + auctioninfo.getAuctionendtime() +
                "<br>车库名称:" + auctioninfo.getCardealers());
        taskcardMap.put("url", qyurl + "/#/bidingDocDetails?aboutId=" + auctioninfo.getId());
        taskcardMap.put("task_id", "taskid" + H_Format.getRandomNum());
        taskcardMap.put("btn", btnList);
        paramMap.put("touser", userstr);
        paramMap.put("msgtype", "taskcard");
        paramMap.put("agentid", agentid);
        paramMap.put("taskcard", taskcardMap);
        //发送模板消息
        sendMessage2(m, templateUtils, paramMap);
        //发送公众号信息
        checkSendMessage(userstr, issend, templateUtils, auctioninfo);
    }

    public void sendGzh(String userstr, String issend, auctioninfo auctioninfo) {
        TemplateUtils templateUtils = new TemplateUtils();
        //发送公众号信息
        //checkSendMessage(userstr, issend, templateUtils, auctioninfo);
        if (issend != null && issend.equals("1")) {
            List<Integer> list = new ArrayList<>();
            //注册了公众号用户：userinfo表中有
            list.add(1);
            List<String> states = userdao.getByStates(list);
            System.out.println("注册了公众号用户:" + states.size());
            for (String openId : states) {
                if (!StringUtils.isEmpty(openId)) {
                    sendMessage2Public(openId, templateUtils, auctioninfo);
                }
            }
        }
    }

    /**
     * 是否推送消息给公众号用户
     *
     * @param touser 企业微信用户
     * @param issend 是否推送：0：不推送 1：推送
     */
    public void checkSendMessage(String touser, String issend, TemplateUtils templateUtils, auctioninfo auctioninfo) {
        //推送全部openid
        if (issend != null && issend.equals("1")) {
            List<Integer> list = new ArrayList<>();
            //注册了公众号用户：userinfo表中有
            list.add(1);
            List<String> states = userdao.getByStates(list);
            for (String openId : states) {
                if (!StringUtils.isEmpty(openId)) {
                    sendMessage2Public(openId, templateUtils, auctioninfo);
                }
            }
            //双重身份
            if (!"".equals(touser) && null != touser) {
                send2Double(touser, templateUtils, auctioninfo);
            }
            //推送未注册公众号的用户：userinfo表中没有，public_user有
            //send2Public(templateUtils,auctioninfo,type);
        }
        //只推送选中的openid
        if (issend != null && issend.equals("0")) {
            //双重身份
            if (!"".equals(touser) && null != touser) {
                send2Double(touser, templateUtils, auctioninfo);
            }
            //推送只有公众号的
            //send2Public(templateUtils,auctioninfo,type);
        }
    }

   /* private void send2Public(TemplateUtils templateUtils,auctioninfo auctioninfo,Integer type) {
        List<String> list = publicUserDao.getAllPublicUser();
        for(String openId:list){
            if (!StringUtils.isEmpty(openId)){
                //给当前企业微信对应的公众号发消息
                sendMessage2Public(openId,templateUtils,auctioninfo);
            }
        }
    }*/

    public void send2Double(String touser, TemplateUtils templateUtils, auctioninfo auctioninfo) {
        //双重身份
        List<String> others = new ArrayList<>();
        if (!StringUtils.isEmpty(touser)) {
            String[] split = touser.split("\\|");
            for (int i = 0; i < split.length; i++) {
                others.add(split[i]);
            }
        }
        List<String> needSend = userdao.getOpenIdByOthers(others);
        for (String openId : needSend) {
            if (!StringUtils.isEmpty(openId)) {
                //给当前企业微信对应的公众号发消息
                sendMessage2Public(openId, templateUtils, auctioninfo);
            }
        }
    }

    public Map<String, Object> getDataMap(auctioninfo auctioninfo, Integer type) {
        Map<String, Object> dataMap = null;
        Map<String, Object> infoMap = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String time = formatter.format(new Date());
        //1:成功
        if (type != null) {
            //发标通知
            if (type.equals(1)) {
                template_id = "yoLpk1kmY85gGWkYeIjDCOFkIVtJ0zZmj_8WE56wUKE";
                dataMap = new HashMap<>();
                infoMap = new HashMap();
                infoMap.put("value", time + "\r\n【神易好车】尊敬的用户，您收到了一份新的标书。");
                infoMap.put("color", "#333333");
                dataMap.put("first", infoMap);
                Map key1 = new HashMap();
                key1.put("value", auctioninfo.getAuctionnumber());
                key1.put("color", "#333333");
                dataMap.put("keyword1", key1);
                Map key2 = new HashMap();
                key2.put("value", auctioninfo.getAuctiontime());
                key2.put("color", "#333333");
                dataMap.put("keyword2", key2);
                Map key3 = new HashMap();
                key3.put("value", auctioninfo.getAuctionendtime());
                key3.put("color", "#333333");
                dataMap.put("keyword3", key3);
                Map remark = new HashMap();
                remark.put("value", auctioninfo.getCardealers());
                remark.put("color", "#333333");
                dataMap.put("remark", remark);
                return dataMap;
            }
            //竞标成功
            //竞标失败
        }
        return dataMap;
    }

    public static List<String> getDifferent(List<String> list1, List<String> list2) {
        Map<String, Integer> map = new HashMap<>(list1.size() + list2.size());
        List<String> diff = new ArrayList<>();
        List<String> maxList = list1;
        List<String> minList = list2;
        if (list2.size() > list1.size()) {
            maxList = list2;
            minList = list1;
        }
        for (String string : maxList) {
            map.put(string, 1);
        }
        for (String string : minList) {
            Integer cc = map.get(string);
            if (cc != null) {
                map.put(string, ++cc);
                continue;
            }
            map.put(string, 1);
        }
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }
        return diff;
    }

    public Map sendMessage2Public(String openid, TemplateUtils templateUtils, auctioninfo auctioninfo) {
        Map m = new HashMap();
        m.put("msg", 0);
        m.put("code", 1);
        Map<String, Object> dataMap = getDataMap(auctioninfo, 1);
        String toUrl = gzhUrl + "/#/bidingDocDetails?aboutId=" + auctioninfo.getId();
        Map map = sendMessage(openid, toUrl, m, templateUtils, dataMap);
        return map;
    }

    public Map corpauctionend(Map map) {
        long longtime = 151200;
        Map m = getTimedTasks(map, longtime);

        return m;
    }

    private Map getTimedTasks(Map map, long longtime) {
        Map m = new HashMap();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TemplateUtils templateUtils = new TemplateUtils();
        String time = sdf.format(new Date());
        Map paramMap = new HashMap();
        Map taskcardMap = new HashMap();
        Map btnMap = new HashMap();
        List btnList = new ArrayList();

        //auctioninfo auctioninfo= auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
        Date endtime = null;


        try {
            endtime = simpleDateFormat.parse(map.get("endtime").toString());


            Calendar cal = Calendar.getInstance();
            cal.setTime(endtime);
            //cal.add(Calendar.DATE,Integer.parseInt(auctioninfo.getCycle()));
            endtime = cal.getTime();
            date = new Date();
            startTime = date.getTime();
            midTime = (endtime.getTime() - startTime) / 1000;
            //定时

            if (midTime > longtime) {
                while (midTime > 0) {
                    midTime--;
                    if (midTime == longtime) {
                        String touser = null;
                        StringBuffer buffer = new StringBuffer();
                        //竞拍即将截止通知
                        auctioninfo auction = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
                        if ("2".equals(auction.getState())) {
                            List<Userinfo> userinfoList = userdao.selectuser(auction.getAuctionnumber());
                            for (Userinfo userinfo : userinfoList) {
                                buffer.append(userinfo.getOther());
                                buffer.append("|");
                            }
                            if (!"".equals(map.get("useridstr").toString())) {
                                touser = map.get("useridstr").toString();
                            } else {
                                touser = buffer.toString();
                            }
                            getContent(auction, time, paramMap, taskcardMap, btnMap, btnList, touser);
                            //发送模板消息
                            sendMessage2(m, templateUtils, paramMap);
                            m.put("msg", 1);
                            m.put("code", 0);
                        }
                        break;
                    }
                    try {
                        Thread.sleep(1000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


            // m =time2(m,templateUtils,paramMap,longtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return m;
    }

    private void getContent(auctioninfo auctioninfo, String time, Map paramMap, Map taskcardMap, Map btnMap, List btnList, String touser) {
        btnMap.put("key", "key" + H_Format.getRandomNum());
        btnMap.put("name", "详情");
        btnMap.put("replace_name", "详情");
        btnList.add(btnMap);
        taskcardMap.put("title", "竞拍即将截止通知");
        taskcardMap.put("description", time + "<br>【神易好车】尊敬的用户，您参与的竞拍即将结束，请尽快提交出价。" +
                "<br>编号:" + auctioninfo.getAuctionnumber() + "<br>发标时间:" + auctioninfo.getAuctiontime() + "<br>截止时间:" + auctioninfo.getAuctionendtime() +
                "<br>车库名称:" + auctioninfo.getCardealers());
        taskcardMap.put("url", qyurl + "/#/bidingDocDetails?aboutId=" + auctioninfo.getId());
        taskcardMap.put("task_id", "taskid" + H_Format.getRandomNum());
        taskcardMap.put("btn", btnList);
        paramMap.put("touser", touser);
        paramMap.put("msgtype", "taskcard");
        paramMap.put("agentid", agentid);
        paramMap.put("taskcard", taskcardMap);
    }

    public Map corpauctionend2(Map map) {
        long longtime = 21600;
        Map m = getTimedTasks(map, longtime);
        return m;
    }


    public Map corpauctionstop() {
        Map m = new HashMap();
        int count = 0;

        List<aucuserid> aucuseridList = aucuseridDao.selectAll();
        for (aucuserid aucuserid : aucuseridList) {
            if (!"3".equals(aucuserid.getState()) && !"4".equals(aucuserid.getState())) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                TemplateUtils templateUtils = new TemplateUtils();
                String time = sdf.format(new Date());
                Map paramMap = new HashMap();
                Map taskcardMap = new HashMap();
                Map btnMap = new HashMap();
                List btnList = new ArrayList();
                String touser = null;
                StringBuffer buffer = new StringBuffer();
                auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(aucuserid.getAucid());
                if (auctioninfo != null) {
                    if (!"1".equals(auctioninfo.getState())) {
                        long newdate = (new Date()).getTime();
                        long olddate = 0;
                        try {
                            olddate = simpleDateFormat.parse(auctioninfo.getAuctionendtime()).getTime();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //String newdate=  simpleDateFormat.format(new Date());
                        if (olddate <= newdate) {
                            List<Userinfo> userinfoList = userdao.selectauctionuser(auctioninfo.getAuctionnumber(), "2");
                            if (userinfoList.size() > 0 && userinfoList != null) {
                                for (Userinfo us : userinfoList) {
                                    buffer.append(us.getOther());
                                    buffer.append("|");
                                }

                                touser = buffer.toString();

                                btnMap.put("key", "key" + H_Format.getRandomNum());
                                btnMap.put("name", "详情");
                                btnMap.put("replace_name", "详情");
                                btnList.add(btnMap);
                                taskcardMap.put("title", "竞拍结束通知");
                                taskcardMap.put("description", time + "<br>【神易好车】尊敬的用户，您参与的竞拍已经结束，后续投标结果将通过消息通知的形式发送给您，请注意关注。" +
                                        "<br>编号:" + auctioninfo.getAuctionnumber() + "<br>发标时间:" + auctioninfo.getAuctiontime() + "<br>截止时间:" + auctioninfo.getAuctionendtime() +
                                        "<br>车库名称:" + auctioninfo.getCardealers());
                                taskcardMap.put("url", qyurl + "/#/bidingDocDetails?aboutId=" + auctioninfo.getId());
                                taskcardMap.put("task_id", "taskid" + H_Format.getRandomNum());
                                taskcardMap.put("btn", btnList);
                                paramMap.put("touser", touser);
                                paramMap.put("msgtype", "taskcard");
                                paramMap.put("agentid", agentid);
                                paramMap.put("taskcard", taskcardMap);

                                sendMessage2(m, templateUtils, paramMap);
                            }
                            auctioninfo a = new auctioninfo();
                            a.setId(aucuserid.getAucid());
                            a.setState("3");
                            count = auctionDao.updateByPrimaryKeySelective(a);

                            aucuserid auc = new aucuserid();
                            auc.setId(aucuserid.getId());
                            auc.setState("3");
                            aucuseridDao.updateByPrimaryKeySelective(auc);


                        }
                    }
                }
            }
        }
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }


    public Map updateauctioninfo(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);


        auctioninfo auctioninfo = new auctioninfo();
        auctioninfo.setId(Integer.parseInt(map.get("auctionid").toString()));
        auctioninfo.setState("1");
        //获取bidder，拿到other字段，赋值给auction的transaction字段
        int bidderId = Integer.parseInt(map.get("bidderid").toString());
        int other = bidderDao.getOtherById(bidderId);
        auctioninfo.setTransactionamount(other + "");
        bidder bidder = new bidder();
        bidder.setId(bidderId);
        bidder.setAuctionstate("1");
        bidder.setPaymentstate("2");
        bidderDao.updateByPrimaryKeySelective(bidder);
        Integer count = auctionDao.updateByPrimaryKeySelective(auctioninfo);

        auctioninfo a = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("auctionid").toString()));
        String[] carstr = a.getCarid().split(",");
        List<Map> bidderuserList = userdao.selectauctionuser2(a.getAuctionnumber(), "2");
        for (Map bidderuser : bidderuserList) {
            bidder bid = new bidder();
            bid.setId(Integer.parseInt(bidderuser.get("bidderid").toString()));
            bid.setOther(0);
            bidderDao.updateByPrimaryKeySelective(bid);
            for (String carid : carstr) {
                offerinfo o = new offerinfo();
                o.setCarid(Integer.parseInt(carid));
                o.setBidderid(Integer.parseInt(bidderuser.get("bidderid").toString()));
                offerinfo offerinfo = offerinfoDao.selectBybidderidandcarid(o);
                if (offerinfo != null) {
                    offerinfo off = new offerinfo();
                    off.setId(offerinfo.getId());
                    off.setOffer("");
                    offerinfoDao.updateByPrimaryKeySelective(off);
                }

            }
        }


        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map listuser(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        map.put("code", 0);
        List<Map> list = userdao.listuser2(map);
        if (list.size() > 0 && list != null) {
            m.put("msg", 1);
            m.put("bidderList", list);
            m.put("code", 0);
        }
        return m;
    }

    public Map updatebidderstate(Map map) {

        Map m = new HashMap();
        m.put("msg", 0);
        bidder bidder = new bidder();
        bidder.setId(Integer.parseInt(map.get("bidderid").toString()));
        bidder.setPaymentstate("1");
        bidder.setOther(0);
        int count = bidderDao.updateByPrimaryKeySelective(bidder);
        bidder b = bidderDao.selectByPrimaryKey(Integer.parseInt(map.get("bidderid").toString()));
        auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(b.getAuctioninfoid());
        String[] carstr = auctioninfo.getCarid().split(",");
        for (String carid : carstr) {
            offerinfo o = new offerinfo();
            o.setCarid(Integer.parseInt(carid));
            o.setBidderid(b.getId());
            offerinfo offerinfo = offerinfoDao.selectBybidderidandcarid(o);
            if (offerinfo != null) {
                offerinfo off = new offerinfo();
                off.setId(offerinfo.getId());
                off.setOffer("");
                offerinfoDao.updateByPrimaryKeySelective(off);
            }
        }
        //生成车辆图片
        if (carstr.length > 0) {
            try {
                //前端传的base64字符串
                String imgSrc =(String)map.get("imgBase64");
                if (!StringUtils.isEmpty(imgSrc)) {
                    String imgTar = carAddress + auctioninfo.getAuctionnumber() + "-" + auctioninfo.getId();
                    ImgUtil.generateImage(imgSrc, "/www/server" + imgTar + ".png");
                    auctioninfo.setCarimg(imgTar + ".png");
                    auctionDao.updateCarImg(auctioninfo);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map issesscar(auctioninfo auctioninfo) {
        Map m = new HashMap();
        m.put("msg", 0);
        int count = auctionDao.updateByPrimaryKeySelective(auctioninfo);
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map selectauction(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);

        Map biddermap = new HashMap();
        auctioninfo a = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
        bidder b = bidderDao.selectByUserid(map.get("userid").toString(), a.getAuctionnumber());
        biddercompany biddercompany = biddercompanyDao.selectByUserid(Integer.parseInt(map.get("userid").toString()));
        if (b == null) {
            bidder bidder = new bidder();
            bidder.setUserid(Integer.parseInt(map.get("userid").toString()));
            bidder.setAuctioninfoid(a.getId());
            bidder.setAuctionnumber(a.getAuctionnumber());
            bidder.setCarid(a.getCarid());
            bidder.setAuctionstate("3");
            bidderDao.insertSelective(bidder);

            List<offerinfo> offerinfoList = offerinfoDao.selectBybidderid(bidder.getId());
            biddermap = JSON.parseObject(JSON.toJSONString(bidder), Map.class);
            biddermap.put("offerList", offerinfoList);
        } else {
            List<offerinfo> offerinfoList = offerinfoDao.selectBybidderid(b.getId());
            biddermap = JSON.parseObject(JSON.toJSONString(b), Map.class);
            biddermap.put("offerList", offerinfoList);
        }


        if (a != null) {
            m.put("auctioninfo", a);
            m.put("bidder", biddermap);
            m.put("biddercompany", biddercompany);
            m.put("msg", 1);
            m.put("code", 1);
        }

        return m;
    }

    public Map selectbidderByid(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        bidder bidder = bidderDao.selectByPrimaryKey(Integer.parseInt(map.get("biderid").toString()));
        if (bidder != null) {
            m.put("bidder", bidder);
            m.put("msg", 1);
            m.put("code", 1);
        }
        return m;
    }

    public Map selectUserByid(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        Userinfo u = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid").toString()));
        if (u != null) {
            m.put("user", u);
            m.put("msg", 1);
            m.put("code", 1);
        }
        return m;
    }

    public Map selectAllUser(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        List<Map> u = userdao.selectAllUser(map);
        if (u != null) {
            m.put("user", u);
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map upateauctioncar(Map map) {

        Map m = new HashMap();
        m.put("msg", 0);
        car car = new car();
        auctioninfo a = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
        if (a != null) {
            String[] carstr = a.getCarid().split(",");
            for (String carid : carstr) {
                car.setId(Integer.parseInt(carid));
                car.setOther("2");
                carDao.updateByPrimaryKeySelective(car);
            }

            auctioninfo auctioninfo = new auctioninfo();
            auctioninfo.setId(a.getId());
            auctioninfo.setState("4");
            auctionDao.updateByPrimaryKeySelective(auctioninfo);
            //清除标书所有竞拍数据，
            bidderDao.deleteByAuctionNumber(a.getAuctionnumber());
            //同时记录标书流拍数据和当前最高出价者及最高出价
            int bidderId =bidderDao.getMaxPrice(a.getAuctionnumber());
            AuctionInfoBidderLoss loss=new AuctionInfoBidderLoss();
            loss.setAuctioninfoid(a.getId());
            loss.setBidderid(bidderId);
            auctionInfoBidderLossDao.save(loss);
            //
            m.put("msg", 1);
            m.put("code", 0);

        } else {
            m.put("msg", "竞拍id不存在");
        }


        return m;
    }

    public Map delbond(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        biddercompany biddercompany = new biddercompany();
        int count = 0;
        String[] str = map.get("bondid").toString().split(",");
        for (String id : str) {
            biddercompany company = biddercompanyDao.selectByPrimaryKey(Integer.parseInt(id));
            //Userinfo userinfo=userdao.selectByPrimaryKey(company.getUserid());
            Userinfo userinfo = new Userinfo();
            userinfo.setId(company.getUserid());
            userinfo.setParticipate("2");
            userdao.updateByPrimaryKeySelective(userinfo);

            biddercompany.setId(Integer.parseInt(id));
            biddercompany.setOther("-1");
            count = biddercompanyDao.updateByPrimaryKeySelective(biddercompany);
        }
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map updatebondgl(biddercompany biddercompany) {

        Map m = new HashMap();
        m.put("msg", 0);
        if (biddercompany.getId() == null) {
            m.put("code", 0);
            m.put("msg", "保证金id不能为空");
            return m;
        }
        int count = biddercompanyDao.updateByPrimaryKeySelective(biddercompany);

        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }
        return m;
    }

    public Map corpauctionend21() {
        Map m = new HashMap();
        m.put("msg", 0);

        List<aucuserid> aucuseridList = aucuseridDao.selectAll();

        for (aucuserid aucuserid : aucuseridList) {
            if (!"3".equals(aucuserid.getState())) {


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                TemplateUtils templateUtils = new TemplateUtils();
                String time = sdf.format(new Date());
                Map paramMap = new HashMap();
                Map taskcardMap = new HashMap();
                Map btnMap = new HashMap();
                List btnList = new ArrayList();
                String touser = null;
                StringBuffer buffer = new StringBuffer();
                auctioninfo auction = auctionDao.selectByPrimaryKey(aucuserid.getAucid());

                String newdate = simpleDateFormat.format(new Date());
                if (newdate.equals(auction.getAuctionendtime())) {
                    getmessage(m, aucuserid, templateUtils, time, paramMap, taskcardMap, btnMap, btnList, auction);
                }

            }

        }


        return m;


    }

    private void getmessage(Map m, aucuserid aucuserid, TemplateUtils templateUtils, String time, Map paramMap, Map taskcardMap, Map btnMap, List btnList, auctioninfo auction) {
        String touser;
        List<String> userstrList = new ArrayList();
        String[] userstr = aucuserid.getUserstr().split("\\|");


        StringBuffer bu = new StringBuffer();
        for (String userqyid : userstr) {
            //
            userstrList.add(userqyid);
        }
        //System.out.println(userstrList.size());
        // System.out.println(userstrList.toString());
        for (int i = 0; i < userstrList.size(); i++) {
            Userinfo userinfo = userdao.selectuser2(userstrList.get(i), auction.getAuctionnumber(), "2");
            if (userinfo != null) {
                userstrList.remove(i);
                i--;
            }
        }

        if (userstrList.size() > 0 && userstrList != null) {
            for (String u : userstrList) {
                bu.append(u);
                bu.append("|");
            }
            touser = bu.toString();
            //System.out.println("touser:"+touser);


            getContent(auction, time, paramMap, taskcardMap, btnMap, btnList, touser);
            //发送模板消息
            sendMessage2(m, templateUtils, paramMap);
        }

        m.put("msg", 1);
        m.put("code", 0);
    }

    public Map corpauctionend22() {

        Map m = new HashMap();
        m.put("msg", 0);

        List<aucuserid> aucuseridList = aucuseridDao.selectAll();

        for (aucuserid aucuserid : aucuseridList) {
            if (!"3".equals(aucuserid.getState())) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                TemplateUtils templateUtils = new TemplateUtils();
                String time = sdf.format(new Date());
                Map paramMap = new HashMap();
                Map taskcardMap = new HashMap();
                Map btnMap = new HashMap();
                List btnList = new ArrayList();
                String touser = null;
                StringBuffer buffer = new StringBuffer();
                auctioninfo auction = auctionDao.selectByPrimaryKey(aucuserid.getAucid());
                try {
                    Date date = simpleDateFormat.parse(auction.getAuctionendtime());
                    String newdate = simpleDateFormat.format(new Date());

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    calendar.add(Calendar.DATE, -1);

                    date = calendar.getTime();
                    String olddate = simpleDateFormat.format(date);
                    if (newdate.equals(olddate)) {
                        getmessage(m, aucuserid, templateUtils, time, paramMap, taskcardMap, btnMap, btnList, auction);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return m;
    }

    public Map updateauction(auctioninfo auctioninfo) {
        Map m = new HashMap();
        m.put("msg", 0);
        if (auctioninfo.getId() != null) {
            m.put("msg", "竞拍id不能为空");
            m.put("code", "0");

        }

        int count = auctionDao.updateByPrimaryKeySelective(auctioninfo);
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", "0");
        }
        //修改生成的车辆图片
        //前端传的base64字符串
        String imgSrc = auctioninfo.getImgBase64();
        if (!StringUtils.isEmpty(imgSrc)) {
            String imgTar = carAddress + auctioninfo.getAuctionnumber() + "-" + auctioninfo.getId();
            try {
                ImgUtil.generateImage(imgSrc, "/www/server" + imgTar + ".png");
                auctioninfo.setCarimg(imgTar + ".png");
                auctionDao.updateCarImg(auctioninfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return m;
    }

    public Map upateauctioncarnotice(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        if ("".equals(map.get("aucid")) || map.get("aucid") == null) {
            m.put("msg", "竞拍id不能为空");
            m.put("code", "0");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TemplateUtils templateUtils = new TemplateUtils();
        String time = sdf.format(new Date());

        String touser = null;
        auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
        List<Map> bidderzbList = userdao.selectauctionuser2(auctioninfo.getAuctionnumber(), "1");
        if (bidderzbList.size() > 0 && bidderzbList != null) {
            Map paramMap = new HashMap();
            Map taskcardMap = new HashMap();
            Map btnMap = new HashMap();
            List btnList = new ArrayList();
            for (Map bm : bidderzbList) {
                bidder bidder = new bidder();
                bidder.setId(Integer.parseInt(bm.get("bidderid").toString()));
                bidder.setAuctionstate("4");
                bidder.setIsviolations("1");
                bidder.setOther(0);
                bidderDao.updateByPrimaryKeySelective(bidder);

                bidder b = bidderDao.selectByPrimaryKey(Integer.parseInt(bm.get("bidderid").toString()));
                auctioninfo a = auctionDao.selectByPrimaryKey(b.getAuctioninfoid());
                String[] carstr = a.getCarid().split(",");
                for (String carid : carstr) {
                    offerinfo o = new offerinfo();
                    o.setCarid(Integer.parseInt(carid));
                    o.setBidderid(b.getId());
                    offerinfo offerinfo = offerinfoDao.selectBybidderidandcarid(o);
                    if (offerinfo != null) {
                        offerinfo off = new offerinfo();
                        off.setId(offerinfo.getId());
                        off.setOffer("");
                        offerinfoDao.updateByPrimaryKeySelective(off);
                    }
                }


                touser = bm.get("other").toString();
            }

            btnMap.put("key", "key" + H_Format.getRandomNum());
            btnMap.put("name", "详情");
            btnMap.put("replace_name", "详情");
            btnList.add(btnMap);
            taskcardMap.put("title", "流拍通知");
            taskcardMap.put("description", time + "<br>【神易好车】尊敬的用户，由于您中标后三日内未打款，我们将会向您扣除违约罚金，您未付款的标书做流拍处理。" +
                    "<br>编号:" + auctioninfo.getAuctionnumber() + "<br>发标时间:" + auctioninfo.getAuctiontime() + "<br>截止时间:" + auctioninfo.getAuctionendtime() +
                    "<br>车库名称:" + auctioninfo.getCardealers());
            taskcardMap.put("url", qyurl + "/#/bidingDocDetails?aboutId=" + auctioninfo.getId() + "&type=see");
            taskcardMap.put("task_id", "taskid" + H_Format.getRandomNum());
            taskcardMap.put("btn", btnList);
            paramMap.put("touser", touser);
            paramMap.put("msgtype", "taskcard");
            paramMap.put("agentid", agentid);
            paramMap.put("taskcard", taskcardMap);

            sendMessage2(m, templateUtils, paramMap);

        } else {
            Map paramMap = new HashMap();
            Map taskcardMap = new HashMap();
            Map btnMap = new HashMap();
            List btnList = new ArrayList();
            StringBuffer buffer = new StringBuffer();
            List<Map> bidderList = userdao.selectauctionuser2(auctioninfo.getAuctionnumber(), "2");
            if (bidderList.size() > 0 && bidderList != null) {
                for (Map biddermap : bidderList) {

                    bidder bidder = new bidder();
                    bidder.setId(Integer.parseInt(biddermap.get("bidderid").toString()));
                    bidder.setAuctionstate("4");
                    bidder.setOther(0);
                    bidderDao.updateByPrimaryKeySelective(bidder);

                    bidder b = bidderDao.selectByPrimaryKey(Integer.parseInt(biddermap.get("bidderid").toString()));
                    auctioninfo a = auctionDao.selectByPrimaryKey(b.getAuctioninfoid());
                    String[] carstr = a.getCarid().split(",");
                    for (String carid : carstr) {
                        offerinfo o = new offerinfo();
                        o.setCarid(Integer.parseInt(carid));
                        o.setBidderid(b.getId());
                        offerinfo offerinfo = offerinfoDao.selectBybidderidandcarid(o);
                        if (offerinfo != null) {
                            offerinfo off = new offerinfo();
                            off.setId(offerinfo.getId());
                            off.setOffer("");
                            offerinfoDao.updateByPrimaryKeySelective(off);
                        }
                    }


                    buffer.append(biddermap.get("other"));
                    buffer.append("|");
                }
                touser = buffer.toString();

                Map btnMap1 = new HashMap();
                List btnList1 = new ArrayList();
                btnMap1.put("key", "key" + H_Format.getRandomNum());
                btnMap1.put("name", "详情");
                btnMap1.put("replace_name", "详情");
                btnList1.add(btnMap1);
                taskcardMap.put("title", "竞拍结果");
                taskcardMap.put("description", time + "<br>【神易好车】尊敬的用户,您参与的投标结果为投标失败。" +
                        "<br>编号:" + auctioninfo.getAuctionnumber() + "<br>发标时间:" + auctioninfo.getAuctiontime() + "<br>截止时间:" + auctioninfo.getAuctionendtime() +
                        "<br>车库名称:" + auctioninfo.getCardealers() + "<br>投标结果: 投标失败");
                taskcardMap.put("url", qyurl + "/#/bidingDocDetails?aboutId=" + auctioninfo.getId() + "&type=see");
                taskcardMap.put("task_id", "taskid" + H_Format.getRandomNum());
                taskcardMap.put("btn", btnList1);
                paramMap.put("touser", touser);
                paramMap.put("msgtype", "taskcard");
                paramMap.put("agentid", agentid);
                paramMap.put("taskcard", taskcardMap);

                //发送模板消息
                sendMessage2(m, templateUtils, paramMap);
            }


        }


        return m;
    }

    public Map delauctioninfo() {
        Map m = new HashMap();
        m.put("msg", 0);
        Map map = new HashMap();
        int coun = 0;
        map.put("isviolations", "1");
        map.put("paymentstate", "1");
        List<Map> list = auctionDao.selectauction(map);
        Date newdate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (Map aucmap : list) {
            try {
                Date olddate = formatter.parse(aucmap.get("auctionendtime").toString());
                Calendar rightNow = Calendar.getInstance();

                rightNow.setTime(olddate);
                rightNow.add(Calendar.MONTH, 1);
                Date dt1 = rightNow.getTime();
                String reStr = formatter.format(dt1);
                String newStr = formatter.format(newdate);
                Date redate = formatter.parse(reStr);
                Date nedate = formatter.parse(newStr);
                // String r=  formatter.format(olddate.getTime());
                if (redate.getTime() <= nedate.getTime()) {

                    String[] carstr = aucmap.get("carid").toString().split(",");
                    for (String carid : carstr) {
                        offerinfo o = new offerinfo();
                        Integer carId=Integer.parseInt(carid);
                        o.setCarid(carId);
                        if (aucmap.get("bidderid") != null) {
                            o.setBidderid(Integer.parseInt(aucmap.get("bidderid").toString()));
                            coun = offerinfoDao.del(o);
                        }
                        //删除中间表
                        AuctionInfoCar infoCar=new AuctionInfoCar();
                        infoCar.setCarid(carId);
                        infoCar.setAuctioninfoid(Integer.parseInt(aucmap.get("id").toString()));
                        auctionInfoCarDao.delete(infoCar);
                    }
                    coun = auctionDao.deleteByPrimaryKey(Integer.parseInt(aucmap.get("id").toString()));
                    if (aucmap.get("bidderid") != null) {
                        coun = bidderDao.deleteByPrimaryKey(Integer.parseInt(aucmap.get("bidderid").toString()));
                    }

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        List<auctioninfo> auctioninfoList = auctionDao.selectAllAuc();
        for (auctioninfo a : auctioninfoList) {
            Date olddate = null;
            try {
                olddate = formatter.parse(a.getAuctionendtime());

                Calendar rightNow = Calendar.getInstance();

                rightNow.setTime(olddate);
                rightNow.add(Calendar.MONTH, 1);
                Date dt1 = rightNow.getTime();
                String reStr = formatter.format(dt1);
                String newStr = formatter.format(newdate);
                Date redate = formatter.parse(reStr);
                Date nedate = formatter.parse(newStr);

                if (redate.getTime() <= nedate.getTime()) {
                    List<Map> bidderList = userdao.selectauctionuser3(a.getAuctionnumber(), "4", "1");
                    for (Map bidderuser : bidderList) {

                        String[] carstr = a.getCarid().split(",");
                        for (String carid : carstr) {
                            offerinfo o = new offerinfo();
                            o.setCarid(Integer.parseInt(carid));
                            o.setBidderid(Integer.parseInt(bidderuser.get("bidderid").toString()));
                            coun = offerinfoDao.del(o);
                        }
                        coun = bidderDao.deleteByPrimaryKey(Integer.parseInt(bidderuser.get("bidderid").toString()));
                    }
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (coun > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        }


        return m;
    }

    public Map listauctioninfopaging(Map map) {

        Map m = new HashMap();
        m.put("msg", 0);
        Map biddermap = new HashMap();
        List list = new ArrayList();
        if (map.get("auctiontime") != null) {
            if ("other".equals(map.get("auctiontime").toString())) {
                map.remove("auctiontime");
                map.put("other", "other");
            } else if ("auctionendtime".equals(map.get("auctiontime").toString())) {
                map.remove("auctiontime");
                map.put("auctionendtime", "auctionendtime");
            }

        } else {
            map.put("id", "id");
        }
        Integer pageInt = 1;
        Integer limitInt = 10;
        Object page = map.get("page");
        if (page != null) {
            pageInt = Integer.parseInt(page.toString());
            map.put("page",pageInt-1);
        }
        //查询总数量
        int totalcount = 0;
//        List<auctioninfo> auctioninfoListk = auctionDao.selectListk4(map);//后台管理系统：违规列表
//        List<auctioninfo> auctioninfoListk = new ArrayList<>();//后台管理系统：违规列表
        if ("4".equals(map.get("state").toString()) && "2".equals(map.get("paymentstate").toString()) && "4".equals(map.get("auctionstate").toString()) && "1".equals(map.get("isviolations").toString())) {
            //现在逻辑根据车架号搜索车辆，只获取包含该车辆的数据
            String carframenumber = (String) map.get("carframenumber");
            List<auctioninfo> resultList = new ArrayList<>();
            if (!StringUtils.isEmpty(carframenumber)) {
                totalcount=auctionDao.selectList7Count(map);
                resultList=auctionDao.selectList7(map);
            } else {
                totalcount=auctionDao.selectList6Count(map);
                resultList = auctionDao.selectList6(map);;
            }
            for (auctioninfo a : resultList) {
                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                if ("".equals(map.get("isviolations").toString())) {
                    m.put("msg", "isviolations 不能为空");
                    m.put("code", 0);
                    return m;
                }
                biddermap.put("isviolations", map.get("isviolations").toString());
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.getCarid().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    carList.add(car);
                }
                auctionMap.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){

                List<Map> bidderList = userdao.selectbidderList(biddermap);

                if (bidderList.size() > 0 && bidderList != null) {
                    auctionMap.put("bidderList", bidderList);
                    list.add(auctionMap);
                }
            }

            //后台管理系统：成交列表
        } else if ("1".equals(map.get("state").toString()) && "1".equals(map.get("paymentstate").toString()) && "1".equals(map.get("auctionstate").toString())) {
            //PageHelper.startPage(Integer.parseInt(map.get("page").toString()),Integer.parseInt(map.get("limit").toString()));
            String carframenumber = (String) map.get("carframenumber");
            List<auctioninfo> resultList = new ArrayList<>();
            if (!StringUtils.isEmpty(carframenumber)) {
                totalcount=auctionDao.selectList7Count(map);
                resultList=auctionDao.selectList7(map);
            } else {
                totalcount=auctionDao.selectList6Count(map);
                resultList=auctionDao.selectList6(map);
            }
            for (auctioninfo a : resultList) {
                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.getCarid().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    carList.add(car);
                }
                auctionMap.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                if (bidderList.size() > 0 && bidderList != null) {
                    auctionMap.put("bidderList", bidderList);
                    list.add(auctionMap);
                }

            }
        }

        if (list.size() > 0) {
            Integer pageNum = Integer.parseInt(map.get("limit").toString());
            double totalDouble = Double.parseDouble(totalcount + "");
            double pageNumDouble = Double.parseDouble(pageNum + "");
            double ceil = Math.ceil(totalDouble / pageNumDouble);
            m.put("code", 0);
            m.put("total", totalcount);
            m.put("data", list);
            //每页数量
            m.put("pageNum", pageNum);
            //共几页
            m.put("pages", (int)ceil);
            m.put("msg", 1);
        } else {
            m.put("code", 0);
            m.put("msg", 0);
        }
        return m;
    }

    public Map listauctioninfopaging1(Map map) {

        Map m = new HashMap();
        m.put("msg", 0);
        Map biddermap = new HashMap();
        List list = null;
        if (map.get("auctiontime") != null) {
            if ("other".equals(map.get("auctiontime").toString())) {
                map.remove("auctiontime");
                map.put("other", "other");
            } else if ("auctionendtime".equals(map.get("auctiontime").toString())) {
                map.remove("auctiontime");
                map.put("auctionendtime", "auctionendtime");
            }

        } else {
            map.put("id", "id");
        }
        List<auctioninfo> auctioninfoListk = auctionDao.selectListk4(map);//后台管理系统：违规列表
        if ("4".equals(map.get("state").toString()) && "2".equals(map.get("paymentstate").toString()) && "4".equals(map.get("auctionstate").toString()) && "1".equals(map.get("isviolations").toString())) {
            list = new ArrayList();
            String carframenumber = (String) map.get("carframenumber");
            List<auctioninfo> resultList = null;
            if (!StringUtils.isEmpty(carframenumber)) {
                List<car> carList = carDao.getByIdAndCarFrameNumber(map);
                if (carList != null && !carList.isEmpty()) {
                    List<Integer> idList = carList.stream().map(car::getId).collect(Collectors.toList());
                    resultList = new ArrayList<>();
                    for (auctioninfo a : auctioninfoListk) {
                        String[] str = a.getCarid().split(",");
                        for (String s : str) {
                            int carIdStr = Integer.parseInt(s);
                            if (idList.contains(carIdStr)) {
                                resultList.add(a);
                            }
                        }
                    }
                }
            } else {
                resultList = auctioninfoListk;
            }
            for (auctioninfo a : resultList) {
                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                if ("".equals(map.get("isviolations").toString())) {
                    m.put("msg", "isviolations 不能为空");
                    m.put("code", 0);
                    return m;
                }
                biddermap.put("isviolations", map.get("isviolations").toString());
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);

                //查询参与的竞拍人
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                if (bidderList.size() > 0 && bidderList != null) {
                    auctionMap.put("bidderList", bidderList);
                    //查询要竞拍的车辆
                    String[] str = a.getCarid().split(",");
                    List carList = new ArrayList();
                    for (String s : str) {
                        car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                        //新增根据id和车架号查询
//                        car car = carDao.getByIdAndCarFrameNumber(Integer.parseInt(s), (String) map.get("carframenumber"));
                        carList.add(car);
                    }
                    auctionMap.put("carList", carList);
                    list.add(auctionMap);
                }
            }//后台管理系统：成交列表
        } else if ("1".equals(map.get("state").toString()) && "1".equals(map.get("paymentstate").toString()) && "1".equals(map.get("auctionstate").toString())) {

            //PageHelper.startPage(Integer.parseInt(map.get("page").toString()),Integer.parseInt(map.get("limit").toString()));
            list = new ArrayList();
            String carframenumber = (String) map.get("carframenumber");
            List<auctioninfo> resultList = null;
            if (!StringUtils.isEmpty(carframenumber)) {
                List<car> carList = carDao.getByIdAndCarFrameNumber(map);
                if (carList != null && !carList.isEmpty()) {
                    List<Integer> idList = carList.stream().map(car::getId).collect(Collectors.toList());
                    resultList = new ArrayList<>();
                    for (auctioninfo a : auctioninfoListk) {
                        String[] str = a.getCarid().split(",");
                        for (String s : str) {
                            int carIdStr = Integer.parseInt(s);
                            if (idList.contains(carIdStr)) {
                                resultList.add(a);
                            }
                        }
                    }
                }
            } else {
                resultList = auctioninfoListk;
            }
            for (auctioninfo a : resultList) {
                biddermap.put("auctionnumber", a.getAuctionnumber());
                if (!"".equals(map.get("userid2").toString())) {
                    biddermap.put("userid2", map.get("userid2").toString());
                }
                if (!"".equals(map.get("auctionstate").toString())) {
                    biddermap.put("auctionstate", map.get("auctionstate").toString());
                }
                if (!"".equals(map.get("paymentstate").toString())) {
                    biddermap.put("paymentstate", map.get("paymentstate").toString());
                }
                Map auctionMap = JSON.parseObject(JSON.toJSONString(a), Map.class);
                //查询要竞拍的车辆
                String[] str = a.getCarid().split(",");
                List carList = new ArrayList();
                for (String s : str) {
                    car car = carDao.selectByPrimaryKey(Integer.parseInt(s));
                    //新增根据id和车架号查询
//                    car car = carDao.getByIdAndCarFrameNumber(Integer.parseInt(s), (String) map.get("carframenumber"));
                    carList.add(car);
                }
                auctionMap.put("carList", carList);
                //查询参与的竞拍人
                //if(map.get("userid") ==null){
                List<Map> bidderList = userdao.selectbidderList(biddermap);
                if (bidderList.size() > 0 && bidderList != null) {
                    auctionMap.put("bidderList", bidderList);
                    list.add(auctionMap);
                }

            }
        }

        if (list.size() > 0 && list != null) {


            int totalcount = auctionDao.selectListPageCount(map);
            //以前逻辑：假分页
            /*int totalcount = list.size();
            int pagecount = 0;
            List<String> subList;
            Integer currentPage = Integer.parseInt(map.get("page").toString());
            Integer pagesize = Integer.parseInt(map.get("limit").toString());

            int m2 = totalcount % pagesize;
            if (m2 > 0) {
                pagecount = totalcount / pagesize + 1;
            } else {
                pagecount = totalcount / pagesize;
            }
            if (m2 == 0) {
                subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
            } else {
                if (currentPage == pagecount) {
                    subList = list.subList((currentPage - 1) * pagesize, totalcount);
                } else {
                    subList = list.subList((currentPage - 1) * pagesize, pagesize * (currentPage));
                }
            }
            //PageHelper.startPage(Integer.parseInt(map.get("page").toString()),Integer.parseInt(map.get("limit").toString()));
            //PageInfo<Map> pageInfo=new PageInfo<>(list);

            // m.put("list",pageInfo);

            m.put("code", 0);
            m.put("total", totalcount);
            m.put("data", subList);
            m.put("pageNum", subList.size());
            m.put("pages", pagecount);
            m.put("msg", 1);*/

            m.put("code", 0);
            m.put("total", totalcount);
            m.put("data", list);
//            m.put("pageNum", list.size());
//            m.put("pages", pagecount);
            m.put("msg", 1);
        } else {
            m.put("code", 0);
            m.put("msg", 0);
        }
        //m.put("pageSize",pageInfo.getPageSize());
        /* m.put("size",pageInfo.getSize());
         */
        return m;
    }

    public Map delbidder(Map map) {
        Map m = new HashMap();
        if (map.get("aucid") == null || map.get("bidderid") == null) {
            m.put("msg", "缺少参数aucid或者bidderid");
            return m;
        }
        auctioninfo auctioninfo = auctionDao.selectByPrimaryKey(Integer.parseInt(map.get("aucid").toString()));
        String[] carstr = auctioninfo.getCarid().split(",");
        int count = 0;
        for (String carid : carstr) {
            offerinfo o = new offerinfo();
            o.setBidderid(Integer.parseInt(map.get("bidderid").toString()));
            o.setCarid(Integer.parseInt(carid));
            offerinfo offerinfo = offerinfoDao.selectBybidderidandcarid(o);
            if (offerinfo != null) {
                count = offerinfoDao.deleteByPrimaryKey(offerinfo.getId());
            }
        }

        count = bidderDao.deleteByPrimaryKey(Integer.parseInt(map.get("bidderid").toString()));
        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        } else {
            m.put("msg", "faill");
        }
        return m;
    }

    public Map updateaucuser(Map map) {
        Map m = new HashMap();

        if (map.get("aucid") == null || map.get("userstr") == null) {
            m.put("msg", "缺少参数aucid或者userstr");
            return m;
        }
        aucuserid a = new aucuserid();
        a.setAucid(Integer.parseInt(map.get("aucid").toString()));
        aucuserid aucuserid = aucuseridDao.selectByaucid(a);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(aucuserid.getUserstr());
        stringBuffer.append("|");
        stringBuffer.append(map.get("userstr").toString());
        aucuserid au = new aucuserid();
        au.setId(aucuserid.getId());
        au.setUserstr(stringBuffer.toString());
        String issend = (String) map.get("issend");
        if (issend != null && !"".equals(issend)) {
            au.setIssend(map.get("issend").toString());
        }
        int count = aucuseridDao.updateByPrimaryKeySelective(au);

        if (count > 0) {
            m.put("msg", 1);
            m.put("code", 0);
        } else {
            m.put("msg", "faill");
        }
        return m;
    }


    public Map selectAllUserInfo(Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        Integer pageInt = 1;
        Integer limitInt = 10;
        Object page = map.get("page");
        if (page != null) {
            pageInt = Integer.parseInt(page.toString());
        }
        Object limit = map.get("limit");
        if (limit != null) {
            limitInt = Integer.parseInt(limit.toString());
        }
        map.put("page", (pageInt - 1) * limitInt);
        map.put("limit", limitInt);
        Long countUserInfo = userdao.countUserInfo(map);
        List<Map> u = userdao.selectAllUserInfo(map);
        if (u != null) {
            m.put("user", u);
            m.put("msg", 1);
            m.put("total", countUserInfo);
            m.put("code", 0);
        }
        return m;
    }

    /**
     * auctionstate: "1"
     * paymentstate: "2"
     * state: "1"
     * userid: "371"
     * userid2: "371"
     *
     * @param map
     * @return
     */
    public Map commonAuction(Map map) {
        Map<String, Object> m = new HashMap<>();
        m.put("msg", 0);
        m.put("code", 0);
        if (map.get("userid2") == null ||
                map.get("auctionstate") == null ||
                map.get("paymentstate") == null ||
                map.get("state") == null) {
            return m;
        }
        Userinfo userinfo = userdao.selectByPrimaryKey(Integer.parseInt(map.get("userid2").toString()));
        if (userinfo != null) {
            Integer state = userinfo.getState();
            if (state != null) {
                //单企业微信用户
                if (state == 0 || state == 2) {
                    List<bidder> bidderList = bidderDao.commonBidder(map);
                    if (bidderList != null && !bidderList.isEmpty()) {
                        Set<String> collect = bidderList.stream().map(bidder::getAuctionnumber).collect(Collectors.toSet());
                        if (!collect.isEmpty()) {
                            List<auctioninfo> auctioninfoList = auctionDao.listByNumber(collect);
                            if (auctioninfoList != null && !auctioninfoList.isEmpty()) {
                                auctioninfoList.forEach(a -> {
                                    String number = a.getAuctionnumber();
                                    List<bidder> bidders = bidderList.stream().filter(b -> b.getAuctionnumber().equals(number)).collect(Collectors.toList());
                                    a.setBidderList(bidders);
                                });
                                m.put("msg", 1);
                                m.put("list", auctioninfoList);
                            }
                        }
                    }
                }
            }
        }
        return m;
    }

    public Map<String,Object> lossAuction(Map map) {
        Map<String,Object> m=new HashMap<>();
        int totalcount = auctionDao.lossAuctionCount(map);
        List<auctioninfo> list = auctionDao.lossAuction(map);
        String limit=map.get("limit").toString();
        if (StringUtils.isEmpty(limit)){
            limit="0";
        }
        Integer pageNum = Integer.parseInt(limit);
        double totalDouble = Double.parseDouble(totalcount + "");
        double pageNumDouble = Double.parseDouble(pageNum + "");
        double ceil = Math.ceil(totalDouble / pageNumDouble);
        m.put("code", 0);
        m.put("total", totalcount);
        m.put("data", list);
        //每页数量
        m.put("pageNum", pageNum);
        //共几页
        m.put("pages", (int)ceil);
        m.put("msg", 1);
        return m;
    }

    public Map<String, Object> updateLossAuction(Map<String, Object> map) {
        Object bidderIdObj = map.get("bidderid");
        Object autionInfoIdObj = map.get("autioninfoid");
        Integer bidderId=0,autionInfoId=0;
        if (bidderIdObj!=null){
            bidderId=Integer.parseInt(bidderIdObj.toString());
        }
        if (bidderIdObj!=null){
            autionInfoId=Integer.parseInt(autionInfoIdObj.toString());
        }
        AuctionInfoBidderLoss loss=new AuctionInfoBidderLoss();
        loss.setBidderid(bidderId);
        loss.setAuctioninfoid(autionInfoId);
        auctionInfoBidderLossDao.updateLossAuction(loss);
        Map<String,Object> m=new HashMap<>();
        m.put("code", 0);
        m.put("msg", 1);
        return m;
    }
}
