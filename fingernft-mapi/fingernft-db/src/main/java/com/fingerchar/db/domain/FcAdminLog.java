package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAdminLog
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_admin_log")
public class FcAdminLog extends BaseEntity {


    /**
     * 管理员
     */
    @TableField("`admin`")
    private String admin;

    /**
     * 管理员地址
     */
    @TableField("`ip`")
    private String ip;

    /**
     * 操作分类
     */
    @TableField("`type`")
    private Integer type;

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

    /**
     * 补充信息
     */
    @TableField("`comment`")
    private String comment;


    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static final String ADMIN = "`admin`";

    public static final String IP = "`ip`";

    public static final String TYPE = "`type`";

    public static final String ACTION = "`action`";

    public static final String STATUS = "`status`";

    public static final String RESULT = "`result`";

    public static final String COMMENT = "`comment`";

    @Override
    public String toString() {
        return "FcAdminLog{" +
        "admin=" + admin +
        ", ip=" + ip +
        ", type=" + type +
        ", action=" + action +
        ", status=" + status +
        ", result=" + result +
        ", comment=" + comment +
        "}";
    }
}
