package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcAdminStorage
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_admin_storage")
public class FcAdminStorage extends BaseEntity {


    /**
     * 文件的唯一索引
     */
    @TableField("`key`")
    private String key;

    /**
     * 文件名
     */
    @TableField("`name`")
    private String name;

    /**
     * 文件类型
     */
    @TableField("`type`")
    private String type;

    /**
     * 文件大小
     */
    @TableField("`size`")
    private Integer size;

    /**
     * 文件访问链接
     */
    @TableField("`url`")
    private String url;

    @TableField("`ipfsHash`")
    private String ipfshash;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIpfshash() {
        return ipfshash;
    }

    public void setIpfshash(String ipfshash) {
        this.ipfshash = ipfshash;
    }

    public static final String KEY = "`key`";

    public static final String NAME = "`name`";

    public static final String TYPE = "`type`";

    public static final String SIZE = "`size`";

    public static final String URL = "`url`";

    public static final String IPFSHASH = "`ipfsHash`";

    @Override
    public String toString() {
        return "FcAdminStorage{" +
        "key=" + key +
        ", name=" + name +
        ", type=" + type +
        ", size=" + size +
        ", url=" + url +
        ", ipfshash=" + ipfshash +
        "}";
    }
}
