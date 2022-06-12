package com.fingerchar.admin.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcContractManager;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcSystem;
import com.fingerchar.db.dto.ConfigContract;
import com.fingerchar.db.dto.ConfigDeploy;
import com.fingerchar.db.dto.ConfigNetwork;
import com.fingerchar.db.vo.ConfigFetchVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.web3j.crypto.Credentials;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class FcSystemConfigService {


	@Autowired
	FcContractManager contractManager;

	@Autowired
	FcSystemConfigManager systemConfigManager;

	/**
	 * 获取系统名称和系统配置的键值对
	 */
	public ConfigFetchVo allShow() {
		List<FcSystem> systemList = this.systemConfigManager.allShow();
		ConfigFetchVo fetchVo = new ConfigFetchVo();
		for (FcSystem item : systemList) {
			if(org.apache.commons.lang3.StringUtils.isEmpty(item.getKeyValue())){
				continue;
			}
			switch (item.getKeyName()){
				case SysConfConstant.IPFS_URL:
					fetchVo.setIpfsUrl(item.getKeyValue());
					break;
				case SysConfConstant.SELLER_FEE:
					fetchVo.setSellerFee(item.getKeyValue());
					break;
				case SysConfConstant.BUYER_FEE:
					fetchVo.setBuyerFee(item.getKeyValue());
					break;
				case SysConfConstant.CDN_URL:
					fetchVo.setCdnUrl(item.getKeyValue());
					break;
				case SysConfConstant.LOGIN_MESSAGE:
					fetchVo.setLoginMessage(item.getKeyValue());
					break;
				case SysConfConstant.WEBSITE:
					fetchVo.setWebsite(item.getKeyValue());
					break;
				case SysConfConstant.CONFIG_DEPLOY:
					ConfigDeploy configDeploy = JSON.parseObject(item.getKeyValue(), ConfigDeploy.class);
					String minerAddress = configDeploy.getMinerAddress();
					fetchVo.setMiner(minerAddress);
					break;
				case SysConfConstant.CONFIG_NETWORK:
					ConfigNetwork configNetwork = JSON.parseObject(item.getKeyValue(), ConfigNetwork.class);
					configNetwork.setRpc(null);
					fetchVo.setNetwork(configNetwork);
					break;
				default:
					break;
			}
		}
		return fetchVo;
	}

	public Map<String, String> all() {
		List<FcSystem> systemList = this.systemConfigManager.all();
		Map<String, String> systemConfigs = new HashMap<>();
		for (FcSystem item : systemList) {
			systemConfigs.put(item.getKeyName(), item.getKeyValue());
		}
		return systemConfigs;
	}

	@Transactional(rollbackFor=Exception.class)
	public Object updateConfig(String key, String value) {
		value = this.convertSystemValue(key, value);
		if(null == value){
			return ResponseUtil.fail(-1, "Invalid config value");
		}
		UpdateWrapper<FcSystem> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcSystem.KEY_NAME, key);
		wrapper.set(FcSystem.KEY_VALUE, value);

		Integer result = this.systemConfigManager.update(key, value);
		if(!result.equals(0)) {
			return ResponseUtil.ok();
		} else {
			return ResponseUtil.fail(-1, "更新失败");
		}
	}

	private String convertSystemValue(String key, String value){
		if(StringUtils.isEmpty(value)){
			return value;
		}
		switch (key){
			case SysConfConstant.CONFIG_DEPLOY:
				return this.convertConfigDeployValue(value);
			case SysConfConstant.CONFIG_CONTRACT:
				this.processConfigContract(value);
				break;
		}

		return value;
	}

	private String convertConfigDeployValue(String value){
		ConfigDeploy configDeploy = JSON.parseObject(value, ConfigDeploy.class);

		String beneficiary = configDeploy.getBeneficiary();
		if(!DappWeb3jUtil.isValidAddress(beneficiary)){
			return null;
		}
		String buyerFeeKey = configDeploy.getBuyerFeeKey();
		Credentials c = Credentials.create(buyerFeeKey);
		configDeploy.setBuyerFeeAddress(c.getAddress().toLowerCase(Locale.ROOT));

		String minerKey = configDeploy.getMinerKey();
		c = Credentials.create(minerKey);
		configDeploy.setMinerAddress(c.getAddress().toLowerCase(Locale.ROOT));
		return JSON.toJSONString(configDeploy);
	}

	private void processConfigContract(String value){
		ConfigDeploy configDeploy = this.systemConfigManager.getConfigDeploy();
		ConfigContract configContract = JSON.parseObject(value, ConfigContract.class);
		String nft721Address = configContract.getNft721Address();
		if(!StringUtils.isEmpty(nft721Address)){
			this.contractManager.setAdminContract(
					configDeploy.getContractName(),
					configDeploy.getContractSymbol(),
					nft721Address,
					ContractType.ERC721.getType()
			);
		}
	}

}
