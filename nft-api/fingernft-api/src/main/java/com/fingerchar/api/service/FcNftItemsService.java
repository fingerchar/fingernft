package com.fingerchar.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcNftItems;


@Service
public class FcNftItemsService {
	
	@Autowired
	IBaseService baseService;

	public IPage<FcNftItems> findListByOwners(List<String> owners, Long categoryId, IPage<FcNftItems> pageInfo) {
		QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
		wrapper.in(FcNftItems.ITEM_OWNER, owners)
			.eq(BaseEntity.DELETED, false);

		if(null != categoryId) {
			wrapper.eq(FcNftItems.CATEGORY_ID, categoryId);
		}
		return this.baseService.findByPage(FcNftItems.class, wrapper, pageInfo);
	}

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

	public IPage<FcNftItems> findByContractAddress(String address, Boolean isSell, IPage<FcNftItems> pageInfo) {
		QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
		wrapper.eq(FcNftItems.ADDRESS, address).eq(FcNftItems.IS_SYNC, true).eq(BaseEntity.DELETED, false);
		if(null != isSell) {			
			wrapper.eq(FcNftItems.ONSELL, isSell);
		}
		wrapper.orderByDesc(BaseEntity.CREATE_TIME);
		return this.baseService.findByPage(FcNftItems.class, wrapper, pageInfo);
	}

	/**
	 * @param nftIds
	 * @return
	 */
	public List<FcNftItems> findListByNftIds(List<Long> nftIds) {
		QueryWrapper<FcNftItems> wrapper = new QueryWrapper<>();
		wrapper.in(FcNftItems.NFT_ID, nftIds)
			.eq(FcNftItems.IS_SYNC, true)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.findByCondition(FcNftItems.class, wrapper);
	}

	
}
