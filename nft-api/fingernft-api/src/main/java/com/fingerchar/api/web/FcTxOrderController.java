package com.fingerchar.api.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.dto.TxOrderInfo;
import com.fingerchar.api.service.FcOrderService;
import com.fingerchar.api.service.FcRedisService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/tx")
public class FcTxOrderController extends BaseController {

	@Autowired
	FcOrderService orderService;

	@Autowired
	FcUserService userService;

	@Autowired
	FcRedisService redisService;

	@PostMapping("add")
	public Object add(TxOrderInfo order) {
		String userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if(null == user) {
			return ResponseUtil.unlogin();
		}
		return this.orderService.addOrder(order, user);
	}
}
