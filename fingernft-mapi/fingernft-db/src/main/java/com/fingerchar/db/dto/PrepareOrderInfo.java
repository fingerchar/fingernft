package com.fingerchar.db.dto;


import com.fingerchar.db.domain.FcOrder;

public class PrepareOrderInfo {

    private Integer type;

    private String owner;

    private String sellToken;

    private String sellTokenId;

    private String sellValue;

    private String sellType;

    private String buyToken;

    private String buyTokenId;

    private String buyValue;

    private String buyType;

    private String sellFee;

    private String salt;

    private String signature;

    private Long nftItemsId;

    private Long orderId;

    private String message;

    private Integer quantity;

    private String buyFee;

    private String r;

    private String v;

    private String s;

    public PrepareOrderInfo() {
    }

    public PrepareOrderInfo(FcOrder order) {
        this.buyToken = order.getBuyerToken();
        this.buyTokenId = order.getBuyerTokenId().toString();
        this.buyType = order.getBuyerType().toString();
        this.buyValue = order.getBuyerValue();
        this.owner = order.getOwner();
        this.sellToken = order.getSellToken();
        this.sellTokenId = order.getSellTokenId().toString();
        this.sellType = order.getSellType().toString();
        this.sellValue = order.getSellValue();
        this.salt = order.getSalt();
    }

    public FcOrder toOrder() {
        FcOrder order = new FcOrder();
        order.setBuyerToken(this.buyToken);
        order.setBuyerTokenId(this.buyTokenId);
        order.setBuyerType(Integer.parseInt(this.buyType));
        order.setBuyerValue(this.buyValue);
        order.setOwner(this.owner);
        order.setSalt(this.salt);
        order.setSellToken(this.sellToken);
        order.setSellTokenId(this.sellTokenId);
        order.setSellType(Integer.parseInt(this.sellType));
        order.setSellValue(this.sellValue);
        order.setSignature(this.signature);
        return order;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getSellValue() {
        return sellValue;
    }

    public void setSellValue(String sellValue) {
        this.sellValue = sellValue;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String sellType) {
        this.sellType = sellType;
    }

    public String getBuyToken() {
        return buyToken;
    }

    public void setBuyToken(String buyToken) {
        this.buyToken = buyToken;
    }

    public String getBuyTokenId() {
        return buyTokenId;
    }

    public void setBuyTokenId(String buyTokenId) {
        this.buyTokenId = buyTokenId;
    }

    public String getBuyValue() {
        return buyValue;
    }

    public void setBuyValue(String buyValue) {
        this.buyValue = buyValue;
    }

    public String getBuyType() {
        return buyType;
    }

    public void setBuyType(String buyType) {
        this.buyType = buyType;
    }

    public String getSellFee() {
        return sellFee;
    }

    public void setSellFee(String sellFee) {
        this.sellFee = sellFee;
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

    public Long getNftItemsId() {
        return nftItemsId;
    }

    public void setNftItemsId(Long nftItemsId) {
        this.nftItemsId = nftItemsId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(String buyFee) {
        this.buyFee = buyFee;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }


}
