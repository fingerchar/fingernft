package com.fingerchar.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcNftCategory;

@Service
public class FcNftCategoryService {

	@Autowired
	IBaseService baseService;

	/*
	* 查询所有为删除的分类
	* */
	public List<FcNftCategory> findAll() {
		QueryWrapper<FcNftCategory> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		return this.baseService.findByCondition(FcNftCategory.class, wrapper);
	}
}
