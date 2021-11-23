package com.fingerchar.controller;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.base.controller.BaseController;
import com.fingerchar.constant.RedisConstant;
import com.fingerchar.domain.FcPayToken;
import com.fingerchar.service.FcPayTokenService;
import com.fingerchar.service.FcRedisService;
import com.fingerchar.service.FcSystemConfigService;
import com.fingerchar.utils.ResponseUtil;

@RestController
@RequestMapping("/admin/config")
public class AdminConfigRefreshController extends BaseController {
	
	public static final Logger logger = LoggerFactory.getLogger(AdminConfigRefreshController.class);
	
	@Autowired
	FcRedisService redisService;
	
	@Autowired
	FcSystemConfigService systemConfigService;
	
	@Autowired
	FcPayTokenService payTokenService;
	
	@PostMapping("refreshsysconfig")
	public Object refreshSystemConfig() {
		logger.info("刷新系统配置数据...");
		Map<String,  String> map = systemConfigService.queryAll();
		Iterator<String> it = map.keySet().iterator();
		String key = null;
		while(it.hasNext()) {
			key = it.next();
			if(!this.redisService.set(RedisConstant.SYS_CONFIG_PRE + key, map.get(key))) {
				logger.error("系统参数=>" + key + "刷新到redis失败!");
			}
		}
		logger.info("刷新系统配置数据完成...");
		return ResponseUtil.ok();
	}
	
	@PostMapping("refreshpaytoken")
	public Object refreshPayToken() {
		logger.info("刷新支付token数据...");
		List<FcPayToken> list = this.payTokenService.findAll();
		int len = list.size();
		FcPayToken token = null;
		for(int i=0; i<len; i++) {
			token = list.get(i);
			if(!this.redisService.set(RedisConstant.PAY_TOKEN_PRE + token.getAddress(), token)) {
				logger.error("支付token=>" + token.getAddress() + "刷新到redis失败!");
			}
		}
		logger.info("刷新支付token完成...");
		return ResponseUtil.ok();
	}
	
}
