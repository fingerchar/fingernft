package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcContract
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_contract")
public class FcContract extends BaseEntity {


    /**
     * nft name
     */
    @TableField("`name`")
    private String name;

    /**
     * nft symbol
     */
    @TableField("`symbol`")
    private String symbol;

    /**
     * 合约地址
     */
    @TableField("`address`")
    private String address;

    /**
     * 短地址
     */
    @TableField("`short_url`")
    private String shortUrl;

    /**
     * 合约版本
     */
    @TableField("`version`")
    private String version;

    /**
     * 图标
     */
    @TableField("`cover`")
    private String cover;

    /**
     * 图标保存Id
     */
    @TableField("`storage_id`")
    private Long storageId;

    /**
     * 合约拥有者
     */
    @TableField("`owner`")
    private String owner;

    /**
     * 是否是官方合约
     */
    @TableField("`is_admin`")
    private Boolean isAdmin;

    /**
     * 是否已验证
     */
    @TableField("`verify`")
    private Boolean verify;

    /**
     * 合约描述
     */
    @TableField("`description`")
    private String description;

    /**
     * 上一次增发的tokenId
     */
    @TableField("`last_token_id`")
    private Long lastTokenId;

    /**
     * 封面
     */
    @TableField("`banner_url`")
    private String bannerUrl;

    /**
     * 获取name和symbol次数
     */
    @TableField("`get_info_times`")
    private Integer getInfoTimes;

    /**
     * 是否支持版权
     */
    @TableField("`is_royalties`")
    private Boolean isRoyalties;

    /**
     * 签名人账号
     */
    @TableField("`signer`")
    private String signer;

    /**
     * 是否已经同步
     */
    @TableField("`is_sync`")
    private Boolean isSync;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Boolean getVerify() {
        return verify;
    }

    public void setVerify(Boolean verify) {
        this.verify = verify;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLastTokenId() {
        return lastTokenId;
    }

    public void setLastTokenId(Long lastTokenId) {
        this.lastTokenId = lastTokenId;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public Integer getGetInfoTimes() {
        return getInfoTimes;
    }

    public void setGetInfoTimes(Integer getInfoTimes) {
        this.getInfoTimes = getInfoTimes;
    }

    public Boolean getIsRoyalties() {
        return isRoyalties;
    }

    public void setIsRoyalties(Boolean isRoyalties) {
        this.isRoyalties = isRoyalties;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public static final String NAME = "`name`";

    public static final String SYMBOL = "`symbol`";

    public static final String ADDRESS = "`address`";

    public static final String SHORT_URL = "`short_url`";

    public static final String VERSION = "`version`";

    public static final String COVER = "`cover`";

    public static final String STORAGE_ID = "`storage_id`";

    public static final String OWNER = "`owner`";

    public static final String IS_ADMIN = "`is_admin`";

    public static final String VERIFY = "`verify`";

    public static final String DESCRIPTION = "`description`";

    public static final String LAST_TOKEN_ID = "`last_token_id`";

    public static final String BANNER_URL = "`banner_url`";

    public static final String GET_INFO_TIMES = "`get_info_times`";

    public static final String IS_ROYALTIES = "`is_royalties`";

    public static final String SIGNER = "`signer`";

    public static final String IS_SYNC = "`is_sync`";

    @Override
    public String toString() {
        return "FcContract{" +
        "name=" + name +
        ", symbol=" + symbol +
        ", address=" + address +
        ", shortUrl=" + shortUrl +
        ", version=" + version +
        ", cover=" + cover +
        ", storageId=" + storageId +
        ", owner=" + owner +
        ", isAdmin=" + isAdmin +
        ", verify=" + verify +
        ", description=" + description +
        ", lastTokenId=" + lastTokenId +
        ", bannerUrl=" + bannerUrl +
        ", getInfoTimes=" + getInfoTimes +
        ", isRoyalties=" + isRoyalties +
        ", signer=" + signer +
        ", isSync=" + isSync +
        "}";
    }
}
