package com.fingerchar.core.manager;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcSystem;
import com.fingerchar.db.dto.ConfigContract;
import com.fingerchar.db.dto.ConfigDeploy;
import com.fingerchar.db.dto.ConfigNetwork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author： Zjm
 * @Date：2022/3/21 17:54
 */
@Service
public class FcSystemConfigManager {
    @Autowired
    private IBaseService baseService;

    public List<FcSystem> allShow(){
        QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
        wrapper.eq(FcSystem.SHOW, true)
                .eq(BaseEntity.DELETED, false);
       return this.baseService.findByCondition(FcSystem.class, wrapper);
    }

    public List<FcSystem> all(){
        QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
        wrapper.eq(FcSystem.DELETED, false);
        return this.baseService.findByCondition(FcSystem.class, wrapper);
    }

    public String getKeyValue(String key){
        FcSystem system = this.get(key);
        if (null != system) {
            return system.getKeyValue();
        }
        return null;
    }

    public ConfigNetwork getConfigNetwork(){
        String value = this.getKeyValue(SysConfConstant.CONFIG_NETWORK);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        ConfigNetwork configNetwork = JSON.parseObject(value, ConfigNetwork.class);
        return configNetwork;
    }

    public ConfigDeploy getConfigDeploy(){
        String value = this.getKeyValue(SysConfConstant.CONFIG_DEPLOY);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        ConfigDeploy configDeploy = JSON.parseObject(value, ConfigDeploy.class);
        return configDeploy;
    }

    public ConfigContract getConfigContract(){
        String value = this.getKeyValue(SysConfConstant.CONFIG_CONTRACT);
        if(StringUtils.isEmpty(value)){
            return null;
        }
        ConfigContract configContract = JSON.parseObject(value, ConfigContract.class);
        return configContract;
    }

    public FcSystem get(String key){
        QueryWrapper<FcSystem> wrapper = new QueryWrapper<>();
        wrapper.eq(FcSystem.KEY_NAME, key).eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcSystem.class, wrapper);
    }

    public Integer update(String key, String value){
        FcSystem system = this.get(key);
        if(null == system){
            return 0;
        }
        value = this.valueToLowerCase(key, value);
        system.setKeyValue(value);
        return this.update(system);
    }

    private String valueToLowerCase(String key, String value){
        return value;
    }

    public Integer update(FcSystem system){
        return this.baseService.update(system);
    }

    public Integer save(String key, String value){
        FcSystem system = this.get(key);
        value = this.valueToLowerCase(key, value);
        if(null == system){
            system = new FcSystem();
            system.setKeyName(key);
            system.setKeyValue(value);
            return this.save(system);
        }
        system.setKeyValue(value);
        return this.update(system);
    }

    public Integer save(FcSystem system) {
        return this.baseService.save(system);
    }
}
