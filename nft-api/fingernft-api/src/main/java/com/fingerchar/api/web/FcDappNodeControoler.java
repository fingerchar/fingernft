package com.fingerchar.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcSystemConfigService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.api.vo.VSRSignInfo;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/dapp")
public class FcDappNodeControoler extends BaseController {

	@Autowired
	FcContractService contractService;
	
	@Autowired
	FcUserService userService;
	
	@Autowired
	FcSystemConfigService configService;
	
	@PostMapping(value = "/sign")
	public Object sign(String address) throws Exception {
		String  userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if(null == user) {
			return ResponseUtil.fail(-1, "Unkown user");
		}
		if(StringUtils.isEmpty(address)) {
			return ResponseUtil.fail(-1, "unkown contract address");
		}
		String tokenId = this.contractService.getTokenId(address);
		if(null == tokenId) {
			return ResponseUtil.fail(-1, "unkown tokenId");
		}
		VSRSignInfo info = DappCryptoUtil.sign(address, tokenId);
		if(null == info) {
			return ResponseUtil.fail(-1, "sign error");
		} else {
			info.setTokenId(tokenId);
			return ResponseUtil.ok(info);
		}
	}
}
