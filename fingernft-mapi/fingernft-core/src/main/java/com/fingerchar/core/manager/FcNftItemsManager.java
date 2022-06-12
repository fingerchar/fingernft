package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.NoticeType;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.db.dao.ext.FcNftItemsExtMapper;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcPayToken;
import com.fingerchar.db.dto.ExchangeBuyLog;
import com.fingerchar.db.dto.ExchangeCancelLog;
import com.fingerchar.db.dto.NftInfo;
import com.fingerchar.db.dto.PrepareOrderInfo;
import com.fingerchar.db.vo.NftParamVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/3/21 18:51
 */
@Service
public class FcNftItemsManager {
    private static final Logger logger = LoggerFactory.getLogger(FcNftItemsManager.class);

    @Autowired
    IBaseService baseService;

    @Autowired
    FcNftItemsExtMapper nftItemsExtMapper;

    @Autowired
    FcPayTokenManager payTokenManager;

    @Autowired
    FcNoticeManager noticeManager;


    public List<FcNftItems> listByMulti(List<NftParamVO> nftParamVOList){
        if (null == nftParamVOList){
            return new ArrayList<>();
        }
        if (nftParamVOList.size()==0){
            return new ArrayList<>();
        }
        return this.nftItemsExtMapper.listByMulti(nftParamVOList);
    }

    public Integer create(NftInfo nft){
        FcNftItems item = new FcNftItems();
        item.setItemOwner(nft.getCreator());
        item.setAddress(nft.getAddress());
        item.setIsSync(nft.getIsSync());
        item.setCategoryId(nft.getCategoryId());
        item.setOnsell(false);
        item.setTokenId(nft.getTokenId());
        return this.baseService.save(item);
    }

    public Integer add( FcContractNft nft, String quanlity, String owner, Long time){
        FcNftItems nftItems = new FcNftItems();
        nftItems.setAddress(nft.getAddress());
        nftItems.setTokenId(nft.getTokenId());
        nftItems.setCategoryId(nft.getCategoryId());
        nftItems.setItemOwner(owner);
        nftItems.setIsSync(true);
        nftItems.setUpdateTime(time);
        nftItems.setCreateTime(time);
        return this.save(nftItems);
    }


    public List<FcNftItems> getList(String address, String tokenId){
        QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftItems.ADDRESS, address)
                .eq(FcNftItems.TOKEN_ID,tokenId)
                .eq(FcNftItems.DELETED, false);
        return this.baseService.findByCondition(FcNftItems.class, wrapper);
    }


    public FcNftItems get(String address, String tokenId, String owner){
        QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftItems.ADDRESS, address)
                .eq(FcNftItems.TOKEN_ID,tokenId)
                .eq(FcNftItems.ITEM_OWNER, owner)
                .eq(FcNftItems.DELETED, false);
        return this.baseService.getByCondition(FcNftItems.class, wrapper);
    }

    public Integer sale(PrepareOrderInfo orderInfo){
        FcNftItems nftItems = this.get(orderInfo.getSellToken(), orderInfo.getSellTokenId(), orderInfo.getOwner());
        FcPayToken payToken = this.payTokenManager.get(orderInfo.getBuyToken());
        nftItems.setOnsell(true);
        nftItems.setPaytokenAddress(payToken.getAddress());
        nftItems.setOnsellTime(System.currentTimeMillis()/1000);
        nftItems.setPaytokenName(payToken.getName());
        nftItems.setPaytokenDecimals(payToken.getDecimals());
        nftItems.setPaytokenSymbol(payToken.getSymbol());
        nftItems.setPrice(orderInfo.getBuyValue());
        return this.update(nftItems);
    }

    public void buy(ExchangeBuyLog log, FcOrder order){
        String address = null;
        String tokenId = null;
        String owner = null;
        Integer type = null;

        FcNftItems nftItems = null;
        if(order.getOrderType().equals(CommonStatus.SALE.getType())){
            address = log.getSellToken();
            tokenId = log.getSellTokenId().toString();
            owner = log.getOwner();
            type = CommonStatus.BUY.getType();
            nftItems = this.get(address, tokenId, owner);
            if(null == nftItems){
                logger.warn("buy nft-item未找到:address=>" + address + "; tokenId=>" + tokenId.toString() + "; owner=>" + owner);
                return;
            }
            nftItems.setOnsell(false);
            this.update(nftItems);
        }else{
            type = CommonStatus.ACCEPT_BID.getType();
        }

        Integer noticeType = NoticeType.TRADE.getType();
        String content = JSON.toJSONString(log);

        // notice seller
        this.noticeManager.add(content, log.getOwner(), type, noticeType, log.getBuyer());
        // notice buyer
        this.noticeManager.add(content, log.getBuyer(), type, noticeType, log.getBuyer());
    }

    public void cancel(ExchangeCancelLog log, FcOrder order){
        Integer noticeType = NoticeType.TRADE.getType();
        String content = JSON.toJSONString(log);
        if(order.getOrderType().equals(CommonStatus.SALE.getType())) {
            // cancel sale
            Integer type = CommonStatus.getStatusByName("Cancel sale").getType();
            FcNftItems nftItems = this.get(log.getSellToken(),log.getSellTokenId().toString(), log.getOwner() );
            if(null == nftItems){
                logger.warn("cancel nft-item未找到:" + JSON.toJSONString(log));
                return;
            }
            this.noticeManager.add(content, nftItems.getItemOwner(), type, noticeType, log.getOwner());
            nftItems.setOnsell(false);
            this.update(nftItems);
        }else{
            // cancel bid
            Integer type = CommonStatus.getStatusByName("Cancel bid").getType();
            List<FcNftItems> nftItemsList = this.getList(log.getBuyToken(), log.getBuyTokenId().toString());
            for(FcNftItems nftItems: nftItemsList){
                this.noticeManager.add(content, nftItems.getItemOwner(), type, noticeType, log.getOwner());
            }
            this.noticeManager.add(content, log.getOwner(), type, noticeType, log.getOwner());
        }
    }


    public Integer update(FcNftItems nftItems){
        return this.baseService.update(nftItems);
    }

    public Integer save(FcNftItems nftItems){
        return this.baseService.saveOrUpdate(nftItems);
    }


    public FcNftItems getByAddressAndTokenId(String address, String tokenId) {
        QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftItems.DELETED, false)
                .eq(FcNftItems.ADDRESS, address)
                .eq(FcNftItems.TOKEN_ID, tokenId);
        return this.baseService.getByCondition(FcNftItems.class, wrapper);
    }
}
