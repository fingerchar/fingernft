package com.fingerchar.api.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.FcSystemConfigService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;

/**
 * config储服务
 */
@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/config")
public class FcConfigController extends BaseController {

    @Autowired
    private FcSystemConfigService fcSystemConfigService;

    /**
     * 获取Key
     * @return
     */
    @PostMapping("/fetch")
    public  Object fetchList() {
        Map<String, String> data = fcSystemConfigService.queryAll();
        return ResponseUtil.ok(data);
    }
}
