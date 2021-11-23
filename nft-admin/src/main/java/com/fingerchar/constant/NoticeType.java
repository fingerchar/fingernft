package com.fingerchar.constant;

public enum NoticeType {
	
	FOLLOW("Follow", 1), LIKE("Like", 2), TRADE("Trade", 3);
	
	private String name;
	
	private Integer type;
	
	private NoticeType(String name, Integer type) {
		this.name = name;
		this.type = type;
	}
	
	public static NoticeType getNoticeTypeByName(String name) {
		for(NoticeType t : NoticeType.values()) {
			if(t.getName().equals(name)) {
				return t;
			}
		}
		return null;
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
