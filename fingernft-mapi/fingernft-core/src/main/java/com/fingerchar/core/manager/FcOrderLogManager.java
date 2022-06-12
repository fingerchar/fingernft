package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcOrderLog;
import com.fingerchar.db.dto.ExchangeBuyLog;
import com.fingerchar.db.dto.ExchangeCancelLog;
import com.fingerchar.db.dto.PrepareOrderInfo;
import com.fingerchar.db.dto.TransferLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author： Zjm
 * @Date：2022/3/21 21:27
 */
@Service
public class FcOrderLogManager {
    @Autowired
    IBaseService baseService;

    @Autowired
    FcPayTokenManager payTokenManager;

    public Integer buy(ExchangeBuyLog log, FcOrder order){
        FcOrderLog orderLog = new FcOrderLog();

        orderLog.setFrom(log.getOwner());
        orderLog.setOrderId(order.getId());
        orderLog.setPreLogId(0L);
        orderLog.setTo(log.getBuyer());
        orderLog.setTxHash(log.getTxHash());
        orderLog.setContent(JSON.toJSONString(order));
        orderLog.setExpired(false);

        Long time = Long.valueOf(log.getBlockTimestamp().toString());
        orderLog.setCreateTime(time);
        orderLog.setUpdateTime(time);
        if(order.getOrderType().equals(1)){
            orderLog.setType(CommonStatus.getStatusByName("Buy").getType());
            orderLog.setToken(log.getSellToken());
            orderLog.setTokenId(log.getSellTokenId().toString());
        }else{
            orderLog.setType(CommonStatus.getStatusByName("Accept bid").getType());
            orderLog.setToken(log.getBuyToken());
            orderLog.setTokenId(log.getBuyTokenId().toString());
        }
        orderLog.setPaytokenAddress(order.getPaytokenAddress());
        orderLog.setPaytokenDecimals(order.getPaytokenDecimals());
        orderLog.setPaytokenName(order.getPaytokenName());
        orderLog.setPaytokenSymbol(order.getPaytokenSymbol());

        return this.baseService.save(orderLog);
    }

    public Integer cancel(ExchangeCancelLog log, FcOrder order){
        FcOrderLog orderLog = new FcOrderLog();
        orderLog.setFrom(log.getOwner());
        orderLog.setOrderId(order.getId());
        orderLog.setPreLogId(0L);
        orderLog.setTo(log.getOwner());
        orderLog.setTxHash(log.getTxHash());
        orderLog.setContent(JSON.toJSONString(order));
        orderLog.setExpired(true);
        Long time = Long.valueOf(log.getBlockTimestamp().toString());
        orderLog.setUpdateTime(time);
        orderLog.setCreateTime(time);
        if(order.getOrderType().equals(1)){
            orderLog.setType(CommonStatus.getStatusByName("Cancel sale").getType());
            orderLog.setToken(log.getSellToken());
            orderLog.setTokenId(log.getSellTokenId().toString());
        }else{
            orderLog.setType(CommonStatus.getStatusByName("Cancel bid").getType());
            orderLog.setToken(log.getBuyToken());
            orderLog.setTokenId(log.getBuyTokenId().toString());
        }
        orderLog.setPaytokenAddress(order.getPaytokenAddress());
        orderLog.setPaytokenDecimals(order.getPaytokenDecimals());
        orderLog.setPaytokenName(order.getPaytokenName());
        orderLog.setPaytokenSymbol(order.getPaytokenSymbol());
        return this.baseService.save(orderLog);
    }

    public Integer sale(PrepareOrderInfo orderInfo, FcOrder order){
        FcOrderLog orderLog = new FcOrderLog();
        orderLog.setFrom(order.getOwner());
        orderLog.setOrderId(order.getId());
        orderLog.setToken(order.getSellToken());
        orderLog.setTokenId(order.getSellTokenId());
        orderLog.setContent(JSON.toJSONString(order));
        orderLog.setType(orderInfo.getType());
        orderLog.setPaytokenAddress(order.getPaytokenAddress());
        orderLog.setPaytokenName(order.getPaytokenName());
        orderLog.setPaytokenSymbol(order.getPaytokenSymbol());
        orderLog.setPaytokenDecimals(order.getPaytokenDecimals());
        return this.save(orderLog);
    }

    public Integer bid(PrepareOrderInfo orderInfo, FcOrder order){

        FcOrderLog orderLog = new FcOrderLog();
        orderLog.setFrom(orderInfo.getOwner());
        orderLog.setOrderId(order.getId());
        orderLog.setToken(order.getBuyerToken());
        orderLog.setTokenId(order.getBuyerTokenId());
        orderLog.setContent(JSON.toJSONString(order));
        orderLog.setType(orderInfo.getType());
        orderLog.setPaytokenAddress(order.getPaytokenAddress());
        orderLog.setPaytokenName(order.getPaytokenName());
        orderLog.setPaytokenSymbol(order.getPaytokenSymbol());
        orderLog.setPaytokenDecimals(order.getPaytokenDecimals());
        return this.save(orderLog);
    }

    public Integer transfer(TransferLog log){
        FcOrderLog orderLog = new FcOrderLog();
        orderLog.setFrom(log.getFrom());
        orderLog.setPreLogId(0L);
        orderLog.setTo(log.getTo());
        orderLog.setTxHash(log.getTxHash());
        orderLog.setContent(JSON.toJSONString(log));
        Long time = Long.valueOf(log.getBlockTimestamp().toString());
        orderLog.setUpdateTime(time);
        orderLog.setCreateTime(time);
        if(log.getTo().equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)){
            orderLog.setType(CommonStatus.BURN.getType());
        }else if(log.getFrom().equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)){
            orderLog.setType(CommonStatus.MINT.getType());
        }else{
            orderLog.setType(CommonStatus.TRANSFER.getType());
        }
        orderLog.setToken(log.getAddress());
        orderLog.setTokenId(log.getTokenId().toString());
        return this.save(orderLog);
    }

    public Integer expireOrderLog(Long orderId){
        UpdateWrapper<FcOrderLog> wrapper = new UpdateWrapper<>();
        wrapper.eq(FcOrderLog.ORDER_ID, orderId)
                .eq(FcOrderLog.EXPIRED, false);
        wrapper.set(FcOrderLog.EXPIRED, true);
        return this.baseService.updateByCondition(FcOrderLog.class, wrapper);
    }

    public Integer update(FcOrderLog orderLog){
        return this.baseService.update(orderLog);
    }

    public Integer save(FcOrderLog orderLog){
        return this.baseService.save(orderLog);
    }
}
