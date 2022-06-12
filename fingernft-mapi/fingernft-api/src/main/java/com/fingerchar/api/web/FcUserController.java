package com.fingerchar.api.web;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.service.*;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.api.utils.JwtHelper;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.constant.SysConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.manager.FcUserFollowManager;
import com.fingerchar.core.util.IpUtil;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.core.util.Str2ListUtils;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.domain.FcUserLog;
import com.fingerchar.db.domain.FcUserToken;
import com.fingerchar.db.dto.SearchNftParamDto;
import com.fingerchar.db.vo.NftInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
    FcSystemConfigManager systemConfigManager;

    @Autowired
    FcUserFollowManager followManager;

    @Autowired
    FcUserTokenService userTokenService;

    /*
     * 一般日志：用户觉得需要查看的一般操作日志，建议是默认的日志级别
     * 安全日志：用户安全相关的操作日志，例如登录、删除管理员
     * */
    public static final Integer LOG_TYPE_AUTH = 1;
    // 1表示登录
    public static final String LOG_ACTION_LOGIN = "1";

    /**
     * 用户登录
     * @param userAddress
     * @param signature
     * @param timestamp
     * @return
     */
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
            return ResponseUtil.fail(-1, "LoginMessage is empty");
        }
        loginMessage = loginMessage.concat(" ").concat(timestamp.toString());
        if (!DappCryptoUtil.validate(signature, loginMessage, userAddress)) {
            this.packUserLog(LOG_TYPE_AUTH, LOG_ACTION_LOGIN, false, "无效的签名", "");
            return ResponseUtil.fail(-1, "invalid sign");
        }

        List<FcUser> userList = userService.queryUserByAddrAndType(userAddress);
        FcUser user = null;
        if (userList.size() > 1) {
            return ResponseUtil.serious();
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
        return this.systemConfigManager.getKeyValue(SysConfConstant.LOGIN_MESSAGE);
    }

    /**
     * 用户刷新页面登录
     * @return
     */
    @PostMapping("reload")
    public Object reload() {
        String token = request.getHeader(SysConstant.WEB_TOKEN_NAME);
        String userAddress = null;
        if (StringUtils.isEmpty(token) || StringUtils.isEmpty((userAddress = JwtHelper.verifyTokenAndGetUserAddress(token)))) {
            return ResponseUtil.fail(-1, "");
        }

        FcUserToken userToken = this.userTokenService.getUserToken(userAddress);
        if(null == userToken || null == userToken.getUserToken() || !userToken.getUserToken().equals(token)) {
            return ResponseUtil.fail(-1, "");
        }

        FcUser user = this.userService.getUserByAddress(userAddress);

        Map<Object, Object> result = new HashMap<Object, Object>();

        result.put("user", user);
        this.packUserLog(LOG_TYPE_AUTH, LOG_ACTION_LOGIN, true, "用户重新登录", user.getAddress());
        return ResponseUtil.ok(result);
    }

    /**
     * 获取个人配置信息
     * @return
     */
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

    /**
     * 修改个人配置信息
     * @param userinfo
     * @return
     */
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

    /**
     * 设置用户背景图片
     * @param url
     * @return
     */
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

    /**
     * 获取用户个人信息
     * @param address 用户地址
     * @return
     */
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

    /**
     *获取用户页面分栏的数量信息
     * @param address 用户地址
     * @return
     */
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
        Integer followCount = followManager.countFollow(fcUser.getAddress());
        Integer followerCount = followManager.countFollower(fcUser.getAddress());
        result.put("saleCount", saleCount);
        result.put("collectionCount", collectionCount);
        result.put("createCount", createCount);
        result.put("likeCount", likeCount);
        result.put("followCount", followCount);
        result.put("followerCount", followerCount);
        return ResponseUtil.ok(result);
    }

    @PostMapping("match")
    public Object match(String address) {
        if (null == address) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<String> paramsList = Str2ListUtils.sliceString2StringArray(address);
        if (paramsList.size() == 0) {
            return ResponseUtil.ok(new ArrayList<>());
        }
        List<FcUser> fcUsers = userService.findListByAddrs(paramsList);
        return ResponseUtil.ok(fcUsers);
    }

    /**
     * 获取用户在售卖中的nft
     * @param address 用户地址
     * @return
     */
    @PostMapping("onsales")
    public Object onsales(String address) {
        if (address == null) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        IPage<NftInfoVo> iPage = contractNftService.findOnSellListByAddress(address, this.getPageInfo());
        return ResponseUtil.okList(iPage);
    }

    /**
     * 获取用户的nft
     * @param address 用户地址
     * @return
     */
    @PostMapping("collections")
    public Object collections(String address, String token, String tokenId) {
        if (address == null) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        SearchNftParamDto paramDto = new SearchNftParamDto();
        paramDto.setOwner(address);
        paramDto.setAddress(token);
        paramDto.setTokenId(tokenId);
        IPage<NftInfoVo> iPage = contractNftService.findListByUserAddress(paramDto, this.getPageInfo());
        return ResponseUtil.okList(iPage);
    }

    /**
     * 获取用户的nft
     * @param paramDto
     * @return
     */
    @PostMapping("nftlist")
    public Object nftlist(SearchNftParamDto paramDto) {
        if (StringUtils.isEmpty(paramDto.getAddress()) ||
            StringUtils.isEmpty(paramDto.getOwner())) {
            return ResponseUtil.okList(new ArrayList<>());
        }
        List<FcContractNft> nftList = this.contractNftService.nftlist(paramDto);
        return ResponseUtil.okList(nftList);
    }


    /**
     * 获取用户铸造的nft
     * @param address 用户地址
     * @return
     */
    @PostMapping("created")
    public Object created(String address) {
        if (null == address) {
            return ResponseUtil.okList(this.getPageInfo());
        }
        IPage<NftInfoVo> iPage = this.contractNftService.findByCreators(address, this.getPageInfo());
        return ResponseUtil.okList(iPage);
    }

    /**
     * 获取用户收藏的nft
     * @param address 用户地址
     * @return
     */
    @PostMapping("like")
    public Object likes(String address) {
        FcUser fcUser = userService.getUserByAddress(address);
        if (fcUser == null) {
            return ResponseUtil.NotFoud();
        }
        if (null == address) {
            return ResponseUtil.okList(this.getPageInfo());
        }

        IPage<NftInfoVo> iPage = this.contractNftService.findByLike(fcUser.getAddress(), this.getPageInfo());
        return ResponseUtil.okList(iPage);
    }


    /**
     * 根据地址批量获取用户地址与昵称
     * @param addrList 用户地址数组
     * @return
     */
    @PostMapping("listbyaddr")
    public Object listByAddress(String[] addrList) {
        List<String> tempList = Arrays.asList(addrList);
        return ResponseUtil.ok(this.userService.findListByAddrs(tempList));
    }

    /**
     * 获取用户的收藏品合约列表与官方合约列表
     * @param address 用户ID
     * @return
     */
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
