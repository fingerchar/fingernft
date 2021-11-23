package com.fingerchar.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.constant.RedisConstant;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcSystemExtMapper;
import com.fingerchar.db.domain.FcSystem;

@Service
public class FcSystemConfigService {
	
	@Autowired
    private IBaseService baseService;
    
    @Autowired
    private FcSystemExtMapper systemExtMapper;
    
    @Autowired
    FcRedisService redisService;

    /**
    * 获取系统名称和系统配置的键值对
    */
    public Map<String, String> queryAll() {
    	QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
    	wrapper.eq(BaseEntity.DELETED, false);
       
        List<FcSystem> systemList = this.baseService.findByCondition(FcSystem.class, wrapper);
        Map<String, String> systemConfigs = new HashMap<>();
        for (FcSystem item : systemList) {
        	if(SysConfConstant.CHAIN_API_URL.equals(item.getKeyName())) {
        		continue;
        	}
            systemConfigs.put(item.getKeyName(), item.getKeyValue());
        }

        return systemConfigs;
    }


    public String get(String key) {
    	QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
    	wrapper.eq(FcSystem.KEY_NAME, key).eq(BaseEntity.DELETED, false);
        FcSystem conf = this.baseService.getByCondition(FcSystem.class, wrapper);
        if(null != conf) {
        	return conf.getKeyValue();
        }
        return null;
    }
    
    public Integer save(String key, String value) {
    	return this.systemExtMapper.update(key, value);
    }
    
    
    public String getValue(String key) {
		Object temp = this.redisService.get(RedisConstant.SYS_CONFIG_PRE + key);
		String sellFee = null;;
		if(null != temp) {
			sellFee = (String)temp;
		} else {
			QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
			wrapper.eq(FcSystem.KEY_NAME, key);
			FcSystem  config = this.baseService.getByCondition(FcSystem.class, wrapper);
			if(null == config) {
				sellFee = null;
			} else {
				sellFee = config.getKeyValue();
			}
		}
		return sellFee;
	}
}
