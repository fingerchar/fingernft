package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcTxOrder
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_tx_order")
public class FcTxOrder extends BaseEntity {


    /**
     * 交易hash
     */
    @TableField("`tx_hash`")
    private String txHash;

    @TableField("`block_number`")
    private Integer blockNumber;

    @TableField("`block_timestamp`")
    private Integer blockTimestamp;


    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Integer getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }

    public Integer getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(Integer blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public static final String TX_HASH = "`tx_hash`";

    public static final String BLOCK_NUMBER = "`block_number`";

    public static final String BLOCK_TIMESTAMP = "`block_timestamp`";

    @Override
    public String toString() {
        return "FcTxOrder{" +
        "txHash=" + txHash +
        ", blockNumber=" + blockNumber +
        ", blockTimestamp=" + blockTimestamp +
        "}";
    }
}
