package com.fingerchar.admin.vo;

import java.math.BigDecimal;

/*
 *
 * @author zjm
 * */
public class FcOrderLogVo {
    protected Long id;

    protected Boolean deleted;

    protected Long createTime;

    protected Long updateTime;

    /**
     * 订单id
     */
    private Long orderId;

    /**
     * 订单发起人地址
     */
    private String from;

    /**
     * 交易对象地址
     */
    private String to;

    /**
     * 交易类型sale_cancel, sale_buy, bid_cancel, bid_accept, erc721_create, erc1155_create, token_tranfer, token_burn
     */
    private Integer type;

    /**
     * 售卖token地址
     */
    private String sellToken;


    /**
     * 购买token地址
     */
    private String buyerToken;


    private BigDecimal accumulatedMoney;

    public FcOrderLogVo() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getSellToken() {
        return sellToken;
    }

    public void setSellToken(String sellToken) {
        this.sellToken = sellToken;
    }

    public String getBuyerToken() {
        return buyerToken;
    }

    public void setBuyerToken(String buyerToken) {
        this.buyerToken = buyerToken;
    }

    public BigDecimal getAccumulatedMoney() {
        return accumulatedMoney;
    }

    public void setAccumulatedMoney(BigDecimal accumulatedMoney) {
        this.accumulatedMoney = accumulatedMoney;
    }

    @Override
    public String toString() {
        return "FcOrderLogVo{" +
                "id=" + id +
                ", deleted=" + deleted +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderId=" + orderId +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", type=" + type +
                ", sellToken='" + sellToken + '\'' +
                ", buyerToken='" + buyerToken + '\'' +
                ", accumulatedMoney=" + accumulatedMoney +
                '}';
    }
}
