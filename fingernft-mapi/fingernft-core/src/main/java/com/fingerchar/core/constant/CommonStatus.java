package com.fingerchar.core.constant;

public enum CommonStatus {
	SALE("Sale",1), EDIT_SALE("Edit sale",2), CANCEL_SALE("Cancel sale", 3),
	BUY("Buy", 4), BID("Bid", 5), EDIT_BID("Edit bid", 6), CANCEL_BID("Cancel bid", 7),
	ACCEPT_BID("Accept bid", 8), MINT("Mint", 9), LIKING("Liking", 10), LIKED("Liked", 11),
	FOLLOWING("Following", 12), FOLLOWED("Followed", 13), BURN("Burn", 14), TRANSFER("Transfer", 15), 
	RECEIVE("Receive", 16), BOUGHT("Bought", 17), BIDDEN("Bidden", 18), RELEASE("Release", 19),
	CREATE_CONTRACT("CreateContract", 29);


	private Integer type;
	
	private String name;
	
	private CommonStatus(String name, Integer type) {
		this.type = type;
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static CommonStatus getStatusByName(String name) {
		for(CommonStatus t : CommonStatus.values()) {
			if(t.getName().equals(name)) {
				return t;
			}
		}
		return null;
	}
	
	public static CommonStatus getStatusByType(Integer type) {
		if(null == type) {
			return null;
		}
		for(CommonStatus t : CommonStatus.values()) {
			if(t.getType().intValue() == type.intValue()) {
				return t;
			}
		}
		return null;
	}
}
