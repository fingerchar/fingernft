package com.fingerchar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcAdminStorage;

@Service
public class FcAdminStorageService {
	
	@Autowired
    private IBaseService baseService;

    public void deleteByKey(String key) {
    	UpdateWrapper<FcAdminStorage> wrapper = new UpdateWrapper<>();
    	wrapper.eq(FcAdminStorage.KEY, key);
    	wrapper.set(BaseEntity.DELETED, true);
    	this.baseService.updateByCondition(FcAdminStorage.class, wrapper);
    }

    public void add(FcAdminStorage storageInfo) {
       this.baseService.save(storageInfo);
    }

    public FcAdminStorage findByKey(String key) {
    	QueryWrapper<FcAdminStorage> wrapper = new QueryWrapper<>();
    	wrapper.eq(FcAdminStorage.KEY, key)
    		.eq(BaseEntity.DELETED, false);
        return this.baseService.getByCondition(FcAdminStorage.class, wrapper);
    }

    public int update(FcAdminStorage storageInfo) {
        return this.baseService.update(storageInfo);
    }

    public FcAdminStorage findById(Long id) {
        return this.baseService.getById(FcAdminStorage.class, id);
    }

	/**
	 * @param key
	 * @param name
	 * @param pageInfo
	 * @param asc
	 * @param sort
	 * @return
	 */
	public IPage<FcAdminStorage> querySelective(String key, String name, Page<FcAdminStorage> pageInfo, boolean asc,
			String sort) {
		QueryWrapper<FcAdminStorage> wrapper = new QueryWrapper<>();

        if (!StringUtils.isEmpty(key)) {
            wrapper.eq(FcAdminStorage.KEY, key);
        }
        if (!StringUtils.isEmpty(name)) {
        	wrapper.like(FcAdminStorage.NAME, name);
        }
        
        wrapper.eq(BaseEntity.DELETED, false);
        if (!StringUtils.isEmpty(sort)) {
            OrderItem orderItem = new OrderItem(sort, asc);
            pageInfo.addOrder(orderItem);
        }
        return this.baseService.findByPage(FcAdminStorage.class, wrapper, pageInfo);
	}
}
