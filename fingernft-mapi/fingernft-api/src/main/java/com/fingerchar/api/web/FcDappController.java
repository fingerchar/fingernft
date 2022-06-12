package com.fingerchar.api.web;

import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/dapp")
public class FcDappController extends BaseController {

	@Autowired
	FcContractService contractService;
	
	@Autowired
	FcUserService userService;

	@Autowired
	FcSystemConfigManager systemConfigManager;
	
	@PostMapping(value = "/sign")
	public Object sign(String address) throws Exception {
		String  userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if(null == user) {
			return ResponseUtil.unlogin();
		}
		return this.contractService.MinerTokenId(address);
	}
}
