package com.fingerchar.db.dto;

/**
 * @Author： Zjm
 * @Date：2022/3/30 19:20
 */
public class ConfigDeploy {
    private String minerKey;
    private String buyerFeeKey;
    private String contractName;

    private String contractSymbol;

    public String getBuyerFeeAddress() {
        return buyerFeeAddress;
    }

    public void setBuyerFeeAddress(String buyerFeeAddress) {
        this.buyerFeeAddress = buyerFeeAddress;
    }

    public String getMinerAddress() {
        return minerAddress;
    }

    public void setMinerAddress(String minerAddress) {
        this.minerAddress = minerAddress;
    }

    private String buyerFeeAddress;
    private String minerAddress;

    public String getContractSymbol() {
        return contractSymbol;
    }

    public void setContractSymbol(String contractSymbol) {
        this.contractSymbol = contractSymbol;
    }

    private String beneficiary;


    public String getMinerKey() {
        return minerKey;
    }

    public void setMinerKey(String minerKey) {
        this.minerKey = minerKey;
    }

    public String getBuyerFeeKey() {
        return buyerFeeKey;
    }

    public void setBuyerFeeKey(String buyerFeeKey) {
        this.buyerFeeKey = buyerFeeKey;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getBeneficiary() {
        return beneficiary;
    }

    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }



}
