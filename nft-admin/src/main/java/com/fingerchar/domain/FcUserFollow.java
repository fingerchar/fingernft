package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;

/**
 * @Description FcUserFollow
 * @Author 
 * @Date 2021-11-21
 * @Version 2.1
 */
@TableName("fc_user_follow")
public class FcUserFollow extends BaseEntity {

    /**
     * 用户地址
     */
    @TableField("`user_address`")
    private String userAddress;

    /**
     * 关注人address
     */
    @TableField("`following_address`")
    private String followingAddress;

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getFollowingAddress() {
        return followingAddress;
    }

    public void setFollowingAddress(String followingAddress) {
        this.followingAddress = followingAddress;
    }

    public static final String USER_ADDRESS = "`user_address`";

    public static final String FOLLOWING_ADDRESS = "`following_address`";

    @Override
    public String toString() {
        return "FcUserFollow{" +
        ", userAddress=" + userAddress +
        ", followingAddress=" + followingAddress +
        "}";
    }
}
