package com.fingerchar.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.annotation.RequiresPermissionsDesc;
import com.fingerchar.domain.FcPayToken;
import com.fingerchar.service.FcPayTokenService;
import com.fingerchar.utils.ResponseUtil;

import java.util.List;

/*
 *
 * @author zjm
 * */
@RestController
@RequestMapping("/admin/paytoken")
public class AdminPayTokenController {

    @Autowired
    private FcPayTokenService payTokenService;


    @PostMapping("/all")
    public Object all(){
        List<FcPayToken> all = payTokenService.findAll();
        return ResponseUtil.okList(all);
    }
    
    @RequiresPermissions("admin:paytoken:disable")
    @RequiresPermissionsDesc(menu = {"支付管理", "支付管理"}, button = "禁用")
    @PostMapping("/disable")
    public Object disable(String address) {
    	if(StringUtils.isEmpty(address)) {
    		return ResponseUtil.fail(-1, "invalid paytoken address");
    	}
    	return this.payTokenService.disable(address);
    }
    
    @RequiresPermissions("admin:paytoken:enable")
    @RequiresPermissionsDesc(menu = {"支付管理", "支付管理"}, button = "启用")
    @PostMapping("/enable")
    public Object enable(String address) {
    	if(StringUtils.isEmpty(address)) {
    		return ResponseUtil.fail(-1, "invalid paytoken address");
    	}
    	return this.payTokenService.enable(address);
    }
    
    @RequiresPermissions("admin:paytoken:save")
    @RequiresPermissionsDesc(menu = {"支付管理", "支付管理"}, button = "保存")
    @PostMapping("/save")
    public Object save(FcPayToken payToken) {
    	if(null == payToken) {
    		return ResponseUtil.fail(-1, "invalid paytoken");
    	}
    	if(StringUtils.isEmpty(payToken.getAddress()) 
    		|| null == payToken.getDecimals()
    		|| null == payToken.getType()
    		|| StringUtils.isEmpty(payToken.getSymbol())
    		|| StringUtils.isEmpty(payToken.getName())) {
    		return ResponseUtil.fail(-1, "invalid paytoken");
    	}
    	return this.payTokenService.saveOrUpdate(payToken);
    }

}
