package com.fingerchar.api.dto;

import com.alibaba.fastjson.JSONObject;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.db.domain.FcNotice;
import com.fingerchar.db.domain.FcUser;

public class FcNoticeInfo {
	
	private Long id;

	private Integer type;
	
	private Integer subType;

    private String owner;

    private Boolean isRead;

    private JSONObject content;
    
    private Long createTime;
    
    private String operator;
    
    private String operatorImg;
    
    private String operatorName;
    
    public FcNoticeInfo(FcNotice notice) {
    	this.id = notice.getId();
    	this.type = notice.getType();
    	this.subType = notice.getSubType();
    	this.owner = notice.getOwner();
    	this.isRead = notice.getIsRead();
    	if(null != notice.getContent()) {
    		this.content = JSONObject.parseObject(notice.getContent());
    	}
    	if(null != notice.getCreateTime()) {
    		this.createTime = notice.getCreateTime();
    	}
    	this.operator = notice.getOperator();
    }
    
    public FcNoticeInfo addOperatorInfo(FcUser user) {
    	if(null != user) {
    		this.operatorImg = user.getAvatar();
        	this.operatorName = user.getNickname();
        	String temp = CommonStatus.getStatusByName("Followed").getType().toString();
    	}
    	return this;
    }
    
    public FcNoticeInfo addFollowDetails(FcUser user) {
    	return this;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSubType() {
		return subType;
	}

	public void setSubType(Integer subType) {
		this.subType = subType;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public JSONObject getContent() {
		return content;
	}

	public void setContent(JSONObject content) {
		this.content = content;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatorImg() {
		return operatorImg;
	}

	public void setOperatorImg(String operatorImg) {
		this.operatorImg = operatorImg;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
}
