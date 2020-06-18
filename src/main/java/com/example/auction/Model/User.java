package com.example.auction.Model;

public class User {

    private Integer id;
    private String username;
    private String tel;
    private String sex;
    private String certificates;
    private String address;
    private String other;


    public User() {
        super();
    }

    public User(Integer id, String username, String tel, String sex, String certificates, String address, String other) {
        this.id = id;
        this.username = username;
        this.tel = tel;
        this.sex = sex;
        this.certificates = certificates;
        this.address = address;
        this.other = other;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", tel='" + tel + '\'' +
                ", sex='" + sex + '\'' +
                ", certificates='" + certificates + '\'' +
                ", address='" + address + '\'' +
                ", other='" + other + '\'' +
                '}';
    }
}
