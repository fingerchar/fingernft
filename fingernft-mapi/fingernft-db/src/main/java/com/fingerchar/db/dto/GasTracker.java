package com.fingerchar.db.dto;

import java.math.BigInteger;

/**
 * @Author： Zjm
 * @Date：2022/4/12 19:27
 */
public class GasTracker {
    private String low;
    private String medium;
    private String high;

    public String getLastBlock() {
        return lastBlock;
    }

    public void setLastBlock(String lastBlock) {
        this.lastBlock = lastBlock;
    }

    private String lastBlock;

    public GasTracker(BigInteger low, BigInteger medium, BigInteger high, BigInteger lastBlock){
        this.low = low.toString();
        this.medium = medium.toString();
        this.high = high.toString();
        this.lastBlock = lastBlock.toString();
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }
}
