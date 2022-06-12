package com.fingerchar.db.vo;

import com.fingerchar.db.dto.ConfigContract;
import com.fingerchar.db.dto.ConfigNetwork;

/**
 * @Author： Zjm
 * @Date：2022/3/31 9:04
 */
public class ConfigFetchVo {
    private String ipfsUrl;
    private String sellerFee;
    private String buyerFee;
    private String cdnUrl;

    private String miner;

    private String loginMessage;

    private String website;

    ConfigNetwork network;
    ConfigContract contract;

    public String getIpfsUrl() {
        return ipfsUrl;
    }

    public void setIpfsUrl(String ipfsUrl) {
        this.ipfsUrl = ipfsUrl;
    }

    public String getSellerFee() {
        return sellerFee;
    }

    public void setSellerFee(String sellerFee) {
        this.sellerFee = sellerFee;
    }

    public String getBuyerFee() {
        return buyerFee;
    }

    public void setBuyerFee(String buyerFee) {
        this.buyerFee = buyerFee;
    }

    public String getCdnUrl() {
        return cdnUrl;
    }

    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    public String getMiner() {
        return miner;
    }

    public void setMiner(String miner) {
        this.miner = miner;
    }

    public String getLoginMessage() {
        return loginMessage;
    }

    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public ConfigNetwork getNetwork() {
        return network;
    }

    public void setNetwork(ConfigNetwork network) {
        this.network = network;
    }

    public ConfigContract getContract() {
        return contract;
    }

    public void setContract(ConfigContract contract) {
        this.contract = contract;
    }
}

