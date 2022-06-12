package com.fingerchar.db.dto;

import java.math.BigInteger;

/**
 * @Author： Zjm
 * @Date：2022/3/23 18:46
 */
public class TransferLog {
    private String address;
    private BigInteger tokenId;
    private String from;
    private String to;

    private String txHash;

    private BigInteger blockTimestamp;

    private Long blockNumber;
    public TransferLog(String address, BigInteger tokenId, String from, String to, String txHash, BigInteger blockTimestamp, Long blockNumber){
        this.address = address;
        this.tokenId = tokenId;
        this.from = from;
        this.to = to;
        this.txHash = txHash;
        this.blockTimestamp = blockTimestamp;
        this.blockNumber = blockNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getTokenId() {
        return tokenId;
    }

    public void setTokenId(BigInteger tokenId) {
        this.tokenId = tokenId;
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
    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public BigInteger getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(BigInteger blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

    public Long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(Long blockNumber) {
        this.blockNumber = blockNumber;
    }

}
