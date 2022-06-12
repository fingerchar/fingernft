package com.fingerchar.api.web;

import com.fingerchar.api.service.FcSystemConfigService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * config储服务
 */
@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/config")
public class FcConfigController extends BaseController {

    @Autowired
    private FcSystemConfigService fcSystemConfigService;

    /**
     * 获取系统配置数据
     * @return
     */
    @PostMapping("/fetch")
    public  Object fetch() {
        return ResponseUtil.ok(fcSystemConfigService.allShow());
    }

    @PostMapping("/gasTracker")
    public  Object gasTracker() {
        return ResponseUtil.ok(fcSystemConfigService.gasTracker());
    }

}
