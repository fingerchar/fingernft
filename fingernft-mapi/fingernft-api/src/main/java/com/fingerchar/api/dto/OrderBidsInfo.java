package com.fingerchar.api.dto;

import com.alibaba.fastjson.JSON;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcOrderLog;
import com.fingerchar.db.domain.FcUser;

public class OrderBidsInfo {

    private Long orderId;

    private String avatar;

    private String nickname;

    private String address;

    private String owner;

    private String sellToken;

    private String sellTokenId;

    private Integer sellType;

    private String sellValue;

    private String buyerToken;

    private String buyerTokenId;

    private Integer buyerType;

    private String buyerValue;

    private String salt;

    private String signature;

    private String sells;

    private String completed;

    private Integer status;

    private Boolean expired;

    private Boolean deleted;

    private String usdtPrice;

    private Integer type;

    private Long time;

    private String paytokenAddress;

    private String paytokenName;

    private Integer paytokenDecimals;

    private String paytokenSymbol;

    public OrderBidsInfo(FcOrderLog log) {
        FcOrder order = JSON.parseObject(log.getContent(), FcOrder.class);
        this.address = order.getOwner();
        this.sellToken = order.getSellToken();
        this.sellValue = order.getSellValue();
        this.sellType = order.getSellType();
        this.sellTokenId = order.getSellTokenId();
        this.buyerToken = order.getBuyerToken();
        this.buyerTokenId = order.getBuyerTokenId();
        this.buyerValue = order.getBuyerValue();
        this.buyerType = order.getBuyerType();
        this.signature = order.getSignature();
        this.salt = order.getSalt();
        this.status = order.getStatus();
        this.sells = order.getSells();
        this.paytokenAddress = order.getPaytokenAddress();
        this.paytokenName = order.getPaytokenName();
        this.paytokenDecimals = order.getPaytokenDecimals();
        this.paytokenSymbol = order.getPaytokenSymbol();

        if(null == order.getExpired()) {
            order.setExpired(false);
        }
        if(null == order.getDeleted()) {
            order.setDeleted(false);
        }
        if (log.getExpired() || log.getDeleted()) {
            this.expired = true;
        } else {
            this.expired = order.getExpired() || order.getDeleted();
        }

        this.deleted = order.getDeleted();
        this.usdtPrice = order.getUsdtPrice();
        this.orderId = log.getOrderId();
        this.type = log.getType();
        this.time = log.getCreateTime();
    }

    public OrderBidsInfo addUserInfo(FcUser user) {
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSellToken() {
        return sellToken;
    }

    public void setSellToken(String sellToken) {
        this.sellToken = sellToken;
    }

    public String getSellTokenId() {
        return sellTokenId;
    }

    public void setSellTokenId(String sellTokenId) {
        this.sellTokenId = sellTokenId;
    }

    public Integer getSellType() {
        return sellType;
    }

    public void setSellType(Integer sellType) {
        this.sellType = sellType;
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

    public String getBuyerTokenId() {
        return buyerTokenId;
    }

    public void setBuyerTokenId(String buyerTokenId) {
        this.buyerTokenId = buyerTokenId;
    }

    public Integer getBuyerType() {
        return buyerType;
    }

    public void setBuyerType(Integer buyerType) {
        this.buyerType = buyerType;
    }

    public String getBuyerValue() {
        return buyerValue;
    }

    public void setBuyerValue(String buyerValue) {
        this.buyerValue = buyerValue;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
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

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSells() {
        return sells;
    }

    public void setSells(String sells) {
        this.sells = sells;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getPaytokenAddress() {
        return paytokenAddress;
    }

    public void setPaytokenAddress(String paytokenAddress) {
        this.paytokenAddress = paytokenAddress;
    }

    public String getPaytokenName() {
        return paytokenName;
    }

    public void setPaytokenName(String paytokenName) {
        this.paytokenName = paytokenName;
    }

    public Integer getPaytokenDecimals() {
        return paytokenDecimals;
    }

    public void setPaytokenDecimals(Integer paytokenDecimals) {
        this.paytokenDecimals = paytokenDecimals;
    }

    public String getPaytokenSymbol() {
        return paytokenSymbol;
    }

    public void setPaytokenSymbol(String paytokenSymbol) {
        this.paytokenSymbol = paytokenSymbol;
    }
}
