package com.fingerchar.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcAdminRole;

@Service
public class FcAdminRoleService {

	@Autowired
	private IBaseService baseService;

	public Set<String> queryByIds(List<Long> roleIds) {
		Set<String> roles = new HashSet<String>();
		if (null == roleIds || roleIds.isEmpty()) {
			return roles;
		}
		QueryWrapper<FcAdminRole> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminRole.ENABLED, true).in(BaseEntity.ID, roleIds);
		List<FcAdminRole> roleList = baseService.findByCondition(FcAdminRole.class, wrapper);

		for (FcAdminRole role : roleList) {
			roles.add(role.getName());
		}

		return roles;

	}

	public IPage<FcAdminRole> querySelective(String name, IPage<FcAdminRole> page, boolean isASC, String orderType) {
		QueryWrapper<FcAdminRole> wrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(name)) {
			wrapper.like(FcAdminRole.NAME, name);
		}
		wrapper.eq(BaseEntity.DELETED, false);
		if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(orderType)) {
			wrapper.orderBy(true, isASC, orderType);
		}
		return baseService.findByPage(FcAdminRole.class, wrapper, page);
	}

	public FcAdminRole findById(Long id) {
		return baseService.getById(FcAdminRole.class, id);
	}

	public void add(FcAdminRole role) {
		baseService.save(role);
	}

	public void deleteById(Long id) {
		baseService.deleteById(FcAdminRole.class, id);
	}

	public void updateById(FcAdminRole role) {
		baseService.update(role);
	}

	public boolean checkExist(String name) {
		QueryWrapper<FcAdminRole> wrapper = new QueryWrapper<>();
		wrapper.eq(FcAdminRole.NAME, name).eq(BaseEntity.DELETED, false);
		return baseService.counts(FcAdminRole.class, wrapper) != 0;
	}

	public List<FcAdminRole> queryAll() {
		QueryWrapper<FcAdminRole> wrapper = new QueryWrapper<>();
		wrapper.or().eq(BaseEntity.DELETED, false);
		return baseService.findByCondition(FcAdminRole.class, wrapper);
	}
}
