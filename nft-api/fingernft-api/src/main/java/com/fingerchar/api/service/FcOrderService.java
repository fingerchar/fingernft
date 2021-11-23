package com.fingerchar.api.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.constant.CommonStatus;
import com.fingerchar.api.constant.NoticeType;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.dto.OrderInterface;
import com.fingerchar.api.dto.PrepareOrderInfo;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.api.utils.TokenExchangeCompute;
import com.fingerchar.api.vo.SignOrderInfo;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcOrderLog;
import com.fingerchar.db.domain.FcPayToken;
import com.fingerchar.db.domain.FcUser;

@Service
public class FcOrderService {

    public static final Logger logger = LoggerFactory.getLogger(FcOrderService.class);

    @Autowired
    FcNoticeService noticeService;

    @Autowired
    FcRedisService redisService;

    @Autowired
    FcPayTokenService payTokenService;

    @Autowired
    FcSystemConfigService configService;

    @Autowired
    IBaseService baseService;

    @Transactional(rollbackFor = Exception.class)
    public Object addOrder(OrderInterface order, FcUser user) {
        CommonStatus status = CommonStatus.getStatusByType(order.getType());
        Object result = null;
        switch (status) {
            case SALE:
            case EDIT_SALE:
                result = processSell(order, user, status.getType());
                break;
            case BID:
            case EDIT_BID:
                result = processBid(order, user, status.getType());
                break;
            default:
                result = ResponseUtil.badArgumentValue();
                break;
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    private Object processBid(OrderInterface inter, FcUser user, Integer type) {
        if (!(inter instanceof PrepareOrderInfo)) {
            return ResponseUtil.badArgumentValue();
        }
        PrepareOrderInfo info = (PrepareOrderInfo) inter;
        String tokenAddress = info.getBuyToken();
        Long tokenId = new Long(info.getBuyTokenId());

        QueryWrapper<FcContract> contractWrapper = new QueryWrapper<>();
        contractWrapper.eq(FcContract.ADDRESS, tokenAddress).eq(BaseEntity.DELETED, false);
        FcContract contract = this.baseService.getByCondition(FcContract.class, contractWrapper);
        if (null == contract) {
            return ResponseUtil.badArgumentValue();
        }

        QueryWrapper<FcContractNft> nftWrapper = new QueryWrapper<>();
        nftWrapper.eq(FcContractNft.ADDRESS, tokenAddress)
                .eq(FcContractNft.TOKEN_ID, tokenId)
                .eq(BaseEntity.DELETED, false);
        FcContractNft nft = this.baseService.getByCondition(FcContractNft.class, nftWrapper);
        if (null == nft) {
            return ResponseUtil.fail(-1, "Unkown nft");
        }

        if (nft.getQuantity() < Integer.valueOf(info.getBuyValue())) {
            return ResponseUtil.fail(-1, "no enough items for bid");
        }

        FcPayToken payToken = this.payTokenService.getPayToken(info.getSellToken());
        if (null == payToken) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }

        FcOrder order = null;
        FcOrderLog oldLog = null;
        String oldTotalPrice = null;
        String usdtPrice = TokenExchangeCompute.computeUsdt(info.getSellValue(), payToken.getRate());

        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.OWNER, user.getAddress())
                //.eq(FcOrder.SALT, info.getSalt())
                .eq(FcOrder.BUYER_TOKEN, info.getBuyToken())
                .eq(FcOrder.BUYER_TOKEN_ID, new Long(info.getBuyTokenId()))
                .eq(BaseEntity.DELETED, false);

        order = this.baseService.getByCondition(FcOrder.class, wrapper);
        if (type.equals(6)) {
            if (null == order) {
                return ResponseUtil.fail(-1, "Bid is not existed or order is canceled");
            }
            payToken = this.payTokenService.getPayToken(order.getSellToken());
            BigDecimal oldPrice = new BigDecimal(order.getSellValue()).divide(new BigDecimal(order.getBuyerValue()), RoundingMode.HALF_UP);
            BigDecimal newPrice = new BigDecimal(info.getSellValue()).divide(new BigDecimal(info.getBuyValue()), RoundingMode.HALF_UP);
            usdtPrice = newPrice.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
            int r = oldPrice.compareTo(newPrice);
            if (order.getBuyerValue().equals(info.getBuyValue())) {
                if (r >= 0) {
                    return ResponseUtil.fail(-1, "New price must greater then the old price");
                }
            } else {
                if (r > 0) {
                    return ResponseUtil.fail(-1, "New price must greater then the old price");
                }
            }
            oldTotalPrice = order.getSellValue();
            order.setSellToken(info.getSellToken());
            order.setSellValue(info.getSellValue());
            order.setSellTokenId(info.getSellTokenId());
            order.setSignature(info.getSignature());
            order.setBuyerValue(info.getBuyValue());
            QueryWrapper<FcOrderLog> logWrapper = new QueryWrapper<>();
            List<Integer> list = new ArrayList<>();
            list.add(CommonStatus.getStatusByName("Bid").getType());
            list.add(CommonStatus.getStatusByName("Edit bid").getType());
            logWrapper.eq(FcOrderLog.ORDER_ID, order.getId())
                    .in(FcOrderLog.TYPE, list)
                    .eq(FcOrderLog.EXPIRED, false);
            logWrapper.eq(FcOrderLog.ORDER_ID, order.getId())
                    .in(FcOrderLog.TYPE, list)
                    .eq(FcOrderLog.EXPIRED, false);
            oldLog = this.baseService.getByCondition(FcOrderLog.class, logWrapper);
        } else {
            if (null != order) {
                return ResponseUtil.fail(-1, "Bid is existed, please cancel it first");
            }
            usdtPrice = new BigDecimal(usdtPrice).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
        }
        if (null == order) {
            order = info.toOrder();
        }
        order.setUsdtPrice(usdtPrice);
        order.setExpired(false);
        order.setDeleted(false);
        order.setStatus(0);
        order.setOrderType(2);
        order.setSells(0L);
        if (null == order.getId()) {
            this.baseService.save(order);
        } else {
            this.baseService.update(order);
        }

        FcOrderLog log = new FcOrderLog();
        log.setFrom(user.getAddress());
        log.setOrderId(order.getId());
        log.setPreLogId(0L);
        log.setToken(order.getBuyerToken());
        log.setTokenId(order.getBuyerTokenId());
        log.setTo("");
        log.setTxHash("");
        log.setContent(JSON.toJSONString(order));
        log.setType(type);
        log.setExpired(false);
        log.setDeleted(false);
        if (null != oldLog) {
            log.setPreLogId(oldLog.getId());
            oldLog.setExpired(true);
            this.baseService.update(oldLog);
        }
        QueryWrapper<FcNftItems> itemsWrapper = new QueryWrapper<>();
        itemsWrapper.eq(FcNftItems.NFT_ID, nft.getId())
                .eq(FcNftItems.ADDRESS, info.getBuyToken())
                .eq(BaseEntity.DELETED, false);
        List<FcNftItems> itemsList = this.baseService.findByCondition(FcNftItems.class, itemsWrapper);

        if (null != itemsList && itemsList.size() > 0) {
            Map<String, Object> content = new HashMap<>();
            content.put("oldPrice", oldTotalPrice);
            content.put("newPrice", info.getSellValue());
            content.put("uint", payToken.getSymbol());
            content.put("biderAddr", user.getAddress());
            content.put("biderImage", user.getAvatar());
            content.put("biderName", user.getNickname());
            content.put("token", info.getBuyToken());
            content.put("tokenId", order.getBuyerTokenId());
            int size = itemsList.size();
            String noticeType = NoticeType.getNoticeTypeByName("Trade").getType().toString();
            for (int i = 0; i < size; i++) {
                this.noticeService.insertNotice(content, itemsList.get(i).getItemOwner(), type.toString(), nft.getImgUrl(), nft.getName(), noticeType, user.getAddress());
            }
        }

        this.redisService.del(user.getAddress());
        this.baseService.save(log);
        return ResponseUtil.ok();
    }

    @Transactional(rollbackFor = Exception.class)
    private Object processSell(OrderInterface inter, FcUser user, Integer type) {

        if (!(inter instanceof PrepareOrderInfo)) {
            return ResponseUtil.badArgumentValue();
        }
        PrepareOrderInfo info = (PrepareOrderInfo) inter;

        String tokenAddress = info.getSellToken();
        Long tokenId = new Long(info.getSellTokenId());

        QueryWrapper<FcContract> contractWrapper = new QueryWrapper<>();
        contractWrapper.eq(FcContract.ADDRESS, tokenAddress).eq(BaseEntity.DELETED, false);
        FcContract contract = this.baseService.getByCondition(FcContract.class, contractWrapper);
        if (null == contract) {
            return ResponseUtil.badArgumentValue();
        }
        QueryWrapper<FcContractNft> nftWrapper = new QueryWrapper<>();
        nftWrapper.eq(FcContractNft.ADDRESS, tokenAddress)
                .eq(FcContractNft.TOKEN_ID, tokenId)
                //.ge(FcContractNft.QUANTITY, new Integer(info.getSellValue()))
                .eq(BaseEntity.DELETED, false);

        FcContractNft nft = this.baseService.getByCondition(FcContractNft.class, nftWrapper);
        if (null == nft) {
            return ResponseUtil.fail(-1, "Then token is not existed or bured");
        }

        QueryWrapper<FcNftItems> itemsWrapper = new QueryWrapper<>();
        itemsWrapper.eq(FcNftItems.NFT_ID, nft.getId())
                .eq(FcNftItems.ITEM_OWNER, user.getAddress())
                .eq(BaseEntity.DELETED, false);
        if (2 != type) {
            itemsWrapper.eq(FcNftItems.ONSELL, false);
        }
        //itemsWrapper.orderByDesc(BaseEntity.CREATE_TIME);
        FcNftItems nftItems = this.baseService.getByCondition(FcNftItems.class, itemsWrapper);
        if (null == nftItems) {
            return ResponseUtil.fail(-1, "Then token is not existed or owner is not you");
        }

        FcPayToken payToken = this.payTokenService.getPayToken(info.getBuyToken());
        if (null == payToken) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }

        FcOrder order = null;
        FcOrderLog oldLog = null;
        String usdtPrice = TokenExchangeCompute.computeUsdt(info.getBuyValue(), payToken.getRate());
        QueryWrapper<FcOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq(FcOrder.OWNER, user.getAddress())
                //.eq(FcOrder.SALT, info.getSalt())
                .eq(FcOrder.SELL_TOKEN, info.getSellToken())
                .eq(FcOrder.SELL_TOKEN_ID, new Long(info.getSellTokenId()))
                .eq(BaseEntity.DELETED, false)
                .eq(FcOrder.EXPIRED, false);
        order = this.baseService.getByCondition(FcOrder.class, orderWrapper);
        if (type.equals(2)) {
            if (null == order) {
                return ResponseUtil.fail(-1, "Order is not existed or order is canceled");
            }
            payToken = this.payTokenService.getPayToken(order.getBuyerToken());
            String oldUsdtPrice = TokenExchangeCompute.computeUsdt(order.getBuyerValue(), payToken.getRate());
            BigDecimal oldPrice = new BigDecimal(oldUsdtPrice).divide(new BigDecimal(order.getSellValue()), RoundingMode.HALF_UP);
            BigDecimal newPrice = new BigDecimal(usdtPrice).divide(new BigDecimal(info.getSellValue()), RoundingMode.HALF_UP);
            usdtPrice = newPrice.setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
            int r = oldPrice.compareTo(newPrice);
            if (nftItems.getQuantity() < Integer.valueOf(info.getSellValue())) {
                return ResponseUtil.fail(-1, "no enough items");
            }

            if (info.getSellValue().equals(order.getSellValue())) {
                if (r <= 0) {
                    return ResponseUtil.fail(-1, "New price must less then the old price");
                }
            } else {
                if (r < 0) {
                    return ResponseUtil.fail(-1, "New price must less then the old price");
                }
            }
            order.setBuyerToken(info.getBuyToken());
            order.setBuyerTokenId(info.getBuyTokenId());
            order.setBuyerValue(info.getBuyValue());
            order.setSignature(info.getSignature());
            order.setSellValue(info.getSellValue());
            order.setSells(0L);
            List<Integer> list = new ArrayList<>();
            list.add(CommonStatus.getStatusByName("Sale").getType());
            list.add(CommonStatus.getStatusByName("Edit sale").getType());
            QueryWrapper<FcOrderLog> logWrapper = new QueryWrapper<>();
            logWrapper.eq(FcOrderLog.ORDER_ID, order.getId())
                    .in(FcOrderLog.TYPE, list)
                    .eq(FcOrderLog.EXPIRED, false);
            oldLog = this.baseService.getByCondition(FcOrderLog.class, logWrapper);
        } else {
            if (null != order) {
                return ResponseUtil.fail(-1, "This token is on sale already");
            }
            usdtPrice = new BigDecimal(usdtPrice).setScale(2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100)).toString();
        }
        if (null == order) {
            order = info.toOrder();
        }
        order.setUsdtPrice(usdtPrice);
        order.setExpired(false);
        order.setDeleted(false);
        order.setStatus(0);
        order.setSignature(info.getSignature());
        order.setOrderType(1);
        order.setSellValue(info.getSellValue());

        nftItems.setOnsell(true);
        payToken = this.payTokenService.getPayToken(info.getBuyToken());
        nftItems.setPaytokenAddress(payToken.getAddress());
        nftItems.setPrice(info.getBuyValue());
        nftItems.setUsdtPrice(new BigDecimal(usdtPrice));
        nftItems.setSellQuantity(Long.valueOf(order.getSellValue()));
        nftItems.setSignature(order.getSignature());
        nftItems.setAddress(tokenAddress);
        nftItems.setCategoryId(nft.getCategoryId());

        this.baseService.update(nft);
        this.baseService.update(nftItems);

        if (null == order.getId()) {
            this.baseService.save(order);
        } else {
            this.baseService.update(order);
        }

        FcOrderLog log = new FcOrderLog();
        log.setFrom(user.getAddress());
        log.setOrderId(order.getId());
        log.setPreLogId(0L);
        log.setToken(order.getSellToken());
        log.setTokenId(order.getSellTokenId());
        log.setTo("");
        log.setTxHash("");
        log.setContent(JSON.toJSONString(order));
        log.setType(type);
        log.setExpired(false);
        log.setDeleted(false);
        if (null != oldLog) {
            log.setPreLogId(oldLog.getId());
            oldLog.setExpired(true);
            this.baseService.update(oldLog);
        }

        this.baseService.save(log);
        this.redisService.del(user.getAddress());
        return ResponseUtil.ok();
    }

    public FcOrder findById(Long orderId) {
        return this.baseService.getById(FcOrder.class, orderId);
    }

    public Object prePare(PrepareOrderInfo info, FcUser user) {
        String sellFee = this.configService.getValue(SysConfConstant.SELLER_FEE);
        if (null == sellFee) {
            logger.error("系统未设置sell fee");
            return ResponseUtil.serious();
        }

        if (null == info.getBuyTokenId()) {
            info.setBuyTokenId("0");
        }

        info.setSellFee(sellFee);

        String sellValue = this.payTokenService.getErcValue(Integer.valueOf(info.getSellType()), info.getSellValue(), info.getSellToken());
        if (null == sellValue) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }
        info.setSellValue(sellValue);
        String buyValue = this.payTokenService.getErcValue(Integer.valueOf(info.getBuyType()), info.getBuyValue(), info.getBuyToken());
        if (null == buyValue) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }
        info.setBuyValue(buyValue);
		SignOrderInfo signOrder = new SignOrderInfo(info.getOwner(), info.getSalt(), info.getSellToken(),
				info.getSellTokenId(), info.getSellType(), info.getBuyToken(), info.getBuyTokenId(), info.getBuyType(),
				info.getSellValue(), info.getBuyValue(), info.getSellFee());
        signOrder = DappCryptoUtil.orderSign(signOrder);
        info.setMessage(signOrder.getSignature());
        info.setSalt(signOrder.getSalt());
        boolean flag = false;
        if (info.getType() == 1 || info.getType() == 2) {
            flag = this.redisService.set(user.getAddress() + info.getSellToken() + info.getSellTokenId(), info, 1800L);
        }
        if (info.getType() == 5 || info.getType() == 6) {
            flag = this.redisService.set(user.getAddress() + info.getBuyToken() + info.getBuyTokenId(), info, 1800L);
        }
        if (flag) {
            return ResponseUtil.ok(info);
        } else {
            return ResponseUtil.serious();
        }
    }

    public Object buyPrepare(String token, String tokenId, String owner, String salt, FcUser user, Integer type) {
        String buyFee = this.configService.getValue(SysConfConstant.BUYER_FEE);
        if (null == buyFee) {
            logger.error("系统未设置buy fee");
            return ResponseUtil.fail(-1, "buy fee is not set");
        }
        String sellFee = this.configService.getValue(SysConfConstant.SELLER_FEE);
        if (null == sellFee) {
            logger.error("系统未设置sell fee");
            return ResponseUtil.fail(-1, "sell fee is not set");
        }
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        FcPayToken payToken = null;
        if (type.intValue() == 1) {
            wrapper.eq(FcOrder.SELL_TOKEN, token)
                    .eq(FcOrder.SELL_TOKEN_ID, tokenId)
                    .eq(FcOrder.OWNER, owner)
                    .eq(FcOrder.SALT, salt)
                    .eq(BaseEntity.DELETED, false);
        } else if (type.intValue() == 2) {
            wrapper.eq(FcOrder.BUYER_TOKEN, token)
                    .eq(FcOrder.BUYER_TOKEN_ID, tokenId)
                    .eq(FcOrder.OWNER, owner)
                    .eq(FcOrder.SALT, salt)
                    .eq(BaseEntity.DELETED, false);
        }
        FcOrder order = this.baseService.getByCondition(FcOrder.class, wrapper);
        if (null == order) {
            return ResponseUtil.fail(-1, "Order is not exist");
        }
        if (null != order.getStatus() && (order.getStatus().intValue() == -1 || order.getStatus().intValue() == 2)) {
            return ResponseUtil.fail(-1, "Order is finished");
        }
        if (null != order.getExpired() && order.getExpired().booleanValue()) {
            return ResponseUtil.fail(-1, "Order is expired");
        }

        PrepareOrderInfo info = new PrepareOrderInfo(order);
        info.setSellFee(sellFee);
        info.setBuyFee(buyFee);
        if (type.intValue() == 1) {
            payToken = this.payTokenService.getPayToken(info.getBuyToken());
        } else if (type.intValue() == 2) {
            payToken = this.payTokenService.getPayToken(info.getSellToken());
        }

        if (null == payToken) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }
        String sellValue = this.payTokenService.getErcValue(Integer.valueOf(info.getSellType()), info.getSellValue(), info.getSellToken());
        if (null == sellValue) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }
        info.setSellValue(sellValue);
        String buyValue = this.payTokenService.getErcValue(Integer.valueOf(info.getBuyType()), info.getBuyValue(), info.getBuyToken());
        if (null == buyValue) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }
        info.setBuyValue(buyValue);
        SignOrderInfo signOrder = new SignOrderInfo(info.getOwner(), info.getSalt(), info.getSellToken(),
				info.getSellTokenId(), info.getSellType(), info.getBuyToken(), info.getBuyTokenId(), info.getBuyType(),
				info.getSellValue(), info.getBuyValue(), info.getSellFee());
        signOrder = DappCryptoUtil.orderSign(signOrder, new Integer(buyFee));
        info.setSignature(signOrder.getSignature());
        info.setR(signOrder.getR());
        info.setS(signOrder.getS());
        info.setV(signOrder.getV());
        info.setSalt(signOrder.getSalt());
        info.setBuyFee(buyFee.toString());
        if (this.redisService.set(user.getAddress(), info, 1800L)) {
            return ResponseUtil.ok(info);
        } else {
            return ResponseUtil.serious();
        }
    }


    public Object get(String caddress, String tokenId, String owner, Integer orderType) {
        FcOrder order = this.findByParams(caddress, tokenId, owner, orderType);
        if (null == order) {
            return ResponseUtil.fail(-1, "order not exist or order is finished");
        }
        return ResponseUtil.ok(order);
    }

    public FcOrder findByParams(String caddress, String tokenId, String owner, Integer orderType) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        if (orderType.intValue() == 1) {
            wrapper.eq(FcOrder.SELL_TOKEN, caddress)
                    .eq(FcOrder.SELL_TOKEN_ID, tokenId)
                    .eq(FcOrder.OWNER, owner)
                    .eq(FcOrder.ORDER_TYPE, orderType)
                    .eq(FcOrder.EXPIRED, false)
                    .eq(FcOrder.STATUS, 0)
                    .eq(BaseEntity.DELETED, false);
        } else if (orderType.intValue() == 2) {
            wrapper.eq(FcOrder.BUYER_TOKEN, caddress)
                    .eq(FcOrder.BUYER_TOKEN_ID, tokenId)
                    .eq(FcOrder.OWNER, owner)
                    .eq(FcOrder.ORDER_TYPE, orderType)
                    .eq(FcOrder.EXPIRED, false)
                    .eq(FcOrder.STATUS, 0)
                    .eq(BaseEntity.DELETED, false);
        } else {
            return null;
        }
        return this.baseService.getByCondition(FcOrder.class, wrapper);
    }

    public List<FcOrder> findActiveBids(String caddress, String tokenId) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.BUYER_TOKEN, caddress)
                .eq(FcOrder.BUYER_TOKEN_ID, tokenId)
                .eq(FcOrder.ORDER_TYPE, 2)
                .eq(FcOrder.EXPIRED, false)
                .eq(FcOrder.STATUS, 0)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.findByCondition(FcOrder.class, wrapper);
    }

    public IPage<FcOrder> findListByUserAddress(String address, IPage<FcOrder> pageInfo) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.OWNER, address)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.findByPage(FcOrder.class, wrapper, pageInfo);
    }
}
