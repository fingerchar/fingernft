package com.fingerchar.api.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.web3j.abi.datatypes.generated.Uint256;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.api.constant.CommonStatus;
import com.fingerchar.api.constant.NoticeType;
import com.fingerchar.api.constant.RedisConstant;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.util.NftDappEventUtils;
import com.fingerchar.api.vo.EventValuesExt;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcOrderLogExtMapper;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcOrderLog;
import com.fingerchar.db.domain.FcPayToken;
import com.fingerchar.db.domain.FcSystem;
import com.fingerchar.db.domain.FcTxOrder;

@Service
public class NftEventService {

    private static final Logger logger = LoggerFactory.getLogger(NftEventService.class);

    public static final String ZERO_ADDRESS = "0x0000000000000000000000000000000000000000";

    @Autowired
    private IBaseService baseService;

    @Autowired
    FcOrderLogExtMapper orderLogExtMapper;

    @Autowired
    FcNoticeService noticeService;

    @Autowired
    FcSystemConfigService configService;
    
    @Autowired
    FcRedisService redisService;

    @Transactional(rollbackFor = Exception.class)
    public void processEvent(Map<String, List<EventValuesExt>> map, String block) throws Exception {
        if (map.isEmpty()) {
        	this.saveLastBlock(block);
            return;
        }
        
        List<FcOrderLog> list = new ArrayList<>();
        List<EventValuesExt> valueList = sortLog(map.get(NftDappEventUtils.BUY_EVENT.getName()));
        Map<String, Integer> orderTypeMap = new HashMap<>();
        if(null != valueList && !valueList.isEmpty()) {
            this.processBuyEvent(valueList, list, orderTypeMap);
        }
        
        String exchangeToken = this.configService.getValue(SysConfConstant.NFT_EXCHANGE);
        if (StringUtils.isEmpty(exchangeToken)) {
            throw new Exception("Unkown nft exchange token");
        }
        valueList = sortLog(map.get(NftDappEventUtils.CANCEL_EVENT.getName()));
        if(null != valueList && !valueList.isEmpty()) {
            this.processCancelEvent(valueList, list);
        }
        
        valueList = sortLog(map.get(NftDappEventUtils.TRANSFER_EVENT.getName()));
        if(null != valueList && !valueList.isEmpty()) {
        	Map<String, List<EventValuesExt>> valueMap = new HashMap<>();
        	valueList.stream().forEach(vo -> {
                if (null == valueMap.get(vo.getTxHash())) {
                    valueMap.put(vo.getTxHash(), new ArrayList<>());
                }
                valueMap.get(vo.getTxHash()).add(vo);
            });
            this.processTransferEvent(valueMap, list, orderTypeMap);
        }
        
        valueList = sortLog(map.get(NftDappEventUtils.SECONDARYSALEFEES_EVENT.getName()));
        if(null != valueList && !valueList.isEmpty()) {
            this.processRoyaltiesEvent(valueList);
        }
        this.addOrderLog(list);
        this.saveLastBlock(block);
    }
    
    private List<EventValuesExt> sortLog(List<EventValuesExt> list) {
        if (null == list) {
            return null;
        }
        Collections.sort(list, new Comparator<EventValuesExt>() {
            public int compare(EventValuesExt val1, EventValuesExt val2) {
                return val1.getBlockNumber().compareTo(val2.getBlockNumber());
            }
        });
        return list;
    }

    
    @Transactional(rollbackFor = Exception.class)
    private void processRoyaltiesEvent(List<EventValuesExt> valueList) {
        if (valueList.isEmpty()) {
            return;
        }
        int len = valueList.size();
        for (int i = 0; i < len; i++) {
            this.processRoyaltiesEvent(valueList.get(i));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void processRoyaltiesEvent(EventValuesExt eventValues) {
        BigInteger tokenId = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
        @SuppressWarnings("unchecked")
        List<Uint256> bps = (List<Uint256>) eventValues.getNonIndexedValues().get(2).getValue();
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, eventValues.getAddress())
                .eq(FcContractNft.TOKEN_ID, tokenId.toString());
        List<FcContractNft> nftList = this.baseService.findByCondition(FcContractNft.class, wrapper);
        if (null == nftList || nftList.isEmpty()) {
            return;
        }
        Set<Long> idSet = nftList.stream().map(FcContractNft::getId).collect(Collectors.toSet());
        UpdateWrapper<FcContractNft> uwrapper = new UpdateWrapper<>();
        List<Long> bpsTemp = bps.stream().map(bi -> bi.getValue().longValueExact()).collect(Collectors.toList());
        uwrapper.set(FcContractNft.ROYALTIES, JSON.toJSONString(bpsTemp));
        uwrapper.in(BaseEntity.ID, idSet);
        this.baseService.updateByCondition(FcContractNft.class, uwrapper);
    }


    @Transactional(rollbackFor = Exception.class)
    private void addOrderLog(List<FcOrderLog> logList) {
        if (logList.size() > 0) {
            this.baseService.saveBatch(logList);
            Set<String> txHashList = logList.stream().map(FcOrderLog::getTxHash).collect(Collectors.toSet());
            if (txHashList.size() > 0) {
                UpdateWrapper<FcTxOrder> wrapper = new UpdateWrapper<>();
                wrapper.set(FcTxOrder.IS_SYNC, true);
                wrapper.in(FcTxOrder.TX_HASH, txHashList);
                this.baseService.updateByCondition(FcTxOrder.class, wrapper);
            }
        }
    }


    @Transactional(rollbackFor = Exception.class)
    private void processBuyEvent(List<EventValuesExt> buyList, List<FcOrderLog> logList, Map<String, Integer> orderTypeMap) throws Exception {
        if (buyList.isEmpty()) {
            return;
        }
        int len = buyList.size();
        for (int i = 0; i < len; i++) {
            this.processBuyEvent(buyList.get(i), logList, orderTypeMap);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void processBuyEvent(EventValuesExt eventValue, List<FcOrderLog> logList, Map<String, Integer> orderTypeMap) throws Exception {
        String txHash = eventValue.getTxHash();
        
        FcTxOrder txOrder = this.getByHash(txHash);
        if (null != txOrder) {
            logger.warn("tx_hash为" + txHash + "已经处理！");
            return;
        }
        String sellToken = (String) eventValue.getIndexedValues().get(0).getValue();
        BigInteger sellTokenId = (BigInteger) eventValue.getIndexedValues().get(1).getValue();
        BigInteger sellValue = (BigInteger) eventValue.getNonIndexedValues().get(0).getValue();
        String owner = (String) eventValue.getNonIndexedValues().get(1).getValue();
        String buyToken = (String) eventValue.getNonIndexedValues().get(2).getValue();
        BigInteger buyTokenId = (BigInteger) eventValue.getNonIndexedValues().get(3).getValue();
        BigInteger buyValue = (BigInteger) eventValue.getNonIndexedValues().get(4).getValue();
        String buyer = (String) eventValue.getNonIndexedValues().get(5).getValue();
        BigInteger salt = (BigInteger) eventValue.getNonIndexedValues().get(7).getValue();

        FcOrder order = null;
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.SELL_TOKEN, sellToken)
                .eq(FcOrder.SELL_TOKEN_ID, sellTokenId.toString())
                .eq(FcOrder.SALT, salt.toString())
                .eq(FcOrder.OWNER, owner)
                .eq(FcOrder.BUYER_TOKEN, buyToken)
                .eq(FcOrder.BUYER_TOKEN_ID, buyTokenId.toString());
        order = this.baseService.getByCondition(FcOrder.class, wrapper);
        boolean isMissing = false;
        if (null == order) {
            logger.warn("要完成的order未找到, 开始生成order信息=>" + JSON.toJSONString(eventValue));
            order = this.createMissingOrder(sellToken, sellValue, sellTokenId, buyToken, buyValue, buyTokenId, owner, salt);
            isMissing = true;
        } else {
            if (!isMissing) {
                if (order.getDeleted()) {
                    logger.warn("此order已经处理=>" + JSON.toJSONString(eventValue));
                    return;
                }
            }
            QueryWrapper<FcOrderLog> logWrapper = new QueryWrapper<>();
            logWrapper.eq(FcOrderLog.TX_HASH, txHash);
            FcOrderLog log = this.baseService.getByCondition(FcOrderLog.class, logWrapper);
            if (null != log) {
                logger.warn("此order已经处理=>" + JSON.toJSONString(eventValue));
                return;
            }

            if (order.getOrderType().intValue() == 1) {
                orderTypeMap.put(txHash, CommonStatus.getStatusByName("Buy").getType());
                this.processBuyEvent(owner, sellToken, sellTokenId, txHash, buyer, null != txOrder, logList, order, eventValue.getTimestamp().longValueExact());
            } else {
                orderTypeMap.put(txHash, CommonStatus.getStatusByName("Accept bid").getType());
                this.processBuyEvent(owner, buyToken, buyTokenId, txHash, buyer, null != txOrder, logList, order, eventValue.getTimestamp().longValueExact());
            }
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    private void processBuyEvent(String owner, String token, BigInteger tokenId, String txHash, String buyer, Boolean hasTxOrder, List<FcOrderLog> logList, FcOrder order, Long timestamp) {
        UpdateWrapper<FcOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set(FcOrder.SELLS, 1);
        updateWrapper.eq(FcOrder.ID, order.getId());
        updateWrapper.set(FcOrder.STATUS, 1).set(FcOrder.EXPIRED, true).set(FcOrder.DELETED, true);
        if (order.getOrderType().intValue() == 2) {
            this.expiredBidLog(order.getId());
        }
        this.baseService.updateByCondition(FcOrder.class, updateWrapper);
        
        QueryWrapper<FcContractNft> nftQueryWrapper = new QueryWrapper<>();
        nftQueryWrapper.eq(FcContractNft.ADDRESS, token).eq(FcContractNft.TOKEN_ID, tokenId);
        FcContractNft nft = this.baseService.getByCondition(FcContractNft.class, nftQueryWrapper);
        Integer type = null;
        String image = null;
        String name = null;
        if (null != nft) {
            image = nft.getImgUrl();
            name = nft.getName();
        }
        if (order.getOrderType().intValue() == 1) {
            type = CommonStatus.getStatusByName("Buy").getType();
        } else {
            type = CommonStatus.getStatusByName("Accept bid").getType();
        }
        logList.add(this.createBuyLog(owner, token, tokenId, buyer, txHash, type, order, timestamp));

        if (!hasTxOrder) {
            this.addTxOrder(txHash);
        }
        this.addBuyNotice(order, txHash, buyer, image, name);
    }
    
    @Transactional(rollbackFor = Exception.class)
    private FcOrder createMissingOrder(String sellToken, BigInteger sellValue, BigInteger sellTokenId, String buyToken, BigInteger buyValue, BigInteger buyTokenId, String owner, BigInteger salt) {
        FcPayToken token = this.getPayToken(sellToken);
        Integer type = 1;
        if (null == token) {
            type = 2;
        }
        FcOrder order = new FcOrder();
        order.setBuyerToken(buyToken);
        order.setBuyerTokenId(buyTokenId.toString());
        order.setBuyerValue(buyValue.toString());
        order.setOwner(owner);
        order.setSalt(salt.toString());
        order.setSellToken(sellToken);
        order.setSellTokenId(sellTokenId.toString());
        order.setSellValue(sellValue.toString());
        order.setStatus(1);
        order.setExpired(true);
        order.setDeleted(true);
        order.setOrderType(type);
        this.baseService.save(order);
        return order;
    }
    
    @Transactional(rollbackFor = Exception.class)
    private void addBuyNotice(FcOrder order, String txHash, String buyer, String image, String name) {
        try {
            Map<String, Object> content = new HashMap<>();
            content.put("price", order.getBuyerValue());
            content.put("sellerAddr", order.getOwner());
            content.put("buyerAddr", buyer);
            content.put("txHash", txHash);
            content.put("amount", String.valueOf(1));
            String noticeType = NoticeType.getNoticeTypeByName("Trade").getType().toString();
            if (order.getOrderType() == 1) {
                FcPayToken payToken = this.getPayToken(order.getBuyerToken());
                content.put("priceUint", payToken.getSymbol());
                content.put("quantity", order.getSellValue());
                content.put("sellerAddr", order.getOwner());
                content.put("token", order.getSellToken());
                content.put("tokenId", order.getSellTokenId());
                Integer type = CommonStatus.getStatusByName("Buy").getType();
                this.noticeService.insertNotice(content, order.getOwner(), type.toString(), image, name, noticeType, buyer);
                type = CommonStatus.getStatusByName("Bought").getType();
                this.noticeService.insertNotice(content, buyer, type.toString(), image, name, noticeType, buyer);
            } else {
                FcPayToken payToken = this.getPayToken(order.getSellToken());
                content.put("priceUint", payToken.getSymbol());
                content.put("quantity", order.getBuyerValue());
                content.put("buyerAddr", buyer);
                content.put("token", order.getBuyerToken());
                content.put("tokenId", order.getBuyerTokenId());
                Integer type = CommonStatus.getStatusByName("Accept bid").getType();
                this.noticeService.insertNotice(content, order.getOwner(), type.toString(), image, name, noticeType, buyer);
                type = CommonStatus.getStatusByName("Bidden").getType();
                this.noticeService.insertNotice(content, buyer, type.toString(), image, name, noticeType, buyer);
            }
        } catch (Exception e) {
            logger.error("插入购买消息异常", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void expiredSaleOrder(String sellToken, Long sellTokenId, String owner) {
        UpdateWrapper<FcOrder> wrapper = new UpdateWrapper<>();
        wrapper.eq(FcOrder.SELL_TOKEN, sellToken)
                .eq(FcOrder.ORDER_TYPE, 1)
                .eq(FcOrder.SELL_TOKEN_ID, sellTokenId)
                .eq(FcOrder.OWNER, owner)
                .eq(BaseEntity.DELETED, false)
                .eq(FcOrder.EXPIRED, false);
        wrapper.set(BaseEntity.DELETED, true);
        wrapper.set(FcOrder.EXPIRED, true);
        this.baseService.updateByCondition(FcOrder.class, wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    private void expiredBidLog(Long orderId) {
        this.orderLogExtMapper.updateByOrderId(orderId);
    }

    @Transactional(rollbackFor = Exception.class)
    private FcOrderLog createBuyLog(String owner, String token, BigInteger tokenId, String buyer, String txHash, Integer type, FcOrder order, Long timestamp) {
        FcOrderLog log = new FcOrderLog();
        log.setFrom(owner);
        log.setOrderId(order.getId());
        log.setPreLogId(0L);
        log.setTo(buyer);
        log.setTxHash(txHash);
        log.setType(type);
        log.setToken(token);
        log.setTokenId(tokenId.toString());
        order.setSells(1L);
        log.setContent(JSON.toJSONString(order));
        log.setExpired(false);
        log.setDeleted(false);
        log.setCreateTime(timestamp);
        log.setUpdateTime(timestamp);
        return log;
    }

    @Transactional(rollbackFor = Exception.class)
    private void processCancelEvent(List<EventValuesExt> cancelList, List<FcOrderLog> logList) throws Exception {
        if (cancelList.isEmpty()) {
            return;
        }
        int len = cancelList.size();
        for (int i = 0; i < len; i++) {
            this.processCancelEvent(cancelList.get(i), logList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private void processCancelEvent(EventValuesExt eventValue, List<FcOrderLog> logList) throws Exception {
        String txHash = eventValue.getTxHash();
        FcTxOrder txOrder = this.getByHash(txHash);
        if (null != txOrder) {
            logger.warn("tx_hash为" + txHash + "已经处理！");
            return;
        }
        String sellToken = (String) eventValue.getIndexedValues().get(0).getValue();
        BigInteger sellTokenId = (BigInteger) eventValue.getIndexedValues().get(1).getValue();
        String owner = (String) eventValue.getNonIndexedValues().get(0).getValue();
        String buyToken = (String) eventValue.getNonIndexedValues().get(1).getValue();
        BigInteger buyTokenId = (BigInteger) eventValue.getNonIndexedValues().get(2).getValue();
        BigInteger salt = (BigInteger) eventValue.getNonIndexedValues().get(3).getValue();
        FcOrder order = null;
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.SELL_TOKEN, sellToken)
                .eq(FcOrder.SELL_TOKEN_ID, sellTokenId.toString())
                .eq(FcOrder.SALT, salt.toString())
                .eq(FcOrder.OWNER, owner)
                .eq(FcOrder.BUYER_TOKEN, buyToken)
                .eq(FcOrder.BUYER_TOKEN_ID, buyTokenId.toString());
        wrapper.orderByDesc(BaseEntity.ID);
        order = this.baseService.getByCondition(FcOrder.class, wrapper);
        if (null == order) {
            logger.warn("要取消的order未找到,跳过, order信息=>" + JSON.toJSONString(eventValue));
        } else {
            if (order.getDeleted()) {
                logger.warn("此order已经处理=>" + JSON.toJSONString(eventValue));
                return;
            }
            Integer type = null;
            if (order.getOrderType().intValue() == 1) {
                type = CommonStatus.getStatusByName("Cancel sale").getType();
                this.processCancelEvent(owner, sellToken, sellTokenId, txHash, order, null != txOrder);

            } else {
                type = CommonStatus.getStatusByName("Cancel bid").getType();
                this.processCancelEvent(owner, buyToken, buyTokenId, txHash, order, null != txOrder);
            }

            UpdateWrapper<FcOrder> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq(BaseEntity.ID, order.getId());
            updateWrapper.set(FcOrder.EXPIRED, true)
                    .set(FcOrder.STATUS, 2)
                    .set(BaseEntity.DELETED, true);
            this.baseService.updateByCondition(FcOrder.class, updateWrapper);
            this.orderLogExtMapper.updateByOrderId(order.getId());
            logList.add(this.createCancelLog(owner, txHash, type, order, eventValue.getTimestamp().longValueExact()));
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    private void processCancelEvent(String owner, String token, BigInteger tokenId, String txHash, FcOrder order, boolean hasTxOrder) {
        FcContractNft nft = null;
        QueryWrapper<FcContractNft> nftQueryWrapper = new QueryWrapper<>();
        nftQueryWrapper.eq(FcContractNft.ADDRESS, token)
                .eq(FcContractNft.TOKEN_ID, tokenId.toString())
                .eq(BaseEntity.DELETED, false);
        nft = this.baseService.getByCondition(FcContractNft.class, nftQueryWrapper);
        Integer type = null;
        FcPayToken payToken = null;
        Map<String, Object> content = new HashMap<>();
        if (order.getOrderType().intValue() == 1) {
            if (null == nft) {
                logger.warn("nft未找到:token=>" + token + "; tokenId=>" + tokenId.toString());
                return;
            }
            QueryWrapper<FcNftItems> itemsQueryWrapper = new QueryWrapper<>();
            itemsQueryWrapper.eq(FcNftItems.NFT_ID, nft.getId())
                    .eq(FcNftItems.ADDRESS, token)
                    .eq(FcNftItems.ITEM_OWNER, owner)
                    .eq(BaseEntity.DELETED, false);
            FcNftItems items = this.baseService.getByCondition(FcNftItems.class, itemsQueryWrapper);
            if (null == items) {
                logger.warn("nft-item未找到:token=>" + token + "; tokenId=>" + tokenId.toString() + "; owner=>" + owner);
                return;
            }
            UpdateWrapper<FcNftItems> itemsUpdateWrapper = new UpdateWrapper<>();
            itemsUpdateWrapper.set(FcNftItems.PRICE, "0")
                    .set(FcNftItems.USDT_PRICE, 0L)
                    .set(FcNftItems.ONSELL, false)
                    .set(FcNftItems.SELL_QUANTITY, 0)
                    .set(FcNftItems.PAYTOKEN_ADDRESS, "");
            itemsUpdateWrapper.eq(BaseEntity.ID, items.getId());
            this.baseService.updateByCondition(FcNftItems.class, itemsUpdateWrapper);
            type = CommonStatus.getStatusByName("Cancel sale").getType();
            payToken = this.getPayToken(order.getBuyerToken());
            content.put("price", order.getBuyerValue());
            content.put("quantity", order.getSellValue());
        } else {
            UpdateWrapper<FcOrderLog> logUpdateWrapper = new UpdateWrapper<>();
            logUpdateWrapper.eq(FcOrderLog.TOKEN, token)
                    .eq(FcOrderLog.TOKEN_ID, tokenId.toString())
                    .eq(FcOrderLog.FROM, owner)
                    .eq(FcOrderLog.EXPIRED, false);
            logUpdateWrapper.set(FcOrderLog.EXPIRED, true);
            this.baseService.updateByCondition(FcOrderLog.class, logUpdateWrapper);

            type = CommonStatus.getStatusByName("Cancel bid").getType();
            payToken = this.getPayToken(order.getSellToken());
            content.put("price", order.getSellValue());
            content.put("quantity", order.getBuyerValue());
        }
        content.put("priceUint", payToken.getSymbol());
        content.put("sellerAddr", order.getOwner());
        content.put("token", token);
        content.put("tokenId", tokenId);
        content.put("txHash", txHash);
        this.addCancelNotice(content, nft, type, owner);
        if (!hasTxOrder) {
            this.addTxOrder(txHash);
        }
    }

    @Transactional
    private void addCancelNotice(Map<String, Object> content, FcContractNft nft, Integer type, String owner) {
        try {
            String image = null;
            String name = null;
            String noticeType = NoticeType.getNoticeTypeByName("Trade").getType().toString();
            if (null != nft) {
                image = nft.getImgUrl();
                name = nft.getName();
                QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
                wrapper.eq(FcNftItems.NFT_ID, nft.getId())
                        .eq(BaseEntity.DELETED, false);
                FcNftItems item = this.baseService.getByCondition(FcNftItems.class, wrapper);
                this.noticeService.insertNotice(content, item.getItemOwner(), type.toString(), image, name, noticeType, owner);
            }
            this.noticeService.insertNotice(content, owner, type.toString(), image, name, noticeType, owner);
        } catch (Exception e) {
            logger.error("插入取消消息异常", e);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    private FcOrderLog createCancelLog(String owner, String txHash, Integer type, FcOrder order, Long timestamp) {
        FcOrderLog log = new FcOrderLog();
        log.setFrom(owner);
        log.setOrderId(order.getId());
        log.setPreLogId(0L);
        log.setTo("");
        if (order.getOrderType().intValue() == 1) {
            log.setToken(order.getSellToken());
            log.setTokenId(order.getSellTokenId());
        } else {
            log.setToken(order.getBuyerToken());
            log.setTokenId(order.getBuyerTokenId());
        }
        log.setTxHash(txHash);
        log.setContent(JSON.toJSONString(order));
        log.setType(type);
        log.setExpired(false);
        log.setDeleted(false);
        log.setCreateTime(timestamp);
        log.setUpdateTime(timestamp);
        return log;
    }

    @Transactional(rollbackFor = Exception.class)
    private void processTransferEvent(Map<String, List<EventValuesExt>> valueMap, List<FcOrderLog> logList, Map<String, Integer> orderTypeMap) throws Exception {
        if (valueMap.isEmpty()) {
            return;
        }
        Iterator<String> it = valueMap.keySet().iterator();
        List<EventValuesExt> transferList = null;
        String txHash = null;
        int len = 0;
        while (it.hasNext()) {
            txHash = it.next();
            FcTxOrder txOrder = this.getByHash(txHash);
            if (null != txOrder && txOrder.getIsSync()) {
                logger.warn("tx_hash为" + txHash + "已经同步！");
                continue;
            }
            transferList = valueMap.get(txHash);
            len = transferList.size();
            for (int i = 0; i < len; i++) {
                this.processTransferEvent(transferList.get(i), logList, orderTypeMap);
            }
            Integer type = orderTypeMap.get(txHash);
            if (null == type) {
                if (null == txOrder) {
                    this.addTxOrder(txHash);
                }
            }
        }
    }
    
    @Transactional(rollbackFor = Exception.class)
    private void addTxOrder(String txHash) {
        FcTxOrder txOrder = new FcTxOrder();
        txOrder = new FcTxOrder();
        txOrder.setTxHash(txHash);
        txOrder.setIsSync(false);
        this.baseService.save(txOrder);
    }

    @Transactional(rollbackFor = Exception.class)
    private void processTransferEvent(EventValuesExt eventValues, List<FcOrderLog> logList, Map<String, Integer> orderTypeMap) throws Exception {
        String txHash = eventValues.getTxHash();
        logger.info("721转移事件");
        String from = (String) eventValues.getIndexedValues().get(0).getValue();
        String to = (String) eventValues.getIndexedValues().get(1).getValue();
        BigInteger tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        
        String address = eventValues.getAddress();
        Map<String, Object> content = new HashMap<>();
        content.put("token", address);
        content.put("tokenId", tokenId);
        content.put("from", from);
        content.put("to", to);
        content.put("txHash", txHash);
        Integer type = null;

        if (ZERO_ADDRESS.equals(from) && !ZERO_ADDRESS.equals(to)) {
            type = CommonStatus.getStatusByName("Mint").getType();
            if (this.processMint(address, tokenId, to, content, type, txHash, eventValues.getTimestamp().longValueExact())) {
                logList.add(this.createTransferLog(from, to, txHash, tokenId, type, eventValues, address));
            }
        } else if (!ZERO_ADDRESS.equals(from) && ZERO_ADDRESS.equals(to)) {
            type = CommonStatus.getStatusByName("Burn").getType();
            if (this.processBurn(address, tokenId, from, content, type)) {
                logList.add(this.createTransferLog(from, to, txHash, tokenId, type, eventValues, address));
            }
        } else {
            Integer orderType = orderTypeMap.get(txHash);
            boolean result = false;
            if (null == orderType) {
                result = this.processTransfer(address, tokenId, from, to, content, txHash, null, eventValues.getTimestamp().longValueExact());
            } else {
                result = this.processTransfer(address, tokenId, from, to, content, txHash, orderType, eventValues.getTimestamp().longValueExact());
            }
            if (result) {
                if (null == orderType) {
                    type = CommonStatus.getStatusByName("Transfer").getType();
                    logList.add(this.createTransferLog(from, to, txHash, tokenId, type, eventValues, address));
                }
            }
        }
    }

    

    private FcPayToken getPayToken(String token) {
        QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
        wrapper.eq(FcPayToken.ADDRESS, token);
        FcPayToken payToken = this.baseService.getByCondition(FcPayToken.class, wrapper);
        return payToken;
    }

    @Transactional(rollbackFor = Exception.class)
    private FcOrderLog createTransferLog(String from, String to, String txHash, BigInteger tokenId, Integer type, EventValuesExt eventvalue, String token) {
        FcOrderLog log = new FcOrderLog();
        log.setFrom(from);
        log.setOrderId(null);
        log.setPreLogId(0L);
        log.setTo(to);
        log.setToken(token);
        log.setContent(JSON.toJSONString(eventvalue));
        log.setTokenId(tokenId.toString());
        log.setTxHash(txHash);
        log.setType(type);
        log.setExpired(false);
        log.setDeleted(false);
        log.setCreateTime(eventvalue.getTimestamp().longValueExact());
        log.setUpdateTime(eventvalue.getTimestamp().longValueExact());
        return log;
    }

    @Transactional(rollbackFor = Exception.class)
    private boolean processTransfer(String address, BigInteger tokenId, String from, String to, Map<String, Object> content, String txHash, Integer orderType, Long time) throws Exception {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, address)
                .eq(FcContractNft.TOKEN_ID, tokenId.toString())
                .eq(BaseEntity.DELETED, false);
        FcContractNft nft = this.baseService.getByCondition(FcContractNft.class, wrapper);
        if (null == nft) {
            logger.warn("需要转移的nft未找到, 开始生成:token=>" + address + "; tokenId=>" + tokenId.toString());
            nft = this.createMissingNft(address, tokenId, from, to, txHash, time);
        }
        QueryWrapper<FcNftItems> itemsWrapper = new QueryWrapper<>();
        itemsWrapper.eq(FcNftItems.NFT_ID, nft.getId()).eq(FcNftItems.ITEM_OWNER, from);
        this.baseService.deleteByCondition(FcNftItems.class, itemsWrapper);
        this.incQuantity(nft, to);
        if (null == orderType) {
            try {
                Integer type = CommonStatus.getStatusByName("Transfer").getType();
                String noticeType = NoticeType.getNoticeTypeByName("Trade").getType().toString();
                this.noticeService.insertNotice(content, from, type.toString(), nft.getImgUrl(), nft.getName(), noticeType, from);
                type = CommonStatus.getStatusByName("Receive").getType();
                this.noticeService.insertNotice(content, to, type.toString(), nft.getImgUrl(), nft.getName(), noticeType, from);
            } catch (Exception e) {
                logger.error("插入转移消息异常", e);
            }
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    private FcContractNft createMissingNft(String address, BigInteger tokenId, String from, String to, String txHash, Long time) {
        FcContractNft nft = new FcContractNft();
        nft.setAddress(address);
        nft.setNftVerify(1);
        nft.setTokenId(tokenId.toString());
        nft.setQuantity(1L);
        nft.setIsSync(true);
        nft.setRoyalties("");
        nft.setDeleted(false);
        nft.setType(3);
        nft.setTxHash(txHash);
        Boolean isMint = NftEventService.ZERO_ADDRESS.equals(from) && !NftEventService.ZERO_ADDRESS.equals(to);
        if (isMint) {
            nft.setCreator(to);
        } else {
            nft.setCreator(from);
        }
        nft.setCategoryId(1L);
        nft.setCreateTime(time);
        nft.setUpdateTime(time);
        this.baseService.save(nft);
        return nft;
    }

    @Transactional(rollbackFor = Exception.class)
    private FcNftItems addMissingItems(FcContractNft nft, String from, String to, Long time) {
        FcNftItems items = new FcNftItems();
        items.setNftId(nft.getId());
        items.setName(nft.getName());
        items.setDescription(nft.getDescription());
        items.setImgUrl(nft.getImgUrl());
        if (ZERO_ADDRESS == from) {
            items.setItemOwner(to);
        } else {
            items.setItemOwner(from);
        }
        items.setQuantity(1L);
        items.setStorageId(nft.getStorageId());
        items.setDeleted(false);
        items.setAddress(nft.getAddress());
        items.setIsSync(true);
        items.setCategoryId(nft.getCategoryId());
        items.setOnsell(false);
        items.setCreateTime(time);
        items.setUpdateTime(time);
        items.setTokenId(nft.getTokenId());
        this.baseService.save(items);
        return items;
    }

    @Transactional(rollbackFor = Exception.class)
    private boolean processMint(String address, BigInteger tokenId, String to, Map<String, Object> content, Integer type, String txHash, Long time) throws Exception {
        logger.info("增发事件");
        QueryWrapper<FcContractNft> nftQueryWrapper = new QueryWrapper<>();
        nftQueryWrapper.eq(FcContractNft.ADDRESS, address)
                .eq(FcContractNft.TOKEN_ID, tokenId.toString())
                .eq(BaseEntity.DELETED, false);
        FcContractNft nft = this.baseService.getByCondition(FcContractNft.class, nftQueryWrapper);

        boolean isMissing = false;
        if (null == nft) {
            logger.warn("nft未找到:token=>" + address + "; tokenId=>" + tokenId);
            nft = this.createMissingNft(address, tokenId, ZERO_ADDRESS, to, txHash, time);
            isMissing = true;
        }
        if (nft.getIsSync() && !isMissing) {
            logger.warn("nft已经存在:token=>" + address + "; tokenId=>" + tokenId);
        }

        QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftItems.NFT_ID, nft.getId())
                .eq(FcNftItems.ADDRESS, nft.getAddress())
                .eq(FcNftItems.ITEM_OWNER, to)
                .eq(BaseEntity.DELETED, false);
        FcNftItems items = this.baseService.getByCondition(FcNftItems.class, wrapper);
        UpdateWrapper<FcContractNft> nftUpdateWrapper = new UpdateWrapper<>();
        UpdateWrapper<FcNftItems> itemsUpdateWrapper = new UpdateWrapper<>();
        if (null == items) {
            logger.warn("nft-item未找到:token=>" + address + "; tokenId=>" + tokenId + "; owner=>" + to);
            items = this.addMissingItems(nft, ZERO_ADDRESS, to, time);
        } else {
            if (!items.getItemOwner().equals(to)) {
                logger.warn("nft-item:token=>" + address + "; tokenId=>" + tokenId + "已经存在，但不属于" + to);
                return false;
            }
            if (isMissing) {
                itemsUpdateWrapper.set(FcNftItems.NFT_ID, nft.getId());
            }
            
        }
        nftUpdateWrapper.set(FcContractNft.IS_SYNC, true);
        nftUpdateWrapper.eq(BaseEntity.ID, nft.getId());
        this.baseService.updateByCondition(FcContractNft.class, nftUpdateWrapper);
        itemsUpdateWrapper.set(FcNftItems.IS_SYNC, true);
        itemsUpdateWrapper.eq(BaseEntity.ID, items.getId());
        this.baseService.updateByCondition(FcNftItems.class, itemsUpdateWrapper);
        try {
            String noticeType = NoticeType.getNoticeTypeByName("Trade").getType().toString();
            this.noticeService.insertNotice(content, to, type.toString(), nft.getImgUrl(), nft.getName(), noticeType, to);
        } catch (Exception e) {
            logger.error("插入增发消息异常", e);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    private boolean processBurn(String address, BigInteger tokenId, String from, Map<String, Object> content, Integer type) throws Exception {
        logger.info("销毁事件");
        QueryWrapper<FcContractNft> nftQueryWrapper = new QueryWrapper<>();
        nftQueryWrapper.eq(FcContractNft.ADDRESS, address)
                .eq(FcContractNft.TOKEN_ID, tokenId.toString())
                .eq(BaseEntity.DELETED, false);
        nftQueryWrapper.orderByDesc(BaseEntity.ID);
        FcContractNft nft = this.baseService.getByCondition(FcContractNft.class, nftQueryWrapper);
        if (null == nft) {
            logger.warn("nft未找到，或许已经销毁:token=>" + address + "; tokenId=>" + tokenId);
            return false;
        }
        if (nft.getDeleted()) {
            logger.warn("nft已经销毁:token=>" + address + "; tokenId=>" + tokenId);
        }

        this.baseService.deleteById(FcContractNft.class, nft.getId());
        
        QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftItems.NFT_ID, nft.getId())
                .eq(FcNftItems.ADDRESS, nft.getAddress())
                .eq(FcNftItems.ITEM_OWNER, from);
        wrapper.orderByDesc(BaseEntity.ID);
        FcNftItems items = this.baseService.getByCondition(FcNftItems.class, wrapper);
        if (null == items) {
            logger.warn("nft-item未找到或者已经销毁:token=>" + address + "; tokenId=>" + tokenId + "; owner=>" + from);
            return false;
        }
        if(items.getDeleted()) {
        	logger.warn("nft-item已经销毁:token=>" + address + "; tokenId=>" + tokenId + "; owner=>" + from);
        }
        this.baseService.deleteById(FcNftItems.class, items.getId());
        try {
            String noticeType = NoticeType.getNoticeTypeByName("Trade").getType().toString();
            this.noticeService.insertNotice(content, from, type.toString(), nft.getImgUrl(), nft.getName(), noticeType, from);
        } catch (Exception e) {
            logger.error("插入销毁信息异常", e);
        }
        return true;
    }
    
    @Transactional(rollbackFor = Exception.class)
    private boolean incQuantity(FcContractNft nft, String owner) {
        QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftItems.NFT_ID, nft.getId())
                .eq(FcNftItems.ITEM_OWNER, owner)
                .eq(BaseEntity.DELETED, false);
        FcNftItems items = this.baseService.getByCondition(FcNftItems.class, wrapper);
        if (null == items) {
            FcNftItems temp = new FcNftItems();
            temp.setAddress(nft.getAddress());
            temp.setCategoryId(nft.getCategoryId());
            temp.setDeleted(false);
            temp.setDescription(nft.getDescription());
            temp.setImgUrl(nft.getImgUrl());
            temp.setIsSync(nft.getIsSync());
            temp.setItemOwner(owner);
            temp.setName(nft.getName());
            temp.setNftId(nft.getId());
            temp.setOnsell(false);
            temp.setPaytokenAddress("");
            temp.setPrice("");
            temp.setQuantity(1L);
            temp.setSellQuantity(0L);
            temp.setSignature("");
            temp.setStorageId(nft.getStorageId());
            temp.setUsdtPrice(BigDecimal.ZERO);
            temp.setTokenId(nft.getTokenId());
            this.baseService.save(temp);
        }
        return true;
    }

    private FcTxOrder getByHash(String txHash) {
        QueryWrapper<FcTxOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcTxOrder.TX_HASH, txHash);
        return baseService.getByCondition(FcTxOrder.class, wrapper);
    }
    
    @Transactional(rollbackFor = Exception.class)
	void saveLastBlock(String block) {
		UpdateWrapper<FcSystem> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcSystem.KEY_NAME,SysConfConstant.LAST_BLOCK);
		wrapper.set(FcSystem.KEY_VALUE, block);
		baseService.updateByCondition(FcSystem.class, wrapper);
		this.redisService.set(RedisConstant.SYS_CONFIG_PRE + SysConfConstant.LAST_BLOCK, block);
	}
}
