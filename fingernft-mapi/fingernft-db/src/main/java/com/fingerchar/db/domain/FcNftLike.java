package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcNftLike
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_nft_like")
public class FcNftLike extends BaseEntity {


    /**
     * nft id
     */
    @TableField("`nft_id`")
    private Long nftId;

    @TableField("`address`")
    private String address;

    @TableField("`token_id`")
    private String tokenId;

    /**
     * 用户地址
     */
    @TableField("`user_address`")
    private String userAddress;


    public Long getNftId() {
        return nftId;
    }

    public void setNftId(Long nftId) {
        this.nftId = nftId;
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

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public static final String NFT_ID = "`nft_id`";

    public static final String ADDRESS = "`address`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String USER_ADDRESS = "`user_address`";

    @Override
    public String toString() {
        return "FcNftLike{" +
        "nftId=" + nftId +
        ", address=" + address +
        ", tokenId=" + tokenId +
        ", userAddress=" + userAddress +
        "}";
    }
}
