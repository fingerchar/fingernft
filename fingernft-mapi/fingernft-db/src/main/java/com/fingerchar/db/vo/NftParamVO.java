package com.fingerchar.db.vo;

import com.fingerchar.db.domain.FcContractNft;

/**
 * @author chenrk
 *
 */
public class NftParamVO {

	private String address;
	
	private String tokenId;

	public NftParamVO(FcContractNft contractNft){
		this.address = contractNft.getAddress();
		this.tokenId = contractNft.getTokenId();
	}

	public NftParamVO(String address ,  String tokenId) {
		this.address = address;
		this.tokenId = tokenId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
}
