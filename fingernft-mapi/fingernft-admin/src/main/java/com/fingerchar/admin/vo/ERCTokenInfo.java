package com.fingerchar.admin.vo;

public class ERCTokenInfo {

	private String name;
	
	private String contractName;
	
	private String contractSymbol;
	
	private Integer contractDecimals;

	private String creator;
	
	private String properties;
	
	private String content;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractSymbol() {
		return contractSymbol;
	}

	public void setContractSymbol(String contractSymbol) {
		this.contractSymbol = contractSymbol;
	}
	
	public Integer getContractDecimals() {
		return contractDecimals;
	}

	public void setContractDecimals(Integer contractDecimals) {
		this.contractDecimals = contractDecimals;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
