package com.fingerchar.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcOrderCache;

@Service
public class FcOrderCacheService {

	@Autowired
	IBaseService baseService;
	
	public FcOrderCache getCacheOrder(String key) {
		if(StringUtils.isEmpty(key)) {
			return null;
		}
		QueryWrapper<FcOrderCache> wrapper = new QueryWrapper<>();
		wrapper.eq(FcOrderCache.KEY, key);
		return this.baseService.getByCondition(FcOrderCache.class, wrapper);
	}

	public void del(String key) {
		QueryWrapper<FcOrderCache> wrapper = new QueryWrapper<>();
		wrapper.eq(FcOrderCache.KEY, key);
		this.baseService.deleteByCondition(FcOrderCache.class, wrapper);
	}

	public boolean add(String key, String value) {
		QueryWrapper<FcOrderCache> wrapper = new QueryWrapper<>();
		wrapper.eq(FcOrderCache.KEY, key);
		FcOrderCache cache = this.baseService.getByCondition(FcOrderCache.class, wrapper);
		if(null == cache) {
			cache = new FcOrderCache();
			cache.setKey(key);
			cache.setValue(value);
			cache.setExpiredTime(System.currentTimeMillis() + 1800000);
			return this.baseService.save(cache) > 0;
		} else {
			cache.setValue(value);
			cache.setExpiredTime(System.currentTimeMillis() + 1800000);
			return this.baseService.update(cache) > 0;
		}
		
	}
}
