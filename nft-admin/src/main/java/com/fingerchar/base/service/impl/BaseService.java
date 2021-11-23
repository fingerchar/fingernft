package com.fingerchar.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;

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
		entity.setCreateTime(System.currentTimeMillis() / 1000);
		entity.setUpdateTime(System.currentTimeMillis() / 1000);
		return this.getMapper(entity.getClass()).insert(entity);
	}

	@Override
	public <T extends BaseEntity> Integer saveBatch(List<T> list)  {
		SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH,false);
		T t = list.get(0);
		@SuppressWarnings("unchecked")
        BaseMapper<T> mapper = this.getMapper(t.getClass());
		int size = list.size();
        try {
            for (int i = 0; i<size; i++) {
            	t = list.get(i);
            	t.setCreateTime(System.currentTimeMillis() / 1000);
            	t.setUpdateTime(System.currentTimeMillis() / 1000);
                mapper.insert(t);
                if((i%1000==999) || i==(size-1)) {
                    session.commit();
                    session.clearCache();
                }
            }
        }catch(Exception e) {
            session.rollback();
            logger.error("Insert Batch fail:", e);
        }finally {
            session.close();
        }
        return size;
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

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Integer update(T entity) {
		entity.setUpdateTime(System.currentTimeMillis() / 1000);
		return this.getMapper(entity.getClass()).updateById(entity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> Integer updateByCondition(Class<T> clazz, UpdateWrapper<T> wrapper) {
		wrapper.set("update_time", System.currentTimeMillis() / 1000);
		return this.getMapper(clazz).update(null, wrapper);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseEntity> Integer updateByCondition(Class<T> clazz, T entity,UpdateWrapper<T> wrapper) {
		wrapper.set("update_time", System.currentTimeMillis() / 1000);
		return this.getMapper(clazz).update(entity, wrapper);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> Integer saveOrUpdate(T entity) {
		if(null != entity.getId()) {
			entity.setUpdateTime(System.currentTimeMillis() / 1000);
			return this.getMapper(entity.getClass()).updateById(entity);
		} else {
			entity.setCreateTime(System.currentTimeMillis() / 1000);
			entity.setUpdateTime(System.currentTimeMillis() / 1000);
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
