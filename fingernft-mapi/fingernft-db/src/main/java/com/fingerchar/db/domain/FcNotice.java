package com.fingerchar.db.domain;

import com.fingerchar.db.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description FcNotice
 * @Author 
 * @Date 2022-06-10
 * @Version 2.1
 */
@TableName("fc_notice")
public class FcNotice extends BaseEntity {


    /**
     * 大类型1follow 2like 3trade
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 消息类型：0: "LIKE"\n1: "FOLLOWING"\n2: "ORDER"\n3: "BID"\n4: "BUY"\n5: "SALE"\n6: "CANCEL"\n7: "CANCEL_BID"\n8: "TRANSFER"\n9: "MINT"\n10: "BURN"
     */
    @TableField("`sub_type`")
    private Integer subType;

    /**
     * 消息对应用户地址
     */
    @TableField("`owner`")
    private String owner;

    /**
     * 操作者地址
     */
    @TableField("`operator`")
    private String operator;

    /**
     * 消息内容
     */
    @TableField("`content`")
    private String content;

    /**
     * 是否已读
     */
    @TableField("`is_read`")
    private Boolean isRead;

    /**
     * 显示图片
     */
    @TableField("`image`")
    private String image;

    /**
     * 显示名称
     */
    @TableField("`name`")
    private String name;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final String TYPE = "`type`";

    public static final String SUB_TYPE = "`sub_type`";

    public static final String OWNER = "`owner`";

    public static final String OPERATOR = "`operator`";

    public static final String CONTENT = "`content`";

    public static final String IS_READ = "`is_read`";

    public static final String IMAGE = "`image`";

    public static final String NAME = "`name`";

    @Override
    public String toString() {
        return "FcNotice{" +
        "type=" + type +
        ", subType=" + subType +
        ", owner=" + owner +
        ", operator=" + operator +
        ", content=" + content +
        ", isRead=" + isRead +
        ", image=" + image +
        ", name=" + name +
        "}";
    }
}
