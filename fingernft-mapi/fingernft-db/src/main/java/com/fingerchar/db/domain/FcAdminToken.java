package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAdminToken
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_admin_token")
public class FcAdminToken extends BaseEntity {


    @TableField("`user_id`")
    private Long userId;

    @TableField("`key`")
    private String key;

    @TableField("`value`")
    private String value;

    @TableField("`minutes`")
    private Integer minutes;

    /**
     * 1 在线：0 下线
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 门店ID
     */
    @TableField("`shop_id`")
    private Integer shopId;

    /**
     * 部门id
     */
    @TableField("`department_id`")
    private Integer departmentId;

    /**
     * 队列ID
     */
    @TableField("`org_id`")
    private Integer orgId;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public static final String USER_ID = "`user_id`";

    public static final String KEY = "`key`";

    public static final String VALUE = "`value`";

    public static final String MINUTES = "`minutes`";

    public static final String STATUS = "`status`";

    public static final String SHOP_ID = "`shop_id`";

    public static final String DEPARTMENT_ID = "`department_id`";

    public static final String ORG_ID = "`org_id`";

    @Override
    public String toString() {
        return "FcAdminToken{" +
        "userId=" + userId +
        ", key=" + key +
        ", value=" + value +
        ", minutes=" + minutes +
        ", status=" + status +
        ", shopId=" + shopId +
        ", departmentId=" + departmentId +
        ", orgId=" + orgId +
        "}";
    }
}
