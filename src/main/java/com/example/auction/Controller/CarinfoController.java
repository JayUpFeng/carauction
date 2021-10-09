package com.example.auction.Controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.auction.Configuration.TemplateUtils;
import com.example.auction.Model.*;
import com.example.auction.Service.PublicUserService;
import com.example.auction.Service.UserService;
import com.example.auction.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Api("CarinfoController相关的api")
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/car")
public class CarinfoController {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private UserService userService;
    @Autowired
    private PublicUserService publicUserService;
    @Autowired
    private com.example.auction.Dao.carDao carDao;
    @Value("${cbsUrl}")
    private String cbsUrl;
    @Value("${cbsImgUrl}")
    private String cbsImgUrl;
    //添加用户
    @RequestMapping(value = "/addUser")
    @ResponseBody
    public Map addUser(@RequestBody Userinfo userinfo) {
        Map map = new HashMap();
        //企业微信登录
        if (!StringUtils.isEmpty(userinfo.getOther())) {
            map = userService.addUser(userinfo);
        }
        //公众号登录
        if (!StringUtils.isEmpty(userinfo.getCode())) {
            map = userService.addPublicUser(userinfo.getCode());
        }
        return map;
    }

    //添加二手车
    @RequestMapping(value = "/addCar")
    @ResponseBody
    public Map addCar(@RequestBody car c) {
        Map map = new HashMap();

        map = userService.addCar(c);

        return map;
    }

    //查询车辆
    @RequestMapping(value = "/carlist")
    @ResponseBody
    public Map carlist(@RequestBody Map map) {
        map = userService.carlist(map);

        return map;
    }

    //修改车辆信息
    @RequestMapping(value = "/updatacar")
    @ResponseBody
    public Map updatacar(@RequestBody car c) {
        Map map = new HashMap();

        map = userService.updatacar(c);

        return map;
    }

    //删除车辆
    @RequestMapping(value = "/delcar")
    @ResponseBody
    public Map delcar(@RequestBody car c) {
        Map map = new HashMap();

        map = userService.delcar(c);

        return map;
    }

    //查询竞拍者列表(可根据手机号和竞拍编号查询)
    @RequestMapping(value = "/listBidder")
    @ResponseBody
    public Map listUser(@RequestBody Map map) {
        Map m = userService.listUser(map);
        return m;
    }


    //查询用户
    @RequestMapping(value = "/listuser")
    @ResponseBody
    public Map listuser(@RequestBody Map map) {
        Map m = userService.listuser(map);
        return m;
    }

    //添加竞拍
    @RequestMapping(value = "/addauctioninfo")
    @ResponseBody
    public Map addauctioninfo(@RequestBody auctioninfo auctioninfo) {
        Map m = userService.addauctioninfo(auctioninfo);
        return m;
    }

    //查询竞拍列表
    @RequestMapping(value = "/listauctioninfo")
    @ResponseBody
    public Map listauctioninfo(@RequestBody Map map) throws ParseException {
        Map m = userService.listauctioninfo(map);
        return m;
    }


    //竞拍列表查询(分页)
    @RequestMapping(value = "/listauctioninfopaging")
    @ResponseBody
    public Map listauctioninfopaging(@RequestBody Map map) {
        long s = System.currentTimeMillis();
        Map m = userService.listauctioninfopaging(map);
        long e = System.currentTimeMillis();
        System.out.println("成交、违规、流拍列表时间====" + (e - s));
        return m;
    }

    //中标修改,确定中标人
    @RequestMapping(value = "/updateauctioninfo")
    @ResponseBody
    public Map updateauctioninfo(@RequestBody Map map) {
        Map m = userService.updateauctioninfo(map);
        return m;
    }

    //添加保证金列表
    @RequestMapping(value = "/addbond")
    @ResponseBody
    public Map addbond(@RequestBody biddercompany biddercompany) {
        Map m = userService.addbond(biddercompany);
        return m;
    }

    //上传保证金凭证
    @RequestMapping(value = "/updatebond")
    @ResponseBody
    public Map updatebond(@RequestBody Map map) {
        Map m = userService.updatebond(map);
        return m;
    }

    //申请退还保证金
    @RequestMapping(value = "/applyrefundbond")
    @ResponseBody
    public Map applyrefundbond(@RequestParam("id") Integer id, @RequestParam("userid") Integer userid) {
        Map m = userService.applyrefundbond(id, userid);
        return m;
    }

    //查询保证金列表
    @RequestMapping(value = "/bondList")
    @ResponseBody
    public Map bondList(@RequestBody Map map) {
        Map m = userService.bondList(map);
        return m;
    }

    //修改保证金的状态
    @RequestMapping(value = "/updateList")
    @ResponseBody
    public Map updateList(@RequestBody biddercompany biddercompany) {
        Map m = userService.updateList(biddercompany);
        return m;
    }

    //查看竞拍车辆详情
    @RequestMapping(value = "/auctioncarList")
    @ResponseBody
    public Map auctioncarList(@RequestBody Map map) {
        Map m = userService.auctioncarList(map);
        return m;
    }

    //添加竞拍人竞拍
    @RequestMapping(value = "/addbidder")
    @ResponseBody

    public Map addbidder(@RequestBody bidder bidder) {
        Map m = userService.addbidder(bidder);
        return m;
    }

    //参数
    // bidderid: 2404
    //carid: 945
    //id: 580
    //offer: "11133"
    //竞拍人报价
    @RequestMapping(value = "/bidderoffer")
    @ResponseBody
    public Map bidderoffer(@RequestBody offerinfo offerinfo) {
        Map m = userService.bidderoffer(offerinfo);
        return m;
    }

    //参数
    // id: 2404
    //totalpay: 555
    //竞拍人投标
    //合并到竞拍人报价里面
//    @RequestMapping(value = "/updatebidder")
//    @ResponseBody
//    public Map updatebidder(@RequestBody Map map) {
//        Map m =  userService.updatebidder(map);
//        return m;
//    }

    //修改竞拍人状态
    //第一次生成凭证的接口，成交操作接口
    @RequestMapping(value = "/updatebidderstate")
    @ResponseBody
    public Map updatebidderstate(@RequestBody Map map) {
        Map m = userService.updatebidderstate(map);
        return m;
    }

    //添加预约看车人
    @RequestMapping(value = "/addappointmentseecar")
    @ResponseBody
    public Map addappointmentseecar(@RequestBody bidder bidder) {
        Map m = userService.addappointmentseecar(bidder);
        return m;
    }

    //添加竞拍者的打款凭证
    @RequestMapping(value = "/addpayvoucher")
    @ResponseBody
    public Map addpayvoucher(@RequestBody Map map) {
        Map m = userService.addpayvoucher(map);
        return m;
    }

    //上传图片
    @RequestMapping(value = "/uploadImg")
    @ResponseBody
    public Map uploadImg(@RequestParam("file") MultipartFile file) {
        Map m = userService.uploadImg(file);
        return m;
    }

    //压缩图
    @RequestMapping(value = "/uploadImg2")
    @ResponseBody
    public Map uploadImg2(@RequestParam("file") MultipartFile file) {
        Map m = userService.uploadImg2(file);
        return m;
    }


    //图片解析
    @RequestMapping(value = "/showImg")
    @ResponseBody
    public Map showImg(String imgUrl, HttpServletResponse response) {
        Map m = new HashMap();
        try {
            userService.IoReadImage(imgUrl, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m;
    }

    //图片解析
    @RequestMapping(value = "/showImg2")
    @ResponseBody
    public Map showImg2(byte[] img, String path) {
        Map m = new HashMap();

        userService.IoReadImage2(img, path);

        return m;
    }

    //中标,未中标通知
    @RequestMapping(value = "/sendWinningbid")
    @ResponseBody
    public Map sendWinningbid(@RequestBody Map map) {
        Map m = userService.sendWinningbid(map);
        return m;
    }


    //保证金签署成功通知
    @RequestMapping(value = "/successfulcontract")
    @ResponseBody
    public Map successfulcontract(@RequestBody Map map) {
        Map m = userService.successfulcontract(map);
        return m;
    }

    //竞拍截止(添加当天提醒)通知
    @RequestMapping(value = "/auctionstart")
    @ResponseBody
    public Map auctionstart(@RequestBody Map map) {
        Map m = userService.auctionstart(map);
        return m;
    }

    //截标前一天发送通知
    @RequestMapping(value = "/auctionend")
    @ResponseBody
    public Map auctionend(@RequestBody Map map) {
        Map m = userService.auctionend(map);
        return m;
    }

    //截标当天发送通知
    @RequestMapping(value = "/auctionend2")
    @ResponseBody
    public Map auctionend2(@RequestBody Map map) {
        Map m = userService.auctionend2(map);
        return m;
    }

    //竞拍结束通知
    @RequestMapping(value = "/auctionend3")
    @ResponseBody
    public Map auctionend3(@RequestBody Map map) {
        Map m = userService.auctionend3(map);
        return m;
    }

    //竞拍失败和中标通知(企业微信)
    @RequestMapping(value = "/corpsendWinningbid")
    @ResponseBody
    public Map corpsendWinningbid(@RequestBody Map map) {
        Map m = userService.corpsendWinningbid(map);
        return m;
    }

    //是否预约看车
    @RequestMapping(value = "/issesscar")
    @ResponseBody
    public Map issesscar(@RequestBody auctioninfo auctioninfo) {
        Map m = userService.issesscar(auctioninfo);
        return m;
    }


    //竞拍通知(添加竞拍时通知,可以选择通知人,若没有则是全部)（企业微信）
    @RequestMapping(value = "/corpauctionstart")
    @ResponseBody
    public Map corpauctionstart(@RequestBody Map map) {
        System.out.println("map=====111" + map);
        Map m = userService.corpauctionstart(map);
        return m;
    }

    //截标前一天晚上6发送通知(企业微信)
    @RequestMapping(value = "/corpauctionend")
    @ResponseBody
    public Map corpauctionend() {
        Map m = userService.corpauctionend22();
        return m;
    }

    //截标当天6点发送通知(企业微信)
    @RequestMapping(value = "/corpauctionend2")
    @ResponseBody
    public Map corpauctionend2() {
        Map m = userService.corpauctionend21();
        return m;
    }

    //竞拍结束通知(企业微信)
    @RequestMapping(value = "/corpauctionstop")
    @ResponseBody
    public Map corpauctionstop() {
        Map m = userService.corpauctionstop();
        return m;
    }

    //参数：
//aucid: "1862"
//userid: "89"
    //查询竞拍和竞拍人数据,如果是bidder是空即添加
    @RequestMapping(value = "/selectauction")
    @ResponseBody
    public Map selectauction(@RequestBody Map map) {
        Map m = userService.selectauction(map);
        return m;
    }


    @RequestMapping(value = "/selectbidderByid")
    @ResponseBody
    public Map selectbidderByid(@RequestBody Map map) {
        Map m = userService.selectbidderByid(map);
        return m;
    }

    @RequestMapping(value = "/selectUserByid")
    @ResponseBody
    public Map selectUserByid(@RequestBody Map map) {
        try {
            Map m = userService.selectUserByid(map);
            return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap();
    }

    //查询全部用户和对应保证金公司
    @RequestMapping(value = "/selectAllUser")
    @ResponseBody
    public Map selectAllUser(@RequestBody Map map) {
        Map m = userService.selectAllUser(map);
        return m;
    }

    //后台管理列表查询所有用户
    @RequestMapping(value = "/selectAllUserInfo")
    @ResponseBody
    public Map selectAllUserInfo(@RequestBody Map map) {
        Map m = userService.selectAllUserInfo(map);
        return m;
    }

    //修改流拍车辆
    @RequestMapping(value = "/upateauctioncar")
    @ResponseBody
    public Map upateauctioncar(@RequestBody Map map) {
        Map m = userService.upateauctioncar(map);
        return m;
    }

    //删除保证金
    @RequestMapping(value = "/delbond")
    @ResponseBody
    public Map delbond(@RequestBody Map map) {
        Map m = userService.delbond(map);
        return m;
    }

    //管理端修改用户保证金信息
    @RequestMapping(value = "/updatebondgl")
    @ResponseBody
    public Map updatebondgl(@RequestBody biddercompany biddercompany) {
        Map m = userService.updatebondgl(biddercompany);
        return m;
    }


    //修改竞拍列表
    @RequestMapping(value = "/updateauction")
    @ResponseBody
    public Map updateauction(@RequestBody auctioninfo auctioninfo) {
        Map m = userService.updateauction(auctioninfo);
        return m;
    }

    //流拍通知
    @RequestMapping(value = "/upateauctioncarnotice")
    @ResponseBody
    public Map upateauctioncarnotice(@RequestBody Map Map) {
        Map m = userService.upateauctioncarnotice(Map);
        return m;
    }

    //删除一个月前的所有竞拍
    @RequestMapping(value = "/delauctioninfo")
    @ResponseBody
    public Map delauctioninfo() {
        Map m = userService.delauctioninfo();
        return m;
    }

    //删除竞拍人
    @RequestMapping(value = "/delbidder")
    @ResponseBody
    public Map delbidder(@RequestBody Map map) {
        Map m = userService.delbidder(map);
        return m;
    }

    //修改查看竞拍人
    @RequestMapping(value = "/updateaucuser")
    @ResponseBody
    public Map updateaucuser(@RequestBody Map map) {
        Map m = userService.updateaucuser(map);
        return m;
    }
    //
    /*@RequestMapping(value = "/sendWinningbid")
    @ResponseBody
    public Map sendWinningbid(@RequestBody Map map) {
        Map m =  userService.sendWinningbid(map);
        return m;
    }*/

    /**
     * app端中标/成交/失败列表
     *
     * @param map
     * @return
     */
    @RequestMapping("/commonAuction")
    @ResponseBody
    public Map commonAuction(@RequestBody Map map) {
        return userService.commonAuction(map);
    }

    @Value("${car.excel}")
    private String carExcel;

    @RequestMapping("/uploadCarExcel")
    @ResponseBody
    public Map uploadCarExcel(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest request) {
        String name = multipartFile.getOriginalFilename();
        request.getSession().setAttribute("upFile", name);
        String fileSavePath = carExcel + UUID.randomUUID().toString().replaceAll("-", "");
        try {
            multipartFile.transferTo(new File(fileSavePath + name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        String jurisdiction =request.getParameter("jurisdiction");
        File file = new File(fileSavePath + name);
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failed = new AtomicInteger(0);
        StringBuilder builder = new StringBuilder();
        Map<String, Object> m = new HashMap<>();
        m.put("msg", 1);
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(0);
            params.setHeadRows(1);
            List<car> result = ExcelImportUtil.importExcel(file, car.class, params);
            if (result != null && !result.isEmpty()) {
                //记录excel中重复的车架号，如果有则失败条数+1，如果没有则保存入库
                Map<String, Integer> frameMap = new HashMap<>();
                for (int i = 0; i < result.size(); i++) {
                    car car = result.get(i);
                    String state = car.getCarstate();
                    String carframenumber = car.getCarframenumber();
                    if (state != null && !"null".equals(state)) {
                        if (frameMap.containsKey(carframenumber)) {
                            //失败条数增1
                            failed.incrementAndGet();
                            builder.append(carframenumber).append(",");
                        } else {
                            //设置userid
                            if (!StringUtils.isEmpty(jurisdiction)){
                                car.setJurisdiction(jurisdiction);
                            }
                            String carstate = car.getCarstate();
                            if ("0".equals(carstate)) {
                                frameMap.put(carframenumber, 0);
                                int count = carDao.getByIdAndCarFrameNumberCount(carframenumber);
                                //1、记录导入成功数量和失败数量
                                //2、车架号和数据库一样，不导入，excel中车架号重复，不导入
                                //数据库中有记录，不导入
                                if (count > 0) {
                                    //失败条数增1
                                    failed.incrementAndGet();
                                    builder.append(carframenumber).append(",");
                                }else{
                                    saveCarData(car,success,failed,builder,carframenumber);
                                }
                            }else{
                                saveCarData(car,success,failed,builder,carframenumber);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            m.put("msg", "后台服务器异常，请联系管理员！");
        }
        String msg = builder.toString();
        Integer count = success.get();
        if (!StringUtils.isEmpty(msg)) {
            if (count > 0) {
                m.put("msg", count + "条导入成功！ " + "车架号：" + msg + "导入失败！");
            } else {
                m.put("msg", "车架号：" + msg + "导入失败！");
            }
        }
        m.put("code", 0);
        m.put("success", count);
        m.put("failed", failed.get());
        return m;
    }
    public void saveCarData(car car, AtomicInteger success, AtomicInteger failed, StringBuilder builder, String carframenumber){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date insuranceexpiredate = car.getInsuranceexpiredate();
        Date yearexpiredate = car.getYearexpiredate();
        Date onbrandtimedate = car.getOnbrandtimedate();
        if (insuranceexpiredate != null) {
            String insuranceexpire = sdf.format(insuranceexpiredate);
            car.setInsuranceexpire(insuranceexpire);
        }
        if (yearexpiredate != null) {
            String yearexpire = sdf.format(yearexpiredate);
            car.setYearexpire(yearexpire);
        }
        if (onbrandtimedate != null) {
            String onbrandtime = sdf.format(onbrandtimedate);
            car.setOnbrandtime(onbrandtime);
        }
        //保存图片
        String carimg = car.getCarimg();
        String orderNo = car.getOrderNo();
        //车外部图片，从查博士获取，根据orderid
        if (StringUtils.isEmpty(carimg)&&!StringUtils.isEmpty(orderNo)){
            Map<String,Object> params=new HashMap<>();
            params.put("orderNo",orderNo);
            String result = HttpUtil.doPost(cbsUrl,params);
            Map<String,Object> map = JSONObject.parseObject(result, Map.class);
            if (map!=null){
                Object obj = map.get("data");
                if (obj!=null){
                    Records records = JSONObject.parseObject(obj.toString(), Records.class);
                    if (records!=null){
                        DetectionData detectionData = records.getDetectionData();
                        if (detectionData!=null){
                            List<Certificate> certificate = detectionData.getCertificate();
                            if (certificate!=null&&!certificate.isEmpty()){
                                for(Certificate ce:certificate){
                                    if (ce.getName().contains("左前45")){
                                        carimg =cbsImgUrl+ ce.getNetImg();
                                    }
                                }
                                if (StringUtils.isEmpty(carimg)){
                                    carimg=cbsImgUrl+ certificate.get(0).getNetImg();
                                }
                            }
                        }

                    }
                }

            }
        }
        String carimgAddr = writePic(carimg);
        String carbosomimg = car.getCarbosomimg();
        String carbosomimgAddr = writePic(carbosomimg);
        String carmotorimg = car.getCarmotorimg();
        String carmotorimgAddr = writePic(carmotorimg);
        car.setCarimg(carimgAddr);
        car.setCarbosomimg(carbosomimgAddr);
        car.setCarmotorimg(carmotorimgAddr);
        //2是没有标书关联。1是有关联
        car.setOther("2");
        int saveCount = carDao.insertSelective(car);
        if (saveCount > 0) {
            success.incrementAndGet();
        } else {
            failed.incrementAndGet();
            builder.append(carframenumber).append(",");
        }
    }

    public String writePic(String pathName) {
        if (StringUtils.isEmpty(pathName)) {
            return "";
        }
        String type = ".png";
        String picName = UUID.randomUUID().toString().replaceAll("-", "");
        String targetPath = carExcel + picName + type;
        try {
            URL url = new URL(pathName);
            //打开链接
            InputStream inStream = url.openStream();
            //得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inStream);
            //new一个文件对象用来保存图片，默认保存当前工程根目录
            File imageFile = new File(targetPath);
            //创建输出流
            FileOutputStream outStream = new FileOutputStream(imageFile);
            //写入数据
            outStream.write(data);
            //关闭输出流
            outStream.close();
            return targetPath.substring(11);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while ((len = inStream.read(buffer)) != -1) {
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 流拍操作
     *
     * @param map
     * @return
     */
    @RequestMapping("/lossAuction")
    @ResponseBody
    public Map<String, Object> lossAuction(@RequestBody Map map) {
        return userService.lossAuction(map);
    }

    /**
     * 修改流拍最高出价人
     *
     * @param map
     * @return
     */
    @PostMapping("/updateLossAuction")
    @ResponseBody
    public Map<String, Object> updateLossAuction(@RequestBody Map map) {
        return userService.updateLossAuction(map);
    }

    //成交、违规列表查询车辆
    @ApiOperation(value = "成交、违规列表查询车辆")
    @PostMapping(value = "/carInfoList")
    @ResponseBody
    public Map<String, Object> carInfoList(@RequestBody Map map) {
        map = userService.carInfoList(map);
        return map;
    }

    @ApiOperation(value = "流拍列表查询车辆")
    //流拍列表查询车辆
    @PostMapping(value = "/carCopyList")
    @ResponseBody
    public Map<String, Object> carCopyList(@RequestBody Map map) {
        map = userService.carCopyList(map);
        return map;
    }
}

