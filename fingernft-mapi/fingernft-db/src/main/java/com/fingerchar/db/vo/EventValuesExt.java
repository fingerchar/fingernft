package com.fingerchar.db.vo;

import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Type;

import java.math.BigInteger;
import java.util.List;

public class EventValuesExt{

    @SuppressWarnings("rawtypes")
    private List<Type> indexedValues;

    @SuppressWarnings("rawtypes")
    private List<Type> nonIndexedValues;

    private String txHash;

    private String address;

    private BigInteger blockNumber;

    private BigInteger blockTimestamp;

    public EventValuesExt(EventValues eventValues, String txHash, String address, BigInteger blockNumber, BigInteger blockTimestamp) {
        this.indexedValues = eventValues.getIndexedValues();
        this.nonIndexedValues = eventValues.getNonIndexedValues();
        this.txHash = txHash;
        this.address = address;
        this.blockNumber = blockNumber;
        this.blockTimestamp = blockTimestamp;
    }

    @SuppressWarnings("rawtypes")
    public List<Type> getIndexedValues() {
        return indexedValues;
    }

    @SuppressWarnings("rawtypes")
    public List<Type> getNonIndexedValues() {
        return nonIndexedValues;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(BigInteger blockNumber) {
        this.blockNumber = blockNumber;
    }

    public BigInteger getBlockTimestamp() {
        return blockTimestamp;
    }

    public void setBlockTimestamp(BigInteger blockTimestamp) {
        this.blockTimestamp = blockTimestamp;
    }

}
