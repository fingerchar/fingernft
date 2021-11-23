package com.fingerchar.api.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContractNft;

@Service
public class FcContractNftService {

    @Resource
    private IBaseService baseService;

	public void setNftVerify() {
		QueryWrapper<FcContractNft> qwrapper = new QueryWrapper<>();
		qwrapper.eq(FcContractNft.NFT_VERIFY, false).eq(BaseEntity.DELETED, false);
		List<FcContractNft> list = this.baseService.findByCondition(FcContractNft.class, qwrapper);
		if(!list.isEmpty()) {
			UpdateWrapper<FcContractNft> wrapper = new UpdateWrapper<>();
			wrapper.in(BaseEntity.ID, list.stream().map(nft->nft.getId()).collect(Collectors.toList()));
			wrapper.set(FcContractNft.NFT_VERIFY, true);
			this.baseService.updateByCondition(FcContractNft.class, wrapper);
		}
		
	}
}
