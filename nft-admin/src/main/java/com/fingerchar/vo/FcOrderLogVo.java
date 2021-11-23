package com.fingerchar.vo;

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

    private Long orderId;

    private String from;

    private String to;

    private Integer type;

    private String sellToken;

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
