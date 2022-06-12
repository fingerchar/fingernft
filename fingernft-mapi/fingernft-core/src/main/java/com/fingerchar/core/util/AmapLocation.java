package com.fingerchar.core.util;


/**
 * 高德地图IP定位接口返回数据类
 */
public class AmapLocation {
    /**
     * 返回结果状态值
     */
    private int status;
    /**
     * 返回状态说明
     */
    private String info;
    /**
     * 状态码
     */
    private String infoCode;
    /**
     * 省份名称
     */
    private String province;
    /**
     * 城市名称
     */
    private String city;
    /**
     * 城市adcode编码
     */
    private String adcode;
    /**
     * 所在城市矩形区域范围
     */
    private String rectangle;

    @Override
    public String toString() {
        return "AmapLocation{" +
                "status=" + status +
                ", info='" + info + '\'' +
                ", infoCode='" + infoCode + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", adcode='" + adcode + '\'' +
                ", rectangle='" + rectangle + '\'' +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(String infoCode) {
        this.infoCode = infoCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getRectangle() {
        return rectangle;
    }

    public void setRectangle(String rectangle) {
        this.rectangle = rectangle;
    }
}
