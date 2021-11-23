package com.fingerchar.dto;

/**
 * @author: Black_Dragon
 * @date: 2021/6/3
 */
public class FcOrderDto {

    /**
     * 商品成交价格
     */
    private String usdtPrice;

    /**
     * 商品类型
     */
    private Integer type;

    public String getUsdtPrice() {
        return usdtPrice;
    }

    public void setUsdtPrice(String usdtPrice) {
        this.usdtPrice = usdtPrice;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
