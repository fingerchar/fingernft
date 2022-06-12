package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcOrderLog
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_order_log")
public class FcOrderLog extends BaseEntity {


    /**
     * 订单id
     */
    @TableField("`order_id`")
    private Long orderId;

    /**
     * 订单发起人地址
     */
    @TableField("`from`")
    private String from;

    /**
     * 交易对象地址
     */
    @TableField("`to`")
    private String to;

    /**
     * 交易类型sale_cancel, sale_buy, bid_cancel, bid_accept, erc721_create, erc1155_create, token_tranfer, token_burn
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 交易hash
     */
    @TableField("`tx_hash`")
    private String txHash;

    /**
     * 前一个订单日志id
     */
    @TableField("`pre_log_id`")
    private Long preLogId;

    /**
     * 操作NFT的地址
     */
    @TableField("`token`")
    private String token;

    /**
     * 操作NFT的tokenId
     */
    @TableField("`token_id`")
    private String tokenId;

    /**
     * 日志体
     */
    @TableField("`content`")
    private String content;

    /**
     * 是否过期
     */
    @TableField("`expired`")
    private Boolean expired;

    @TableField("`paytoken_address`")
    private String paytokenAddress;

    @TableField("`paytoken_name`")
    private String paytokenName;

    @TableField("`paytoken_symbol`")
    private String paytokenSymbol;

    @TableField("`paytoken_decimals`")
    private Integer paytokenDecimals;


    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Long getPreLogId() {
        return preLogId;
    }

    public void setPreLogId(Long preLogId) {
        this.preLogId = preLogId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public String getPaytokenAddress() {
        return paytokenAddress;
    }

    public void setPaytokenAddress(String paytokenAddress) {
        this.paytokenAddress = paytokenAddress;
    }

    public String getPaytokenName() {
        return paytokenName;
    }

    public void setPaytokenName(String paytokenName) {
        this.paytokenName = paytokenName;
    }

    public String getPaytokenSymbol() {
        return paytokenSymbol;
    }

    public void setPaytokenSymbol(String paytokenSymbol) {
        this.paytokenSymbol = paytokenSymbol;
    }

    public Integer getPaytokenDecimals() {
        return paytokenDecimals;
    }

    public void setPaytokenDecimals(Integer paytokenDecimals) {
        this.paytokenDecimals = paytokenDecimals;
    }

    public static final String ORDER_ID = "`order_id`";

    public static final String FROM = "`from`";

    public static final String TO = "`to`";

    public static final String TYPE = "`type`";

    public static final String TX_HASH = "`tx_hash`";

    public static final String PRE_LOG_ID = "`pre_log_id`";

    public static final String TOKEN = "`token`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String CONTENT = "`content`";

    public static final String EXPIRED = "`expired`";

    public static final String PAYTOKEN_ADDRESS = "`paytoken_address`";

    public static final String PAYTOKEN_NAME = "`paytoken_name`";

    public static final String PAYTOKEN_SYMBOL = "`paytoken_symbol`";

    public static final String PAYTOKEN_DECIMALS = "`paytoken_decimals`";

    @Override
    public String toString() {
        return "FcOrderLog{" +
        "orderId=" + orderId +
        ", from=" + from +
        ", to=" + to +
        ", type=" + type +
        ", txHash=" + txHash +
        ", preLogId=" + preLogId +
        ", token=" + token +
        ", tokenId=" + tokenId +
        ", content=" + content +
        ", expired=" + expired +
        ", paytokenAddress=" + paytokenAddress +
        ", paytokenName=" + paytokenName +
        ", paytokenSymbol=" + paytokenSymbol +
        ", paytokenDecimals=" + paytokenDecimals +
        "}";
    }
}
