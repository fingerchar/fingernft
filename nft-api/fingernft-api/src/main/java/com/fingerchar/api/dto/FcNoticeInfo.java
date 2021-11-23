package com.fingerchar.api.dto;

import com.alibaba.fastjson.JSONObject;
import com.fingerchar.api.constant.CommonStatus;
import com.fingerchar.db.domain.FcNotice;
import com.fingerchar.db.domain.FcUser;

public class FcNoticeInfo {
	
	private Long id;

	private String type;
	
	private String subType;

    private Long userId;

    private String owner;

    private Boolean isRead;

    private JSONObject content;
    
    private String image;
    
    private String name;
    
    private Long createTime;
    
    private String operator;
    
    private String operatorImg;
    
    private String operatorName;
    
    public FcNoticeInfo(FcNotice notice) {
    	this.id = notice.getId();
    	this.type = notice.getType();
    	this.subType = notice.getSubType();
    	this.userId = notice.getUserId();
    	this.owner = notice.getOwner();
    	this.isRead = notice.getIsRead();
    	this.image = notice.getImage();
    	this.name = notice.getName();
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
        	if(temp.equals(this.getSubType())) {
        		this.image = user.getAvatar();
        		this.name = user.getNickname();
        	}
    	}
    	return this;
    }
    
    public FcNoticeInfo addFollowDetails(FcUser user) {
    	this.image = user.getAvatar();
    	this.name = user.getNickname();
    	return this;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
