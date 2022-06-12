package com.fingerchar.db.vo.notice;

import com.fingerchar.db.domain.FcContractNft;

/**
 * @Author： Zjm
 * @Date：2022/4/1 14:39
 */
public class NftInfoVo {
    private String address;

    private String tokenId;

    private String metadataUrl;

    private String metadataContent;

    public NftInfoVo(){

    }

    public NftInfoVo(FcContractNft nft){
        this.address = nft.getAddress();
        this.tokenId = nft.getTokenId();
        this.metadataContent = nft.getMetadataContent();
        this.metadataUrl = nft.getMetadataUrl();
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

    public String getMetadataUrl() {
        return metadataUrl;
    }

    public void setMetadataUrl(String metadataUrl) {
        this.metadataUrl = metadataUrl;
    }

    public String getMetadataContent() {
        return metadataContent;
    }

    public void setMetadataContent(String metadataContent) {
        this.metadataContent = metadataContent;
    }

}
