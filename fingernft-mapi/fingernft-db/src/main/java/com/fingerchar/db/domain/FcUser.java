package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcUser
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_user")
public class FcUser extends BaseEntity {


    /**
     * 昵称
     */
    @TableField("`nickname`")
    private String nickname;

    /**
     * 头像
     */
    @TableField("`avatar`")
    private String avatar;

    /**
     * 钱包账号地址
     */
    @TableField("`address`")
    private String address;

    /**
     * 登录类型
     */
    @TableField("`login_type`")
    private String loginType;

    @TableField("`last_login_time`")
    private Long lastLoginTime;

    /**
     * 最后登录ip
     */
    @TableField("`last_login_ip`")
    private String lastLoginIp;

    /**
     * 用户是否验证
     */
    @TableField("`user_verify`")
    private Integer userVerify;

    /**
     * 用户短地址
     */
    @TableField("`short_url`")
    private String shortUrl;

    /**
     * 用户简介
     */
    @TableField("`brief`")
    private String brief;

    @TableField("`banner_url`")
    private String bannerUrl;

    /**
     * 是否属于本站用户
     */
    @TableField("`is_web`")
    private Boolean isWeb;


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

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
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

    public Boolean getIsWeb() {
        return isWeb;
    }

    public void setIsWeb(Boolean isWeb) {
        this.isWeb = isWeb;
    }

    public static final String NICKNAME = "`nickname`";

    public static final String AVATAR = "`avatar`";

    public static final String ADDRESS = "`address`";

    public static final String LOGIN_TYPE = "`login_type`";

    public static final String LAST_LOGIN_TIME = "`last_login_time`";

    public static final String LAST_LOGIN_IP = "`last_login_ip`";

    public static final String USER_VERIFY = "`user_verify`";

    public static final String SHORT_URL = "`short_url`";

    public static final String BRIEF = "`brief`";

    public static final String BANNER_URL = "`banner_url`";

    public static final String IS_WEB = "`is_web`";

    @Override
    public String toString() {
        return "FcUser{" +
        "nickname=" + nickname +
        ", avatar=" + avatar +
        ", address=" + address +
        ", loginType=" + loginType +
        ", lastLoginTime=" + lastLoginTime +
        ", lastLoginIp=" + lastLoginIp +
        ", userVerify=" + userVerify +
        ", shortUrl=" + shortUrl +
        ", brief=" + brief +
        ", bannerUrl=" + bannerUrl +
        ", isWeb=" + isWeb +
        "}";
    }
}
