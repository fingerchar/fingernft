package com.fingerchar.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.annotation.RequiresPermissionsDesc;
import com.fingerchar.base.controller.BaseController;
import com.fingerchar.domain.StaNft;
import com.fingerchar.domain.StaNftDeal;
import com.fingerchar.service.FcOrderService;
import com.fingerchar.service.FcUserService;
import com.fingerchar.service.StaNftDealService;
import com.fingerchar.service.StaNftService;
import com.fingerchar.service.StaUserService;
import com.fingerchar.utils.ResponseUtil;

/*
 * 后台统计
 * @author zjm
 * */

@RestController
@RequestMapping("/admin/statistics")
public class AdminStatisticsController extends BaseController {

    @Autowired
    private FcOrderService orderService;
    @Autowired
    private StaNftService staNftService;
    @Autowired
    private StaNftDealService staNftDealService;
    @Autowired
    private StaUserService staUserService;
    @Autowired
    private FcUserService userService;

    @RequiresPermissions("admin:statistics:transaction")
    @RequiresPermissionsDesc(menu = {"交易管理", "交易统计"}, button = "统计")
    @PostMapping("/transaction")
    public Object transaction() {
        Map<String, Object> objectMap = orderService.statTransaction();
        return ResponseUtil.ok(objectMap);
    }

    @PostMapping("/list")
    public Object getIndex(Long staTime){
        if (null != staTime){
            staTime = staTime - staTime % 3600;
        }else {
            staTime = System.currentTimeMillis() / 1000 - System.currentTimeMillis() / 1000 % (24 * 60 * 60);
        }
        Map<String,Object> result = new HashMap<>();
        List<StaNft> staNftList = staNftService.getList(staTime);
        Map<String, Object> contrastNft = staNftService.contrastNft(staNftList, staTime);
        List<StaNftDeal> staNftDealList = staNftDealService.getList(staTime);
        Map<String,Object> contrastNftDeal= staNftDealService.contrastNftDeal(staNftDealList, staTime);
        Map<String, Integer> staUserList = staUserService.getList(staTime);
        Map<String, Object> newCreateList = userService.newCreateList(staTime);
        result.put("staNftList",staNftList);
        result.put("staNftDealList",staNftDealList);
        result.put("staUserList",staUserList);
        result.put("newCreateList",newCreateList);
        result.put("contrastNftDeal",contrastNftDeal);
        result.put("contrastNft",contrastNft);
        return ResponseUtil.ok(result);
    }
}
