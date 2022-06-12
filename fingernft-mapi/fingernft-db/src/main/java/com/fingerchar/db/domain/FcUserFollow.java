package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcUserFollow
 * @Author 
 * @Date 2022-06-10
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
     * 关注人地址
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
        "userAddress=" + userAddress +
        ", followingAddress=" + followingAddress +
        "}";
    }
}
