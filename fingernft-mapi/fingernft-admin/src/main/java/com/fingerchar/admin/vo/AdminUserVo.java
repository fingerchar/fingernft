package com.fingerchar.admin.vo;

import com.alibaba.fastjson.JSON;
import com.fingerchar.db.domain.FcAdminUser;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class AdminUserVo {

    private Long id;

    private Long createTime;

    private Boolean deleted;

    private Long updateTime;

    private String avatar;

    private String username;

    private String nickname;

    private String password;

    private String lastLoginIp;

    private Long lastLoginTime;

    private Long[] roleIds;

    private String phone;

    private Long departmentId;

    private Long jobId;

    private String wechat;

    private String wechatQrcode;

    private Boolean status;

    public AdminUserVo(){

    }

    public AdminUserVo(FcAdminUser user) {
        this.avatar = user.getAvatar();
        this.createTime = user.getCreateTime();
        this.deleted = user.getDeleted();
        this.id = user.getId();
        this.departmentId = user.getDepartmentId();
        this.jobId = user.getJobId();
        this.lastLoginIp = user.getLastLoginIp();
        this.lastLoginTime = user.getLastLoginTime();
        this.nickname = user.getNickname();
        this.password = user.getPassword();
        this.phone = user.getPhone();
        this.status = user.getStatus();
        this.updateTime = user.getUpdateTime();
        this.wechat = user.getWechat();
        this.wechatQrcode = user.getWechatQrcode();
        this.username = user.getUsername();
        String roleIds = user.getRoleIds();
        if (StringUtils.isEmpty(roleIds)){
            throw new AuthorizationException("no role.");
        }
        List<Long> temp = JSON.parseArray(roleIds, Long.class);
        this.roleIds = temp.toArray(new Long[temp.size()]);
    }
    
    public FcAdminUser toAdminUser() {
    	FcAdminUser user = new FcAdminUser();
    	if (null != this.getId()){
            user.setId(this.getId());
        }
        user.setAvatar(this.avatar);
        user.setUsername(this.username);
        user.setNickname(this.getNickname());
        user.setPassword(this.password);
        user.setLastLoginIp(this.getLastLoginIp());
        user.setLastLoginTime(this.getLastLoginTime());
        Long[] roleIds = this.getRoleIds();
        if (roleIds == null){
            user.setRoleIds("[]");
        }else {
            String replace = ArrayUtils.toString(roleIds,",");
            user.setRoleIds(replace.replace("{", "[").replace("}","]"));
        }
        user.setPhone(this.getPhone());
        user.setDepartmentId(this.getDepartmentId());
        user.setJobId(this.getJobId());
        user.setWechat(this.getWechat());
        user.setWechatQrcode(this.getWechatQrcode());
        user.setStatus(this.getStatus());
    	return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

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

    public Long[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Long[] roleIds) {
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

    @Override
    public String toString() {
        return "AdminUserVo{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", deleted=" + deleted +
                ", updateTime=" + updateTime +
                ", avatar='" + avatar + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", password='" + password + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", lastLoginTime=" + lastLoginTime +
                ", roleIds=" + Arrays.toString(roleIds) +
                ", phone='" + phone + '\'' +
                ", departmentId=" + departmentId +
                ", jobId=" + jobId +
                ", wechat='" + wechat + '\'' +
                ", wechatQrcode='" + wechatQrcode + '\'' +
                ", status=" + status +
                '}';
    }
}
