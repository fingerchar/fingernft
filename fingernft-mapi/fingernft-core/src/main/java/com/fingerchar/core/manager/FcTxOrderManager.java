package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcTxOrder;
import com.fingerchar.db.vo.EventValuesExt;
import com.fingerchar.db.vo.TxOrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author： Zjm
 * @Date：2022/3/23 21:00
 */
@Service
public class FcTxOrderManager {
    @Autowired
    IBaseService baseService;

    public Integer saveBatch(List<EventValuesExt> list){
        if(list.isEmpty()){
            return 0;
        }
        List<FcTxOrder> txOrderList = new ArrayList<>();
        FcTxOrder txOrder = null;
        for(EventValuesExt valuesExt: list){
            txOrder = new FcTxOrder();
            txOrder.setTxHash(valuesExt.getTxHash());
            Integer blockNumber = Integer.valueOf(valuesExt.getBlockNumber().toString());
            txOrder.setBlockNumber(blockNumber);
            Integer time = Integer.valueOf(valuesExt.getBlockTimestamp().toString());
            txOrder.setBlockTimestamp(time);
            // txOrder.setIsSync(true);

            Long currentTime = System.currentTimeMillis()/1000;
            txOrder.setUpdateTime(currentTime);
            txOrder.setCreateTime(currentTime);
            txOrderList.add(txOrder);
        }
        if(null == txOrderList || txOrderList.size()==0){
            return null;
        }
        //对txOrderList根据TxHash去重
        List<FcTxOrder> collect = txOrderList.stream().filter(distinctByKey(FcTxOrder::getTxHash)).collect(Collectors.toList());
        return this.baseService.saveBatch(collect);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    public TxOrderInfo info(){
        TxOrderInfo orderInfo = new TxOrderInfo();
        FcTxOrder txOrder = this.lastOne(true);
        if(null != txOrder){
            orderInfo.setEarlyBlockNumber(txOrder.getBlockNumber());
            txOrder = this.lastOne(false);
            orderInfo.setLastBlockNumber(txOrder.getBlockNumber());
        }
        Integer total = this.total();
        orderInfo.setTxAmount(total);
        return orderInfo;
    }

    public FcTxOrder lastOne(Boolean asc){
        QueryWrapper<FcTxOrder> wrapper = new QueryWrapper<>();
        wrapper.last("limit 1");
        if(!asc){
            wrapper.orderByDesc(FcTxOrder.BLOCK_NUMBER);
        }else{
            wrapper.orderByAsc(FcTxOrder.BLOCK_NUMBER);
        }
        List<FcTxOrder>  txOrderList = this.baseService.findByCondition(FcTxOrder.class, wrapper);
        if(txOrderList.isEmpty()){
            return null;
        }
        return txOrderList.get(0);
    }

    public Integer total(){
        QueryWrapper<FcTxOrder> wrapper = new QueryWrapper<>();
        return this.baseService.counts(FcTxOrder.class, wrapper);
    }

    public List<FcTxOrder> getList(Integer start, Integer end){
        QueryWrapper<FcTxOrder> wrapper = new QueryWrapper<>();
        wrapper.ge(FcTxOrder.BLOCK_NUMBER, start)
                .le(FcTxOrder.BLOCK_NUMBER, end);
        return this.baseService.findByCondition(FcTxOrder.class, wrapper);
    }

}
