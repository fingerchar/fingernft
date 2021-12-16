package com.fingerchar.api.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.dto.NftItemsInfo;
import com.fingerchar.api.dto.UserVo;
import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcNftItemsService;
import com.fingerchar.api.service.FcOrderService;
import com.fingerchar.api.service.FcSystemConfigService;
import com.fingerchar.api.service.FcUserFollowService;
import com.fingerchar.api.service.FcUserLogService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.service.FcUserTokenService;
import com.fingerchar.api.service.UserTokenManager;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.api.utils.IpUtil;
import com.fingerchar.api.utils.ListUtils;
import com.fingerchar.api.utils.Str2ArrayUtils;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.domain.FcUserFollow;
import com.fingerchar.db.domain.FcUserLog;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/user")
public class FcUserController extends BaseController {
    
	@Autowired
    FcUserLogService userLogService;

    @Autowired
    FcUserService userService;

    @Autowired
    FcContractNftService contractNftService;

    @Autowired
    FcContractService contractService;

    @Autowired
    FcUserFollowService userFollowService;

    @Autowired
    FcNftItemsService nftItemsService;

    @Autowired
    FcOrderService orderService;

    @Autowired
    FcSystemConfigService systemConfigService;

    @Autowired
    FcUserTokenService userTokenService;

    @Autowired
    IBaseService baseService;

    /*
     * 一般日志：用户觉得需要查看的一般操作日志，建议是默认的日志级别
     * 安全日志：用户安全相关的操作日志，例如登录、删除管理员
     * */
    public static final Integer LOG_TYPE_GENERAL = 0;
    public static final Integer LOG_TYPE_AUTH = 1;
    // 1表示登录
    public static final String LOG_ACTION_LOGIN = "1";

    @PostMapping("login")
    public Object login(String userAddress, String signature, Long timestamp) {
        if (StringUtils.isEmpty(userAddress) || StringUtils.isEmpty(signature) || null == timestamp) {
            return ResponseUtil.fail(-1, "invalid login params");
        }

        if (System.currentTimeMillis() / 1000 - timestamp > 1800) {
            return ResponseUtil.fail(-1, "invalid message");
        }
        String loginMessage = this.getLoginMessage();
        if (StringUtils.isEmpty(loginMessage)) {
            return ResponseUtil.fail(-1, "invalid message");
        }
        loginMessage = String.format(loginMessage, timestamp);
        if (!DappCryptoUtil.validate(signature, loginMessage, userAddress)) {
            this.packUserLog(LOG_TYPE_AUTH, LOG_ACTION_LOGIN, false, "无效的签名", "");
            return ResponseUtil.fail(-1, "invalid sign");
        }

        List<FcUser> userList = userService.queryUserByAddrAndType(userAddress);
        FcUser user = null;
        if (userList.size() > 1) {
            return ResponseUtil.fail(-1, "found more then 2 users, please concat service manager");
        } else if (userList.size() == 0) {
            user = new FcUser();
            user.setAddress(userAddress);
            user.setLoginType("1");
            user.setIsWeb(true);
            user.setLastLoginTime(System.currentTimeMillis() / 1000);
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            userService.add(user);
            this.packUserLog(LOG_TYPE_AUTH, LOG_ACTION_LOGIN, true, "新用户注册成功", userAddress);
        } else {
            user = userList.get(0);
            // 更新登录情况
            user.setLastLoginTime(System.currentTimeMillis() / 1000);
            user.setLastLoginIp(IpUtil.getIpAddr(request));
            user.setIsWeb(true);
            user.setLoginType("1");
            if (userService.updateById(user) == 0) {
                this.packUserLog(LOG_TYPE_AUTH, LOG_ACTION_LOGIN, false, "用户信息更新失败", "");
                return ResponseUtil.updatedDataFailed();
            }
            this.packUserLog(LOG_TYPE_AUTH, LOG_ACTION_LOGIN, true, "用户登录成功", userAddress);
        }

        // token
        String token = UserTokenManager.generateToken(user.getAddress());
        userTokenService.saveOrUpdate(user.getAddress(), token);
        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", token);
        result.put("user", user);
        return ResponseUtil.ok(result);
    }


    /**
     * 封装用户日志信息
     */
    private void packUserLog(Integer type, String action, Boolean succeed, String result, String address) {
        FcUserLog log = new FcUserLog();

        if (address != null && !"".equals(address)) {
            log.setAddress(address);
        } else {
            log.setAddress("匿名用户");
        }

        log.setIp(IpUtil.getIpAddr(request));

        log.setType(type);
        log.setAction(action);
        log.setStatus(succeed);
        log.setResult(result);
        userLogService.add(log);
    }

    private String getLoginMessage() {
        return this.systemConfigService.get(SysConfConstant.LOGIN_MESSAGE);
    }

    @PostMapping("reload")
    public Object login() {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userService.getUserByAddress(userAddress);
        if (null == user) {
            return ResponseUtil.unauthz();
        }
        // 重新授权
        //String token = UserTokenManager.generateToken(user.getAddress());
        /*this.redisPublicService.set(RedisConstant.TOKEN + user.getAddress(), token);
        this.redisPublicService.expire(RedisConstant.TOKEN + user.getAddress(),  30 * 24 * 60 * 60);
        userTokenService.saveOrUpdate(user.getAddress(), token);*/
        Map<Object, Object> result = new HashMap<Object, Object>();
        //result.put("token", token);
        result.put("user", user);
        this.packUserLog(LOG_TYPE_AUTH, LOG_ACTION_LOGIN, true, "用户重新登录", user.getAddress());
        return ResponseUtil.ok(result);
    }

    @PostMapping("profile")
    public Object config() {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userService.getUserByAddress(userAddress);
        if (null == user) {
            return ResponseUtil.unauthz();
        }

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("user", user);
        return ResponseUtil.ok(result);
    }

    @PostMapping("setprofile")
    public Object setprofile(FcUser userinfo) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userService.getUserByAddress(userinfo.getAddress());
        if (null == user) {
            return ResponseUtil.unlogin();
        }
        FcUser userByAddress = this.userService.getUserByAddress(userAddress);
        if (userByAddress.getId().longValue() != user.getId().longValue()) {
            return ResponseUtil.badArgument();
        }
        userinfo.setId(user.getId());
        userinfo.setLoginType("1");
        userinfo.setDeleted(false);
        this.userService.updateUserinfo(userinfo);
        return ResponseUtil.ok();
    }

    @PostMapping("setbanner")
    public Object setBanner(String url) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userService.getUserByAddress(userAddress);
        this.userService.setBanner(user.getId(), url);
        return ResponseUtil.ok();
    }

    @PostMapping("info")
    public Object info(String address) {
        if (address == null) {
            return ResponseUtil.ok(new JSONObject());
        }
        FcUser fcUser = this.userService.getUserByAddress(address);
        if (fcUser == null) {
            return ResponseUtil.ok(new JSONObject());
        }
        return ResponseUtil.ok(fcUser);
    }

    @PostMapping("stat")
    public Object stat(String address) {
        if (StringUtils.isEmpty(address)) {
            return ResponseUtil.ok(new JSONObject());
        }
        FcUser fcUser = userService.getUserByAddress(address);
        if (fcUser == null) {
            return ResponseUtil.ok(new JSONObject());
        }
        Map<String, Object> result = new HashMap<String, Object>(6);
        Integer saleCount = this.nftItemsService.countOnsale(address);
        Integer collectionCount = this.nftItemsService.countCollections(address);
        Integer createCount = this.contractNftService.countCreatorNft(address);
        Integer likeCount = this.contractNftService.countLike(fcUser.getAddress());
        Integer followCount = userFollowService.countByFollowingAddress(fcUser.getAddress());
        Integer followingCount = userFollowService.countByOwner(fcUser.getAddress());
        result.put("saleCount", saleCount);
        result.put("collectionCount", collectionCount);
        result.put("createCount", createCount);
        result.put("likeCount", likeCount);
        result.put("followCount", followCount);
        result.put("followingCount", followingCount);
        return ResponseUtil.ok(result);
    }

    @PostMapping("match")
    public Object match(String address) {
        if (null == address) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<String> paramsList = Str2ArrayUtils.sliceString2StringArray(address);
        if (paramsList.size() == 0) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<FcUser> fcUsers = userService.findListByAddrs(paramsList);
        return ResponseUtil.ok(fcUsers);
    }

    @PostMapping("onsales")
    public Object onsales(String address) {
        if (address == null) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        IPage<FcContractNft> list = contractNftService.findOnSellListByAddress(address, this.getPageInfo());
        return ResponseUtil.okList(this.processNft(list), list);
    }

    @PostMapping("collections")
    public Object collections(String address) {
        if (address == null) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        IPage<FcContractNft> list = contractNftService.findListByUserAddress(address, this.getPageInfo());
        return ResponseUtil.okList(this.processNft(list), list);
    }

    @PostMapping("created")
    public Object created(String address) {
        if (null == address) {
            return ResponseUtil.okList(this.getPageInfo());
        }

        IPage<FcContractNft> itemsList = this.contractNftService.findByCreators(address, this.getPageInfo());
        return ResponseUtil.okList(this.processNft(itemsList), itemsList);
    }

    @PostMapping("like")
    public Object likes(String address) {
        FcUser fcUser = userService.getUserByAddress(address);
        if (fcUser == null) {
            return ResponseUtil.NotFoud();
        }
        if (null == address) {
            return ResponseUtil.okList(this.getPageInfo());
        }

        IPage<FcContractNft> itemsList = this.contractNftService.findByLike(fcUser.getAddress(), this.getPageInfo());

        return ResponseUtil.okList(this.processNft(itemsList), itemsList);
    }

    private List<Map<String, Object>> processNft(IPage<FcContractNft> list) {
        List<Map<String, Object>> result = new ArrayList<>(list.getRecords().size());
        if (list != null && !list.getRecords().isEmpty()) {
            List<Long> nftIds = list.getRecords().stream().map(FcContractNft::getId).collect(Collectors.toList());
            List<String> contractAddrs = list.getRecords().stream().map(FcContractNft::getAddress).collect(Collectors.toList());
            List<FcContract> contractList = this.contractService.findListByAdress(contractAddrs);
            List<FcNftItems> itemsList = this.nftItemsService.findListByNftIds(nftIds);
            List<String> userAddrs = itemsList.stream().map(FcNftItems::getItemOwner).collect(Collectors.toList());
            List<FcUser> userList = this.userService.findListByAddrs(userAddrs);

            Map<String, Object> res = null;
            FcContract fcContract = null;
            FcUser fcUser = null;
            List<FcNftItems> tempItemsList = null;
            Map<String, Object> itemsMap = null;
            List<Map<String, Object>> tempList = null;
            //List<FcOrder> activeBidList = null;
            QueryWrapper<FcOrder> orderWrapper = null;
            FcOrder order = null;
            for (FcContractNft nft : list.getRecords()) {
                res = new HashMap<>(4);
                fcContract = ListUtils.getByPredicate(contractList, contract -> contract.getAddress().toLowerCase().equals(nft.getAddress().toLowerCase()));
                tempItemsList = itemsList.stream().filter(item -> item.getNftId().equals(nft.getId())).collect(Collectors.toList());
                //activeBidList = this.orderService.findActiveBids(nft.getAddress(), nft.getTokenId());
                tempList = new ArrayList<>();
                if (!tempItemsList.isEmpty()) {
                    for (FcNftItems item : tempItemsList) {
                        itemsMap = new HashMap<>(4);
                        NftItemsInfo itemInfo = new NftItemsInfo(item);
                        fcUser = ListUtils.getByPredicate(userList, user -> user.getAddress().toLowerCase().equals(item.getItemOwner().toLowerCase()));
                        if (null != fcUser) {
                            itemsMap.put("user", new UserVo(fcUser));
                        } else {
                            itemsMap.put("user", new UserVo(item.getItemOwner()));
                        }
                        if (null != item.getOnsell() && item.getOnsell().booleanValue()) {
                            orderWrapper = new QueryWrapper<>();
                            orderWrapper.eq(FcOrder.OWNER, item.getItemOwner())
                                    .eq(FcOrder.SELL_TOKEN, nft.getAddress())
                                    .eq(FcOrder.SELL_TOKEN_ID, nft.getTokenId())
                                    .eq(FcOrder.STATUS, 0)
                                    .eq(FcOrder.EXPIRED, false)
                                    .eq(BaseEntity.DELETED, false);
                            order = this.baseService.getByCondition(FcOrder.class, orderWrapper);
                            if (null != order) {
                                itemInfo.setCompleted(order.getSells());
                                itemInfo.setSellValue(order.getSellValue());
                                itemInfo.setBuyValue(order.getBuyerValue());
                                itemInfo.setPayTokenAddress(order.getBuyerToken());
                            } else {
                                itemInfo.setCompleted(0L);
                                itemInfo.setSellValue("0");
                                itemInfo.setBuyValue("");
                                itemInfo.setPayTokenAddress("");
                            }
                        } else {
                            itemInfo.setCompleted(0L);
                            itemInfo.setSellValue("0");
                            itemInfo.setBuyValue("");
                            itemInfo.setPayTokenAddress("");
                        }
                        itemsMap.put("item", itemInfo);
                        tempList.add(itemsMap);
                    }
                }
                res.put("contract", fcContract);
                res.put("itemList", tempList);
                res.put("nft", nft);
                //res.put("bids", activeBidList);
                result.add(res);
            }
        }

        return result;

    }

    @PostMapping("trade")
    public Object trade(String address) {
        FcUser fcUser = userService.getUserByAddress(address);
        if (fcUser == null) {
            return ResponseUtil.NotFoud();
        }

        IPage<FcOrder> fcOrders = orderService.findListByUserAddress(address, this.getPageInfo());
        List<Map<String, Object>> result = new ArrayList<>(fcOrders.getRecords().size());

        List<String> userAddrs = fcOrders.getRecords().stream().map(FcOrder::getOwner).collect(Collectors.toList());
        List<FcUser> userList = this.userService.findListByAddrs(userAddrs);

        if (null != fcOrders.getRecords() && !fcOrders.getRecords().isEmpty()) {
            Map<String, Object> res = null;
            FcContract fcContract = null;
            FcContractNft fcContractNft = null;
            FcUser fcUser1 = null;
            for (FcOrder fcOrder : fcOrders.getRecords()) {
                res = new HashMap<>(4);
                if (fcOrder.getOrderType().intValue() == 1) {
                    fcContract = contractService.getByAddress(fcOrder.getSellToken());
                    fcContractNft = contractNftService.findByTokenId(fcOrder.getSellTokenId(), fcOrder.getSellToken());
                } else if (fcOrder.getOrderType().intValue() == 2) {
                    fcContract = contractService.getByAddress(fcOrder.getBuyerToken());
                    fcContractNft = contractNftService.findByTokenId(fcOrder.getBuyerTokenId(), fcOrder.getBuyerToken());
                }
                fcUser1 = ListUtils.getByPredicate(userList, user -> user.getAddress().toLowerCase().equals(fcOrder.getOwner().toLowerCase()));
                res.put("contract", fcContract);
                res.put("nft", fcContractNft);
                res.put("user", fcUser1);
                res.put("order", fcOrder);
                result.add(res);
            }
        }
        return ResponseUtil.okList(result, fcOrders);

    }

    @PostMapping("activity")
    public Object activity(String address) {
        if (address == null) {
            return ResponseUtil.badArgumentValue();
        }
        FcUser fcUser = userService.getUserByAddress(address);
        if (fcUser == null) {
            return ResponseUtil.NotFoud();
        }
        IPage<FcUserFollow> fcUserFollows = userFollowService.findListByFollowingAddress(fcUser.getAddress(), this.getPageInfo());
        List<String> addrs= fcUserFollows.getRecords().stream().map(FcUserFollow::getUserAddress).collect(Collectors.toList());

        if (!fcUserFollows.getRecords().isEmpty() && addrs.size() > 0) {
            List<FcUser> fcUsers = userService.findListByAddrs(addrs);
            List<String> userAddress = fcUsers.stream().map(FcUser::getAddress).collect(Collectors.toList());
            List<FcContractNft> fcContractNfts = contractNftService.findListByOwners(userAddress);
            List<Map<String, Object>> result = new ArrayList<>(fcUserFollows.getRecords().size());
            if (!fcContractNfts.isEmpty() && fcContractNfts.size() > 0) {
                for (FcContractNft fcContractNft : fcContractNfts) {
                    Map<String, Object> res = new HashMap<>(2);
                    FcContract fcContract = contractService.findById(fcContractNft.getContractId());
                    res.put("contract", fcContract);
                    res.put("nft", fcContractNft);
                    result.add(res);
                }
            }

        }
        return ResponseUtil.ok();

    }

    @PostMapping("follows")
    public Object follows(String address) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(address)) {
            return ResponseUtil.badArgumentValue();
        }
        FcUser fcUser = userService.getUserByAddress(address);
        if (fcUser == null) {
            return ResponseUtil.NotFoud();
        }
        IPage<FcUserFollow> fcUserFollows = userFollowService.findListByFollowingAddress(fcUser.getAddress(), this.getPageInfo());
        if (null == fcUserFollows || fcUserFollows.getRecords().isEmpty()) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        List<Map<String, Object>> result = new ArrayList<>(fcUserFollows.getRecords().size());
        Map<String, Object> res = null;
        FcUser fcUser1 = null;
        FcUserFollow myFollow = null;
        long count = 0;
        FcUser user = this.userService.getUserByAddress(userAddress);
        for (FcUserFollow fcUserFollow : fcUserFollows.getRecords()) {
            res = new HashMap<>(5);
            fcUser1 = userService.getUserByAddress(fcUserFollow.getUserAddress());
            if (fcUser1 != null) {
                if (StringUtils.isEmpty(userAddress)) {
                    res.put("isFollow", false);
                } else {
                    if (fcUserFollow.getUserAddress().equals(user.getAddress())) {
                        res.put("isFollow", true);
                    } else {
                        myFollow = this.userFollowService.findByFAddrUAddr(fcUserFollow.getUserAddress(), user.getAddress());
                        if (null == myFollow) {
                            res.put("isFollow", false);
                        } else {
                            res.put("isFollow", true);
                        }
                    }
                }
                if (StringUtils.isEmpty(fcUser1.getAvatar())) {
                    res.put("avatar", "");
                } else {
                    res.put("avatar", fcUser1.getAvatar());
                }
                if (StringUtils.isEmpty(fcUser1.getNickname())) {
                    res.put("nickname", "");
                } else {
                    res.put("nickname", fcUser1.getNickname());
                }
                count = userFollowService.countByFollowingAddress(fcUser1.getAddress());
                res.put("count", count);
                res.put("address", fcUser1.getAddress());
                result.add(res);
            }
        }
        return ResponseUtil.okList(result, fcUserFollows);
    }

    @PostMapping("following")
    public Object following(String address) {
        String userAddress = (String) request.getAttribute("userAddress");
        logger.info("userAddress=>" + userAddress);
        if (StringUtils.isEmpty(address)) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        FcUser fcUser = userService.getUserByAddress(address);
        if (fcUser == null) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        IPage<FcUserFollow> fcUserFollows = userFollowService.findListByUserAddress(fcUser.getAddress(), this.getPageInfo());
        if (null == fcUserFollows || fcUserFollows.getRecords().isEmpty()) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        List<Map<String, Object>> result = new ArrayList<>(fcUserFollows.getRecords().size());
        FcUser user = this.userService.getUserByAddress(userAddress);
        Map<String, Object> res = null;
        FcUser fcUser1 = null;
        FcUserFollow myFollow = null;
        long count = 0;
        for (FcUserFollow fcUserFollow : fcUserFollows.getRecords()) {
            res = new HashMap<>(5);
            fcUser1 = this.userService.getUserByAddress(fcUserFollow.getFollowingAddress());
            if (fcUser1 != null) {
                if (StringUtils.isEmpty(userAddress)) {
                    res.put("isFollow", false);
                } else {
                    if (fcUserFollow.getFollowingAddress().equals(user.getAddress())) {
                        res.put("isFollow", true);
                    } else {
                        myFollow = this.userFollowService.findByFAddrUAddr(fcUserFollow.getUserAddress(), user.getAddress());
                        if (null == myFollow) {
                            res.put("isFollow", false);
                        } else {
                            res.put("isFollow", true);
                        }
                    }
                }
                if (StringUtils.isEmpty(fcUser1.getAvatar())) {
                    res.put("avatar", "");
                } else {
                    res.put("avatar", fcUser1.getAvatar());
                }
                if (StringUtils.isEmpty(fcUser1.getNickname())) {
                    res.put("nickname", "");
                } else {
                    res.put("nickname", fcUser1.getNickname());
                }
                count = userFollowService.countByFollowingAddress(fcUserFollow.getFollowingAddress());
                res.put("count", count);
                res.put("address", fcUser1.getAddress());
                result.add(res);
            }
        }
        return ResponseUtil.okList(result, fcUserFollows);
    }

    /**
     * @param token
     * @param tokenId
     * @return
     */
    @PostMapping("bid")
    public Object bid(String token, String tokenId) {
        String userAddress = (String) request.getAttribute("userAddress");
        if (StringUtils.isEmpty(userAddress)) {
            return ResponseUtil.unlogin();
        }
        FcUser user = this.userService.getUserByAddress(userAddress);
        if (null == user) {
            return ResponseUtil.unlogin();
        }
        return this.userService.bid(user);
    }

    @PostMapping("listbyaddr")
    public Object listByAddress(String[] addrList) {
        List<String> tempList = Arrays.asList(addrList);
        return ResponseUtil.ok(this.userService.findListByAddrs(tempList));
    }

    @PostMapping("listcontract")
    public Object listcontract(String address) {
        if (StringUtils.isEmpty(address)) {
            return ResponseUtil.ok(this.contractService.findSystemContract());
        }
        FcUser user = this.userService.getUserByAddress(address);
        if (null == user) {
            return ResponseUtil.ok(this.contractService.findSystemContract());
        }
        return ResponseUtil.ok(this.contractService.findByUserAddress(user.getAddress()));
    }
}
