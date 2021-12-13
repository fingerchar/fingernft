package com.fingerchar.db.domain;

import java.math.BigDecimal;
import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcNftItems
 * @Author 
 * @Date 2021-12-12
 * @Version 2.1
 */
@TableName("`fc_nft_items`")
public class FcNftItems extends BaseEntity {


    /**
     * nft  id
     */
    @TableField("`nft_id`")
    private Long nftId;

    @TableField("`name`")
    private String name;

    @TableField("`description`")
    private String description;

    /**
     * 图片资源地址
     */
    @TableField("`img_url`")
    private String imgUrl;

    /**
     * 图片资源保存id
     */
    @TableField("`storage_id`")
    private Long storageId;

    /**
     * 价格
     */
    @TableField("`price`")
    private String price;

    /**
     * usdt价格
     */
    @TableField("`usdt_price`")
    private BigDecimal usdtPrice;

    /**
     * 支付方式地址
     */
    @TableField("`paytoken_address`")
    private String paytokenAddress;

    /**
     * 售卖数量
     */
    @TableField("`sell_quantity`")
    private Long sellQuantity;

    /**
     * 数量
     */
    @TableField("`quantity`")
    private Long quantity;

    /**
     * 签名
     */
    @TableField("`signature`")
    private String signature;

    /**
     * token拥有者
     */
    @TableField("`item_owner`")
    private String itemOwner;

    /**
     * 是否在售
     */
    @TableField("`onsell`")
    private Boolean onsell;

    @TableField("`category_id`")
    private Long categoryId;

    @TableField("`address`")
    private String address;

    @TableField("`is_sync`")
    private Boolean isSync;

    @TableField("`token_id`")
    private String tokenId;


    public Long getNftId() {
        return nftId;
    }

    public void setNftId(Long nftId) {
        this.nftId = nftId;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public BigDecimal getUsdtPrice() {
        return usdtPrice;
    }

    public void setUsdtPrice(BigDecimal usdtPrice) {
        this.usdtPrice = usdtPrice;
    }

    public String getPaytokenAddress() {
        return paytokenAddress;
    }

    public void setPaytokenAddress(String paytokenAddress) {
        this.paytokenAddress = paytokenAddress;
    }

    public Long getSellQuantity() {
        return sellQuantity;
    }

    public void setSellQuantity(Long sellQuantity) {
        this.sellQuantity = sellQuantity;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getItemOwner() {
        return itemOwner;
    }

    public void setItemOwner(String itemOwner) {
        this.itemOwner = itemOwner;
    }

    public Boolean getOnsell() {
        return onsell;
    }

    public void setOnsell(Boolean onsell) {
        this.onsell = onsell;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public static final String NFT_ID = "`nft_id`";

    public static final String NAME = "`name`";

    public static final String DESCRIPTION = "`description`";

    public static final String IMG_URL = "`img_url`";

    public static final String STORAGE_ID = "`storage_id`";

    public static final String PRICE = "`price`";

    public static final String USDT_PRICE = "`usdt_price`";

    public static final String PAYTOKEN_ADDRESS = "`paytoken_address`";

    public static final String SELL_QUANTITY = "`sell_quantity`";

    public static final String QUANTITY = "`quantity`";

    public static final String SIGNATURE = "`signature`";

    public static final String ITEM_OWNER = "`item_owner`";

    public static final String ONSELL = "`onsell`";

    public static final String CATEGORY_ID = "`category_id`";

    public static final String ADDRESS = "`address`";

    public static final String IS_SYNC = "`is_sync`";

    public static final String TOKEN_ID = "`token_id`";

    @Override
    public String toString() {
        return "FcNftItems{" +
        "nftId=" + nftId +
        ", name=" + name +
        ", description=" + description +
        ", imgUrl=" + imgUrl +
        ", storageId=" + storageId +
        ", price=" + price +
        ", usdtPrice=" + usdtPrice +
        ", paytokenAddress=" + paytokenAddress +
        ", sellQuantity=" + sellQuantity +
        ", quantity=" + quantity +
        ", signature=" + signature +
        ", itemOwner=" + itemOwner +
        ", onsell=" + onsell +
        ", categoryId=" + categoryId +
        ", address=" + address +
        ", isSync=" + isSync +
        ", tokenId=" + tokenId +
        "}";
    }
}
