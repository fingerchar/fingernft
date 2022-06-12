package com.fingerchar.db.dto;

/**
 * @Author： Zjm
 * @Date：2022/3/30 9:32
 */
public class SearchNftParamDto {
    private String creator;
    private String owner;
    private String address;
    private String tokenId;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
