package com.fingerchar.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.StaNft;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *
 * @author zjm
 * */
@Service
public class StaNftService {
    @Resource
    private IBaseService baseService;

    @Transactional
    public boolean add(StaNft staNft) {
        return baseService.save(staNft) > 0;
    }

    @Transactional
    public boolean addBatch(List<StaNft> staNft) {
        return baseService.saveBatch(staNft) > 0;
    }

    public List<StaNft> getList(Long staTime) {
        QueryWrapper<StaNft> wrapper = new QueryWrapper<>();
        wrapper.eq(StaNft.STA_TIME, staTime);
        wrapper.orderByDesc(StaNft.COUNT_NFT);
        return baseService.findByCondition(StaNft.class, wrapper);
    }

    public Map<String,Object> contrastNft(List<StaNft> staNfts, Long staTime){
        Map<String,Object> result = new HashMap<>();
        //昨日销售数量
        List<Integer> yesterdayNewlyAddedNft = new ArrayList<>();
        //昨日销售金额
        List<Integer> yesterdayChangeHandsNft = new ArrayList<>();
        int length = 0;
        StaNft data = null;
        for (StaNft staNft : staNfts) {
            if (length++ >= 10){
                break;
            }
            data = getYesterdayList(staTime, staNft.getAddress());
            if (null == data){
                yesterdayNewlyAddedNft.add(0);
                yesterdayChangeHandsNft.add(0);
            }else {
                yesterdayNewlyAddedNft.add(data.getNewlyAddedNft());
                yesterdayChangeHandsNft.add(data.getChangeHandsNft());
            }

        }
        result.put("yesterdayNewlyAddedNft",yesterdayNewlyAddedNft);
        result.put("yesterdayChangeHandsNft",yesterdayChangeHandsNft);

        return result;
    }

    public StaNft getYesterdayList(Long staTime,String address) {
        QueryWrapper<StaNft> wrapper = new QueryWrapper<>();
        long oneDay = 24 * 60 * 60;
        staTime = staTime - oneDay;
        wrapper.eq(StaNft.STA_TIME, staTime - oneDay)
                .eq(StaNft.ADDRESS,address);
        wrapper.orderByDesc(StaNft.COUNT_NFT);
        return baseService.getByCondition(StaNft.class, wrapper);
    }
}
