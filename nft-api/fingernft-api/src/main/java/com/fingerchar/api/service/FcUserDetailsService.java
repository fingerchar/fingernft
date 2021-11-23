package com.fingerchar.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.domain.FcUserDetails;

@Service
public class FcUserDetailsService {

	@Autowired
	IBaseService baseService;

	public FcUserDetails findByUserId(Long userId) {
		QueryWrapper<FcUserDetails> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUserDetails.USER_ID, userId);
		return this.baseService.getByCondition(FcUserDetails.class, wrapper);
	}

	public Integer saveOrUpdate(FcUserDetails details) {
		return this.baseService.saveOrUpdate(details);
	}
}
