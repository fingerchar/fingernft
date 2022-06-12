package com.fingerchar.db.vo;

import java.math.BigDecimal;

/**
 * @author chenrk
 *
 */
public class FcContractVO {

	private Long id;
	
    private String name;

    private String symbol;

    private String address;

    private String shortUrl;

    private String cover;

    private String owner;

    private Boolean isAdmin;
    
    private String description;
    
    private Long totalSell;
    
    private BigDecimal totalDeal;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getShortUrl() {
		return shortUrl;
	}

	public void setShortUrl(String shortUrl) {
		this.shortUrl = shortUrl;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getTotalSell() {
		return totalSell;
	}

	public void setTotalSell(Long totalSell) {
		this.totalSell = totalSell;
	}

	public BigDecimal getTotalDeal() {
		return totalDeal;
	}

	public void setTotalDeal(BigDecimal totalDeal) {
		this.totalDeal = totalDeal;
	}
	
}
