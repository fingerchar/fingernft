package com.fingerchar.api.service;

import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.OrderStatus;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.*;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.dto.ConfigDeploy;
import com.fingerchar.db.dto.PrepareOrderInfo;
import com.fingerchar.db.dto.SignOrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Service
public class FcOrderService{

    public static final Logger logger = LoggerFactory.getLogger(FcOrderService.class);

    @Autowired
    FcNoticeService noticeService;

    @Autowired
    FcPayTokenManager payTokenManager;

    @Autowired
    FcSystemConfigManager systemConfigManager;

    @Autowired
    FcOrderManager orderManager;

    @Autowired
    FcContractManager contractManager;

    @Autowired
    FcContractNftManager contractNftManager;

    @Autowired
    FcNftItemsManager nftItemsManager;

    @Autowired
    FcContractService contractService;

    @Autowired
    IBaseService baseService;

    @Transactional(rollbackFor = Exception.class)
    public Object addOrder(PrepareOrderInfo order) {
        CommonStatus status = CommonStatus.getStatusByType(order.getType());
        switch (status) {
            case SALE:
            case EDIT_SALE:
                return processSell(order);
            case BID:
            case EDIT_BID:
                return processBid(order);
        }
        return ResponseUtil.fail(-1, "order type is incorrect");
    }

    private Object processBid(PrepareOrderInfo info) {
        FcContract contract = this.contractManager.get(info.getBuyToken());
        if (null == contract) {
            return ResponseUtil.fail(-1, "nft contract is not existed");
        }

        FcContractNft nft = this.contractNftManager.get(info.getBuyToken(), info.getBuyTokenId());
        if (null == nft) {
            return ResponseUtil.fail(-1, "Unkown nft");
        }

        if (new BigInteger("1").compareTo(new BigInteger(info.getBuyValue())) < 0) {
            return ResponseUtil.fail(-1, "no enough items for bid");
        }

        FcPayToken payToken = this.payTokenManager.get(info.getSellToken());
        if (null == payToken) {
            return ResponseUtil.fail(-1, "paytoken is not existed");
        }

        FcOrder order = this.orderManager.getBuyerOrder(info.getBuyToken(), info.getBuyTokenId(), info.getOwner());

        if (info.getType().equals(6)) {
            if (null == order) {
                return ResponseUtil.fail(-1, "Bid is not existed or order is canceled");
            }
            BigDecimal oldValue = new BigDecimal(order.getSellValue()).divide(new BigDecimal(order.getBuyerValue()), RoundingMode.HALF_UP);
            BigDecimal newValue = new BigDecimal(info.getSellValue()).divide(new BigDecimal(info.getBuyValue()), RoundingMode.HALF_UP);
            if(oldValue.compareTo(newValue) >= 0){
                return ResponseUtil.fail(-1, "New price must greater then the old price");
            }
        } else {
            if (null != order) {
                return ResponseUtil.fail(-1, "Bid is existed, please cancel it first");
            }
        }
        info.setBuyType(ContractType.ERC721.getType().toString());

        Integer count = this.contractNftManager.bid(info);
        if(count.equals(0)){
            return ResponseUtil.fail(-1, "add bid order fail");
        }
        return ResponseUtil.ok();
    }

    private Object processSell(PrepareOrderInfo info) {
        FcContract contract = this.contractManager.get(info.getSellToken());
        if (null == contract) {
            return ResponseUtil.badArgumentValue();
        }

        FcContractNft nft = this.contractNftManager.get(info.getSellToken(), info.getSellTokenId());
        if (null == nft) {
            return ResponseUtil.fail(-1, "Then token is not existed or bured");
        }

        FcNftItems nftItems = this.nftItemsManager.get(info.getSellToken(), info.getSellTokenId(), info.getOwner());

        if (null == nftItems) {
            return ResponseUtil.fail(-1, "Then token is not existed or owner is not you");
        }
        if (nftItems.getIsSync() &&
                !new BigInteger("1").equals(new BigInteger(info.getSellValue()))
        ) {
            return ResponseUtil.fail(-1, "no enough quantity");
        }

        FcPayToken payToken = this.payTokenManager.get(info.getBuyToken());
        if (null == payToken) {
            return ResponseUtil.fail(-1, "Unkown pay type");
        }

        FcOrder order = this.orderManager.getSellOrder(info.getSellToken(), info.getSellTokenId(), info.getOwner());
        if (info.getType().equals(2)) {
            if (null == order) {
                return ResponseUtil.fail(-1, "Order is not existed or order is canceled");
            }
            BigDecimal oldValue = new BigDecimal(order.getBuyerValue()).divide(new BigDecimal(order.getSellValue()), RoundingMode.HALF_UP);
            BigDecimal newValue = new BigDecimal(info.getBuyValue()).divide(new BigDecimal(info.getSellValue()), RoundingMode.HALF_UP);
            if(oldValue.compareTo(newValue) <= 0) {
                return ResponseUtil.fail(-1, "New price must less then the old price");
            }

        } else {
            if (null != order) {
                return ResponseUtil.fail(-1, "This nft is on sale already");
            }
        }

        info.setSellType(ContractType.ERC721.getType().toString());
        Integer count = this.contractNftManager.sale(info);
        if(count.equals(0)){
            return ResponseUtil.fail(-1, "add sale order fail");
        }
        return ResponseUtil.ok();
    }


    public Object prepareOrder(PrepareOrderInfo info, FcUser user) {
        String sellFee = this.systemConfigManager.getKeyValue(SysConfConstant.SELLER_FEE);
        if (null == sellFee) {
            return ResponseUtil.fail(-1, "unset sellFee");
        }
        if(info.getType().equals(OrderStatus.SALE.getType())){
            info.setSellType(ContractType.ERC721.getType().toString());
        }else{
            info.setBuyType(ContractType.ERC721.getType().toString());
        }

        info.setSellFee(sellFee);
        SignOrderInfo signOrder = new SignOrderInfo(info);
        signOrder = DappCryptoUtil.prepareOrder(signOrder);
        info.setMessage(signOrder.getSignature());
        info.setSalt(signOrder.getSalt());
        return ResponseUtil.ok(info);
    }


    public Object buyPrepare(PrepareOrderInfo orderInfo) {
        FcOrder order = null;
        if (orderInfo.getType().intValue() == 1) {
            order = this.orderManager.getSellOrder(orderInfo.getSellToken(), orderInfo.getSellTokenId(), orderInfo.getOwner(), orderInfo.getSalt());
        } else if (orderInfo.getType().intValue() == 2) {
            order = this.orderManager.getBuyerOrder(orderInfo.getBuyToken(), orderInfo.getBuyTokenId(), orderInfo.getOwner(), orderInfo.getSalt());
        }
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
        info.setSellFee(order.getSellFee().toString());
        info.setBuyFee(order.getBuyFee().toString());
        SignOrderInfo signOrder = new SignOrderInfo(info);

        ConfigDeploy configDeploy = this.systemConfigManager.getConfigDeploy();
        String buyerFeeKey = configDeploy.getBuyerFeeKey();
        signOrder = DappCryptoUtil.orderSign(signOrder, order.getBuyFee(), buyerFeeKey);
        info.setSignature(signOrder.getSignature());
        info.setR(signOrder.getR());
        info.setS(signOrder.getS());
        info.setV(signOrder.getV());
        info.setSalt(signOrder.getSalt());
        return ResponseUtil.ok(info);
    }


    public Object get(String caddress, String tokenId, String owner, Integer orderType) {
        FcOrder order = null;
        if(orderType.equals(OrderStatus.SALE.getType())) {
            order = this.orderManager.getActiveSellOrder(caddress, tokenId, owner, orderType);
        }else if(orderType.equals(OrderStatus.BID.getType())){
            order = this.orderManager.getActiveBuyerOrder(caddress, tokenId, owner, orderType);
        }
        if (null == order) {
            return ResponseUtil.fail(-1, "order not exist or order is finished");
        }
        return ResponseUtil.ok(order);
    }

}
