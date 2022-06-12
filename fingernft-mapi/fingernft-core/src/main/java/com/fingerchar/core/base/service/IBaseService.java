package com.fingerchar.core.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.db.base.BaseEntity;

import java.util.List;

public interface IBaseService {

	<T extends BaseEntity> Integer save(T entity);

	<T extends BaseEntity> Integer saveBatch(List<T> list);

	<T extends BaseEntity> Integer deleteById(Class<T> clazz, Long id);

	<T extends BaseEntity> Integer deleteByCondition(Class<T> clazz, Wrapper<T> wrapper);

	<T extends BaseEntity> Integer deleteBatch(Class<T> clazz, List<Long> idList);

	<T extends BaseEntity> Integer update(T entity);

	<T extends BaseEntity> Integer updateBatch(List<T> list);

	<T extends BaseEntity> Integer updateByCondition(Class<T> clazz, UpdateWrapper<T> wrapper);

	<T extends BaseEntity> Integer updateByCondition(Class<T> clazz, T entity,UpdateWrapper<T> wrapper);

	<T extends BaseEntity> Integer saveOrUpdate(T entity);

	<T extends BaseEntity> T get(T entity);

	<T extends BaseEntity> T getById(Class<T> clazz, Long id);

	<T extends BaseEntity> T getByCondition(Class<T> clazz, Wrapper<T> wrapper);

	<T extends BaseEntity> List<T> find(T entity);

	<T extends BaseEntity> List<T> findByCondition(Class<T> clazz, Wrapper<T> wrapper);

	<T extends BaseEntity> IPage<T> findByPage(Class<T> clazz, Wrapper<T> wrapper, IPage<T> page);

	<T extends BaseEntity> IPage<T> findByPage(T entity, IPage<T> page);

	<T extends BaseEntity> Integer counts(Class<T> clazz,Wrapper<T> wrapper);

}
