package com.fingerchar.db.vo;

public class FcContractNftWithBidVO {

	private Long id;

    private String address;

    private Long categoryId;

    private String imgUrl;

    private String tokenId;

    private Boolean locked;

    private String lockedContent;

    private String name;

    private String description;

    private String royalties;

    private String properties;

    private Integer nftVerify;

    private Boolean isSync;

    private String creator;

    private String txHash;

    private String animUrl;

    private String metadataContent;
    
    private Long bidNums;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public String getLockedContent() {
		return lockedContent;
	}

	public void setLockedContent(String lockedContent) {
		this.lockedContent = lockedContent;
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

	public String getRoyalties() {
		return royalties;
	}

	public void setRoyalties(String royalties) {
		this.royalties = royalties;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public Integer getNftVerify() {
		return nftVerify;
	}

	public void setNftVerify(Integer nftVerify) {
		this.nftVerify = nftVerify;
	}

	public Boolean getIsSync() {
		return isSync;
	}

	public void setIsSync(Boolean isSync) {
		this.isSync = isSync;
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

	public String getAnimUrl() {
		return animUrl;
	}

	public void setAnimUrl(String animUrl) {
		this.animUrl = animUrl;
	}

	public String getMetadataContent() {
		return metadataContent;
	}

	public void setMetadataContent(String metadataContent) {
		this.metadataContent = metadataContent;
	}

	public Long getBidNums() {
		return bidNums;
	}

	public void setBidNums(Long bidNums) {
		this.bidNums = bidNums;
	}
    
    
}
