package com.fingerchar.db.vo;

/**
 * @Author： Zjm
 * @Date：2022/3/30 16:06
 */
public class TxOrderInfo {
    private Integer lastBlockNumber;
    private Integer earlyBlockNumber;
    private Integer txAmount;


    public Integer getLastBlockNumber() {
        return lastBlockNumber;
    }

    public void setLastBlockNumber(Integer lastBlockNumber) {
        this.lastBlockNumber = lastBlockNumber;
    }

    public Integer getEarlyBlockNumber() {
        return earlyBlockNumber;
    }

    public void setEarlyBlockNumber(Integer earlyBlockNumber) {
        this.earlyBlockNumber = earlyBlockNumber;
    }

    public Integer getTxAmount() {
        return txAmount;
    }

    public void setTxAmount(Integer txAmount) {
        this.txAmount = txAmount;
    }

}
