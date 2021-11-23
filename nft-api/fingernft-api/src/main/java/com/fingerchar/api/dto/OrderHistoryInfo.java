package com.fingerchar.api.dto;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.fingerchar.api.constant.CommonStatus;
import com.fingerchar.db.domain.*;

public class OrderHistoryInfo {

    private String avatar;

    private String nickname;

    private String address;

    private String sellToken;

    private String sellValue;

    private String buyerToken;

    private String buyerValue;

    private Integer status;

    private Boolean expired;

    private Boolean deleted;

    private Integer type;

    private Long sells;

    private Long completed;

    private Long time;

    private Long orderId;

    public OrderHistoryInfo(FcOrderLog log) {
        CommonStatus status = CommonStatus.getStatusByType(log.getType());
        switch (status) {
            case SALE:
            case EDIT_SALE:
            case CANCEL_SALE:
                this.address = log.getFrom();
                if (!StringUtils.isEmpty(log.getContent())) {
                    FcOrder order = JSON.parseObject(log.getContent(), FcOrder.class);
                    this.setOrderValue(order);
                }
                break;
            case BUY:
                if (!StringUtils.isEmpty(log.getContent())) {
                    FcOrder order = JSON.parseObject(log.getContent(), FcOrder.class);
                    this.setOrderValue(order);
                }
                this.address = log.getTo();
                break;
            case BID:
            case EDIT_BID:
            case CANCEL_BID:
            case ACCEPT_BID:
                this.address = log.getTo();
                if (!StringUtils.isEmpty(log.getContent())) {
                    FcOrder order = JSON.parseObject(log.getContent(), FcOrder.class);
                    this.setOrderValue(order);
                }
                break;
            case MINT:
                this.address = log.getTo();
                break;
            case BURN:
            case TRANSFER:
                this.address = log.getFrom();
                break;
            default:
                break;
        }
        this.orderId = log.getOrderId();
        this.type = log.getType();
        this.expired = log.getExpired() || log.getDeleted();
        this.time = log.getCreateTime();
    }

    private void setOrderValue(FcOrder order) {
        this.sellToken = order.getSellToken();
        this.sellValue = order.getSellValue();
        this.buyerToken = order.getBuyerToken();
        this.buyerValue = order.getBuyerValue();
        this.status = order.getStatus();
        this.address = order.getOwner();

        if(null == order.getExpired()) {
            order.setExpired(false);
        }
        if(null == order.getDeleted()) {
            order.setDeleted(false);
        }
        this.deleted = order.getDeleted() || order.getExpired();

        this.sells = order.getSells();
    }

    public OrderHistoryInfo addUserInfo(FcUser user) {
        this.avatar = user.getAvatar();
        this.nickname = user.getNickname();
        this.address = user.getAddress();
        return this;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSellToken() {
        return sellToken;
    }

    public void setSellToken(String sellToken) {
        this.sellToken = sellToken;
    }

    public String getSellValue() {
        return sellValue;
    }

    public void setSellValue(String sellValue) {
        this.sellValue = sellValue;
    }

    public String getBuyerToken() {
        return buyerToken;
    }

    public void setBuyerToken(String buyerToken) {
        this.buyerToken = buyerToken;
    }

    public String getBuyerValue() {
        return buyerValue;
    }

    public void setBuyerValue(String buyerValue) {
        this.buyerValue = buyerValue;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getSells() {
        return sells;
    }

    public void setSells(Long sells) {
        this.sells = sells;
    }

    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
