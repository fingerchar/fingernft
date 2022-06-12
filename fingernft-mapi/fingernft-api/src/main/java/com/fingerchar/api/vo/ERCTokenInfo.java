package com.fingerchar.api.vo;

public class ERCTokenInfo {

	private String name;
	
	private String contractName;
	
	private String contractSymbol;
	
	private String type;
	
	private String imageUrl;
	
	private String animUrl;
	
	private String creator;
	
	private String properties;
	
	private Long quantity;
	
	private String description;
	
	private String content;
	
	private String uri;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAnimUrl() {
		return animUrl;
	}

	public void setAnimUrl(String animUrl) {
		this.animUrl = animUrl;
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

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public final String getContent() {
		return content;
	}

	public final void setContent(String content) {
		this.content = content;
	}

	public final String getUri() {
		return uri;
	}

	public final void setUri(String uri) {
		this.uri = uri;
	}
}
