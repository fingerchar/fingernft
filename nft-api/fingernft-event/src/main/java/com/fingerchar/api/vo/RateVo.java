package com.fingerchar.api.vo;

public class RateVo {

	private String current_price_usd;
	
	private String name;
	
	private String code;

	public String getCurrent_price_usd() {
		return current_price_usd;
	}

	public void setCurrent_price_usd(String current_price_usd) {
		this.current_price_usd = current_price_usd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
