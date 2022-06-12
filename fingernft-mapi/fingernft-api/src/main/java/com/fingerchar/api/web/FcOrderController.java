package com.fingerchar.api.web;

import com.fingerchar.db.dto.PrepareOrderInfo;
import com.fingerchar.api.service.FcOrderService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/order")
public class FcOrderController extends BaseController {

	@Autowired
	FcOrderService orderService;
	
	@Autowired
	FcUserService userService;

	@PostMapping("add")
	public Object add(PrepareOrderInfo order) {
		String userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if(null == user) {
			return ResponseUtil.unlogin();
		}
		if(!user.getAddress().equalsIgnoreCase(order.getOwner())){
			return ResponseUtil.fail(-1, "order owner incorrect");
		}
		if(null == order.getType()){
			return ResponseUtil.fail(-1,"order type is empty");
		}

		if(!DappCryptoUtil.validate(order.getSignature(), order.getMessage(), user.getAddress())) {
			return ResponseUtil.fail(-1, "signature is incorrect");
		}
		return this.orderService.addOrder(order);
	}

	@PostMapping(value = "/prepare")
	public Object prepare(PrepareOrderInfo order) throws Exception {
		String userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if(null == user) {
			return ResponseUtil.unlogin();
		}

		if(!this.checkOrder(order, user.getAddress())) {
			return ResponseUtil.fail(-1, "invalid order");
		}
		return this.orderService.prepareOrder(order, user);
	}

	@PostMapping(value = "/buyerFee")
	public Object buyPrepare(PrepareOrderInfo order) throws Exception {
		if((
				StringUtils.isEmpty(order.getBuyToken()) ||
				null == order.getBuyTokenId() ||
				StringUtils.isEmpty(order.getOwner()) ||
				StringUtils.isEmpty(order.getSalt()) ||
				null == order.getType()) &&
		((
				StringUtils.isEmpty(order.getSellToken()) ||
				null == order.getSellTokenId() ||
				StringUtils.isEmpty(order.getOwner()) ||
				StringUtils.isEmpty(order.getSalt()) ||
				null == order.getType()
		))
		) {
			return ResponseUtil.fail(-1, "parameter invalid");
		}
		String userAddress = (String) request.getAttribute("userAddress");
		if(null == userAddress) {
			return ResponseUtil.unlogin();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if(null == user) {
			return ResponseUtil.unlogin();
		}
		return this.orderService.buyPrepare(order);
	}

	@PostMapping(value = "/get")
	public Object get(String caddress, String tokenId, String owner, Integer type) {
		return this.orderService.get(caddress, tokenId, owner, type);
	}

	/**
	 * 验证order数据是否完整
	 * @param order
	 * @param address
	 * @return
	 */
	private boolean checkOrder(PrepareOrderInfo order, String address) {
		if(null == order
				|| StringUtils.isEmpty(order.getOwner())
				|| !address.equalsIgnoreCase(order.getOwner())
				|| StringUtils.isEmpty(order.getBuyToken())
				|| StringUtils.isEmpty(order.getBuyValue())
				|| StringUtils.isEmpty(order.getBuyTokenId())
				|| StringUtils.isEmpty(order.getSellToken())
				|| StringUtils.isEmpty(order.getSellTokenId())
				|| StringUtils.isEmpty(order.getSellValue())
		) {
			return false;
		}
		
		int temp = new BigDecimal(order.getBuyValue()).compareTo(new BigDecimal("0"));
		if(temp <= 0) {
			return false;
		}
		temp = new BigDecimal(order.getSellValue()).compareTo(new BigDecimal("0"));
		if(temp <= 0) {
			return false;
		}
		
		return true;
	}
}
