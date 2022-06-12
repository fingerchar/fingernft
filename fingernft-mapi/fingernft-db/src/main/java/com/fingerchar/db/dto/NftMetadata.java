package com.fingerchar.db.dto;

/**
 * @Author： Zjm
 * @Date：2022/3/27 15:53
 */
public class NftMetadata {

    private String address;

    private String tokenId;

    private String name;

    private String description;

    private Long storageId;

    private String animUrl;

    private Long animStorageId;

    private String properties;

    private String metadataUrl;

    private String metadataContent;

    public NftMetadata(NftInfo nftInfo){
        this.address = nftInfo.getAddress();
        this.tokenId = nftInfo.getTokenId();
        this.name = nftInfo.getName();
        this.description = nftInfo.getDescription();
        this.storageId = nftInfo.getStorageId();
        this.animUrl = nftInfo.getAnimUrl();
        this.animStorageId = nftInfo.getAnimStorageId();
        this.properties = nftInfo.getProperties();
        this.metadataUrl = nftInfo.getMetadataUrl();
        this.metadataContent = nftInfo.getMetadataContent();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStorageId() {
        return storageId;
    }

    public void setStorageId(Long storageId) {
        this.storageId = storageId;
    }

    public String getAnimUrl() {
        return animUrl;
    }

    public void setAnimUrl(String animUrl) {
        this.animUrl = animUrl;
    }

    public Long getAnimStorageId() {
        return animStorageId;
    }

    public void setAnimStorageId(Long animStorageId) {
        this.animStorageId = animStorageId;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
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
