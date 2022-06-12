package com.fingerchar.db.vo;

import com.fingerchar.db.domain.FcUser;

/**
 * @Author： Zjm
 * @Date：2022/3/29 9:11
 */
public class UserBaseInfoVo {
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;
    private String avatar;
    private String nickname;

    public UserBaseInfoVo(FcUser user){
        if(null != user) {
            this.address = user.getAddress();
            this.avatar = user.getAvatar();
            this.nickname = user.getNickname();
        }
    }

    public UserBaseInfoVo(String address){
        this.address = address;
    }

    public UserBaseInfoVo(String address, FcUser user){
        this.address = address;
        if(null != user) {
            this.avatar = user.getAvatar();
            this.nickname = user.getNickname();
        }
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
