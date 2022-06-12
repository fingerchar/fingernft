package com.fingerchar.api.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author chenrk
 *
 */
public class HomeIndexParamsVO {

	private String address;
	private Long cate;
	private List<String> contracts;
	private String payToken;
	private BigDecimal minPrice;
	private BigDecimal maxPrice;
	private String search; 
	private String sort;
	private String order;
	private Integer sellType;
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Long getCate() {
		return cate;
	}
	public void setCate(Long cate) {
		this.cate = cate;
	}
	public List<String> getContracts() {
		return contracts;
	}
	public void setContracts(List<String> contracts) {
		this.contracts = contracts;
	}
	public String getPayToken() {
		return payToken;
	}
	public void setPayToken(String payToken) {
		this.payToken = payToken;
	}
	public BigDecimal getMinPrice() {
		return minPrice;
	}
	public void setMinPrice(BigDecimal minPrice) {
		this.minPrice = minPrice;
	}
	public BigDecimal getMaxPrice() {
		return maxPrice;
	}
	public void setMaxPrice(BigDecimal maxPrice) {
		this.maxPrice = maxPrice;
	}
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public Integer getSellType() {
		return sellType;
	}
	public void setSellType(Integer sellType) {
		this.sellType = sellType;
	}
	
}

