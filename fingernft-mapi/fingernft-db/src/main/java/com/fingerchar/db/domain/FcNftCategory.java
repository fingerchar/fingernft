package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcNftCategory
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_nft_category")
public class FcNftCategory extends BaseEntity {


    /**
     * 类目名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 排序
     */
    @TableField("`order`")
    private Integer order;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public static final String NAME = "`name`";

    public static final String ORDER = "`order`";

    @Override
    public String toString() {
        return "FcNftCategory{" +
        "name=" + name +
        ", order=" + order +
        "}";
    }
}
