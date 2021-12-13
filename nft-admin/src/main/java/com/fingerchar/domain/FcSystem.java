package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcSystem
 * @Author 
 * @Date 2021-12-12
 * @Version 2.1
 */
@TableName("`fc_system`")
public class FcSystem extends BaseEntity {


    /**
     * 系统配置名
     */
    @TableField("`key_name`")
    private String keyName;

    /**
     * 系统配置值
     */
    @TableField("`key_value`")
    private String keyValue;

    /**
     * 是否在前台显示
     */
    @TableField("`show`")
    private Boolean show;


    public String getKeyName() {
        return keyName;
    }

    public void setKeyName(String keyName) {
        this.keyName = keyName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public Boolean getShow() {
        return show;
    }

    public void setShow(Boolean show) {
        this.show = show;
    }

    public static final String KEY_NAME = "`key_name`";

    public static final String KEY_VALUE = "`key_value`";

    public static final String SHOW = "`show`";

    @Override
    public String toString() {
        return "FcSystem{" +
        "keyName=" + keyName +
        ", keyValue=" + keyValue +
        ", show=" + show +
        "}";
    }
}
