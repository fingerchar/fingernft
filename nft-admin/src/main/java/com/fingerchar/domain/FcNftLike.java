package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcNftLike
 * @Author 
 * @Date 2021-12-11
 * @Version 2.1
 */
@TableName("`fc_nft_like`")
public class FcNftLike extends BaseEntity {


    /**
     * nft id
     */
    @TableField("`nft_id`")
    private Long nftId;

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

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public static final String NFT_ID = "`nft_id`";

    public static final String USER_ADDRESS = "`user_address`";

    @Override
    public String toString() {
        return "FcNftLike{" +
        "nftId=" + nftId +
        ", userAddress=" + userAddress +
        "}";
    }
}
