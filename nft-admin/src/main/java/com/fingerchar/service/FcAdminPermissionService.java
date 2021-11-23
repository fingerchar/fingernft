package com.fingerchar.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcAdminPermission;

@Service
public class FcAdminPermissionService {
	
	@Autowired
	private IBaseService baseService;

	public Set<String> queryByRoleIds(List<Long> roleIds) {
		Set<String> permissions = new HashSet<String>();
		if (null == roleIds || roleIds.isEmpty()) {
			return permissions;
		}
		QueryWrapper<FcAdminPermission> wrapper = new QueryWrapper<>();
		wrapper.in(FcAdminPermission.ROLE_ID, roleIds).eq(BaseEntity.DELETED, false);
		List<FcAdminPermission> permissionList = baseService.findByCondition(FcAdminPermission.class, wrapper);

		for (FcAdminPermission permission : permissionList) {
			permissions.add(permission.getPermission());
		}

		return permissions;
	}

	public Set<String> queryByRoleId(Long roleId) {
		Set<String> permissions = new HashSet<String>();
		if (roleId == null) {
			return permissions;
		}
		QueryWrapper<FcAdminPermission> wrapper = new QueryWrapper<>();
		wrapper.eq(FcAdminPermission.ROLE_ID, roleId).eq(BaseEntity.DELETED, false);

		List<FcAdminPermission> permissionList = baseService.findByCondition(FcAdminPermission.class, wrapper);

		for (FcAdminPermission permission : permissionList) {
			permissions.add(permission.getPermission());
		}

		return permissions;
	}

	public boolean checkSuperPermission(Long roleId) {
		if (roleId == null) {
			return false;
		}
		QueryWrapper<FcAdminPermission> wrapper = new QueryWrapper<>();
		wrapper.eq(FcAdminPermission.ROLE_ID, roleId).eq(BaseEntity.DELETED, false).eq(FcAdminPermission.PERMISSION,
				"*");

		return baseService.findByCondition(FcAdminPermission.class, wrapper).size() != 0;
	}

	public void deleteByRoleId(Long roleId) {
		QueryWrapper<FcAdminPermission> wrapper = new QueryWrapper<>();
		wrapper.eq(FcAdminPermission.ROLE_ID, roleId).eq(BaseEntity.DELETED, false);
		baseService.deleteByCondition(FcAdminPermission.class, wrapper);
	}

	public void add(FcAdminPermission FcAdminPermission) {
		baseService.save(FcAdminPermission);
	}
}
