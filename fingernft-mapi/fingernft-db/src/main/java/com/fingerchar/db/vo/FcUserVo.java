package com.fingerchar.db.vo;

import java.math.BigDecimal;

/*
 *
 * @author zjm
 * */
public class FcUserVo {
    private Long id;

    private Long createTime;

    private Boolean deleted;

    private Long updateTime;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 钱包账号地址
     */
    private String address;

    /**
     * 登录类型
     */
    private String loginType;

    /**
     * 最后登录时间
     */
    private Long lastLoginTime;

    /**
     * 最后登录ip
     */
    private String lastLoginIp;

    /**
     * 用户是否验证
     */
    private Integer userVerify;

    /**
     * 用户短地址
     */
    private String shortUrl;

    /**
     * 用户简介
     */
    private String brief;

    private String haveNFT;

    private String createNFT;

    private String saleNFT;

    private String buyNFT;

    private String accumulatedVolume;

    private BigDecimal accumulatedMoney;

    public FcUserVo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Long getUpdateTime() {
        return updateTime;
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

    public String getHaveNFT() {
        return haveNFT;
    }

    public void setHaveNFT(String haveNFT) {
        this.haveNFT = haveNFT;
    }

    public String getCreateNFT() {
        return createNFT;
    }

    public void setCreateNFT(String createNFT) {
        this.createNFT = createNFT;
    }

    public String getSaleNFT() {
        return saleNFT;
    }

    public void setSaleNFT(String saleNFT) {
        this.saleNFT = saleNFT;
    }

    public String getBuyNFT() {
        return buyNFT;
    }

    public void setBuyNFT(String buyNFT) {
        this.buyNFT = buyNFT;
    }

    public String getAccumulatedVolume() {
        return accumulatedVolume;
    }

    public void setAccumulatedVolume(String accumulatedVolume) {
        this.accumulatedVolume = accumulatedVolume;
    }

    public BigDecimal getAccumulatedMoney() {
        return accumulatedMoney;
    }

    public void setAccumulatedMoney(BigDecimal accumulatedMoney) {
        this.accumulatedMoney = accumulatedMoney;
    }

    @Override
    public String toString() {
        return "FcUserVo{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", deleted=" + deleted +
                ", updateTime=" + updateTime +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", address='" + address + '\'' +
                ", loginType='" + loginType + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", userVerify=" + userVerify +
                ", shortUrl='" + shortUrl + '\'' +
                ", brief='" + brief + '\'' +
                ", haveNFT=" + haveNFT +
                ", createNFT=" + createNFT +
                ", saleNFT=" + saleNFT +
                ", buyNFT=" + buyNFT +
                ", accumulatedVolume=" + accumulatedVolume +
                ", accumulatedMoney=" + accumulatedMoney +
                '}';
    }
}
