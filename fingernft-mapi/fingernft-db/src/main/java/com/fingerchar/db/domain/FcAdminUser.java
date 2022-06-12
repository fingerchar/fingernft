package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAdminUser
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_admin_user")
public class FcAdminUser extends BaseEntity {


    /**
     * 头像图片
     */
    @TableField("`avatar`")
    private String avatar;

    /**
     * 管理员名称
     */
    @TableField("`username`")
    private String username;

    /**
     * 昵称
     */
    @TableField("`nickname`")
    private String nickname;

    /**
     * 管理员密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 最近一次登录IP地址
     */
    @TableField("`last_login_ip`")
    private String lastLoginIp;

    @TableField("`last_login_time`")
    private Long lastLoginTime;

    /**
     * 角色列表
     */
    @TableField("`role_ids`")
    private String roleIds;

    /**
     * 手机号码
     */
    @TableField("`phone`")
    private String phone;

    /**
     * 部门id
     */
    @TableField("`department_id`")
    private Long departmentId;

    /**
     * 对应job表中职位
     */
    @TableField("`job_id`")
    private Long jobId;

    /**
     * 微信号
     */
    @TableField("`wechat`")
    private String wechat;

    /**
     * 微信二维码
     */
    @TableField("`wechat_qrcode`")
    private String wechatQrcode;

    /**
     * 是否禁用：0否，1是
     */
    @TableField("`status`")
    private Boolean status;


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getWechatQrcode() {
        return wechatQrcode;
    }

    public void setWechatQrcode(String wechatQrcode) {
        this.wechatQrcode = wechatQrcode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public static final String AVATAR = "`avatar`";

    public static final String USERNAME = "`username`";

    public static final String NICKNAME = "`nickname`";

    public static final String PASSWORD = "`password`";

    public static final String LAST_LOGIN_IP = "`last_login_ip`";

    public static final String LAST_LOGIN_TIME = "`last_login_time`";

    public static final String ROLE_IDS = "`role_ids`";

    public static final String PHONE = "`phone`";

    public static final String DEPARTMENT_ID = "`department_id`";

    public static final String JOB_ID = "`job_id`";

    public static final String WECHAT = "`wechat`";

    public static final String WECHAT_QRCODE = "`wechat_qrcode`";

    public static final String STATUS = "`status`";

    @Override
    public String toString() {
        return "FcAdminUser{" +
        "avatar=" + avatar +
        ", username=" + username +
        ", nickname=" + nickname +
        ", password=" + password +
        ", lastLoginIp=" + lastLoginIp +
        ", lastLoginTime=" + lastLoginTime +
        ", roleIds=" + roleIds +
        ", phone=" + phone +
        ", departmentId=" + departmentId +
        ", jobId=" + jobId +
        ", wechat=" + wechat +
        ", wechatQrcode=" + wechatQrcode +
        ", status=" + status +
        "}";
    }
}
