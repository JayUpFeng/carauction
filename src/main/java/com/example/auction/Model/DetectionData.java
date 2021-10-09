package com.example.auction.Model;

import java.util.List;

/**
 * @作者：zhanghe
 * @时间：2021-02-24 21:20:40
 * @注释：
 */
public class DetectionData {
    private Integer id;
    private List<Certificate> certificate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Certificate> getCertificate() {
        return certificate;
    }

    public void setCertificate(List<Certificate> certificate) {
        this.certificate = certificate;
    }
}
