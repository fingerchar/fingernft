package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcUserDetails
 * @Author 
 * @Date 2021-11-21
 * @Version 2.1
 */
@TableName("fc_user_details")
public class FcUserDetails extends BaseEntity {


    /**
     * 用户id
     */
    @TableField("`user_id`")
    private Long userId;

    /**
     * 用户地址
     */
    @TableField("`user_address`")
    private String userAddress;

    /**
     * 邮箱
     */
    @TableField("`email`")
    private String email;

    /**
     * 推特账号
     */
    @TableField("`twitter`")
    private String twitter;

    /**
     * 微信账号
     */
    @TableField("`wechart`")
    private String wechart;

    /**
     * QQ账号
     */
    @TableField("`qq`")
    private String qq;

    /**
     * 验证类型 1.个人，2公司
     */
    @TableField("`type`")
    private String type;

    /**
     * 公司名称
     */
    @TableField("`company`")
    private String company;

    /**
     * 地址（个人地址或公司地址）
     */
    @TableField("`address`")
    private String address;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getWechart() {
        return wechart;
    }

    public void setWechart(String wechart) {
        this.wechart = wechart;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static final String USER_ID = "`user_id`";

    public static final String USER_ADDRESS = "`user_address`";

    public static final String EMAIL = "`email`";

    public static final String TWITTER = "`twitter`";

    public static final String WECHART = "`wechart`";

    public static final String QQ = "`qq`";

    public static final String TYPE = "`type`";

    public static final String COMPANY = "`company`";

    public static final String ADDRESS = "`address`";

    @Override
    public String toString() {
        return "FcUserDetails{" +
        "userId=" + userId +
        ", userAddress=" + userAddress +
        ", email=" + email +
        ", twitter=" + twitter +
        ", wechart=" + wechart +
        ", qq=" + qq +
        ", type=" + type +
        ", company=" + company +
        ", address=" + address +
        "}";
    }
}
