package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcTxOrder
 * @Author 
 * @Date 2021-12-11
 * @Version 2.1
 */
@TableName("`fc_tx_order`")
public class FcTxOrder extends BaseEntity {


    /**
     * 交易hash
     */
    @TableField("`tx_hash`")
    private String txHash;

    /**
     * 是否已经同步
     */
    @TableField("`is_sync`")
    private Boolean isSync;


    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Boolean getIsSync() {
        return isSync;
    }

    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    public static final String TX_HASH = "`tx_hash`";

    public static final String IS_SYNC = "`is_sync`";

    @Override
    public String toString() {
        return "FcTxOrder{" +
        "txHash=" + txHash +
        ", isSync=" + isSync +
        "}";
    }
}
