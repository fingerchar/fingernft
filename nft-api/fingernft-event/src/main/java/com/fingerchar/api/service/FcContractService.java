package com.fingerchar.api.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContract;

@Service
public class FcContractService  {

    @Resource
    private IBaseService baseService;

	/**
	 * @param string
	 * @return
	 */
	public List<String> findAllAddress() {
		QueryWrapper<FcContract> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcContract.IS_ADMIN, true);
		List<FcContract> list = this.baseService.findByCondition(FcContract.class, wrapper);
		return list.stream().map(contract->contract.getAddress()).collect(Collectors.toList());
	}

}
