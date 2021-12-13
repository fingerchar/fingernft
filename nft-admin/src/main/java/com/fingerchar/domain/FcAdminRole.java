package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAdminRole
 * @Author 
 * @Date 2021-12-11
 * @Version 2.1
 */
@TableName("`fc_admin_role`")
public class FcAdminRole extends BaseEntity {


    /**
     * 角色名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 角色描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 是否启用
     */
    @TableField("`enabled`")
    private Boolean enabled;

    @TableField("`is_system`")
    private Boolean isSystem;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public static final String NAME = "`name`";

    public static final String DESC = "`desc`";

    public static final String ENABLED = "`enabled`";

    public static final String IS_SYSTEM = "`is_system`";

    @Override
    public String toString() {
        return "FcAdminRole{" +
        "name=" + name +
        ", desc=" + desc +
        ", enabled=" + enabled +
        ", isSystem=" + isSystem +
        "}";
    }
}
