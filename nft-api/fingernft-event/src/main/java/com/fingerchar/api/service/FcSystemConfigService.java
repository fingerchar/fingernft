package com.fingerchar.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.constant.RedisConstant;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcSystem;

@Service
public class FcSystemConfigService {

	@Autowired
	private IBaseService baseService;

	@Autowired
	FcRedisService redisService;

	public String get(String key) {
		QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcSystem.KEY_NAME, key);
		FcSystem conf = baseService.getByCondition(FcSystem.class, wrapper);
		if (null != conf) {
			return conf.getKeyValue();
		}
		return null;
	}

	public String getValue(String key) {
		Object temp = this.redisService.get(RedisConstant.SYS_CONFIG_PRE + key);
		String value = null;;
		if(null != temp) {
			value = (String)temp;
		} else {
			QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
			wrapper.eq(FcSystem.KEY_NAME, key);
			FcSystem  config = this.baseService.getByCondition(FcSystem.class, wrapper);
			if(null == config) {
				value = null;
			} else {
				value = config.getKeyValue();
			}
		}
		return value;
	}
}
