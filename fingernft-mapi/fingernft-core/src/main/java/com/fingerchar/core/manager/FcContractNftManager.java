package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.NoticeType;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.dto.*;
import com.fingerchar.db.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author： Zjm
 * @Date：2022/3/21 17:53
 */
@Service
public class FcContractNftManager {

    private static final Logger logger = LoggerFactory.getLogger(FcContractNftManager.class);

    @Autowired
    IBaseService baseService;

    @Autowired
    FcNftItemsManager nftItemsManager;

    @Autowired
    FcNftCategoryManager nftCategoryManager;

    @Autowired
    FcSystemConfigManager systemConfigManager;

    @Autowired
    FcContractManager contractManager;

    @Autowired
    FcOrderManager orderManager;

    @Autowired
    FcNoticeManager noticeManager;

    @Autowired
    FcUserManager userManager;


    @Autowired
    FcOrderLogManager orderLogManager;


    public IPage<FcContractNft> page(IPage<FcContractNft> page, QueryWrapper<FcContractNft> wrapper){
        return this.baseService.findByPage(FcContractNft.class, wrapper, page);
    }

    public List<NftInfoVo> getNftInfoList(List<FcContractNft> contractNftList){
        List<NftParamVO> nftParamVOList = contractNftList.stream().map(vo -> new NftParamVO(vo)).collect(Collectors.toList());
        List<FcNftItems> nftItemsList = this.nftItemsManager.listByMulti(nftParamVOList);

        List<String> addressList = nftItemsList.stream().map(vo ->vo.getItemOwner()).collect(Collectors.toList());
        Set<String> addressSet = new HashSet<>(addressList);
        addressList = new ArrayList<>(addressSet);
        List<FcUser> userList = this.userManager.listByMulti(addressList);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));

        Map<String, List<NftItemInfoVo>> map = new HashMap<>();

        String key = null;
        for(FcNftItems nftItems: nftItemsList){
            key = nftItems.getAddress() + ":" + nftItems.getTokenId();
            if(!map.containsKey(key)){
                map.put(key, new ArrayList<>());
            }
            map.get(key).add(new NftItemInfoVo(nftItems, userMap.get(nftItems.getItemOwner())));
        }

        return contractNftList.stream().map(
                vo -> new NftInfoVo(
                        vo,
                        map.get(vo.getAddress() + ":" + vo.getTokenId())
                )
            ).collect(Collectors.toList());
    }

    public IPage<NftInfoVo> getNftInfoList(IPage<FcContractNft> iPage){
        List<FcContractNft> contractNftList = iPage.getRecords();
        List<NftInfoVo> nftInfoVoList = this.getNftInfoList(contractNftList);
        IPage<NftInfoVo> nftInfoVoIPage = new Page<>();
        nftInfoVoIPage.setPages(iPage.getPages());
        nftInfoVoIPage.setSize(iPage.getSize());
        nftInfoVoIPage.setTotal(iPage.getTotal());
        nftInfoVoIPage.setRecords(nftInfoVoList);
        nftInfoVoIPage.setCurrent(iPage.getCurrent());
        return nftInfoVoIPage;
    }

    public NftInfoVo getNftInfo(FcContractNft contractNft){
        List<FcNftItems> nftItemsList = this.nftItemsManager.getList(contractNft.getAddress(), contractNft.getTokenId());
        FcUser creator = this.userManager.get(contractNft.getCreator());
        List<String> addressList = nftItemsList.stream().map(vo -> vo.getItemOwner()).collect(Collectors.toList());
        List<FcUser> userList = this.userManager.listByMulti(addressList);

        Map<String, FcUser> map = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
        List<NftItemInfoVo> itemInfoVoList = nftItemsList.stream().map(vo -> new NftItemInfoVo(vo, map.get(vo.getItemOwner()))).collect(Collectors.toList());

        return new NftInfoVo(contractNft, creator, itemInfoVoList);
    }

    public FcContractNft create(NftInfo nft){
        FcContractNft contractNft = this.get(nft.getAddress(), nft.getTokenId());
        if(null != contractNft){
            return contractNft;
        }

        contractNft = new FcContractNft();
        contractNft.setAddress(nft.getAddress());
        contractNft.setTokenId(nft.getTokenId());
        contractNft.setName(nft.getName());
        contractNft.setDescription(nft.getDescription());
        contractNft.setRoyalties(nft.getRoyalties());
        contractNft.setIsSync(nft.getIsSync());
        contractNft.setCreator(nft.getCreator());
        contractNft.setCategoryId(nft.getCategoryId());
        contractNft.setMetadataUrl(nft.getMetadataUrl());
        contractNft.setMetadataContent(nft.getMetadataContent());

        String value = this.systemConfigManager.getKeyValue(SysConfConstant.NFT_DEFAULT_VERIFY);
        if(value.equals("true")){
            contractNft.setNftVerify(1);
        }

        this.save(contractNft);
        this.nftItemsManager.create(nft);
        return contractNft;
    }




    public Integer sale(PrepareOrderInfo orderInfo){
        this.nftItemsManager.sale(orderInfo);
        return this.orderManager.sale(orderInfo);
    }

    public Integer bid(PrepareOrderInfo orderInfo){
        List<FcNftItems> nftItemsList = this.nftItemsManager.getList(orderInfo.getBuyToken(), orderInfo.getBuyTokenId());
        String content = JSON.toJSONString(orderInfo);
        Integer noticeType = NoticeType.TRADE.getType();

        for(FcNftItems nftItems: nftItemsList){
            this.noticeManager.add(content, nftItems.getItemOwner(), orderInfo.getType(), noticeType, orderInfo.getOwner());
        }
        return this.orderManager.bid(orderInfo);
    }

    public void buy(ExchangeBuyLog log){

        FcOrder order = this.orderManager.get(log);
        if(null == order){
            return;
        }
        this.nftItemsManager.buy(log, order);
        this.orderManager.buy(log, order);
    }

    public void cancel(ExchangeCancelLog log){
        FcOrder order = this.orderManager.get(log);
        if(null == order){
            return;
        }
        this.nftItemsManager.cancel(log, order);
        this.orderManager.cancel(log, order);
    }

    public FcContractNft getActive(String address, String tokenId) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, address)
                .eq(FcContractNft.TOKEN_ID, tokenId)
                .eq(FcContractNft.IS_SYNC, false)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcContractNft.class, wrapper);
    }

    public FcContractNft get(String address, String tokenId) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, address)
                .eq(FcContractNft.TOKEN_ID, tokenId)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcContractNft.class, wrapper);
    }


    public Integer transfer(TransferLog log){
        if(log.getFrom().equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS) &&
                log.getTo().equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)
        ){
            // from and to is zero address
            return 0;
        }

        String tokenId = log.getTokenId().toString();
        FcContract contract = contractManager.get(log.getAddress());
        if(null == contract ){
            contractManager.add(log.getAddress());
        }
        FcContractNft nft = this.get(log.getAddress(), tokenId);
        if(null == nft){
            if(!log.getFrom().equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)){
                return 0;
            }
            // mint transfer
            nft = this.add(log);
        }
        if(!nft.getIsSync()){
            nft.setIsSync(true);
        }

        if(log.getTo().equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)){
            // burn transfer
            nft.setDeleted(true);
        }

        FcNftItems nftItems = null;
        String content = JSON.toJSONString(log);
        Integer noticeType = NoticeType.TRADE.getType();;
        Integer transferType = CommonStatus.TRANSFER.getType();
        if(!log.getFrom().equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)){
            nftItems = this.nftItemsManager.get(log.getAddress(), tokenId, log.getFrom());
            nftItems.setDeleted(true);
            this.nftItemsManager.update(nftItems);

            noticeManager.add(content, log.getFrom(), transferType, noticeType, log.getFrom());
        }

        if(!log.getTo().equalsIgnoreCase(SysConfConstant.ZERO_ADDRESS)){
            nftItems = this.nftItemsManager.get(log.getAddress(), tokenId, log.getTo());
            if(null == nftItems){
                nftItems = new FcNftItems();
                nftItems.setAddress(log.getAddress());
                nftItems.setTokenId(tokenId);
                nftItems.setItemOwner(log.getTo());
            }
            nftItems.setIsSync(true);
            this.nftItemsManager.save(nftItems);
            Integer receiveType = CommonStatus.RECEIVE.getType();
            noticeManager.add(content, log.getTo(), receiveType, noticeType, log.getFrom());
        }

        this.orderLogManager.transfer(log);
        nft = this.getMedia(nft);
        return this.update(nft);
    }

    private FcContractNft getMedia(FcContractNft nft) {
        String uri = null;
        ERCTokenInfo tokenInfo = null;
        if(!StringUtils.isEmpty(nft.getMetadataContent())){
            return nft;
        }
        if(null != nft.getGetMetaTimes()){
            if (nft.getGetMetaTimes().compareTo(13) > 0){
                logger.warn("已经获取超过13次 放弃获取");
                return nft;
            }
        }
        if(StringUtils.isEmpty(nft.getMetadataUrl())){
            uri = DappWeb3jUtil.getErc721Uri(nft.getAddress(), nft.getTokenId());
            if(StringUtils.isEmpty(uri)){
                logger.warn("uri is not exist");
                return nft;
            }
        }
        nft.setMetadataUrl(uri);

        try {
            tokenInfo = DappWeb3jUtil.processNftUri(nft.getMetadataUrl());
            if (null != tokenInfo) {
                nft.setName(tokenInfo.getName());
                nft.setDescription(tokenInfo.getDescription());
                nft.setMetadataContent(tokenInfo.getContent());
            } else {
                nft.setGetMetaTimes(nft.getGetMetaTimes() + 1);
            }
        } catch (Exception e) {
            logger.error("获取资源信息异常", e);
        }
        return nft;
    }


    public FcContractNft add(TransferLog log){
        FcContractNft nft = new FcContractNft();
        nft.setAddress(log.getAddress());
        nft.setTokenId(log.getTokenId().toString());
        nft.setCreator(log.getTo());
        nft.setIsSync(true);
        nft.setTxHash(log.getTxHash());

        String value = this.systemConfigManager.getKeyValue(SysConfConstant.NFT_DEFAULT_VERIFY);
        if(value.equals("true")){
            nft.setNftVerify(1);
        }

        FcNftCategory category = nftCategoryManager.getDefault();
        if(null != category){
            nft.setCategoryId(category.getId());
        }
        this.save(nft);

        return nft;
    }

    public Integer updateBatch(List<FcContractNft> nftList){
        return this.baseService.updateBatch(nftList);
    }

    public Integer update(FcContractNft nft){
        return this.baseService.update(nft);
    }

    public Integer save(FcContractNft nft){
        return this.baseService.save(nft);
    }


}
