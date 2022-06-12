package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAdminDepartment
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_admin_department")
public class FcAdminDepartment extends BaseEntity {


    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 图标地址
     */
    @TableField("`icon`")
    private String icon;

    /**
     * 描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 排序
     */
    @TableField("`sort`")
    private Integer sort;

    /**
     * 状态: 0：停用，1：启用(默认为1)
     */
    @TableField("`status`")
    private Integer status;

    @TableField("`create_user`")
    private Long createUser;

    @TableField("`update_user`")
    private Long updateUser;

    @TableField("`delete_user`")
    private Long deleteUser;

    @TableField("`delete_time`")
    private Long deleteTime;

    @TableField("`parent`")
    private Long parent;

    @TableField("`code`")
    private String code;

    /**
     * 职位ID
     */
    @TableField("`job_id`")
    private Long jobId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Long getDeleteUser() {
        return deleteUser;
    }

    public void setDeleteUser(Long deleteUser) {
        this.deleteUser = deleteUser;
    }

    public Long getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Long deleteTime) {
        this.deleteTime = deleteTime;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public static final String NAME = "`name`";

    public static final String ICON = "`icon`";

    public static final String DESC = "`desc`";

    public static final String SORT = "`sort`";

    public static final String STATUS = "`status`";

    public static final String CREATE_USER = "`create_user`";

    public static final String UPDATE_USER = "`update_user`";

    public static final String DELETE_USER = "`delete_user`";

    public static final String DELETE_TIME = "`delete_time`";

    public static final String PARENT = "`parent`";

    public static final String CODE = "`code`";

    public static final String JOB_ID = "`job_id`";

    @Override
    public String toString() {
        return "FcAdminDepartment{" +
        "name=" + name +
        ", icon=" + icon +
        ", desc=" + desc +
        ", sort=" + sort +
        ", status=" + status +
        ", createUser=" + createUser +
        ", updateUser=" + updateUser +
        ", deleteUser=" + deleteUser +
        ", deleteTime=" + deleteTime +
        ", parent=" + parent +
        ", code=" + code +
        ", jobId=" + jobId +
        "}";
    }
}
