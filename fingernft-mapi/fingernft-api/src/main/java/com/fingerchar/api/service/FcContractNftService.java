package com.fingerchar.api.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.utils.DappWeb3jUtil;
import com.fingerchar.api.vo.ERCTokenInfo;
import com.fingerchar.api.vo.HomeIndexParamsVO;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.*;
import com.fingerchar.core.util.ListUtils;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcContractNftExtMapper;
import com.fingerchar.db.domain.*;
import com.fingerchar.db.dto.NftInfo;
import com.fingerchar.db.dto.SearchNftParamDto;
import com.fingerchar.db.vo.FcOrderVo;
import com.fingerchar.db.vo.NftInfoVo;
import com.fingerchar.db.vo.NftParamVO;
import com.fingerchar.db.vo.orderlog.OrderLogVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FcContractNftService {

    private static final Logger logger = LoggerFactory.getLogger(FcContractNftService.class);

    @Autowired
    IBaseService baseService;

    @Autowired
    private FcUserManager userManager;

    @Autowired
    private StorageService storageService;

    @Autowired
    FcContractNftExtMapper nftExtMapper;

    @Autowired
    FcOrderManager orderManager;

    @Autowired
    FcNftCategoryManager nftCategoryManager;

    @Autowired
    FcSystemConfigManager systemConfigManager;

    @Autowired
    FcContractService contractService;
    
    @Autowired
    FcContractNftManager contractNftManager;


    /**
     * 获取所有的nft(?这个是不是不合理)
     *
     * @return
     */
    public IPage<FcContractNft> findCollections(IPage<FcContractNft> pageInfo) {
        QueryWrapper<com.fingerchar.db.domain.FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.IS_SYNC, true).eq(BaseEntity.DELETED, false);
        return this.baseService.findByPage(FcContractNft.class, wrapper, pageInfo);
    }

    /**
     * 新增nft(创建)
     *
     * @param nft
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public FcContractNft save(NftInfo nft) {
        nft = this.uploadMetadata(nft);
        if (null == nft.getCategoryId()) {
            FcNftCategory nftCategory = this.nftCategoryManager.getDefault();
            if(null != nftCategory){
                nft.setContractId(nftCategory.getId());
            }
        }
        return this.contractNftManager.create(nft);
    }

    private NftInfo uploadMetadata(NftInfo nft) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", nft.getName());
        map.put("description", nft.getDescription());
        FcStorage storage = this.baseService.getById(FcStorage.class, nft.getStorageId());
        if (StringUtils.isEmpty(nft.getAnimUrl())) {
            map.put("animation_url", "");
            map.put("image", storage.getIpfshash());
        } else {
            map.put("image", storage.getIpfshash() + "/" + storage.getKey());
            storage = this.baseService.getById(FcStorage.class, nft.getAnimStorageId());
            map.put("animation_url", storage.getIpfshash() + "/" + storage.getKey());
        }
        String website = this.systemConfigManager.getKeyValue(SysConfConstant.WEBSITE);

        map.put("external_url", website + "/detail/" + nft.getAddress() + ":" + nft.getTokenId());
        if (StringUtils.isEmpty(nft.getProperties())) {
            map.put("attributes", new ArrayList<>());
        } else {
            JSONArray ja = JSONArray.parseArray(nft.getProperties());
            map.put("attributes", ja);
        }
        String metadata = JSON.toJSONString(map);
        InputStream is = new ByteArrayInputStream(metadata.getBytes());
        storage = this.storageService.store(is, metadata.getBytes().length, "application/json", nft.getName() + ".json");
        nft.setMetadataUrl(storage.getIpfshash());
        nft.setMetadataContent(metadata);
        return nft;
    }

    /**
     * 根据合约地址和tokenid获取nft详情
     *
     * @param token
     * @param tokenId
     * @return
     */
    public Object detail(String token, String tokenId) {
        FcContractNft nft = this.contractNftManager.get(token, tokenId);
        if (null == nft) {
            return ResponseUtil.ok();
        }
        NftInfoVo nftInfo = this.contractNftManager.getNftInfo(nft);
        return ResponseUtil.ok(nftInfo);
    }


    /**
     * 根据合约地址查询所有合约下的nft
     *
     * @param address
     * @return
     */
    public IPage<FcContractNft> findAllByAddress(String address, IPage<FcContractNft> pageInfo) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, address).eq(BaseEntity.DELETED, false);
        return this.baseService.findByPage(FcContractNft.class, wrapper, pageInfo);
    }

    /**
     * 根据合约地址和tokenid获取
     *
     * @param token
     * @param tokenId
     * @return
     */
    public Object owners(String token, String tokenId) {

        FcContractNft nft = this.contractNftManager.get(token, tokenId);
        if (null == nft) {
            return ResponseUtil.ok(new ArrayList<>());
        }

        NftInfoVo nftInfoVo = this.contractNftManager.getNftInfo(nft);
        return ResponseUtil.okList(nftInfoVo.getItems());
    }

    /**
     * 根据合约地址和tokenid获取bid订单信息
     *
     * @param token
     * @param tokenId
     * @return
     */
    public Object bids(String token, String tokenId) {
        List<Integer> typeList = new ArrayList<>();
        typeList.add(CommonStatus.BID.getType());
        typeList.add(CommonStatus.EDIT_BID.getType());
        typeList.add(CommonStatus.CANCEL_BID.getType());
        typeList.add(CommonStatus.ACCEPT_BID.getType());

        QueryWrapper<FcOrderLog> logWrapper = new QueryWrapper<>();
        logWrapper.eq(FcOrderLog.TOKEN, token)
                .eq(FcOrderLog.TOKEN_ID, tokenId)
                .in(FcOrderLog.TYPE, typeList);
        logWrapper.orderByDesc(BaseEntity.CREATE_TIME);
        List<FcOrderLog> logList = this.baseService.findByCondition(FcOrderLog.class, logWrapper);
        if (null == logList || logList.isEmpty()) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<String> fromList = logList.stream().map(FcOrderLog::getFrom).collect(Collectors.toList());
        List<String> toList = logList.stream().map(FcOrderLog::getTo).collect(Collectors.toList());
        fromList = fromList.stream().filter(vo->!StringUtils.isEmpty(vo)).collect(Collectors.toList());
        toList = toList.stream().filter(vo->!StringUtils.isEmpty(vo)).collect(Collectors.toList());
        fromList.addAll(toList);
        fromList = ListUtils.unrepeated(fromList);      // 去重

        List<String> saltList = new ArrayList<>();
        FcOrder order = null;
        for(FcOrderLog orderLog: logList){
            order = JSON.parseObject(orderLog.getContent(), FcOrder.class);
            saltList.add(order.getSalt());
        }
        saltList = ListUtils.unrepeated(saltList);
        List<FcOrder> orderList = this.orderManager.allbymulti(saltList);

        Map<Long, Map<String, Object>> orderMap = new HashMap<>();
        for(FcOrder order1: orderList){
            orderMap.put(order1.getId(), JSON.parseObject(JSON.toJSONString(order1)));
        }

        List<FcUser> userList = this.userManager.listByMulti(fromList);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
        List<OrderLogVo> orderLogVoList = logList.stream().map(
                vo -> new OrderLogVo(vo, orderMap.get(vo.getOrderId()), userMap.get(vo.getFrom()), userMap.get(vo.getTo()))
        ).collect(Collectors.toList());
        return ResponseUtil.ok(orderLogVoList);
    }


    public List<FcOrderVo> activebids(List<NftParamVO> paramVoList){
        return this.orderManager.activebids(paramVoList);
    }


    public List<FcOrderVo> activesales(List<NftParamVO> paramVoList){
        return this.orderManager.activesales(paramVoList);
    }

    /**
     * 根据合约地址和tokenid获取订单历史信息
     *
     * @param token
     * @param tokenId
     * @return
     */
    public Object history(String token, String tokenId) {
        QueryWrapper<FcOrderLog> logWrapper = new QueryWrapper<>();
        logWrapper.eq(FcOrderLog.TOKEN, token)
                .eq(FcOrderLog.TOKEN_ID, tokenId);
        logWrapper.orderByDesc(BaseEntity.CREATE_TIME);
        List<FcOrderLog> logList = this.baseService.findByCondition(FcOrderLog.class, logWrapper);
        if (null == logList || logList.isEmpty()) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<String> fromList = logList.stream().map(FcOrderLog::getFrom).collect(Collectors.toList());
        List<String> toList = logList.stream().map(FcOrderLog::getTo).collect(Collectors.toList());
        fromList = fromList.stream().filter(vo->!StringUtils.isEmpty(vo)).collect(Collectors.toList());
        toList = toList.stream().filter(vo->!StringUtils.isEmpty(vo)).collect(Collectors.toList());
        fromList.addAll(toList);
        fromList = ListUtils.unrepeated(fromList);      // 去重
        List<FcUser> userList = this.userManager.listByMulti(fromList);
        Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));

        List<String> saltList = new ArrayList<>();
        FcOrder order = null;
        for(FcOrderLog orderLog: logList){
            if(orderLog.getOrderId().equals(0)){
                continue;
            }
            order = JSON.parseObject(orderLog.getContent(), FcOrder.class);
            saltList.add(order.getSalt());
        }
        saltList = ListUtils.unrepeated(saltList);
        List<FcOrder> orderList = this.orderManager.allbymulti(saltList);
        Map<Long, Map<String, Object>> orderMap = new HashMap<>();
        for(FcOrder order1: orderList){
            orderMap.put(order1.getId(), JSON.parseObject(JSON.toJSONString(order1)));
        }

        List<OrderLogVo> orderLogVoList = new ArrayList<>();
        Map<String, Object> content = null;
        for(FcOrderLog orderLog: logList){
            if(orderLog.getOrderId().equals(0)){
                content = JSON.parseObject(orderLog.getContent());
            }else{
                content = orderMap.get(orderLog.getOrderId());
            }
            orderLogVoList.add(
                    new OrderLogVo(
                        orderLog,
                        content,
                        userMap.get(orderLog.getFrom()),
                        userMap.get(orderLog.getTo())
                    )
            );
        }

        return ResponseUtil.ok(orderLogVoList);
    }


    /**
     * 根据用户id和用户地址，获取用户like items个数
     *
     * @param address
     * @return
     */
    public Integer countLike(String address) {
        QueryWrapper<FcNftLike> wrapper = new QueryWrapper<>();
        wrapper.eq(FcNftLike.USER_ADDRESS, address)
                .eq(BaseEntity.DELETED, false);
        List<FcNftLike> list = this.baseService.findByCondition(FcNftLike.class, wrapper);
        if (null == list || list.isEmpty()) {
            return 0;
        }
        return this.nftExtMapper.countContractLike(list);
    }

    /**
     * @param info
     * @return
     */
    public Map<String, String> getMedia(String[] info) {
        List<NftParamVO> paramVOList = new ArrayList<>();
        String[] tempList = null;
        for(int i = 0; i < info.length; i++){
            tempList = info[i].split(":");
            paramVOList.add(new NftParamVO(tempList[0],tempList[1]));
        }
        List<FcContractNft> nftList = this.nftExtMapper.listByMulti(paramVOList);
        Map<String, String> map = new HashMap<>();
        String key = null;
        String uri = null;
        ERCTokenInfo tokenInfo = null;
        List<FcContractNft> updateNftList = new ArrayList<>();
        for(FcContractNft nft: nftList){
            key = nft.getAddress() + ":" + nft.getTokenId();
            if(!StringUtils.isEmpty(nft.getMetadataContent())){
                map.put(key, nft.getMetadataContent());
                continue;
            }
            if(nft.getGetMetaTimes().compareTo(13) > 0){
                logger.warn("已经获取超过13次 放弃获取");
                continue;
            }
            if(StringUtils.isEmpty(nft.getMetadataUrl())){
                uri = DappWeb3jUtil.getErc721Uri(nft.getAddress(), nft.getTokenId());
                if(StringUtils.isEmpty(uri)){
                    logger.warn("uri is not exist");
                    continue;
                }
            }
            nft.setMetadataUrl(uri);

            try {
                tokenInfo = DappWeb3jUtil.processUri(nft.getMetadataUrl());
                if (null != tokenInfo) {
                    map.put(key, tokenInfo.getContent());
                    nft.setName(tokenInfo.getName());
                    nft.setDescription(tokenInfo.getDescription());
                    nft.setMetadataContent(tokenInfo.getContent());
                } else {
                    nft.setGetMetaTimes(nft.getGetMetaTimes() + 1);
                }
                updateNftList.add(nft);
            } catch (Exception e) {
                logger.error("获取资源信息异常", e);
            }
        }
        if(!updateNftList.isEmpty()){
          this.contractNftManager.updateBatch(updateNftList);
        }
        return map;
    }

    public IPage<NftInfoVo> findListForIndex(
            IPage<FcContractNft> pageInfo,
            HomeIndexParamsVO params
    ) {
        String address = params.getAddress();
        Long cate = params.getCate();
        List<String> contracts = params.getContracts();
        String payToken = params.getPayToken();
        BigDecimal minPrice = params.getMinPrice();
        BigDecimal maxPrice = params.getMaxPrice();
        String search = params.getSearch();
        String sort = params.getSort();
        String order = params.getOrder();
		if (StringUtils.isEmpty(address)) {
			address = null;
		}
		if (null == contracts || contracts.isEmpty()) {
			contracts = null;
		}
		if(StringUtils.isEmpty(payToken)) {
			payToken = null;
		}
		if(StringUtils.isEmpty(search)) {
			search = null;
		}
		IPage<FcContractNft> iPage = null;
        iPage = this.nftExtMapper.findVerifyNft(pageInfo, address, cate, contracts, payToken, minPrice, maxPrice, search, sort, order, true, 1);
        IPage<NftInfoVo> nftInfoVoIPage = this.contractNftManager.getNftInfoList(iPage);
        return nftInfoVoIPage;
	}


    public Object findContractNft(IPage<FcContractNft> pageInfo, String address, Boolean isSell) {
        if (StringUtils.isEmpty(address)) {
            address = null;
        }
        IPage<FcContractNft> data = this.nftExtMapper.findContractNft(pageInfo, address, isSell);
        IPage<NftInfoVo> nftInfoList = this.contractNftManager.getNftInfoList(data);
        return ResponseUtil.okList(nftInfoList);
    }

    public IPage<NftInfoVo> findOnSellListByAddress(String owner, IPage<FcContractNft> pageInfo) {
    	IPage<FcContractNft> iPage = this.nftExtMapper.findOnsaleNft(pageInfo, owner);
        IPage<NftInfoVo> nftInfoVoIPage = this.contractNftManager.getNftInfoList(iPage);
        return nftInfoVoIPage;
    }


    public Object findSearch(String name, IPage<FcContractNft> pageInfo) {
    	IPage<FcContractNft> data = this.nftExtMapper.searchNft(pageInfo, name);
        IPage<NftInfoVo> nftInfoList = this.contractNftManager.getNftInfoList(data);
        return ResponseUtil.okList(nftInfoList);
    }

    public IPage<NftInfoVo> findListByUserAddress(SearchNftParamDto paramDto, IPage<FcContractNft> pageInfo) {
    	IPage<FcContractNft> iPage = this.nftExtMapper.findCollectionNft(pageInfo, paramDto);
        IPage<NftInfoVo> nftInfoVoIPage = this.contractNftManager.getNftInfoList(iPage);
        return nftInfoVoIPage;
    }

    public List<FcContractNft> nftlist(SearchNftParamDto paramDto){
        return this.nftExtMapper.nftlist(paramDto);
    }

    public Object listByTokenAndTokenId(Set<NftParamVO> params){
        List<FcContractNft> contractNfts = this.nftExtMapper.listByTokenAndTokenId(params);
        List<NftInfoVo> nftInfoList = this.contractNftManager.getNftInfoList(contractNfts);
        return ResponseUtil.okList(nftInfoList);
    }

    public Integer countCreatorNft(String address) {
        return this.nftExtMapper.countCreatorNft(address);
    }

    public IPage<NftInfoVo> findByCreators(String creator, IPage<FcContractNft> pageInfo) {
    	IPage<FcContractNft> iPage = this.nftExtMapper.findCreatorNft(pageInfo, creator);
        IPage<NftInfoVo> nftInfoVoIPage = this.contractNftManager.getNftInfoList(iPage);
        return nftInfoVoIPage;
    }

    public IPage<NftInfoVo> findByLike(String userAddress, IPage<FcContractNft> pageInfo) {
    	IPage<FcContractNft> iPage = this.nftExtMapper.findLikeNft(pageInfo, userAddress);
        IPage<NftInfoVo> nftInfoVoIPage = this.contractNftManager.getNftInfoList(iPage);
        return nftInfoVoIPage;
    }

    /**
     * 统计合约地址下有多少个有效的在售item
     *
     * @param address
     * @return
     */
    public Integer countContractOnsale(String address) {
        return this.nftExtMapper.countContractOnsale(address);
    }


    /**
     * 统计合约地址下有多少个有效的item
     *
     * @param address
     * @return
     */
    public Integer countContractCollections(String address) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, address)
                .eq(FcContractNft.IS_SYNC, true)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.counts(FcContractNft.class, wrapper);
    }

    /**
     * @param info
     * @return
     */
    public Map<String, String> getRoyalties(String[] info) {
        String temp = null;
        Map<String, String> map = new HashMap<>();
        String token = null;
        String tokenId = null;
        QueryWrapper<FcContractNft> nftQWrapper = null;
        UpdateWrapper<FcContractNft> nftUWrapper = null;
        FcContractNft nft = null;
        List<BigInteger> royalties = null;
        Map<String, Boolean> supportMap = new HashMap<>();
        FcContract contract = null;
        for (int i = 0; i < info.length; i++) {
            temp = info[i];
            token = temp.split(":")[0];
            tokenId = temp.split(":")[1];
            nftQWrapper = new QueryWrapper<>();
            nftQWrapper.eq(FcContractNft.ADDRESS, token).eq(FcContractNft.TOKEN_ID, tokenId);
            nft = this.baseService.getByCondition(FcContractNft.class, nftQWrapper);
            if (null == nft) {
                logger.warn("nft不存在");
                continue;
            }
            if (!StringUtils.isEmpty(nft.getRoyalties())) {
                if ("[]".equals(nft.getRoyalties()) || "0".equals(nft.getRoyalties())) {
                    map.put(temp, "");
                } else {
                    map.put(temp, nft.getRoyalties());
                }
                continue;
            }
            if (null == supportMap.get(token)) {
                contract = this.contractService.getByAddress(token);
                if (null == contract) {
                    map.put(temp, "");
                    continue;
                }
                if (null == contract.getIsRoyalties()) {
                    contract.setIsRoyalties(DappWeb3jUtil.isSupportRoyalties(token));
                    this.baseService.update(contract);
                }
                supportMap.put(token, contract.getIsRoyalties());
            }
            if (!supportMap.get(token)) {
                map.put(temp, "");
                continue;
            }
            royalties = null;
            try {
                royalties = DappWeb3jUtil.getRoyalties(token, tokenId);
            } catch (Exception e) {
                logger.error("获取版权信息异常", e);
            }
            if (null == royalties) {
                map.put(temp, "");
                continue;
            }
            nftUWrapper = new UpdateWrapper<>();
            nftUWrapper.set(FcContractNft.ROYALTIES, JSON.toJSONString(royalties));
            nftUWrapper.eq(BaseEntity.ID, nft.getId());
            this.baseService.updateByCondition(FcContractNft.class, nftUWrapper);

        }
        return map;
    }


}
