package com.fingerchar.core.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.LambdaUtils;
import com.fingerchar.db.base.BaseEntity;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseService implements IBaseService {

	public static final Logger logger = LoggerFactory.getLogger(BaseService.class);

	public static final String ENTITY_STR = "domain";
	public static final String MAPPER_STR = "dao";
	public static final String  APPEND_STR = "Mapper";

	@Autowired
	SqlSession sqlSession;
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends BaseEntity> BaseMapper getMapper(Class<T> clazz)  {
		String className = clazz.getName().replace(ENTITY_STR, MAPPER_STR) + APPEND_STR;
		BaseMapper<T> mapper = null;
		try {
			mapper = (BaseMapper<T>) sqlSession.getMapper(Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return mapper;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Integer save(T entity)  {
		if(null == entity.getCreateTime()) {			
			entity.setCreateTime(System.currentTimeMillis() / 1000);
		}
		if(null == entity.getUpdateTime()) {			
			entity.setUpdateTime(System.currentTimeMillis() / 1000);
		}
		return this.getMapper(entity.getClass()).insert(entity);
	}

	@Override
	public <T extends BaseEntity> Integer saveBatch(List<T> list)  {
		SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		T t = list.get(0);
		@SuppressWarnings("unchecked")
        BaseMapper<T> mapper = this.getMapper(t.getClass());
		list.stream().forEach(LambdaUtils.consumerWithIndex((vo, idx)-> {
			vo.setCreateTime(System.currentTimeMillis() / 1000);
			vo.setUpdateTime(System.currentTimeMillis() / 1000);
			mapper.insert(vo);
			if(idx % 1000 == 0 || idx == list.size() - 1) {
				//手动每1000个一提交，提交后无法回滚 
				session.commit();
				//清理缓存，防止溢出
				session.clearCache();
			}
		}));
		return list.size();
	}

	@Override
	public <T extends BaseEntity> Integer deleteById(Class<T> clazz, Long id) {
		return this.getMapper(clazz).deleteById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> Integer deleteByCondition(Class<T> clazz, Wrapper<T> wrapper) {
		return this.getMapper(clazz).delete(wrapper);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> Integer deleteBatch(Class<T> clazz, List<Long> idList) {
		return this.getMapper(clazz).deleteBatchIds(idList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> Integer updateBatch(List<T> list)  {
		SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
		T t = list.get(0);
		@SuppressWarnings("unchecked")
		BaseMapper<T> mapper = this.getMapper(t.getClass());
		list.stream().forEach(LambdaUtils.consumerWithIndex((vo, idx)-> {
			if(null == vo.getUpdateTime()){
				vo.setUpdateTime(System.currentTimeMillis() / 1000);
			}
			mapper.updateById(vo);
			if(idx % 1000 == 0 || idx == list.size() - 1) {
				//手动每1000个一提交，提交后无法回滚
				session.commit();
				//清理缓存，防止溢出
				session.clearCache();
			}
		}));
		return list.size();
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Integer update(T entity) {
		if(null == entity.getUpdateTime()) {			
			entity.setUpdateTime(System.currentTimeMillis() / 1000);
		}
		return this.getMapper(entity.getClass()).updateById(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> Integer updateByCondition(Class<T> clazz, UpdateWrapper<T> wrapper) {
		if(wrapper.getSqlSet().indexOf("update_time") < 0) {		
			wrapper.set("update_time", System.currentTimeMillis() / 1000);
		}
		return this.getMapper(clazz).update(null, wrapper);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> Integer updateByCondition(Class<T> clazz, T entity,UpdateWrapper<T> wrapper) {
		if(wrapper.getSqlSet().indexOf("update_time") < 0) {			
			wrapper.set("update_time", System.currentTimeMillis() / 1000);
		}
		return this.getMapper(clazz).update(entity, wrapper);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Integer saveOrUpdate(T entity) {
		if(null != entity.getId()) {
			if(null == entity.getUpdateTime()) {
				entity.setUpdateTime(System.currentTimeMillis() / 1000);
			}
			return this.getMapper(entity.getClass()).updateById(entity);
		} else {
			if(null == entity.getCreateTime()) {
				entity.setCreateTime(System.currentTimeMillis() / 1000);
			}
			if(null == entity.getUpdateTime()) {
				entity.setUpdateTime(System.currentTimeMillis() / 1000);
			}
			return this.getMapper(entity.getClass()).insert(entity);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> T get(T entity)  {
		QueryWrapper<T> wrapper = new QueryWrapper<T>(entity);
		return (T) this.getMapper(entity.getClass()).selectOne(wrapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> T getById(Class<T> clazz, Long id) {
		return (T) this.getMapper(clazz).selectById(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> T getByCondition(Class<T> clazz, Wrapper<T> wrapper) {
		return (T) this.getMapper(clazz).selectOne(wrapper);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T> find(T entity)  {
		QueryWrapper<T> wrapper = new QueryWrapper<T>(entity);
		return this.getMapper(entity.getClass()).selectList(wrapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> List<T> findByCondition(Class<T> clazz, Wrapper<T> wrapper) {
		return this.getMapper(clazz).selectList(wrapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> IPage<T> findByPage(Class<T> clazz, Wrapper<T> wrapper, IPage<T> page) {
		return this.getMapper(clazz).selectPage(page, wrapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> IPage<T> findByPage(T entity, IPage<T> page) {
		QueryWrapper<T> wrapper = new QueryWrapper<T>(entity);
		return this.getMapper(entity.getClass()).selectPage(page, wrapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> Integer counts(Class<T> clazz,Wrapper<T> wrapper) {
		return this.getMapper(clazz).selectCount(wrapper);
	}
}
