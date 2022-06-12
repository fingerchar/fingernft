package com.fingerchar.db.vo;

import com.fingerchar.db.domain.FcUser;

/**
 * @Author： Zjm
 * @Date：2022/3/29 9:07
 */
public class UserInfoVo {
    private String avatar;
    private String nickname;
    private Integer userVerify;
    private String shortUrl;
    private String brief;
    private String bannerUrl;

    public UserInfoVo(FcUser user){
        this.avatar = user.getAvatar();
        this.nickname = user.getNickname();
        this.userVerify = user.getUserVerify();
        this.shortUrl = user.getShortUrl();
        this.bannerUrl = user.getBannerUrl();
        this.brief = user.getBrief();
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

    public Integer getUserVerify() {
        return userVerify;
    }

    public void setUserVerify(Integer userVerify) {
        this.userVerify = userVerify;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

}
