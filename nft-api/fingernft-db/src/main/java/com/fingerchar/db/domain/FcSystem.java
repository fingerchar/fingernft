package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcSystem
 * @Author 
 * @Date 2021-11-21
 * @Version 2.1
 */
@TableName("fc_system")
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

    public static final String KEY_NAME = "`key_name`";

    public static final String KEY_VALUE = "`key_value`";

    @Override
    public String toString() {
        return "FcSystem{" +
        "keyName=" + keyName +
        ", keyValue=" + keyValue +
        "}";
    }
}
