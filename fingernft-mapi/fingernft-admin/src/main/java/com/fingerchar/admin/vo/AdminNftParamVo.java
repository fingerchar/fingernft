package com.fingerchar.admin.vo;

public class AdminNftParamVo {
    private String categoryId;
    private String creator;
    private String order;
    private String sort;
    private String address;
    private String tokenId;
    private Integer nftVerify;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public Integer getNftVerify() {
        return nftVerify;
    }

    public void setNftVerify(Integer nftVerify) {
        this.nftVerify = nftVerify;
    }
}
