package com.fingerchar.api.dto;

import com.fingerchar.db.domain.FcOrder;

public class ActiveBidInfo {

	private Long id;

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

    private Integer orderType;

    private String completed;
    
    private Long createTime;
    
    private Long updateTIme;

    private String paytokenAddress;
    private String paytokenName;


	private Integer paytokenDecimals;
    private String paytokenSymbol;
    
    public ActiveBidInfo() {
    	
    }
    
    public ActiveBidInfo(FcOrder order) {
    	this.buyerToken = order.getBuyerToken();
    	this.buyerTokenId = order.getBuyerTokenId();
    	this.buyerType = order.getBuyerType();
    	this.buyerValue = order.getBuyerValue();
    	this.completed = order.getSells();
    	this.createTime = order.getCreateTime();
    	this.id = order.getId();
    	this.orderType = order.getOrderType();
    	this.owner = order.getOwner();
    	this.salt = order.getSalt();
    	this.sellToken = order.getSellToken();
    	this.sellTokenId = order.getSellTokenId();
    	this.sellType = order.getSellType();
    	this.sellValue = order.getSellValue();
    	this.signature = order.getSignature();
    	this.updateTIme = order.getUpdateTime();
    	this.paytokenAddress = order.getPaytokenAddress();
    	this.paytokenName = order.getPaytokenName();
    	this.paytokenDecimals = order.getPaytokenDecimals();
    	this.paytokenSymbol = order.getPaytokenSymbol();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTIme() {
		return updateTIme;
	}

	public void setUpdateTIme(Long updateTIme) {
		this.updateTIme = updateTIme;
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
