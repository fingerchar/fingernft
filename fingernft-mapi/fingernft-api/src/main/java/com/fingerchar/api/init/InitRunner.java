package com.fingerchar.api.init;

import com.alibaba.fastjson.JSON;
import com.fingerchar.api.service.StorageService;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.api.utils.DappWeb3jUtil;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.storage.IpfsStorage;
import com.fingerchar.core.util.SpringContextUtil;
import com.fingerchar.db.dto.ConfigDeploy;
import com.fingerchar.db.dto.ConfigNetwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.StringUtils;

@Component
public class InitRunner implements CommandLineRunner {
	
	private static final Logger logger = LoggerFactory.getLogger(InitRunner.class);
	
	@Autowired
	FcSystemConfigManager systemConfigManager;

	@Override
	public void run(String... args) throws Exception {
		logger.info("开始初始化数据");
		this.initKey();
		this.initIpfs();
		this.initWeb3j();
		logger.info("初始化完成...");
	}
	
	private void initKey() {
		logger.info("初始化签名key");
		ConfigDeploy configDeploy = this.systemConfigManager.getConfigDeploy();
		if(null == configDeploy){
			return;
		}
		String mintKey = configDeploy.getMinerKey();
		String buyerFeeKey = configDeploy.getBuyerFeeKey();
		DappCryptoUtil.setMintKey(mintKey);
		DappCryptoUtil.setBuyerFeeKey(buyerFeeKey);
		logger.info("初始化签名key完成");
	}
	
	
	private void initIpfs() {
		logger.info("初始化ipfs");
		String host = this.systemConfigManager.getKeyValue(SysConfConstant.IPFS_SERVER_IP);
		String port = this.systemConfigManager.getKeyValue(SysConfConstant.IPFS_SERVER_PORT);
		String remoteServer = this.systemConfigManager.getKeyValue(SysConfConstant.IPFS_REMOTE_SERVER);

		String staticPath = this.systemConfigManager.getKeyValue(SysConfConstant.STATIC_LOCAL_PATH);
		if(StringUtils.isEmpty(staticPath)){
			staticPath = "/";
		}
		staticPath = staticPath.endsWith("/") ? staticPath + "static/upload" : staticPath + "/static/upload";
		String uploadPath = "/static/upload";

		StorageService storageService = SpringContextUtil.getBean(StorageService.class);
		IpfsStorage storage = new IpfsStorage();
		storage.setHost(host);
		storage.setPort(StringUtils.isEmpty(port)? 0 : Integer.valueOf(port));
		storage.setLoclLocation(staticPath);
		storage.setRemoteService(remoteServer);
		storage.setRequestBase(uploadPath);
		storageService.setStorage(storage);
		logger.info("初始化ipfs完成");
	}
	
	private void initWeb3j() {
		String value = this.systemConfigManager.getKeyValue(SysConfConstant.CONFIG_NETWORK);
		if(value.isEmpty()){
			return;
		}
		ConfigNetwork configNetwork = JSON.parseObject(value, ConfigNetwork.class);
		if(configNetwork.getRpc().isEmpty()){
			return;
		}

		logger.info("初始化web3j");
		DappWeb3jUtil.initWeb3j(configNetwork.getRpc());
		logger.info("初始化web3j完成");
	}
}
