package com.fingerchar.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.admin.annotation.RequiresPermissionsDesc;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcPayToken;
import com.fingerchar.admin.service.FcPayTokenService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 *
 * @author zjm
 * */
@RestController
@RequestMapping("/admin/paytoken")
public class AdminPayTokenController extends BaseController {

	@Autowired
    private FcPayTokenService payTokenService;

    @PostMapping("/list")
    public Object list(){
		IPage<FcPayToken> payTokenList = payTokenService.list(this.getPageInfo());
        return ResponseUtil.okList(payTokenList);
    }
    
    @RequiresPermissions("admin:paytoken:delete")
    @RequiresPermissionsDesc(menu = {"币种管理", "币种管理"}, button = "禁用")
    @PostMapping("/delete")
    public Object disable(String address) {
    	if(StringUtils.isEmpty(address)) {
    		return ResponseUtil.fail(-1, "invalid paytoken address");
    	}
    	return this.payTokenService.delete(address);
    }

    @RequiresPermissions("admin:paytoken:create")
    @RequiresPermissionsDesc(menu = {"币种管理", "币种管理"}, button = "保存")
    @PostMapping("/create")
    public Object create(FcPayToken payToken) {
    	if(null == payToken) {
    		return ResponseUtil.fail(-1, "invalid paytoken");
    	}
    	if(( payToken.getType() != 0 && StringUtils.isEmpty(payToken.getAddress()) ) ||
				null == payToken.getType() ) {
    		return ResponseUtil.fail(-1, "invalid paytoken");
    	}
    	return this.payTokenService.save(payToken);
    }
    
    @RequiresPermissions("admin:paytoken:update")
    @RequiresPermissionsDesc(menu = {"币种管理", "币种管理"}, button = "更新")
    @PostMapping("/update")
    public Object update(FcPayToken payToken) {
    	if(null == payToken || null == payToken.getId()) {
    		return ResponseUtil.fail(-1, "invalid paytoken");
    	}
    	if( ( payToken.getType() != 0 && StringUtils.isEmpty(payToken.getAddress()) )
    		|| null == payToken.getType() ) {
    		return ResponseUtil.fail(-1, "invalid paytoken");
    	}
    	return this.payTokenService.update(payToken);
    }

}
