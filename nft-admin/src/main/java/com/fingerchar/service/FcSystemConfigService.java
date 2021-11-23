package com.fingerchar.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.constant.RedisConstant;
import com.fingerchar.dao.ext.FcSystemExtMapper;
import com.fingerchar.domain.FcSystem;

@Service
public class FcSystemConfigService {

	@Autowired
	private FcSystemExtMapper systemExtMapper;

	@Autowired
	private IBaseService baseService;

	@Autowired
	FcRedisService redisService;

	public Map<String, String> queryAll() {
		QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);

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

	public Integer save(String key, String value) {
		return this.systemExtMapper.update(key, value);
	}

	public void updateConfig(Map<String, String> data) {
		for (Map.Entry<String, String> entry : data.entrySet()) {
			UpdateWrapper<FcSystem> wrapper = new UpdateWrapper<>();
			wrapper.eq(FcSystem.KEY_NAME, entry.getKey());
			wrapper.set(FcSystem.KEY_VALUE, entry.getValue());
			baseService.updateByCondition(FcSystem.class, wrapper);
		}

	}

	public boolean updateConfig(String keyName, String keyValue) {
		UpdateWrapper<FcSystem> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcSystem.KEY_NAME, keyName);
		wrapper.set(FcSystem.KEY_VALUE, keyValue);
		boolean flag = false;
		flag = baseService.updateByCondition(FcSystem.class, wrapper) > 0;
		if (flag) {
			flag = this.redisService.set(RedisConstant.SYS_CONFIG_PRE + keyName, keyValue);
		}
		return flag;
	}

	public void addConfig(String key, String value) {
		FcSystem system = new FcSystem();
		system.setKeyName(key);
		system.setKeyValue(value);
		baseService.save(system);

	}

	public List<FcSystem> queryList() {
		QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
		wrapper.or().eq(BaseEntity.DELETED, false);
		List<FcSystem> systemList = baseService.findByCondition(FcSystem.class, wrapper);
		return systemList;
	}
}
