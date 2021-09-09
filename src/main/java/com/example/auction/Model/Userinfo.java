package com.example.auction.Model;

public class Userinfo {

    private Integer id;
    private String username;
    private String tel;
    private String sex;
    private String certificates;
    private String address;
    //企业微信登录时的userid，或者公众号登录进来的tel
    private String other;
    private String participate;
    //公众号登录时的code
    private String code;
    //公众号openid
    private String openid;
    //用户身份：0：企业微信 1：公众号 2：企业微信、公众号
    private Integer state;
    //用户编号，即车商编号
    private Integer usernumber;


    public Userinfo() {
        super();
    }

    public Userinfo(Integer id, String username, String tel, String sex, String certificates, String address, String other, String participate, String code, String openid, Integer state, Integer usernumber) {
        this.id = id;
        this.username = username;
        this.tel = tel;
        this.sex = sex;
        this.certificates = certificates;
        this.address = address;
        this.other = other;
        this.participate = participate;
        this.code = code;
        this.openid = openid;
        this.state = state;
        this.usernumber = usernumber;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCertificates() {
        return certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getParticipate() {
        return participate;
    }

    public void setParticipate(String participate) {
        this.participate = participate;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(Integer usernumber) {
        this.usernumber = usernumber;
    }

    @Override
    public String toString() {
        return "Userinfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", tel='" + tel + '\'' +
                ", sex='" + sex + '\'' +
                ", certificates='" + certificates + '\'' +
                ", address='" + address + '\'' +
                ", other='" + other + '\'' +
                ", participate='" + participate + '\'' +
                ", code='" + code + '\'' +
                ", openid='" + openid + '\'' +
                ", state=" + state +
                ", usernumber='" + usernumber + '\'' +
                '}';
    }
}
