package com.example.auction.Model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class car {
    private Integer id;
    @Excel(name = "车型")
    private String carname;
    @Excel(name = "车辆颜色")
    private String carcolor;
    @Excel(name = "车牌号")
    private String carplatenumber;
    @Excel(name = "车主姓名")
    private String carhostid;
    @Excel(name = "车架号")
    private String carframenumber;
    @Excel(name = "发动机号")
    private String motornumber;
    @Excel(name = "品牌")
    private String brandcarseries;
    @Excel(name = "钥匙数量")
    private String keynumber;
    @Excel(name = "行驶里程")
    private String actualservicelife;
    @Excel(name = "上牌日期",importFormat="yyyy/MM/dd")
    private Date onbrandtimedate;
    //数据库保存字段
    private String onbrandtime;
    @Excel(name = "行驶证")
    private String drivinglicense;
    @Excel(name = "评估价")
    private String assessmentprice;
    @Excel(name = "是否事故车")
    private String accidentvehicle;
//    @Excel(name = "底价")
    private String floor;
    private String assessmentpricetwo;
    @Excel(name = "车外图片上传")
    private String carimg;
    private String other;
    @Excel(name = "车库名称")
    private String carport;
    private String cararea;
    private String caraddress;
    @Excel(name = "车内部图片上传")
    private String carbosomimg;
    @Excel(name = "车发动机图片上传")
    private String carmotorimg;
    private String carnumber;
    private String dealtime;
    private String inspectionhttp;
    @Excel(name = "车系")
    private String vehiclesys;
    private String jurisdiction;
    private String carthumbnail;
    //0：上线 1：储备
    @Excel(name = "状态", replace = {"上线_0", "储备_1"})
    private String carstate;
    //批号
    @Excel(name = "批号")
    private String batchnumber;
    //报告连接地址
    @Excel(name = "报告地址")
    private String reporturl;
    //保险截止日期
    @Excel(name = "保险截止",importFormat="yyyy/MM/dd")
    private Date insuranceexpiredate;
    //数据库保存字段
    private String insuranceexpire;
    //年险截止日期
    @Excel(name = "年险截止",importFormat="yyyy/MM/dd")
    private Date yearexpiredate;
    //数据库保存字段
    private String yearexpire;
    //查博士项目根据订单编号获取车辆外部图片
    @Excel(name = "订单编号")
    private String orderNo;
    public car() {
        super();
    }

    public car(Integer id, String carname, String carcolor, String carplatenumber, String carhostid, String carframenumber, String motornumber, String brandcarseries, String keynumber, String actualservicelife, String onbrandtime, String drivinglicense, String assessmentprice, String accidentvehicle, String floor, String assessmentpricetwo, String carimg, String other, String carport, String cararea, String caraddress, String carbosomimg, String carmotorimg, String carnumber, String dealtime, String inspectionhttp, String vehiclesys, String jurisdiction, String carthumbnail, String carstate, String batchnumber, String reporturl,
               String insuranceexpire,String yearexpire
    ) {
        this.id = id;
        this.carname = carname;
        this.carcolor = carcolor;
        this.carplatenumber = carplatenumber;
        this.carhostid = carhostid;
        this.carframenumber = carframenumber;
        this.motornumber = motornumber;
        this.brandcarseries = brandcarseries;
        this.keynumber = keynumber;
        this.actualservicelife = actualservicelife;
        this.onbrandtime = onbrandtime;
        this.drivinglicense = drivinglicense;
        this.assessmentprice = assessmentprice;
        this.accidentvehicle = accidentvehicle;
        this.floor = floor;
        this.assessmentpricetwo = assessmentpricetwo;
        this.carimg = carimg;
        this.other = other;
        this.carport = carport;
        this.cararea = cararea;
        this.caraddress = caraddress;
        this.carbosomimg = carbosomimg;
        this.carmotorimg = carmotorimg;
        this.carnumber = carnumber;
        this.dealtime = dealtime;
        this.inspectionhttp = inspectionhttp;
        this.vehiclesys = vehiclesys;
        this.jurisdiction = jurisdiction;
        this.carthumbnail = carthumbnail;
        this.carstate = carstate;
        this.batchnumber = batchnumber;
        this.reporturl = reporturl;
        this.insuranceexpire = insuranceexpire;
        this.yearexpire = yearexpire;
    }

    public String getCarthumbnail() {
        return carthumbnail;
    }

    public void setCarthumbnail(String carthumbnail) {
        this.carthumbnail = carthumbnail;
    }

    public String getJurisdiction() {
        return jurisdiction;
    }

    public void setJurisdiction(String jurisdiction) {
        this.jurisdiction = jurisdiction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCarname() {
        return carname;
    }

    public void setCarname(String carname) {
        this.carname = carname;
    }

    public String getCarcolor() {
        return carcolor;
    }

    public void setCarcolor(String carcolor) {
        this.carcolor = carcolor;
    }

    public String getCarplatenumber() {
        return carplatenumber;
    }

    public void setCarplatenumber(String carplatenumber) {
        this.carplatenumber = carplatenumber;
    }

    public String getCarhostid() {
        return carhostid;
    }

    public void setCarhostid(String carhostid) {
        this.carhostid = carhostid;
    }

    public String getCarframenumber() {
        return carframenumber;
    }

    public void setCarframenumber(String carframenumber) {
        this.carframenumber = carframenumber;
    }

    public String getMotornumber() {
        return motornumber;
    }

    public void setMotornumber(String motornumber) {
        this.motornumber = motornumber;
    }

    public String getBrandcarseries() {
        return brandcarseries;
    }

    public void setBrandcarseries(String brandcarseries) {
        this.brandcarseries = brandcarseries;
    }

    public String getKeynumber() {
        return keynumber;
    }

    public void setKeynumber(String keynumber) {
        this.keynumber = keynumber;
    }

    public String getActualservicelife() {
        return actualservicelife;
    }

    public void setActualservicelife(String actualservicelife) {
        this.actualservicelife = actualservicelife;
    }

    public String getOnbrandtime() {
        return onbrandtime;
    }

    public void setOnbrandtime(String onbrandtime) {
        this.onbrandtime = onbrandtime;
    }

    public String getDrivinglicense() {
        return drivinglicense;
    }

    public void setDrivinglicense(String drivinglicense) {
        this.drivinglicense = drivinglicense;
    }

    public String getAssessmentprice() {
        return assessmentprice;
    }

    public void setAssessmentprice(String assessmentprice) {
        this.assessmentprice = assessmentprice;
    }

    public String getAccidentvehicle() {
        return accidentvehicle;
    }

    public void setAccidentvehicle(String accidentvehicle) {
        this.accidentvehicle = accidentvehicle;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getAssessmentpricetwo() {
        return assessmentpricetwo;
    }

    public void setAssessmentpricetwo(String assessmentpricetwo) {
        this.assessmentpricetwo = assessmentpricetwo;
    }

    public String getCarimg() {
        return carimg;
    }

    public void setCarimg(String carimg) {
        this.carimg = carimg;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getCarport() {
        return carport;
    }

    public void setCarport(String carport) {
        this.carport = carport;
    }

    public String getCararea() {
        return cararea;
    }

    public void setCararea(String cararea) {
        this.cararea = cararea;
    }

    public String getCaraddress() {
        return caraddress;
    }

    public void setCaraddress(String caraddress) {
        this.caraddress = caraddress;
    }

    public String getCarbosomimg() {
        return carbosomimg;
    }

    public void setCarbosomimg(String carbosomimg) {
        this.carbosomimg = carbosomimg;
    }

    public String getCarmotorimg() {
        return carmotorimg;
    }

    public void setCarmotorimg(String carmotorimg) {
        this.carmotorimg = carmotorimg;
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber;
    }

    public String getDealtime() {
        return dealtime;
    }

    public void setDealtime(String dealtime) {
        this.dealtime = dealtime;
    }

    public String getInspectionhttp() {
        return inspectionhttp;
    }

    public void setInspectionhttp(String inspectionhttp) {
        this.inspectionhttp = inspectionhttp;
    }

    public String getVehiclesys() {
        return vehiclesys;
    }

    public void setVehiclesys(String vehiclesys) {
        this.vehiclesys = vehiclesys;
    }

    public String getCarstate() {
        return carstate;
    }

    public void setCarstate(String carstate) {
        this.carstate = carstate;
    }

    public String getBatchnumber() {
        return batchnumber;
    }

    public void setBatchnumber(String batchnumber) {
        this.batchnumber = batchnumber;
    }

    public String getReporturl() {
        return reporturl;
    }

    public void setReporturl(String reporturl) {
        this.reporturl = reporturl;
    }

    public void setInsuranceexpire(String insuranceexpire) {
        this.insuranceexpire = insuranceexpire;
    }

    public void setYearexpire(String yearexpire) {
        this.yearexpire = yearexpire;
    }

    public String getInsuranceexpire() {
        return insuranceexpire;
    }

    public String getYearexpire() {
        return yearexpire;
    }

    public Date getInsuranceexpiredate() {
        return insuranceexpiredate;
    }

    public Date getYearexpiredate() {
        return yearexpiredate;
    }

    public void setInsuranceexpiredate(Date insuranceexpiredate) {
        this.insuranceexpiredate = insuranceexpiredate;
    }

    public void setYearexpiredate(Date yearexpiredate) {
        this.yearexpiredate = yearexpiredate;
    }

    public Date getOnbrandtimedate() {
        return onbrandtimedate;
    }

    public void setOnbrandtimedate(Date onbrandtimedate) {
        this.onbrandtimedate = onbrandtimedate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "car{" +
                "id=" + id +
                ", carname='" + carname + '\'' +
                ", carcolor='" + carcolor + '\'' +
                ", carplatenumber='" + carplatenumber + '\'' +
                ", carhostid='" + carhostid + '\'' +
                ", carframenumber='" + carframenumber + '\'' +
                ", motornumber='" + motornumber + '\'' +
                ", brandcarseries='" + brandcarseries + '\'' +
                ", keynumber='" + keynumber + '\'' +
                ", actualservicelife='" + actualservicelife + '\'' +
                ", onbrandtime='" + onbrandtime + '\'' +
                ", drivinglicense='" + drivinglicense + '\'' +
                ", assessmentprice='" + assessmentprice + '\'' +
                ", accidentvehicle='" + accidentvehicle + '\'' +
                ", floor='" + floor + '\'' +
                ", assessmentpricetwo='" + assessmentpricetwo + '\'' +
                ", carimg='" + carimg + '\'' +
                ", other='" + other + '\'' +
                ", carport='" + carport + '\'' +
                ", cararea='" + cararea + '\'' +
                ", caraddress='" + caraddress + '\'' +
                ", carbosomimg='" + carbosomimg + '\'' +
                ", carmotorimg='" + carmotorimg + '\'' +
                ", carnumber='" + carnumber + '\'' +
                ", dealtime='" + dealtime + '\'' +
                ", inspectionhttp='" + inspectionhttp + '\'' +
                ", vehiclesys='" + vehiclesys + '\'' +
                ", jurisdiction='" + jurisdiction + '\'' +
                ", carthumbnail='" + carthumbnail + '\'' +
                ", carstate='" + carstate + '\'' +
                ", batchnumber='" + batchnumber + '\'' +
                ", reporturl='" + reporturl + '\'' +
                ", insuranceexpire='" + insuranceexpire + '\'' +
                ", yearexpire='" + yearexpire + '\'' +
                '}';
    }
}