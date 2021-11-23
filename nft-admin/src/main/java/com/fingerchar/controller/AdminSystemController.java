package com.fingerchar.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.base.controller.BaseController;
import com.fingerchar.service.FcSystemConfigService;
import com.fingerchar.utils.ResponseUtil;

/*
 *
 * @author zjm
 * */
@RestController
@RequestMapping("/admin/config")
public class AdminSystemController extends BaseController {

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
    
    @PostMapping("/update")
    public Object update(String keyName, String keyValue) {
    	if(this.fcSystemConfigService.updateConfig(keyName, keyValue)) {
    		return ResponseUtil.ok();
    	} else {
    		return ResponseUtil.fail();
    	}
    }
}
