package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FcStorageService {
	
    @Autowired
    private IBaseService baseService;

    public void add(FcStorage storageInfo) {
    	this.baseService.save(storageInfo);
    }

    public FcStorage findByKey(String key) {
    	QueryWrapper<FcStorage> wrapper = new QueryWrapper<>();
    	wrapper.eq(FcStorage.KEY, key);
        return this.baseService.getByCondition(FcStorage.class, wrapper);
    }

    public int update(FcStorage storageInfo) {
        return this.baseService.update(storageInfo);
    }

    public FcStorage findById(Long id) {
        return this.baseService.getById(FcStorage.class, id);
    }

}
