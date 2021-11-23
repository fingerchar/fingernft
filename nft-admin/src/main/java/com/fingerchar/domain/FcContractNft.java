package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcContractNft
 * @Author 
 * @Date 2021-11-16
 * @Version 2.1
 */
@TableName("`fc_contract_nft`")
public class FcContractNft extends BaseEntity {


    /**
     * 合约id
     */
    @TableField("`contract_id`")
    private Long contractId;

    /**
     * 合约地址
     */
    @TableField("`address`")
    private String address;

    /**
     * 分类id
     */
    @TableField("`category_id`")
    private Long categoryId;

    /**
     * 图片地址
     */
    @TableField("`img_url`")
    private String imgUrl;

    /**
     * 图片保存Id
     */
    @TableField("`storage_id`")
    private Long storageId;

    /**
     * 对应tokenId
     */
    @TableField("`token_id`")
    private String tokenId;

    /**
     * 数量
     */
    @TableField("`quantity`")
    private Long quantity;

    /**
     * 是否锁定
     */
    @TableField("`locked`")
    private Boolean locked;

    /**
     * 锁定描述
     */
    @TableField("`locked_content`")
    private String lockedContent;

    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 描述
     */
    @TableField("`description`")
    private String description;

    /**
     * 版权
     */
    @TableField("`royalties`")
    private String royalties;

    /**
     * 属性
     */
    @TableField("`properties`")
    private String properties;

    /**
     * 是否已验证
     */
    @TableField("`nft_verify`")
    private Integer nftVerify;

    /**
     * 是否已同步链
     */
    @TableField("`is_sync`")
    private Boolean isSync;

    /**
     * 类型
     */
    @TableField("`type`")
    private Integer type;

    /**
     * nft拥有者
     */
    @TableField("`creator`")
    private String creator;

    /**
     * 区块链交易hash值
     */
    @TableField("`tx_hash`")
    private String txHash;

    /**
     * 视频音频地址
     */
    @TableField("`anim_url`")
    private String animUrl;

    /**
     * 视频音频地址id
     */
    @TableField("`anim_storage_id`")
    private Long animStorageId;

    /**
     * 资源地址
     */
    @TableField("`metadata_url`")
    private String metadataUrl;

    /**
     * 资源内容
     */
    @TableField("`metadata_content`")
    private String metadataContent;

    /**
     * 获取资源次数
     */
    @TableField("`get_meta_times`")
    private Integer getMetaTimes;

    /**
     * 查看次数
     */
    @TableField("`view_nums`")
    private Long viewNums;

    /**
     * 锁定时长
     */
    @TableField("`lock_time`")
    private Long lockTime;


    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public String getLockedContent() {
        return lockedContent;
    }

    public void setLockedContent(String lockedContent) {
        this.lockedContent = lockedContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoyalties() {
        return royalties;
    }

    public void setRoyalties(String royalties) {
        this.royalties = royalties;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Integer getNftVerify() {
        return nftVerify;
    }

    public void setNftVerify(Integer nftVerify) {
        this.nftVerify = nftVerify;
    }

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getAnimUrl() {
        return animUrl;
    }

    public void setAnimUrl(String animUrl) {
        this.animUrl = animUrl;
    }

    public Long getAnimStorageId() {
        return animStorageId;
    }

    public void setAnimStorageId(Long animStorageId) {
        this.animStorageId = animStorageId;
    }

    public String getMetadataUrl() {
        return metadataUrl;
    }

    public void setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }

    public String getMetadataContent() {
        return metadataContent;
    }

    public void setMetadataContent(String metadataContent) {
        this.metadataContent = metadataContent;
    }

    public Integer getGetMetaTimes() {
        return getMetaTimes;
    }

    public void setGetMetaTimes(Integer getMetaTimes) {
        this.getMetaTimes = getMetaTimes;
    }

    public Long getViewNums() {
        return viewNums;
    }

    public void setViewNums(Long viewNums) {
        this.viewNums = viewNums;
    }

    public Long getLockTime() {
        return lockTime;
    }

    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }

    public static final String CONTRACT_ID = "`contract_id`";

    public static final String ADDRESS = "`address`";

    public static final String CATEGORY_ID = "`category_id`";

    public static final String IMG_URL = "`img_url`";

    public static final String STORAGE_ID = "`storage_id`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String QUANTITY = "`quantity`";

    public static final String LOCKED = "`locked`";

    public static final String LOCKED_CONTENT = "`locked_content`";

    public static final String NAME = "`name`";

    public static final String DESCRIPTION = "`description`";

    public static final String ROYALTIES = "`royalties`";

    public static final String PROPERTIES = "`properties`";

    public static final String NFT_VERIFY = "`nft_verify`";

    public static final String IS_SYNC = "`is_sync`";

    public static final String TYPE = "`type`";

    public static final String CREATOR = "`creator`";

    public static final String TX_HASH = "`tx_hash`";

    public static final String ANIM_URL = "`anim_url`";

    public static final String ANIM_STORAGE_ID = "`anim_storage_id`";

    public static final String METADATA_URL = "`metadata_url`";

    public static final String METADATA_CONTENT = "`metadata_content`";

    public static final String GET_META_TIMES = "`get_meta_times`";

    public static final String VIEW_NUMS = "`view_nums`";

    public static final String LOCK_TIME = "`lock_time`";

    @Override
    public String toString() {
        return "FcContractNft{" +
        "contractId=" + contractId +
        ", address=" + address +
        ", categoryId=" + categoryId +
        ", imgUrl=" + imgUrl +
        ", storageId=" + storageId +
        ", tokenId=" + tokenId +
        ", quantity=" + quantity +
        ", locked=" + locked +
        ", lockedContent=" + lockedContent +
        ", name=" + name +
        ", description=" + description +
        ", royalties=" + royalties +
        ", properties=" + properties +
        ", nftVerify=" + nftVerify +
        ", isSync=" + isSync +
        ", type=" + type +
        ", creator=" + creator +
        ", txHash=" + txHash +
        ", animUrl=" + animUrl +
        ", animStorageId=" + animStorageId +
        ", metadataUrl=" + metadataUrl +
        ", metadataContent=" + metadataContent +
        ", getMetaTimes=" + getMetaTimes +
        ", viewNums=" + viewNums +
        ", lockTime=" + lockTime +
        "}";
    }
}
