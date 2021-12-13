package com.fingerchar.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description StaNft
 * @Author 
 * @Date 2021-12-13
 * @Version 2.1
 */
@TableName("`sta_nft`")
public class StaNft extends BaseEntity {


    /**
     * 名称
     */
    @TableField("`name`")
    private String name;

    /**
     * nft地址
     */
    @TableField("`address`")
    private String address;

    /**
     *  总计nft
     */
    @TableField("`count_nft`")
    private Integer countNft;

    /**
     * 待审核nft
     */
    @TableField("`reviewed_nft`")
    private Integer reviewedNft;

    /**
     * 新增nft
     */
    @TableField("`newly_added_nft`")
    private Integer newlyAddedNft;

    /**
     * 转手nft
     */
    @TableField("`change_hands_nft`")
    private Integer changeHandsNft;

    /**
     * 统计时间
     */
    @TableField("`sta_time`")
    private Long staTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getCountNft() {
        return countNft;
    }

    public void setCountNft(Integer countNft) {
        this.countNft = countNft;
    }

    public Integer getReviewedNft() {
        return reviewedNft;
    }

    public void setReviewedNft(Integer reviewedNft) {
        this.reviewedNft = reviewedNft;
    }

    public Integer getNewlyAddedNft() {
        return newlyAddedNft;
    }

    public void setNewlyAddedNft(Integer newlyAddedNft) {
        this.newlyAddedNft = newlyAddedNft;
    }

    public Integer getChangeHandsNft() {
        return changeHandsNft;
    }

    public void setChangeHandsNft(Integer changeHandsNft) {
        this.changeHandsNft = changeHandsNft;
    }

    public Long getStaTime() {
        return staTime;
    }

    public void setStaTime(Long staTime) {
        this.staTime = staTime;
    }

    public static final String NAME = "`name`";

    public static final String ADDRESS = "`address`";

    public static final String COUNT_NFT = "`count_nft`";

    public static final String REVIEWED_NFT = "`reviewed_nft`";

    public static final String NEWLY_ADDED_NFT = "`newly_added_nft`";

    public static final String CHANGE_HANDS_NFT = "`change_hands_nft`";

    public static final String STA_TIME = "`sta_time`";

    @Override
    public String toString() {
        return "StaNft{" +
        "name=" + name +
        ", address=" + address +
        ", countNft=" + countNft +
        ", reviewedNft=" + reviewedNft +
        ", newlyAddedNft=" + newlyAddedNft +
        ", changeHandsNft=" + changeHandsNft +
        ", staTime=" + staTime +
        "}";
    }
}
