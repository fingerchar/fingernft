package com.fingerchar.db.vo;

import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcUser;

import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/3/25 14:28
 */
public class NftInfoVo {
    private String address;

    private String tokenId;

    private Long categoryId;

    private String royalties;

    private Integer nftVerify;

    private String creator;

    private UserBaseInfoVo user;

    public UserBaseInfoVo getUser() {
        return user;
    }

    public void setUser(UserBaseInfoVo user) {
        this.user = user;
    }

    private String txHash;

    private String metadataUrl;

    private String metadataContent;

    private List<NftItemInfoVo> items;


    public NftInfoVo(FcContractNft contractNft, List<NftItemInfoVo> items) {
        this.address = contractNft.getAddress();
        this.categoryId = contractNft.getCategoryId();
        this.tokenId = contractNft.getTokenId();
        this.royalties = contractNft.getRoyalties();
        this.nftVerify = contractNft.getNftVerify();
        this.creator = contractNft.getCreator();
        this.txHash = contractNft.getTxHash();
        this.metadataContent = contractNft.getMetadataContent();
        this.metadataUrl = contractNft.getMetadataUrl();
        this.items = items;
    }


    public NftInfoVo(FcContractNft contractNft, FcUser creator, List<NftItemInfoVo> items) {
        this.address = contractNft.getAddress();
        this.categoryId = contractNft.getCategoryId();
        this.tokenId = contractNft.getTokenId();
        this.royalties = contractNft.getRoyalties();
        this.nftVerify = contractNft.getNftVerify();
        this.creator = contractNft.getCreator();
        this.user = new UserBaseInfoVo(creator);
        this.txHash = contractNft.getTxHash();
        this.metadataContent = contractNft.getMetadataContent();
        this.metadataUrl = contractNft.getMetadataUrl();
        this.items = items;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getRoyalties() {
        return royalties;
    }

    public void setRoyalties(String royalties) {
        this.royalties = royalties;
    }

    public Integer getNftVerify() {
        return nftVerify;
    }

    public void setNftVerify(Integer nftVerify) {
        this.nftVerify = nftVerify;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
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

    public List<NftItemInfoVo> getItems() {
        return items;
    }

    public void setItems(List<NftItemInfoVo> items) {
        this.items = items;
    }

}
