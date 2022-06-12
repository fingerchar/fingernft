package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcUserToken
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_user_token")
public class FcUserToken extends BaseEntity {


    /**
     * 用户钱包地址
     */
    @TableField("`user_address`")
    private String userAddress;

    /**
     * 用户登录token
     */
    @TableField("`user_token`")
    private String userToken;


    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public static final String USER_ADDRESS = "`user_address`";

    public static final String USER_TOKEN = "`user_token`";

    @Override
    public String toString() {
        return "FcUserToken{" +
        "userAddress=" + userAddress +
        ", userToken=" + userToken +
        "}";
    }
}
