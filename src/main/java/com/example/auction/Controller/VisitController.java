package com.example.auction.Controller;

import com.example.auction.Configuration.Utils;
import com.example.auction.Dao.*;
import com.example.auction.Model.*;
import com.example.auction.Service.VisitService;
import com.example.auction.util.ImgUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @作者 : zhangHe
 * @时间 : 2020/08/23 12:34
 * @描述 :
 */
@RestController
@RequestMapping("/car/visit")
public class VisitController {
    private Logger logger = LoggerFactory.getLogger(VisitController.class);
    @Autowired
    private VisitService visitService;
    @Autowired
    private Userdao userdao;
    @Autowired
    private carDao carDao;
    @Autowired
    private AuctionInfoCarImgDao auctionInfoCarImgDao;
    @Autowired
    private com.example.auction.Dao.bidderDao bidderDao;
    @Autowired
    private com.example.auction.Dao.auctioninfoDao auctioninfoDao;
    @Autowired
    private AuctionInfoCarDao auctionInfoCarDao;
    @PostMapping("/list")
    public Map list(@RequestBody Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        try {
            List<Visit> list = visitService.list(map);
            if (list != null && list.size() > 0) {
                int totalcount = list.size();
                int pagecount = 0;
                List<Visit> subList;
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
                m.put("code", 0);
                m.put("total", totalcount);
                m.put("data", subList);
                m.put("pageNum", subList.size());
                m.put("pages", pagecount);
                m.put("msg", 1);
            } else {
                m.put("code", 0);
                m.put("msg", 0);
            }
        } catch (Exception e) {
            logger.error(Utils.getStackMsg(e));
        }
        return m;
    }

    @PostMapping("/getByUserId")
    public Map getByUserId(@RequestBody Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        try {
            List<Visit> list = null;
            Object userid = map.get("userid");
            if (userid != null) {
                Integer userId = Integer.parseInt(userid.toString());
                list = visitService.getByUserId(userId);
            }
            if (list != null && list.size() > 0) {
                int totalcount = list.size();
                int pagecount = 0;
                List<Visit> subList;
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
                m.put("code", 0);
                m.put("total", totalcount);
                m.put("data", subList);
                m.put("pageNum", subList.size());
                m.put("pages", pagecount);
                m.put("msg", 1);
            } else {
                m.put("code", 0);
                m.put("msg", 0);
            }
        } catch (Exception e) {
            logger.error(Utils.getStackMsg(e));
        }
        return m;
    }

    @PostMapping("/getByUserIdAuctionIdCarId")
    public Map getByUserIdAuctionIdCarId(@RequestBody Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        try {
            List<Visit> list = null;
            Object userid = map.get("userid");
            Integer userId = 0;
            Integer carId = 0;
            Integer auctionId = 0;
            if (userid != null) {
                userId = Integer.parseInt(userid.toString());
            }
            Object carid = map.get("carid");
            if (userid != null) {
                carId = Integer.parseInt(carid.toString());
            }
            Object auctionid = map.get("auctionid");
            if (userid != null) {
                auctionId = Integer.parseInt(auctionid.toString());
            }
            list = visitService.getByUserIdAuctionIdCarId(userId, auctionId, carId);
            if (list != null && list.size() > 0) {
                int totalcount = list.size();
                int pagecount = 0;
                List<Visit> subList;
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
                m.put("code", 0);
                m.put("total", totalcount);
                m.put("data", subList);
                m.put("pageNum", subList.size());
                m.put("pages", pagecount);
                m.put("msg", 1);
            } else {
                m.put("code", 0);
                m.put("msg", 0);
            }
        } catch (Exception e) {
            logger.error(Utils.getStackMsg(e));
        }
        return m;
    }

    @PostMapping("/updateState")
    public Map updateState(@RequestBody Map map) {
        Map m = new HashMap();
        m.put("msg", 0);
        try {
            Object userid = map.get("userid");
            Integer updateState = null;
            if (userid != null) {
                Integer userId = Integer.parseInt(userid.toString());
                updateState = visitService.updateState(userId);
            }
            if (updateState != null && updateState > 0) {
                m.put("code", 0);
                m.put("msg", 0);
            }
        } catch (Exception e) {
            logger.error(Utils.getStackMsg(e));
        }
        return m;
    }

    @GetMapping("updateUser")
    public String updateUser() {
        List<auctioninfo> auctioninfos = auctioninfoDao.selectAllAuc();
        if (auctioninfos.size()>0){
            for (auctioninfo auctioninfo:auctioninfos){
                Integer id = auctioninfo.getId();
                String carid = auctioninfo.getCarid();
                if (!StringUtils.isEmpty(carid)){
                    String[] split = carid.split(",");
                    for (int i = 0; i < split.length; i++) {
                        String carId = split[i];
                        Integer carIdInt=Integer.parseInt(carId);
                        AuctionInfoCar infoCar=new AuctionInfoCar();
                        infoCar.setCarid(carIdInt);
                        infoCar.setAuctioninfoid(id);
                        int dao = auctionInfoCarDao.getByInfoIdAndCarId(infoCar);
                        if (dao==0){
                            auctionInfoCarDao.save(infoCar);
//                            auctionInfoCarDao.delete(infoCar);
                        }
                    }
                }
            }
        }

        return "ok";
    }
    @Value("${pic.address}")
    private String a;
    @GetMapping("img")
    public String img() {

        //车辆图片-车型名称-车牌号-车架号-编号-成交日期-成交金额-成交车商编号6位数
        Integer carId= 86;
        car car = carDao.selectByPrimaryKey(carId);
        //车型名称
        String carname = car.getCarname();
        //车牌号
        String carplatenumber = car.getCarplatenumber();
        //车架号
        String carframenumber = car.getCarframenumber();
        //标书编号
        String auctionnumber = "";
        //成交日期
        String auctionendtime = "";
        //成交金额
        String transactionamount = "200000";
        String carimg = car.getCarimg();
        String imgSrc="";
        String imgTar=a+ UUID.randomUUID().toString().replaceAll("-","");
        if (!StringUtils.isEmpty(carimg)){
            imgSrc = carimg.split(",")[0];
        }else{
            //用默认的图片
            imgSrc =a+"377c3d3e5d1a45e09470f6c5461ed136.jpg";
        }
        Integer userid = 87;
        Userinfo userinfo = userdao.selectByPrimaryKey(userid);
        //成交车商编号6位数
        Integer usernumber = userinfo.getUsernumber();
        try {
            ImgUtil.drawImg(imgSrc,imgTar,carname,carplatenumber,carframenumber,auctionnumber,auctionendtime,transactionamount,usernumber+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ok";
    }
}
