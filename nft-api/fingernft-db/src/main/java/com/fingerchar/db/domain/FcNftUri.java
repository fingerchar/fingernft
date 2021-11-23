package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcNftUri
 * @Author 
 * @Date 2021-11-21
 * @Version 2.1
 */
@TableName("fc_nft_uri")
public class FcNftUri extends BaseEntity {


    /**
     * 合约地址
     */
    @TableField("`token`")
    private String token;

    /**
     * 合约token id
     */
    @TableField("`token_id`")
    private String tokenId;

    /**
     * 合约uri
     */
    @TableField("`uri`")
    private String uri;

    /**
     * 类型
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 执行次数
     */
    @TableField("`times`")
    private Integer times;

    /**
     * 执行状态
     */
    @TableField("`status`")
    private Boolean status;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static final String TOKEN = "`token`";

    public static final String TOKEN_ID = "`token_id`";

    public static final String URI = "`uri`";

    public static final String TYPE = "`type`";

    public static final String TIMES = "`times`";

    public static final String STATUS = "`status`";

    @Override
    public String toString() {
        return "FcNftUri{" +
        "token=" + token +
        ", tokenId=" + tokenId +
        ", uri=" + uri +
        ", type=" + type +
        ", times=" + times +
        ", status=" + status +
        "}";
    }
}
