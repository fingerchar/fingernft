package com.fingerchar.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.StaNftDeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *
 * @author zjm
 * */
@Service
public class StaNftDealService {

    @Autowired
    private IBaseService baseService;

    @Transactional
    public boolean add(StaNftDeal nftDeal) {
        Integer save = baseService.save(nftDeal);
        return save > 0;
    }

    @Transactional
    public boolean addBatch(List<StaNftDeal> nftDeal) {
        Integer save = baseService.saveBatch(nftDeal);
        return save > 0;
    }

    public List<StaNftDeal> getList(Long staTime) {
        QueryWrapper<StaNftDeal> wrapper = new QueryWrapper<>();
        wrapper.eq(StaNftDeal.STA_TIME, staTime);
        wrapper.orderByDesc(StaNftDeal.SUM_MONEY);
        return baseService.findByCondition(StaNftDeal.class, wrapper);
    }

    public Map<String,Object> contrastNftDeal(List<StaNftDeal> staNftDeals,Long staTime){
        Map<String,Object> result = new HashMap<>();
        //昨日销售数量
        List<Integer> yesterdaySalesVolume = new ArrayList<>();
        //昨日销售金额
        List<BigDecimal> yesterdaySumMoney = new ArrayList<>();
        int length = 0;
        StaNftDeal data = null;
        for (StaNftDeal staNftDeal : staNftDeals) {
            if (length++ >= 10){
                break;
            }
            data = getYesterdayList(staTime, staNftDeal.getAddress());
            if (null == data){
                yesterdaySalesVolume.add(0);
                yesterdaySumMoney.add(new BigDecimal(0));
            }else {
                yesterdaySalesVolume.add(data.getSalesVolume());
                yesterdaySumMoney.add(data.getSumMoney());
            }

        }
        result.put("yesterdaySalesVolume",yesterdaySalesVolume);
        result.put("yesterdaySumMoney",yesterdaySumMoney);

        return result;
    }

    public StaNftDeal getYesterdayList(Long staTime,String address) {
        QueryWrapper<StaNftDeal> wrapper = new QueryWrapper<>();
        long oneDay = 24 * 60 * 60;
        staTime = staTime - oneDay;
        wrapper.eq(StaNftDeal.STA_TIME, staTime - oneDay)
                .eq(StaNftDeal.ADDRESS,address);
        wrapper.orderByDesc(StaNftDeal.SUM_MONEY);
        return baseService.getByCondition(StaNftDeal.class, wrapper);
    }

}
