package com.example.auction.Model;

/**
 * @作者：zhanghe
 * @时间：2021-02-28 12:58:03
 * @注释：
 */
public class Records{
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String orderNo;
    private String vin;
    private DetectionData detectionData;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public DetectionData getDetectionData() {
        return detectionData;
    }

    public void setDetectionData(DetectionData detectionData) {
        this.detectionData = detectionData;
    }
}
