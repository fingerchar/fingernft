package com.fingerchar.api.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.dto.PrepareOrderInfo;
import com.fingerchar.api.service.FcOrderCacheService;
import com.fingerchar.api.service.FcOrderService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcOrderCache;
import com.fingerchar.db.domain.FcUser;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/order")
public class FcOrderController extends BaseController {

	@Autowired
	FcOrderService orderService;
	
	@Autowired
	FcOrderCacheService orderCacheService;

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
		if(StringUtils.isEmpty(order.getType())){
			return ResponseUtil.fail(-1,"not type found ");
		}

		if(order.getType() == 1 || order.getType() == 2 || order.getType() == 5 || order.getType() == 6) {
			FcOrderCache cache = null;
			if(order.getType() == 1 || order.getType() == 2) {		
				cache = this.orderCacheService.getCacheOrder(user.getAddress() + order.getSellToken() + order.getSellTokenId());
			} else {
				cache = this.orderCacheService.getCacheOrder(user.getAddress() + order.getBuyToken() + order.getBuyTokenId());
			}
			if(null == cache || cache.getExpiredTime() < System.currentTimeMillis()) {
				return ResponseUtil.fail(-1, "Error order");
			}
			
			PrepareOrderInfo cacheOrder = JSON.parseObject(cache.getValue(), PrepareOrderInfo.class);
			
			if(!cacheOrder.getSalt().equals(order.getSalt()) || !cacheOrder.getOwner().toLowerCase().equals(order.getOwner().toLowerCase())) {
				return ResponseUtil.fail(-1, "Error order");
			}
			if(!DappCryptoUtil.validate(order.getSignature(), order.getMessage(), user.getAddress())) {
				return ResponseUtil.fail(-1, "Signature error");
			}
		}
		return this.orderService.addOrder(order, user);
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
		return this.orderService.prePare(order, user);
	}

	@PostMapping(value = "/details")
	public Object orderDetails(Long orderId) {
		if(null == orderId) {
			return ResponseUtil.fail(-1, "order id can't be null");
		}
		return ResponseUtil.ok(this.orderService.findById(orderId));
	}

	@PostMapping(value = "/buyerFee")
	public Object buyPrepare(String token, String tokenId, String owner, String salt, Integer type) throws Exception {
		if(StringUtils.isEmpty(tokenId) || null == tokenId || StringUtils.isEmpty(owner) || StringUtils.isEmpty(salt) || null == type) {
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
		return this.orderService.buyPrepare(token, tokenId, owner, salt, user, type);
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
				|| !address.toLowerCase().equals(order.getOwner().toLowerCase())
				|| StringUtils.isEmpty(order.getBuyToken())
				|| StringUtils.isEmpty(order.getBuyValue())
				|| StringUtils.isEmpty(order.getSellToken())
				|| StringUtils.isEmpty(order.getSellTokenId())
				|| StringUtils.isEmpty(order.getSellValue())
				|| StringUtils.isEmpty(order.getBuyType())
				|| StringUtils.isEmpty(order.getSellType())
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
