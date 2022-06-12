package com.fingerchar.db.vo;

import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcUser;

/**
 * @Author： Zjm
 * @Date：2022/4/11 20:34
 */
public class FcOrderVo {

    private String owner;
    private String sellToken;
    private String sellTokenId;
    private Integer sellType;
    private String sellValue;
    private String buyerToken;
    private String buyerTokenId;
    private Integer buyerType;
    private String buyerValue;
    private Integer orderType;
    private String paytokenAddress;
    private String paytokenName;
    private Integer paytokenDecimals;
    private String paytokenSymbol;

    private Long updateTime;
    private Long createTime;

    UserBaseInfoVo user;

    public FcOrderVo(FcOrder order, FcUser user){
        this.owner = order.getOwner();
        this.sellToken = order.getSellToken();
        this.sellTokenId = order.getSellTokenId();
        this.sellType = order.getSellType();
        this.sellValue = order.getSellValue();
        this.buyerToken = order.getBuyerToken();
        this.buyerTokenId = order.getBuyerTokenId();
        this.buyerType = order.getBuyerType();
        this.buyerValue = order.getBuyerValue();
        this.orderType = order.getOrderType();
        this.paytokenAddress = order.getPaytokenAddress();
        this.paytokenName = order.getPaytokenName();
        this.paytokenDecimals = order.getPaytokenDecimals();
        this.paytokenSymbol = order.getPaytokenSymbol();
        this.updateTime = order.getUpdateTime();
        this.createTime = order.getCreateTime();

        this.user = new UserBaseInfoVo(user);
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
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

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public UserBaseInfoVo getUser() {
        return user;
    }

    public void setUser(UserBaseInfoVo user) {
        this.user = user;
    }

}
