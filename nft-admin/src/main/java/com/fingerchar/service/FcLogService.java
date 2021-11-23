package com.fingerchar.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcAdminLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class FcLogService {

	@Autowired
	private IBaseService baseService;

	public void deleteById(Long id) {
		baseService.deleteById(FcAdminLog.class, id);
	}

	public void add(FcAdminLog log) {
		baseService.save(log);

	}

	public IPage<FcAdminLog> querySelective(String name, IPage<FcAdminLog> page, boolean isASC, String sortType) {
		QueryWrapper<FcAdminLog> wrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(name)) {
			wrapper.like(FcAdminLog.ADMIN, name);
		}
		wrapper.eq(BaseEntity.DELETED, false);
		if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
			wrapper.orderBy(true, isASC, sortType);
		}
		return baseService.findByPage(FcAdminLog.class, wrapper, page);

	}
}
