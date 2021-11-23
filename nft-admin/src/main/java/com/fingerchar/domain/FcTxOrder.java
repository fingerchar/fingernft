package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcTxOrder
 * @Author 
 * @Date 2021-11-16
 * @Version 2.1
 */
@TableName("`fc_tx_order`")
public class FcTxOrder extends BaseEntity {


    /**
     * 订单id
     */
    @TableField("`order_id`")
    private Long orderId;

    /**
     * 交易hash
     */
    @TableField("`tx_hash`")
    private String txHash;

    /**
     * 卖方
     */
    @TableField("`from`")
    private String from;

    /**
     * 买方
     */
    @TableField("`to`")
    private String to;

    /**
     * token地址
     */
    @TableField("`token`")
    private String token;

    /**
     * tokenid
     */
    @TableField("`token_id`")
    private Long tokenId;

    /**
     * 交易类型：sale_cancel, sale_buy, bid_cancel, bid_accept, erc721_create, erc1155_create, token_tranfer, token_burn
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 交易序列号
     */
    @TableField("`nonce`")
    private Long nonce;

    /**
     * 交易输入信息
     */
    @TableField("`input`")
    private String input;

    /**
     * 盐
     */
    @TableField("`salt`")
    private String salt;

    /**
     * 是否已经同步
     */
    @TableField("`is_sync`")
    private Boolean isSync;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getNonce() {
        return nonce;
    }

    public void setNonce(Long nonce) {
        this.nonce = nonce;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public static final String ORDER_ID = "`order_id`";

    public static final String TX_HASH = "`tx_hash`";

    public static final String FROM = "`from`";

    public static final String TO = "`to`";

    public static final String TOKEN = "`token`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String TYPE = "`type`";

    public static final String NONCE = "`nonce`";

    public static final String INPUT = "`input`";

    public static final String SALT = "`salt`";

    public static final String IS_SYNC = "`is_sync`";

    @Override
    public String toString() {
        return "FcTxOrder{" +
        "orderId=" + orderId +
        ", txHash=" + txHash +
        ", from=" + from +
        ", to=" + to +
        ", token=" + token +
        ", tokenId=" + tokenId +
        ", type=" + type +
        ", nonce=" + nonce +
        ", input=" + input +
        ", salt=" + salt +
        ", isSync=" + isSync +
        "}";
    }
}
