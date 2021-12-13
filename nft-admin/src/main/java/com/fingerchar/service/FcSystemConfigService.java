package com.fingerchar.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.constant.SysConfConstant;
import com.fingerchar.domain.FcContract;
import com.fingerchar.domain.FcSystem;
import com.fingerchar.storage.IpfsStorage;
import com.fingerchar.storage.LocalStorage;
import com.fingerchar.utils.HttpUtils;
import com.fingerchar.utils.NftDappEventUtils;
import com.fingerchar.utils.SpringContextUtil;

@Service
public class FcSystemConfigService {
	
	private static final Logger logger = LoggerFactory.getLogger(FcSystemConfigService.class);

	@Autowired
	private IBaseService baseService;

	public Map<String, String> queryAll() {
		QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
		List<FcSystem> systemList = this.baseService.findByCondition(FcSystem.class, wrapper);
		Map<String, String> systemConfigs = new HashMap<>();
		for (FcSystem item : systemList) {
			systemConfigs.put(item.getKeyName(), item.getKeyValue());
		}
		return systemConfigs;
	}

	public String get(String key) {
		QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcSystem.KEY_NAME, key);
		FcSystem conf = baseService.getByCondition(FcSystem.class, wrapper);
		if (null != conf) {
			return conf.getKeyValue();
		}
		return null;
	}

	@Transactional(rollbackFor=Exception.class)
	public boolean updateConfig(String key, String value) {
		UpdateWrapper<FcSystem> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcSystem.KEY_NAME, key);
		wrapper.set(FcSystem.KEY_VALUE, value);
		if(key.equals(SysConfConstant.NFT721)) {
			UpdateWrapper<FcContract> updateWrapper = new UpdateWrapper<>();
			updateWrapper.eq(FcContract.IS_ADMIN, true).eq(FcContract.TYPE, 3);
			updateWrapper.set(FcContract.ADDRESS, value);
			this.baseService.updateByCondition(FcContract.class, updateWrapper);
		}
		boolean flag = baseService.updateByCondition(FcSystem.class, wrapper) > 0;
		if(SysConfConstant.REFRESH_KEYS.contains(key)) {
			try {
				this.refresh();
			} catch (IOException e) {
				logger.error("刷新失败", e);
			}
		}
		return flag;
	}

	public boolean addConfig(String key, String value,  Boolean show) {
		FcSystem system = new FcSystem();
		system.setKeyName(key);
		system.setKeyValue(value);
		system.setShow(show);
		return baseService.save(system) > 0;

	}

	public List<FcSystem> queryList() {
		QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
		wrapper.or().eq(BaseEntity.DELETED, false);
		List<FcSystem> systemList = baseService.findByCondition(FcSystem.class, wrapper);
		return systemList;
	}
	
	public String refresh() throws IOException {
		QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
    	wrapper.in(FcSystem.KEY_NAME, SysConfConstant.REFRESH_KEYS);
    	List<FcSystem> list = this.baseService.findByCondition(FcSystem.class, wrapper);
    	if(null == list || list.isEmpty()) {
    		throw new RuntimeException("refresh configs is empty");
    	}
    	
    	String url = this.get(SysConfConstant.WEB_SITE);
    	Map<String, String> configs = new HashMap<>();
    	list.stream().forEach(config-> {
    		configs.put(config.getKeyName(), config.getKeyValue());
    	});
    	StorageService storageService = SpringContextUtil.getBean(StorageService.class);
		IpfsStorage storage = new IpfsStorage();
    	storage.setHost(configs.get(SysConfConstant.IPFS_SERVER_IP));
		storage.setPort(Integer.valueOf(configs.get(SysConfConstant.IPFS_SERVER_PORT)));
		storage.setRemoteService(configs.get(SysConfConstant.IPFS_REMOTE_SERVER));
		storage.setRequestBase(configs.get(SysConfConstant.STORAGE_REQUEST_BASE));
		storage.setLoclLocation(configs.get(SysConfConstant.STORAGE_LOCAL_PATH));
		storageService.setIpfsStorage(storage);
		LocalStorage localStorage = new LocalStorage();
		localStorage.setAddress(configs.get(SysConfConstant.STORAGE_REQUEST_BASE));
		localStorage.setStoragePath(configs.get(SysConfConstant.STORAGE_LOCAL_PATH));
		NftDappEventUtils.initWeb3j(configs.get(SysConfConstant.CHAIN_API_URL));
		storageService.setStorage(localStorage);
    	Map<String, String> params = new HashMap<>();
    	params.put("data", JSON.toJSONString(configs));
    	return HttpUtils.post(url + "/fingernft/refresh/refreshconfig", JSON.toJSONString(params));
	}
}
