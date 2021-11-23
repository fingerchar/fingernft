package com.fingerchar.api.dto;

import com.fingerchar.db.domain.FcNftItems;

public class NftItemsInfo {

	private Long id;

    private Long nftId;

    private String imgUrl;

    private Long storageId;

    private String price;

    private Long paytokenId;

    private Long quantity;

    private String signature;

    private String owner;

    private Boolean onsell;

    private Boolean deleted;

    private Long createTime;

    private Long updateTime;

    private Long categoryId;

    private String address;
    
    private String payTokenAddress;
    
    private String sellValue;
    
    private String buyValue;
	
	private Long completed;
    
    public NftItemsInfo(FcNftItems items) {
    	this.address = items.getAddress();
    	this.categoryId = items.getCategoryId();
    	this.createTime = items.getCreateTime();
    	this.deleted = items.getDeleted();
    	this.id = items.getId();
    	this.imgUrl = items.getImgUrl();
    	this.owner = items.getItemOwner();
    	this.nftId = items.getNftId();
    	this.onsell = items.getOnsell();
    	this.price = items.getPrice();
    	this.quantity = items.getQuantity();
    	this.signature = items.getSignature();
    	this.storageId = items.getStorageId();
    	this.updateTime = items.getUpdateTime();
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNftId() {
		return nftId;
	}

	public void setNftId(Long nftId) {
		this.nftId = nftId;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getStorageId() {
		return storageId;
	}

	public void setStorageId(Long storageId) {
		this.storageId = storageId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Long getPaytokenId() {
		return paytokenId;
	}

	public void setPaytokenId(Long paytokenId) {
		this.paytokenId = paytokenId;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Boolean getOnsell() {
		return onsell;
	}

	public void setOnsell(Boolean onsell) {
		this.onsell = onsell;
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPayTokenAddress() {
		return payTokenAddress;
	}

	public void setPayTokenAddress(String payTokenAddress) {
		this.payTokenAddress = payTokenAddress;
	}

	public String getSellValue() {
		return sellValue;
	}

	public void setSellValue(String sellValue) {
		this.sellValue = sellValue;
	}

	public String getBuyValue() {
		return buyValue;
	}

	public void setBuyValue(String buyValue) {
		this.buyValue = buyValue;
	}

	public Long getCompleted() {
		return completed;
	}

	public void setCompleted(Long completed) {
		this.completed = completed;
	}	
}
