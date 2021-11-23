package com.fingerchar.dto;

import java.math.BigInteger;

/*
 *
 * @author zjm
 * */
public class SellAssets {
	
    private String token;
    
    private BigInteger tokenId;
    
    private BigInteger assetType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigInteger getTokenId() {
        return tokenId;
    }

    public void setTokenId(BigInteger tokenId) {
        this.tokenId = tokenId;
    }

    public BigInteger getAssetType() {
        return assetType;
    }

    public void setAssetType(BigInteger assetType) {
        this.assetType = assetType;
    }

    @Override
    public String toString() {
        return "SellAssets{" +
                "token='" + token + '\'' +
                ", tokenId=" + tokenId +
                ", assetType=" + assetType +
                '}';
    }
}
