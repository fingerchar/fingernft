package com.fingerchar.db.vo;

import java.math.BigDecimal;

public class IndexUserVO {

private Long id;
	
    private String nickname;

    private String avatar;
    
    private String address;

    private Integer userVerify;

    private String shortUrl;
    
    private String brief;
    
    private BigDecimal totalSellAmount;
    
    private Long totalSell;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getUserVerify() {
		return userVerify;
	}

	public void setUserVerify(Integer userVerify) {
		this.userVerify = userVerify;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public BigDecimal getTotalSellAmount() {
		return totalSellAmount;
	}

	public void setTotalSellAmount(BigDecimal totalSellAmount) {
		this.totalSellAmount = totalSellAmount;
	}

	public Long getTotalSell() {
		return totalSell;
	}

	public void setTotalSell(Long totalSell) {
		this.totalSell = totalSell;
	}
}
