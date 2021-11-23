package com.fingerchar.api.dto;

import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcUser;

public class ItemOwners {

	private Long sellQuantity;
	
	private Long quantity;
	
	private Boolean onsell;
	
	private String nickname;
	
	private String address;
	
	private String avatar;
	
	private String price;
	
	private String payTokenAddress;
	
	private Long createTime;
	
	private String sellValue;
	
	private String buyValue;
	
	private Long completed;

	/**
	 *
	 */
	public ItemOwners(FcNftItems items, FcUser user) {
		this.sellQuantity = items.getSellQuantity();
		this.onsell = items.getOnsell();
		this.nickname = user.getNickname();
		this.address = user.getAddress();
		this.avatar = user.getAvatar();
		this.price = items.getPrice();
		this.quantity = items.getQuantity();
		this.payTokenAddress = items.getPaytokenAddress();
		this.createTime = items.getCreateTime();
	}

	public Long getSellQuantity() {
		return sellQuantity;
	}

	public void setSellQuantity(Long sellQuantity) {
		this.sellQuantity = sellQuantity;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public Boolean getOnsell() {
		return onsell;
	}

	public void setOnsell(Boolean onsell) {
		this.onsell = onsell;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPayTokenAddress() {
		return payTokenAddress;
	}

	public void setPayTokenAddress(String payTokenAddress) {
		this.payTokenAddress = payTokenAddress;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
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
