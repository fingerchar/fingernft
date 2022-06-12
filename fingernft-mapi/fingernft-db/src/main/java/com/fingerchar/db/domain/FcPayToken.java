package com.fingerchar.db.domain;

import java.math.BigDecimal;
import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcPayToken
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_pay_token")
public class FcPayToken extends BaseEntity {


    /**
     * token名称
     */
    @TableField("`name`")
    private String name;

    /**
     * token symbol
     */
    @TableField("`symbol`")
    private String symbol;

    /**
     * token 精度
     */
    @TableField("`decimals`")
    private Integer decimals;

    /**
     * token 汇率
     */
    @TableField("`rate`")
    private BigDecimal rate;

    /**
     * token 合约地址
     */
    @TableField("`address`")
    private String address;

    /**
     * token 图像
     */
    @TableField("`avatar`")
    private String avatar;

    /**
     * token 图像保存id
     */
    @TableField("`storage_id`")
    private Long storageId;

    /**
     * 0 ETH 1 WETH 2 ERC1155 3 ERC721 4 ERC721Deprecated
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 默认支付类型
     */
    @TableField("`is_default`")
    private Integer isDefault;


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

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public static final String NAME = "`name`";

    public static final String SYMBOL = "`symbol`";

    public static final String DECIMALS = "`decimals`";

    public static final String RATE = "`rate`";

    public static final String ADDRESS = "`address`";

    public static final String AVATAR = "`avatar`";

    public static final String STORAGE_ID = "`storage_id`";

    public static final String TYPE = "`type`";

    public static final String IS_DEFAULT = "`is_default`";

    @Override
    public String toString() {
        return "FcPayToken{" +
        "name=" + name +
        ", symbol=" + symbol +
        ", decimals=" + decimals +
        ", rate=" + rate +
        ", address=" + address +
        ", avatar=" + avatar +
        ", storageId=" + storageId +
        ", type=" + type +
        ", isDefault=" + isDefault +
        "}";
    }
}
