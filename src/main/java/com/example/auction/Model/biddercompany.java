package com.example.auction.Model;

import java.util.Date;

public class biddercompany {
    private Integer id;

    private Integer userid;

    private String company;

    private String companytel;

    private String companyaddress;

    private String companycertificates;

    private String state;

    private String other;

    private String bond;

    private String contactsname;

    private String contactstel;

    private String contactsaddress;

    private String payvoucher;

    private String updateday;

    public biddercompany(Integer id, Integer userid, String company, String companytel, String companyaddress, String companycertificates, String state, String other, String bond, String contactsname, String contactstel, String contactsaddress, String payvoucher, String updateday) {
        this.id = id;
        this.userid = userid;
        this.company = company;
        this.companytel = companytel;
        this.companyaddress = companyaddress;
        this.companycertificates = companycertificates;
        this.state = state;
        this.other = other;
        this.bond = bond;
        this.contactsname = contactsname;
        this.contactstel = contactstel;
        this.contactsaddress = contactsaddress;
        this.payvoucher = payvoucher;
        this.updateday = updateday;
    }

    public biddercompany() {
        super();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public String getCompanytel() {
        return companytel;
    }

    public void setCompanytel(String companytel) {
        this.companytel = companytel == null ? null : companytel.trim();
    }

    public String getCompanyaddress() {
        return companyaddress;
    }

    public void setCompanyaddress(String companyaddress) {
        this.companyaddress = companyaddress == null ? null : companyaddress.trim();
    }

    public String getCompanycertificates() {
        return companycertificates;
    }

    public void setCompanycertificates(String companycertificates) {
        this.companycertificates = companycertificates == null ? null : companycertificates.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other == null ? null : other.trim();
    }

    public String getBond() {
        return bond;
    }

    public void setBond(String bond) {
        this.bond = bond == null ? null : bond.trim();
    }

    public String getContactsname() {
        return contactsname;
    }

    public void setContactsname(String contactsname) {
        this.contactsname = contactsname == null ? null : contactsname.trim();
    }

    public String getContactstel() {
        return contactstel;
    }

    public void setContactstel(String contactstel) {
        this.contactstel = contactstel == null ? null : contactstel.trim();
    }

    public String getContactsaddress() {
        return contactsaddress;
    }

    public void setContactsaddress(String contactsaddress) {
        this.contactsaddress = contactsaddress == null ? null : contactsaddress.trim();
    }

    public String getPayvoucher() {
        return payvoucher;
    }

    public void setPayvoucher(String payvoucher) {
        this.payvoucher = payvoucher == null ? null : payvoucher.trim();
    }

    public String getUpdateday() {
        return updateday;
    }

    public void setUpdateday(String updateday) {
        this.updateday = updateday;
    }
}