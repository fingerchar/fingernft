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

    @PostMapping("/list")
    public Object list(){
        List<FcPayToken> all = payTokenService.findAll();
        return ResponseUtil.okList(all);
    }
    
    @RequiresPermissions("admin:paytoken:delete")
    @RequiresPermissionsDesc(menu = {"支付管理", "支付管理"}, button = "禁用")
    @PostMapping("/delete")
    public Object disable(String address) {
    	if(StringUtils.isEmpty(address)) {
    		return ResponseUtil.fail(-1, "invalid paytoken address");
    	}
    	return this.payTokenService.delete(address);
    }

    @RequiresPermissions("admin:paytoken:create")
    @RequiresPermissionsDesc(menu = {"支付管理", "支付管理"}, button = "保存")
    @PostMapping("/create")
    public Object create(FcPayToken payToken) {
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
    	return this.payTokenService.save(payToken);
    }
    
    @RequiresPermissions("admin:paytoken:update")
    @RequiresPermissionsDesc(menu = {"支付管理", "支付管理"}, button = "更新")
    @PostMapping("/update")
    public Object update(FcPayToken payToken) {
    	if(null == payToken || null == payToken.getId()) {
    		return ResponseUtil.fail(-1, "invalid paytoken");
    	}
    	if(StringUtils.isEmpty(payToken.getAddress()) 
    		|| null == payToken.getDecimals()
    		|| null == payToken.getType()
    		|| StringUtils.isEmpty(payToken.getSymbol())
    		|| StringUtils.isEmpty(payToken.getName())) {
    		return ResponseUtil.fail(-1, "invalid paytoken");
    	}
    	return this.payTokenService.update(payToken);
    }

}
