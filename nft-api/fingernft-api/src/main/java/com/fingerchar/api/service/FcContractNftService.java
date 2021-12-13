package com.fingerchar.api.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.api.constant.CommonStatus;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.dto.ItemOwners;
import com.fingerchar.api.dto.NftItemsInfo;
import com.fingerchar.api.dto.OrderBidsInfo;
import com.fingerchar.api.dto.OrderHistoryInfo;
import com.fingerchar.api.utils.DappWeb3jUtil;
import com.fingerchar.api.vo.ERCTokenInfo;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcContractNftExtMapper;
import com.fingerchar.db.dao.ext.FcNftCategoryExtMapper;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftCategory;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcNftLike;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcOrderLog;
import com.fingerchar.db.domain.FcStorage;
import com.fingerchar.db.domain.FcSystem;
import com.fingerchar.db.domain.FcUser;

@Service
public class FcContractNftService {

    private static final Logger logger = LoggerFactory.getLogger(FcContractNftService.class);


    @Autowired
    IBaseService baseService;

    @Autowired
    FcNftCategoryExtMapper categoryExtMapper;

    @Autowired
    private StorageService storageService;

    @Autowired
    FcContractNftExtMapper nftExtMapper;

    @Autowired
    FcContractService contractService;

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
     * @param user
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public String save(FcContractNft nft, FcUser user) {
        if (null == nft.getCategoryId()) {
            QueryWrapper<FcNftCategory> wrapper = new QueryWrapper<>();
            wrapper.eq(BaseEntity.DELETED, false);
            wrapper.orderByAsc(FcNftCategory.ORDER);
            FcNftCategory c = this.categoryExtMapper.selectOne();
            nft.setCategoryId(c.getId());
        }
        Integer count = this.baseService.save(nft);
        if (count > 0) {
            FcNftItems item = new FcNftItems();
            item.setNftId(nft.getId());
            item.setName(nft.getName());
            item.setDescription(nft.getDescription());
            item.setImgUrl(nft.getImgUrl());
            item.setItemOwner(nft.getCreator());
            item.setQuantity(nft.getQuantity());
            item.setStorageId(nft.getStorageId());
            item.setAddress(nft.getAddress());
            item.setIsSync(nft.getIsSync());
            item.setCategoryId(nft.getCategoryId());
            item.setOnsell(false);
            item.setTokenId(nft.getTokenId());
            count = this.baseService.save(item);
            if (count > 0) {
                return this.uploadMetadata(nft);
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    private String uploadMetadata(FcContractNft nft) {
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
        map.put("external_url", this.getWebSite() + "/detail/" + nft.getAddress() + ":" + nft.getTokenId());
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
        this.baseService.update(nft);
        return storage.getIpfshash().replace("ipfs:/", "");
    }

    private String getWebSite() {
    	QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
        wrapper.eq(FcSystem.KEY_NAME, SysConfConstant.WEB_SITE);
        FcSystem system = this.baseService.getByCondition(FcSystem.class, wrapper);
        if (null == system) {
            return "";
        } else {
            return system.getKeyValue();
        }
    }

    /**
     * 根据合约地址和tokenid获取nft详情
     *
     * @param token
     * @param tokenId
     * @return
     */
    public Object detail(String token, String tokenId) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, token).eq(FcContractNft.TOKEN_ID, tokenId).eq(BaseEntity.DELETED, false);
        FcContractNft nft = this.baseService.getByCondition(FcContractNft.class, wrapper);
        if (null == nft) {
            return ResponseUtil.ok();
        }
        QueryWrapper<FcNftItems> itemsWrapper = new QueryWrapper<>();
        itemsWrapper.eq(FcNftItems.NFT_ID, nft.getId()).eq(BaseEntity.DELETED, false);
        List<FcNftItems> items = this.baseService.findByCondition(FcNftItems.class, itemsWrapper);
        if (null == items || items.isEmpty()) {
            return ResponseUtil.ok();
        }
        int len = items.size();
        FcNftItems item = null;
        FcOrder order = null;
        NftItemsInfo info = null;
        QueryWrapper<FcOrder> orderWrapper = null;
        List<NftItemsInfo> infoList = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            item = items.get(i);
            info = new NftItemsInfo(item);
            if (null != item.getOnsell() && item.getOnsell().booleanValue()) {
                orderWrapper = new QueryWrapper<>();
                orderWrapper.eq(FcOrder.OWNER, item.getItemOwner())
                        .eq(FcOrder.SELL_TOKEN, token)
                        .eq(FcOrder.SELL_TOKEN_ID, tokenId)
                        .eq(FcOrder.STATUS, 0)
                        .eq(FcOrder.EXPIRED, false)
                        .eq(BaseEntity.DELETED, false);
                order = this.baseService.getByCondition(FcOrder.class, orderWrapper);
                if (null != order) {
                    info.setPayTokenAddress(order.getBuyerToken());
                    info.setSellValue(order.getSellValue());
                    info.setBuyValue(order.getBuyerValue());
                    info.setCompleted(order.getSells());
                }
            }
            infoList.add(info);
        }
        QueryWrapper<FcUser> userWrapper = new QueryWrapper<>();
        userWrapper.eq(FcUser.ADDRESS, nft.getCreator());
        FcUser user = this.baseService.getByCondition(FcUser.class, userWrapper);
        Map<String, Object> map = new HashMap<>();
        map.put("nft", nft);
        map.put("items", infoList);
        map.put("creator", user);
        return ResponseUtil.ok(map);
    }

    /**
     * 根据用户地址获取用户创建的nft(分页)
     *
     * @param page
     * @param limit
     * @param address
     * @return
     */
    public IPage<FcContractNft> findByOwners(Integer page, Integer limit, String address) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.CREATOR, address)
                .eq(FcContractNft.IS_SYNC, true)
                .eq(BaseEntity.DELETED, false);
        wrapper.orderByDesc(BaseEntity.CREATE_TIME);
        IPage<FcContractNft> pageInfo = new Page<>(page, limit);
        return this.baseService.findByPage(FcContractNft.class, wrapper, pageInfo);
    }

    /**
     * 根据id获取nft
     *
     * @param nid
     * @return
     */
    public FcContractNft findById(Long nid) {
        return this.baseService.getById(FcContractNft.class, nid);
    }

    public List<FcContractNft> findByIds(List<Long> ids) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.in(BaseEntity.ID, ids);
        return this.baseService.findByCondition(FcContractNft.class, wrapper);
    }

    /**
     * 根据用户地址获取用户创建的nft
     *
     * @param owners
     * @return
     */
    public List<FcContractNft> findListByOwners(List<String> owners) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.IS_SYNC, true)
                .eq(BaseEntity.DELETED, false)
                .in(FcContractNft.CREATOR, owners);
        return this.baseService.findByCondition(FcContractNft.class, wrapper);
    }

    /**
     * 根据合约地址和tokenid获取nft(似乎有重复方法，待确认)
     *
     * @param tokenId
     * @param address
     * @return
     */
    public FcContractNft findByTokenId(String tokenId, String address) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.TOKEN_ID, tokenId)
                .eq(FcContractNft.ADDRESS, address)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcContractNft.class, wrapper);
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
        QueryWrapper<FcContractNft> nftWrapper = new QueryWrapper<>();
        nftWrapper.eq(FcContractNft.ADDRESS, token)
                .eq(FcContractNft.TOKEN_ID, tokenId)
                .eq(BaseEntity.DELETED, false);
        FcContractNft nft = this.baseService.getByCondition(FcContractNft.class, nftWrapper);
        if (null == nft) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        QueryWrapper<FcNftItems> itemsWrapper = new QueryWrapper<>();
        itemsWrapper.eq(FcNftItems.NFT_ID, nft.getId())
                .eq(BaseEntity.DELETED, false);
        List<FcNftItems> itemsList = this.baseService.findByCondition(FcNftItems.class, itemsWrapper);
        if (null == itemsList) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<String> addrList = itemsList.stream().map(x -> x.getItemOwner()).collect(Collectors.toList());
        QueryWrapper<FcUser> userWrapper = new QueryWrapper<>();
        userWrapper.in(FcUser.ADDRESS, addrList);
        List<FcUser> userList = this.baseService.findByCondition(FcUser.class, userWrapper);
        if (null == userList) {
            return ResponseUtil.ok(new ArrayList<>());
        }

        List<ItemOwners> result = itemsList.stream().flatMap(one -> userList.stream()
                        .filter(two -> one.getItemOwner().toLowerCase().equals(two.getAddress().toLowerCase())).map(two -> new ItemOwners(one, two)))
                .collect(Collectors.toList());
        QueryWrapper<FcOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.eq(FcOrder.SELL_TOKEN, token)
                .eq(FcOrder.SELL_TOKEN_ID, tokenId)
                .eq(FcOrder.ORDER_TYPE, 1)
                .eq(FcOrder.EXPIRED, false)
                .eq(BaseEntity.DELETED, false);
        List<FcOrder> orderList = this.baseService.findByCondition(FcOrder.class, orderWrapper);
        orderList.stream().forEach(order -> {
            result.stream().forEach(item -> {
                if (order.getOwner().toLowerCase().equals(item.getAddress().toLowerCase())) {
                    item.setSellValue(order.getSellValue());
                    item.setBuyValue(order.getBuyerValue());
                    item.setCompleted(order.getSells());
                }
            });
        });
        return ResponseUtil.ok(result);
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
        CommonStatus status = CommonStatus.getStatusByName("Bid");
        typeList.add(status.getType());
        status = CommonStatus.getStatusByName("Edit bid");
        typeList.add(status.getType());
        status = CommonStatus.getStatusByName("Cancel bid");
        typeList.add(status.getType());
        status = CommonStatus.getStatusByName("Accept bid");
        typeList.add(status.getType());

        QueryWrapper<FcOrderLog> logWrapper = new QueryWrapper<>();
        logWrapper.eq(FcOrderLog.TOKEN, token)
                .eq(FcOrderLog.TOKEN_ID, tokenId)
                .in(FcOrderLog.TYPE, typeList);
        logWrapper.orderByDesc(BaseEntity.CREATE_TIME);
        List<FcOrderLog> logList = this.baseService.findByCondition(FcOrderLog.class, logWrapper);
        if (null == logList || logList.isEmpty()) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<Long> orderIdList = logList.stream().filter(log -> null != log.getOrderId()).map(log -> log.getOrderId()).collect(Collectors.toList());
        if (orderIdList.isEmpty()) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        QueryWrapper<FcOrder> orderWrapper = new QueryWrapper<>();
        orderWrapper.in(BaseEntity.ID, orderIdList);
        List<FcOrder> orderList = this.baseService.findByCondition(FcOrder.class, orderWrapper);

        List<OrderBidsInfo> bidList = logList.stream().map(log -> new OrderBidsInfo(log)).collect(Collectors.toList());

        List<OrderBidsInfo> tempList = bidList;
        orderList.stream().forEach(order -> {
            tempList.stream().forEach(his -> {
                if (order.getId().equals(his.getOrderId())) {
                    his.setCompleted(order.getSells());
                }
            });
        });

        List<String> addressList = bidList.stream().map(his -> his.getAddress()).collect(Collectors.toList());
        QueryWrapper<FcUser> userWrapper = new QueryWrapper<>();
        userWrapper.in(FcUser.ADDRESS, addressList);
        List<FcUser> userList = this.baseService.findByCondition(FcUser.class, userWrapper);

        bidList = bidList.stream().flatMap(bids -> userList.stream()
                        .filter(user -> bids.getAddress().toLowerCase().equals(user.getAddress().toLowerCase())).map(user -> bids.addUserInfo(user)))
                .collect(Collectors.toList());
        return ResponseUtil.ok(bidList);
    }

    /**
     * 根据合约地址和tokenid获取有效的bid订单信息
     *
     * @param token
     * @param tokenId
     * @return
     */
    public List<FcOrder> activebids(String token, String tokenId) {
        QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
        wrapper.eq(FcOrder.BUYER_TOKEN, token)
                .eq(FcOrder.BUYER_TOKEN_ID, tokenId)
                .eq(FcOrder.EXPIRED, false)
                .eq(FcOrder.STATUS, 0)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.findByCondition(FcOrder.class, wrapper);
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
        logWrapper.orderByDesc(BaseEntity.CREATE_TIME, FcOrderLog.TYPE);
        List<FcOrderLog> logList = this.baseService.findByCondition(FcOrderLog.class, logWrapper);
        if (null == logList || logList.isEmpty()) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<OrderHistoryInfo> hisList = logList.stream().map(log -> new OrderHistoryInfo(log)).collect(Collectors.toList());
        List<Long> orderIdList = logList.stream().filter(log -> null != log.getOrderId()).map(log -> log.getOrderId()).collect(Collectors.toList());
        if (null != orderIdList && !orderIdList.isEmpty()) {
            QueryWrapper<FcOrder> orderWrapper = new QueryWrapper<>();
            orderWrapper.in(BaseEntity.ID, orderIdList);
            List<FcOrder> orderList = this.baseService.findByCondition(FcOrder.class, orderWrapper);
            List<OrderHistoryInfo> tempList = hisList;
            orderList.stream().forEach(order -> {
                tempList.stream().forEach(his -> {
                    if (order.getId().equals(his.getOrderId())) {
                        his.setCompleted(order.getSells());
                    }
                });
            });
        }

        List<String> addressList = hisList.stream().map(his -> his.getAddress().toLowerCase()).collect(Collectors.toList());
        if (null != addressList && !addressList.isEmpty()) {
            QueryWrapper<FcUser> userWrapper = new QueryWrapper<>();
            userWrapper.in(FcUser.ADDRESS, addressList);
            List<FcUser> userList = this.baseService.findByCondition(FcUser.class, userWrapper);
            List<OrderHistoryInfo> tempList = hisList;
            userList.stream().forEach(user -> {
                tempList.stream().forEach(his -> {
                    if (user.getAddress().toLowerCase().equals(his.getAddress().toLowerCase())) {
                        his.addUserInfo(user);
                    }
                });
            });
        }
        return ResponseUtil.ok(hisList);
    }

    /**
     * 根据合约地址和tokenid获取nft
     *
     * @param address
     * @param tokenId
     * @return
     */
    public FcContractNft findByAddressTokenId(String address, String tokenId) {
        QueryWrapper<FcContractNft> wrapper = new QueryWrapper<>();
        wrapper.eq(FcContractNft.ADDRESS, address)
                .eq(FcContractNft.TOKEN_ID, tokenId)
                .eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcContractNft.class, wrapper);
    }

    /**
     * 根据用户id和用户地址，获取用户like items个数
     *
     * @param id
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
        List<Long> idsList = list.stream().map(like -> like.getNftId()).collect(Collectors.toList());
        QueryWrapper<FcContractNft> nftWrapper = new QueryWrapper<>();
        nftWrapper.in(BaseEntity.ID, idsList)
                .eq(BaseEntity.DELETED, false)
                .eq(FcContractNft.IS_SYNC, true);
        return this.baseService.counts(FcContractNft.class, nftWrapper);
    }

    /**
     * @param info
     * @return
     */
    public Map<String, String> getMedia(String[] info) {
        String temp = null;
        ERCTokenInfo tokenInfo = null;
        Map<String, String> map = new HashMap<>();
        String token = null;
        String tokenId = null;
        QueryWrapper<FcContractNft> nftQWrapper = null;
        UpdateWrapper<FcContractNft> nftUWrapper = null;
        String uri = null;
        FcContractNft nft = null;
        for (int i = 0; i < info.length; i++) {
            temp = info[i];
            token = temp.split(":")[0];
            tokenId = temp.split(":")[1];
            nftQWrapper = new QueryWrapper<>();
            nftQWrapper.eq(FcContractNft.ADDRESS, token).eq(FcContractNft.TOKEN_ID, tokenId).eq(FcContractNft.DELETED, false);
            nft = this.baseService.getByCondition(FcContractNft.class, nftQWrapper);
            if (null == nft || nft.getGetMetaTimes() >= 13) {
                logger.warn("nft不存在或者已经获取超过13次");
                continue;
            }
            if (!StringUtils.isEmpty(nft.getMetadataContent())) {
                map.put(temp, nft.getMetadataContent());
                continue;
            }
            if (StringUtils.isEmpty(nft.getMetadataUrl())) {
                if (nft.getType() == 3) {
                    uri = DappWeb3jUtil.getErc721Uri(nft.getAddress(), nft.getTokenId());
                }
                if (StringUtils.isEmpty(uri)) {
                    logger.warn("未找到uri");
                    continue;
                }
                nft.setMetadataUrl(uri);
            }

            try {
                tokenInfo = DappWeb3jUtil.processUri(nft.getMetadataUrl());
                nftUWrapper = new UpdateWrapper<>();
                if (null != tokenInfo) {
                    map.put(temp, tokenInfo.getContent());
                    nftUWrapper.set(FcContractNft.METADATA_CONTENT, tokenInfo.getContent());
                    nftUWrapper.set(FcContractNft.METADATA_URL, nft.getMetadataUrl());
                } else {
                    nftUWrapper.setSql(FcContractNft.GET_META_TIMES + " = " + FcContractNft.GET_META_TIMES + " + 1");
                }
                nftUWrapper.eq(BaseEntity.ID, nft.getId());
                this.baseService.updateByCondition(FcContractNft.class, nftUWrapper);
            } catch (Exception e) {
                logger.error("获取资源信息异常", e);
            }
        }
        return map;
    }

    public IPage<FcContractNft> findListForIndex(IPage<FcContractNft> pageInfo, String address, Long cate,
                                                 List<String> contracts, String sort, String order, Boolean isSell) {
        if (StringUtils.isEmpty(cate)) {
            cate = null;
        }
        if (StringUtils.isEmpty(address)) {
            address = null;
        }
        if (null == contracts || contracts.isEmpty()) {
            contracts = null;
        }
        return this.nftExtMapper.findVerifyNft(pageInfo, address, cate, contracts, sort, order, isSell);
    }

    public IPage<FcContractNft> findContractNft(IPage<FcContractNft> pageInfo, String address, Boolean isSell) {
        if (StringUtils.isEmpty(address)) {
            address = null;
        }
        return this.nftExtMapper.findContractNft(pageInfo, address, isSell);
    }

    public IPage<FcContractNft> findListForIndex(IPage<FcContractNft> pageInfo, String address, Long cate,
                                                 List<String> contracts, String sort, String order) {
        return this.findListForIndex(pageInfo, address, cate, contracts, sort, order, null);
    }

    public IPage<FcContractNft> findOnSellListByAddress(String owner, IPage<FcContractNft> pageInfo) {
        return this.nftExtMapper.findOnsaleNft(pageInfo, owner);
    }

    public IPage<FcContractNft> findSearch(String name, IPage<FcContractNft> pageInfo) {
        return this.nftExtMapper.searchNft(pageInfo, name);
    }

    public IPage<FcContractNft> findListByUserAddress(String owner, IPage<FcContractNft> pageInfo) {
        return this.nftExtMapper.findCollectionNft(pageInfo, owner);
    }

    public Integer countCreatorNft(String address) {
        return this.nftExtMapper.countCreatorNft(address);
    }

    public IPage<FcContractNft> findByCreators(String creator, IPage<FcContractNft> pageInfo) {
        return this.nftExtMapper.findCreatorNft(pageInfo, creator);
    }

    public IPage<FcContractNft> findByLike(String userAddress, IPage<FcContractNft> pageInfo) {
        return this.nftExtMapper.findLikeNft(pageInfo, userAddress);
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
                .eq(FcContractNft.NFT_VERIFY, true)
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
