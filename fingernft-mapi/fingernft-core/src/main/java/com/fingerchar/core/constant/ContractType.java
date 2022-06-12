package com.fingerchar.core.constant;

public enum ContractType {

	ETH("ETH", 0), ERC20("ERC20", 1), ERC721("ERC721", 3), ERC721Deprecated("ERC721Deprecated", 4);
	
	private String name;
	
	private Integer type;
	
	public static Integer getAssetType(String name) {
		for(ContractType t : ContractType.values()) {
			if(t.getName().equals(name)) {
				return t.getType();
			}
		}
		return null;
	}
	
	public static ContractType getContractType(Integer type) {
		for(ContractType t : ContractType.values()) {
			if(t.getType().intValue() == type.intValue()) {
				return t;
			}
		}
		return null;
	}
	
	private ContractType(String name, Integer type) {
		this.name = name;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
