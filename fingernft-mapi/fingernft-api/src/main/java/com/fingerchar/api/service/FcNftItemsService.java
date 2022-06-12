package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcNftItems;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FcNftItemsService {
	
	@Autowired
	IBaseService baseService;


	public Integer countOnsale(String address) {
		QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNftItems.ITEM_OWNER, address)
			.eq(FcNftItems.IS_SYNC, true)
			.eq(BaseEntity.DELETED, false)
			.eq(FcNftItems.ONSELL, true);
		return this.baseService.counts(FcNftItems.class, wrapper);
	}

	public Integer countCollections(String address) {
		QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNftItems.ITEM_OWNER, address)
			.eq(FcNftItems.IS_SYNC, true)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.counts(FcNftItems.class, wrapper);
	}


}
