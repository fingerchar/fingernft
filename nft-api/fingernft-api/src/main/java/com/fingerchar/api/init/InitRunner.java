package com.fingerchar.api.init;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.fingerchar.api.config.KeyProperties;
import com.fingerchar.api.constant.RedisConstant;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.FcPayTokenService;
import com.fingerchar.api.service.FcRedisService;
import com.fingerchar.api.service.FcSystemConfigService;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.api.utils.DappWeb3jUtil;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcPayToken;

@Component
public class InitRunner implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(InitRunner.class);
	
	@Autowired
	FcRedisService redisService;
	
	@Autowired
	FcSystemConfigService systemConfigService;

	@Autowired
	FcPayTokenService payTokenService;
	
	@Autowired
	IBaseService baseService;
	
	@Override
	public void run(String... args) throws Exception {
		logger.info("开始初始化数据");
		this.initSystemConfig();
		this.initPayToken();
		this.initKey();
		this.initWeb3j();
		logger.info("初始化完成...");
	}
	
	private void initSystemConfig() {
		logger.info("初始化系统配置数据...");
		Map<String,  String> map = systemConfigService.queryAll();
		Iterator<String> it = map.keySet().iterator();
		String key = null;
		while(it.hasNext()) {
			key = it.next();
			if(!this.redisService.set(RedisConstant.SYS_CONFIG_PRE + key, map.get(key))) {
				logger.error("系统参数=>" + key + "初始化到redis失败!");
			}
		}
		logger.info("初始化系统配置数据完成...");
	}

	
	private void initPayToken() {
		logger.info("初始化支付token数据...");
		List<FcPayToken> list = this.payTokenService.findAll();
		int len = list.size();
		FcPayToken token = null;
		for(int i=0; i<len; i++) {
			token = list.get(i);
			this.redisService.set(RedisConstant.PAY_TOKEN_PRE + token.getAddress(), token);
		}
		logger.info("初始化支付token完成...");
	}
	
	private void initKey() {
		logger.info("初始化密钥数据...");
		DappCryptoUtil.setKeyPath(KeyProperties.getKeyPath());
		DappCryptoUtil.setPubKey(KeyProperties.getRsaPubKey());
		DappCryptoUtil.initKey();
		logger.info("初始化密钥数据完成...");
	}
	
	private void initWeb3j() {
		String url = this.systemConfigService.get(SysConfConstant.CHAIN_API_URL);
		logger.info("初始化web3j");
		DappWeb3jUtil.initWeb3j(url);
		logger.info("初始化web3j完成");
	}
}
