package com.fingerchar.core.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcNftItemsExtMapper;
import com.fingerchar.db.dao.ext.FcOrderExtMapper;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcPayToken;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.dto.ExchangeBuyLog;
import com.fingerchar.db.dto.ExchangeCancelLog;
import com.fingerchar.db.dto.PrepareOrderInfo;
import com.fingerchar.db.vo.FcOrderVo;
import com.fingerchar.db.vo.NftParamVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FcOrderManager {
    private static final Logger logger = LoggerFactory.getLogger(FcOrderManager.class);

    @Autowired
    IBaseService baseService;

    @Autowired
    FcNftItemsManager nftItemsManager;

    @Autowired
    FcNftItemsExtMapper nftItemsExtMapper;

    @Autowired
    FcOrderLogManager orderLogManager;

    @Autowired
    FcUserManager userManager;

    @Autowired
    FcOrderExtMapper orderExtMapper;

    @Autowired
    FcNoticeManager noticeManager;

    @Autowired
    FcSystemConfigManager systemConfigManager;

    @Autowired
    FcPayTokenManager payTokenManager;

    public List<FcOrder> allbymulti(List<String> saltList){
        if(saltList.isEmpty()){
            return new ArrayList<>();
        }
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.in(FcOrder.SALT, saltList);
        return this.baseService.findByCondition(FcOrder.class, wrapper);
    }

    public List<FcOrderVo> activesales(List<NftParamVO> params){
        List<FcOrder> orderList = orderExtMapper.salelistbymulti(params);

        List<FcNftItems> itemsList = nftItemsExtMapper.listByMulti(params);
        Set<String> ownerList = itemsList.stream().map(vo -> vo.getItemOwner()+":"+vo.getAddress()+":"+vo.getTokenId()).collect(Collectors.toSet());

        orderList = orderList.stream().filter(
                vo -> ownerList.contains(vo.getOwner() + ":" + vo.getSellToken() + ":" + vo.getSellTokenId())
        ).collect(Collectors.toList());
        if(orderList.isEmpty()){
            return new ArrayList<>();
        }

        List<String> owners = orderList.stream().map(FcOrder::getOwner).collect(Collectors.toList());
        List<FcUser> userList = this.userManager.listByMulti(owners);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
        List<FcOrderVo> orderVoList = orderList.stream().map(vo->new FcOrderVo(vo, userMap.get(vo.getOwner()))).collect(Collectors.toList());
        return orderVoList;
    }

    public List<FcOrderVo> activebids(List<NftParamVO> params){
        List<FcOrder> orderList = orderExtMapper.bidlistbymulti(params);
        List<String> owners = orderList.stream().map(FcOrder::getOwner).collect(Collectors.toList());
        List<FcUser> userList = this.userManager.listByMulti(owners);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
        List<FcOrderVo> orderVoList = orderList.stream().map(vo->new FcOrderVo(vo, userMap.get(vo.getOwner()))).collect(Collectors.toList());
        return orderVoList;
    }

    public FcOrder get(ExchangeBuyLog log) {
        return this.get(
                log.getSellToken(),
                log.getSellTokenId().toString(),
                log.getBuyToken(),
                log.getBuyTokenId().toString(),
                log.getOwner(),
                log.getSalt().toString());
    }

    public FcOrder get(ExchangeCancelLog log) {
        return this.get(
                log.getSellToken(),
                log.getSellTokenId().toString(),
                log.getBuyToken(),
                log.getBuyTokenId().toString(),
                log.getOwner(),
                log.getSalt().toString()
        );
    }

    private FcOrder get(
            String sellToken,
            String sellTokenId,
            String buyerToken,
            String buyerTokenId,
            String owner,
            String salt) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.SELL_TOKEN, sellToken)
                .eq(FcOrder.SELL_TOKEN_ID, sellTokenId)
                .eq(FcOrder.BUYER_TOKEN, buyerToken)
                .eq(FcOrder.BUYER_TOKEN_ID, buyerTokenId)
                .eq(FcOrder.OWNER, owner)
                .eq(FcOrder.SALT, salt)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcOrder.class, wrapper);
    }

    public FcOrder getActiveSellOrder(String sellToken, String sellTokenId, String owner, Integer orderType) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.SELL_TOKEN, sellToken)
                .eq(FcOrder.SELL_TOKEN_ID, sellTokenId)
                .eq(FcOrder.OWNER, owner)
                .eq(FcOrder.ORDER_TYPE, orderType)
                .eq(FcOrder.EXPIRED, false)
                .eq(FcOrder.STATUS, 0)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcOrder.class, wrapper);
    }

    public FcOrder getActiveBuyerOrder(String buyerToken, String buyerTokenId, String owner, Integer orderType) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.BUYER_TOKEN, buyerToken)
                .eq(FcOrder.BUYER_TOKEN_ID, buyerTokenId)
                .eq(FcOrder.OWNER, owner)
                .eq(FcOrder.ORDER_TYPE, orderType)
                .eq(FcOrder.EXPIRED, false)
                .eq(FcOrder.STATUS, 0)
                .eq(FcOrder.DELETED, false);
        return this.baseService.getByCondition(FcOrder.class, wrapper);
    }

    public FcOrder getSellOrder(String sellToken, String sellTokenId, String owner) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.SELL_TOKEN, sellToken)
                .eq(FcOrder.SELL_TOKEN_ID, sellTokenId)
                .eq(FcOrder.OWNER, owner)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcOrder.class, wrapper);
    }

    public FcOrder getBuyerOrder(String buyerToken, String buyerTokenId, String owner) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.BUYER_TOKEN, buyerToken)
                .eq(FcOrder.BUYER_TOKEN_ID, buyerTokenId)
                .eq(FcOrder.OWNER, owner)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcOrder.class, wrapper);
    }

    public FcOrder getSellOrder(String sellToken, String sellTokenId, String owner, String salt) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.SELL_TOKEN, sellToken)
                .eq(FcOrder.SELL_TOKEN_ID, sellTokenId)
                .eq(FcOrder.OWNER, owner)
                .eq(FcOrder.SALT, salt)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcOrder.class, wrapper);
    }

    public FcOrder getBuyerOrder(String buyerToken, String buyerTokenId, String owner, String salt) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.BUYER_TOKEN, buyerToken)
                .eq(FcOrder.BUYER_TOKEN_ID, buyerTokenId)
                .eq(FcOrder.OWNER, owner)
                .eq(FcOrder.SALT, salt)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcOrder.class, wrapper);
    }

    public Integer sale(PrepareOrderInfo orderInfo) {
        FcOrder order = this.getSellOrder(orderInfo.getSellToken(), orderInfo.getSellTokenId(), orderInfo.getOwner());
        FcPayToken payToken = payTokenManager.get(orderInfo.getBuyToken());
        if (null == order) {
            order = orderInfo.toOrder();
        } else {
            order.setBuyerToken(orderInfo.getBuyToken());
            order.setBuyerTokenId(orderInfo.getBuyTokenId());
            order.setBuyerValue(orderInfo.getBuyValue());
            order.setSignature(orderInfo.getSignature());
            order.setSellValue(orderInfo.getSellValue());
            order.setSells("0");
            this.orderLogManager.expireOrderLog(order.getId());
        }
        order.setPaytokenAddress(payToken.getAddress());
        order.setPaytokenName(payToken.getName());
        order.setPaytokenSymbol(payToken.getSymbol());
        order.setPaytokenDecimals(payToken.getDecimals());

        order.setStatus(0);
        order.setOrderType(1);

        order.setSellFee(Integer.parseInt(this.systemConfigManager.getKeyValue(SysConfConstant.SELLER_FEE)));
        order.setBuyFee(Integer.parseInt(this.systemConfigManager.getKeyValue(SysConfConstant.BUYER_FEE)));

        if (null == order.getId()) {
            this.save(order);
        } else {
            this.update(order);
        }
        return this.orderLogManager.sale(orderInfo, order);
    }

    public Integer bid(PrepareOrderInfo orderInfo) {
        FcOrder order = this.getBuyerOrder(orderInfo.getBuyToken(), orderInfo.getBuyTokenId(), orderInfo.getOwner());
        FcPayToken payToken = payTokenManager.get(orderInfo.getSellToken());
        if (null == order) {
            order = orderInfo.toOrder();
        } else {
            order.setSellToken(orderInfo.getSellToken());
            order.setSellValue(orderInfo.getSellValue());
            order.setSellTokenId(orderInfo.getSellTokenId());
            order.setSignature(orderInfo.getSignature());
            order.setBuyerValue(orderInfo.getBuyValue());
            // expire order log
            this.orderLogManager.expireOrderLog(order.getId());
        }

        order.setPaytokenName(payToken.getName());
        order.setPaytokenSymbol(payToken.getSymbol());
        order.setPaytokenDecimals(payToken.getDecimals());
        order.setPaytokenAddress(payToken.getAddress());
        order.setOrderType(2);
        order.setSells("0");
        order.setSellFee(Integer.parseInt(this.systemConfigManager.getKeyValue(SysConfConstant.SELLER_FEE)));
        order.setBuyFee(Integer.parseInt(this.systemConfigManager.getKeyValue(SysConfConstant.BUYER_FEE)));

        if (null == order.getId()) {
            this.save(order);
        } else {
            this.update(order);
        }
        return this.orderLogManager.bid(orderInfo, order);
    }


    public Integer buy(ExchangeBuyLog log, FcOrder order) {
        BigInteger amount = log.getAmount();

        BigInteger sells = new BigInteger(order.getSells()).add(amount);
        order.setSells(sells.toString());

        BigInteger value = new BigInteger(order.getSellValue());

        if (value.compareTo(sells) <= 0) {
            order.setStatus(1);
            order.setExpired(true);
            order.setDeleted(true);
            this.orderLogManager.expireOrderLog(order.getId());
        }

        this.orderLogManager.buy(log, order);

        return this.update(order);
    }


    public Integer cancel(ExchangeCancelLog log, FcOrder order) {
        this.orderLogManager.expireOrderLog(order.getId());

        this.orderLogManager.cancel(log, order);

        order.setExpired(true);
        order.setStatus(2);
        order.setDeleted(true);
        return this.update(order);
    }

    public Integer update(FcOrder order) {
        return this.baseService.update(order);
    }

    public Integer save(FcOrder order) {
        return this.baseService.save(order);
    }

    /**
     * @param time      最小的创建时间
     * @return
     */
    public Integer bidCountByCondition(Long time) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.STATUS, 0)
                .eq(FcOrder.ORDER_TYPE, 2)
                .eq(FcOrder.DELETED, false);
        if (null != time) {
            wrapper.gt(FcOrder.CREATE_TIME, time);
        }
        return this.baseService.counts(FcOrder.class, wrapper);
    }
}
