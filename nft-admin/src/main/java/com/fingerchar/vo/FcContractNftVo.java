package com.fingerchar.vo;

public class FcContractNftVo {
    private Long id;

    private Long createTime;

    private Boolean deleted;

    private Long updateTime;

    /**
     * 合约id
     */
    private Long contractId;

    /**
     * 合约地址
     */
    private String address;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 图片地址
     */
    private String imgUrl;

    /**
     * 图片保存Id
     */
    private Long storageId;

    /**
     * 对应tokenId
     */
    private String tokenId;

    /**
     * 数量
     */
    private Long quantity;

    /**
     * 是否锁定
     */
    private Boolean locked;

    /**
     * 锁定描述
     */
    private String lockedContent;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 版权
     */
    private String royalties;

    /**
     * 属性
     */
    private String properties;

    /**
     * 是否已验证
     */
    private Integer nftVerify;

    /**
     * 是否已同步链
     */
    private Boolean isSync;

    /**
     * 类型
     */
    private Integer type;

    /**
     * nft拥有者
     */
    private String creator;

    /**
     * 区块链交易hash值
     */
    private String txHash;

    /**
     * 视频音频地址
     */
    private String animUrl;

    /**
     * 视频音频地址id
     */
    private Long animStorageId;

    /**
     * 资源地址
     */
    private String metadataUrl;

    /**
     * 资源内容
     */
    private String metadataContent;

    /**
     * 售卖数量
     */
    private Long sellQuantity;

    /*
    * 分类名称
    * */
    private String categoryName;

    /**
     * 价格
     */
    private String price;

    public FcContractNftVo() {
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getSync() {
        return isSync;
    }

    public void setSync(Boolean sync) {
        isSync = sync;
    }

    public Long getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(Long sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    @Override
    public String toString() {
        return "FcContractNftVo{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", deleted=" + deleted +
                ", updateTime=" + updateTime +
                ", contractId=" + contractId +
                ", address='" + address + '\'' +
                ", categoryId=" + categoryId +
                ", imgUrl='" + imgUrl + '\'' +
                ", storageId=" + storageId +
                ", tokenId=" + tokenId +
                ", quantity=" + quantity +
                ", locked=" + locked +
                ", lockedContent='" + lockedContent + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", royalties='" + royalties + '\'' +
                ", properties='" + properties + '\'' +
                ", nftVerify=" + nftVerify +
                ", isSync=" + isSync +
                ", type=" + type +
                ", creator='" + creator + '\'' +
                ", txHash='" + txHash + '\'' +
                ", animUrl='" + animUrl + '\'' +
                ", animStorageId=" + animStorageId +
                ", sellQuantity=" + sellQuantity +
                ", categoryName='" + categoryName + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
