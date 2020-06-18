package com.example.auction.Model;

public class car {
    private Integer id;

    private String carname;

    private String carcolor;

    private String carplatenumber;

    private String carhostid;

    private String carframenumber;

    private String motornumber;

    private String brandcarseries;

    private Integer keynumber;

    private Integer actualservicelife;

    private String onbrandtime;

    private String drivinglicense;

    private String assessmentprice;

    private String accidentvehicle;

    private String floor;

    private String assessmentpricetwo;

    private String carimg;

    private String other;

    private String carport;

    private String cararea;

    private String caraddress;

    private String carbosomimg;

    private String carmotorimg;

    private String carnumber;

    private String dealtime;

    public car(Integer id, String carname, String carcolor, String carplatenumber, String carhostid, String carframenumber, String motornumber, String brandcarseries, Integer keynumber, Integer actualservicelife, String onbrandtime, String drivinglicense, String assessmentprice, String accidentvehicle, String floor, String assessmentpricetwo, String carimg, String other, String carport, String cararea, String caraddress, String carbosomimg, String carmotorimg, String carnumber, String dealtime) {
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
    }

    public car() {
        super();
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
        this.carname = carname == null ? null : carname.trim();
    }

    public String getCarcolor() {
        return carcolor;
    }

    public void setCarcolor(String carcolor) {
        this.carcolor = carcolor == null ? null : carcolor.trim();
    }

    public String getCarplatenumber() {
        return carplatenumber;
    }

    public void setCarplatenumber(String carplatenumber) {
        this.carplatenumber = carplatenumber == null ? null : carplatenumber.trim();
    }

    public String getCarhostid() {
        return carhostid;
    }

    public void setCarhostid(String carhostid) {
        this.carhostid = carhostid == null ? null : carhostid.trim();
    }

    public String getCarframenumber() {
        return carframenumber;
    }

    public void setCarframenumber(String carframenumber) {
        this.carframenumber = carframenumber == null ? null : carframenumber.trim();
    }

    public String getMotornumber() {
        return motornumber;
    }

    public void setMotornumber(String motornumber) {
        this.motornumber = motornumber == null ? null : motornumber.trim();
    }

    public String getBrandcarseries() {
        return brandcarseries;
    }

    public void setBrandcarseries(String brandcarseries) {
        this.brandcarseries = brandcarseries == null ? null : brandcarseries.trim();
    }

    public Integer getKeynumber() {
        return keynumber;
    }

    public void setKeynumber(Integer keynumber) {
        this.keynumber = keynumber;
    }

    public Integer getActualservicelife() {
        return actualservicelife;
    }

    public void setActualservicelife(Integer actualservicelife) {
        this.actualservicelife = actualservicelife;
    }

    public String getOnbrandtime() {
        return onbrandtime;
    }

    public void setOnbrandtime(String onbrandtime) {
        this.onbrandtime = onbrandtime == null ? null : onbrandtime.trim();
    }

    public String getDrivinglicense() {
        return drivinglicense;
    }

    public void setDrivinglicense(String drivinglicense) {
        this.drivinglicense = drivinglicense == null ? null : drivinglicense.trim();
    }

    public String getAssessmentprice() {
        return assessmentprice;
    }

    public void setAssessmentprice(String assessmentprice) {
        this.assessmentprice = assessmentprice == null ? null : assessmentprice.trim();
    }

    public String getAccidentvehicle() {
        return accidentvehicle;
    }

    public void setAccidentvehicle(String accidentvehicle) {
        this.accidentvehicle = accidentvehicle == null ? null : accidentvehicle.trim();
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor == null ? null : floor.trim();
    }

    public String getAssessmentpricetwo() {
        return assessmentpricetwo;
    }

    public void setAssessmentpricetwo(String assessmentpricetwo) {
        this.assessmentpricetwo = assessmentpricetwo == null ? null : assessmentpricetwo.trim();
    }

    public String getCarimg() {
        return carimg;
    }

    public void setCarimg(String carimg) {
        this.carimg = carimg == null ? null : carimg.trim();
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }

    public String getCarport() {
        return carport;
    }

    public void setCarport(String carport) {
        this.carport = carport == null ? null : carport.trim();
    }

    public String getCararea() {
        return cararea;
    }

    public void setCararea(String cararea) {
        this.cararea = cararea == null ? null : cararea.trim();
    }

    public String getCaraddress() {
        return caraddress;
    }

    public void setCaraddress(String caraddress) {
        this.caraddress = caraddress == null ? null : caraddress.trim();
    }

    public String getCarbosomimg() {
        return carbosomimg;
    }

    public void setCarbosomimg(String carbosomimg) {
        this.carbosomimg = carbosomimg == null ? null : carbosomimg.trim();
    }

    public String getCarmotorimg() {
        return carmotorimg;
    }

    public void setCarmotorimg(String carmotorimg) {
        this.carmotorimg = carmotorimg == null ? null : carmotorimg.trim();
    }

    public String getCarnumber() {
        return carnumber;
    }

    public void setCarnumber(String carnumber) {
        this.carnumber = carnumber == null ? null : carnumber.trim();
    }

    public String getDealtime() {
        return dealtime;
    }

    public void setDealtime(String dealtime) {
        this.dealtime = dealtime == null ? null : dealtime.trim();
    }
}