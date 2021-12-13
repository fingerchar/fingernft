package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcOrderCache
 * @Author 
 * @Date 2021-12-12
 * @Version 2.1
 */
@TableName("`fc_order_cache`")
public class FcOrderCache extends BaseEntity {


    @TableField("`key`")
    private String key;

    @TableField("`value`")
    private String value;

    @TableField("`expired_time`")
    private Long expiredTime;


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

    public Long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Long expiredTime) {
        this.expiredTime = expiredTime;
    }

    public static final String KEY = "`key`";

    public static final String VALUE = "`value`";

    public static final String EXPIRED_TIME = "`expired_time`";

    @Override
    public String toString() {
        return "FcOrderCache{" +
        "key=" + key +
        ", value=" + value +
        ", expiredTime=" + expiredTime +
        "}";
    }
}
