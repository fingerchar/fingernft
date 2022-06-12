package com.fingerchar.db.dto;

import java.math.BigInteger;

/**
 * @Author： Zjm
 * @Date：2022/3/23 17:12
 */
public class ExchangeCancelLog {
    private String sellToken;

    private BigInteger sellTokenId;

    private String owner;

    private String buyToken;

    private BigInteger buyTokenId;

    private BigInteger salt;

    private String txHash;

    private BigInteger blockTimestamp;

    public ExchangeCancelLog(
            String sellToken,
            BigInteger sellTokenId,
            String owner,
            String buyToken,
            BigInteger buyTokenId,
            BigInteger salt,
            String txHash,
            BigInteger blockTimestamp
    ){
        this.sellToken = sellToken;
        this.sellTokenId = sellTokenId;
        this.owner = owner;
        this.buyToken = buyToken;
        this.buyTokenId = buyTokenId;
        this.salt = salt;
        this.txHash = txHash;
        this.blockTimestamp = blockTimestamp;
    }

    public String getSellToken() {
        return sellToken;
    }

    public void setSellToken(String sellToken) {
        this.sellToken = sellToken;
    }

    public BigInteger getSellTokenId() {
        return sellTokenId;
    }

    public void setSellTokenId(BigInteger sellTokenId) {
        this.sellTokenId = sellTokenId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getBuyToken() {
        return buyToken;
    }

    public void setBuyToken(String buyToken) {
        this.buyToken = buyToken;
    }

    public BigInteger getBuyTokenId() {
        return buyTokenId;
    }

    public void setBuyTokenId(BigInteger buyTokenId) {
        this.buyTokenId = buyTokenId;
    }

    public BigInteger getSalt() {
        return salt;
    }

    public void setSalt(BigInteger salt) {
        this.salt = salt;
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

}
