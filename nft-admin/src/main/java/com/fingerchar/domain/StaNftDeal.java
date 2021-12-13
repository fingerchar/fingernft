package com.fingerchar.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fingerchar.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;

/**
 * @Description StaNftDeal
 * @Author 
 * @Date 2021-12-13
 * @Version 2.1
 */
@TableName("`sta_nft_deal`")
public class StaNftDeal extends BaseEntity {


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
     * 总流水
     */
    @TableField("`total_flow`")
    private BigDecimal totalFlow;

    /**
     * gas费用
     */
    @TableField("`gas_money`")
    private BigDecimal gasMoney;

    /**
     * 平台佣金
     */
    @TableField("`platform_money`")
    private BigDecimal platformMoney;

    /**
     * 当前盈利
     */
    @TableField("`now_profit`")
    private BigDecimal nowProfit;

    /**
     * 销售数量
     */
    @TableField("`sales_volume`")
    private Integer salesVolume;

    /**
     * 总计金额
     */
    @TableField("`sum_money`")
    private BigDecimal sumMoney;

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

    public BigDecimal getTotalFlow() {
        return totalFlow;
    }

    public void setTotalFlow(BigDecimal totalFlow) {
        this.totalFlow = totalFlow;
    }

    public BigDecimal getGasMoney() {
        return gasMoney;
    }

    public void setGasMoney(BigDecimal gasMoney) {
        this.gasMoney = gasMoney;
    }

    public BigDecimal getPlatformMoney() {
        return platformMoney;
    }

    public void setPlatformMoney(BigDecimal platformMoney) {
        this.platformMoney = platformMoney;
    }

    public BigDecimal getNowProfit() {
        return nowProfit;
    }

    public void setNowProfit(BigDecimal nowProfit) {
        this.nowProfit = nowProfit;
    }

    public Integer getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Integer salesVolume) {
        this.salesVolume = salesVolume;
    }

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }

    public Long getStaTime() {
        return staTime;
    }

    public void setStaTime(Long staTime) {
        this.staTime = staTime;
    }

    public static final String NAME = "`name`";

    public static final String ADDRESS = "`address`";

    public static final String TOTAL_FLOW = "`total_flow`";

    public static final String GAS_MONEY = "`gas_money`";

    public static final String PLATFORM_MONEY = "`platform_money`";

    public static final String NOW_PROFIT = "`now_profit`";

    public static final String SALES_VOLUME = "`sales_volume`";

    public static final String SUM_MONEY = "`sum_money`";

    public static final String STA_TIME = "`sta_time`";

    @Override
    public String toString() {
        return "StaNftDeal{" +
        "name=" + name +
        ", address=" + address +
        ", totalFlow=" + totalFlow +
        ", gasMoney=" + gasMoney +
        ", platformMoney=" + platformMoney +
        ", nowProfit=" + nowProfit +
        ", salesVolume=" + salesVolume +
        ", sumMoney=" + sumMoney +
        ", staTime=" + staTime +
        "}";
    }
}
