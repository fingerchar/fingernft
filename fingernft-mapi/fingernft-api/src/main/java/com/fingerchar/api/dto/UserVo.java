package com.fingerchar.api.dto;

import com.fingerchar.db.domain.FcUser;

public class UserVo {

    private String nickname;

    private String avatar;

    private String address;
    
    public UserVo() {
    	
    }
    
    public UserVo(FcUser user) {
    	this.nickname = user.getNickname();
    	this.avatar = user.getAvatar();
    	this.address = user.getAddress();
    }
    
    public UserVo(String address) {
    	this.address = address;
    }

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
