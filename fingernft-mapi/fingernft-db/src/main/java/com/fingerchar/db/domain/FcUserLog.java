package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcUserLog
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_user_log")
public class FcUserLog extends BaseEntity {


    /**
     * 用户地址
     */
    @TableField("`address`")
    private String address;

    /**
     * 登录类型1：登录，2: 退出
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 用户ip地址
     */
    @TableField("`ip`")
    private String ip;

    /**
     * 操作动作
     */
    @TableField("`action`")
    private String action;

    /**
     * 操作状态
     */
    @TableField("`status`")
    private Boolean status;

    /**
     * 操作结果，或者成功消息，或者失败消息
     */
    @TableField("`result`")
    private String result;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static final String ADDRESS = "`address`";

    public static final String TYPE = "`type`";

    public static final String IP = "`ip`";

    public static final String ACTION = "`action`";

    public static final String STATUS = "`status`";

    public static final String RESULT = "`result`";

    @Override
    public String toString() {
        return "FcUserLog{" +
        "address=" + address +
        ", type=" + type +
        ", ip=" + ip +
        ", action=" + action +
        ", status=" + status +
        ", result=" + result +
        "}";
    }
}
