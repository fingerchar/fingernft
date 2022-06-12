package com.fingerchar.api.utils.encryvo;

import java.math.BigInteger;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.generated.Uint8;

public class Assets extends StaticStruct {

	private String token;
	private BigInteger tokenId;
	private BigInteger assetType;
	
	public Assets(String token, BigInteger tokenId, BigInteger assetType) {
    	super(new Address(token), new Uint(tokenId), new Uint8(assetType));
    	this.token = token;
    	this.tokenId = tokenId;
    	this.assetType = assetType;
    }
    
    public Assets(Address token, Uint tokenId, Uint8 assetType) {
    	super(tokenId, tokenId, assetType);
    	this.token = token.getValue();
    	this.tokenId = tokenId.getValue();
    	this.assetType = assetType.getValue();
    }

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
    
    
}
