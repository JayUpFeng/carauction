package com.example.auction.Model;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhanghe
 * @since 2021-02-25
 */
public class Certificate  {

    private static final long serialVersionUID = 1L;

    //元数据新增字段
    /**
     * 图片名称
     */
    private String name;
    /**
     * 图片地址 (需加图片前缀)
     */
    private String netImg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetImg() {
        return netImg;
    }

    public void setNetImg(String netImg) {
        this.netImg = netImg;
    }
}
