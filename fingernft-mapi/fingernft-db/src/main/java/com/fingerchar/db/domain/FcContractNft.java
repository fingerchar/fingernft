package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcContractNft
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_contract_nft")
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

    @TableField("`name`")
    private String name;

    @TableField("`description`")
    private String description;

    /**
     * 分类id
     */
    @TableField("`category_id`")
    private Long categoryId;

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
     * 版权
     */
    @TableField("`royalties`")
    private String royalties;

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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
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

    public String getRoyalties() {
        return royalties;
    }

    public void setRoyalties(String royalties) {
        this.royalties = royalties;
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

    public static final String CONTRACT_ID = "`contract_id`";

    public static final String ADDRESS = "`address`";

    public static final String NAME = "`name`";

    public static final String DESCRIPTION = "`description`";

    public static final String CATEGORY_ID = "`category_id`";

    public static final String STORAGE_ID = "`storage_id`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String ROYALTIES = "`royalties`";

    public static final String NFT_VERIFY = "`nft_verify`";

    public static final String IS_SYNC = "`is_sync`";

    public static final String CREATOR = "`creator`";

    public static final String TX_HASH = "`tx_hash`";

    public static final String METADATA_URL = "`metadata_url`";

    public static final String METADATA_CONTENT = "`metadata_content`";

    public static final String GET_META_TIMES = "`get_meta_times`";

    @Override
    public String toString() {
        return "FcContractNft{" +
        "contractId=" + contractId +
        ", address=" + address +
        ", name=" + name +
        ", description=" + description +
        ", categoryId=" + categoryId +
        ", storageId=" + storageId +
        ", tokenId=" + tokenId +
        ", royalties=" + royalties +
        ", nftVerify=" + nftVerify +
        ", isSync=" + isSync +
        ", creator=" + creator +
        ", txHash=" + txHash +
        ", metadataUrl=" + metadataUrl +
        ", metadataContent=" + metadataContent +
        ", getMetaTimes=" + getMetaTimes +
        "}";
    }
}
