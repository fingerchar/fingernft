package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcAdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class FcAdminUserService {

	@Autowired
	private IBaseService baseService;

	/**
	 * 根据主键id获取管理员昵称(销售人员姓名)
	 *
	 * @param id
	 *            主键id(newjztx_order表的admin_id)
	 * @return 返回管理员表的管理员昵称即销售人员姓名
	 */
	public String findNameById(Long id) {
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		// 未删除 未禁用
		wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminUser.STATUS, false).eq(BaseEntity.ID,
				id);
		FcAdminUser admin = baseService.getByCondition(FcAdminUser.class, wrapper);
		if (null != admin) {
			return admin.getNickname();
		}
		return "";
	}

	public List<FcAdminUser> findAdmin(String username) {
		/*
		 * FcAdminUserExample example = new FcAdminUserExample();
		 * example.or().andUsernameEqualTo(username).andDeletedEqualTo(false); return
		 * userMapper.selectByExample(example);
		 */
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminUser.STATUS, false)
				.eq(FcAdminUser.USERNAME, username);
		return baseService.findByCondition(FcAdminUser.class, wrapper);
	}

	public FcAdminUser findAdmin(Long id) {

		/* return userMapper.selectByPrimaryKey(id); */
		return baseService.getById(FcAdminUser.class, id);
	}

	public IPage<FcAdminUser> querySelective(String username, String mobile, IPage<FcAdminUser> page, boolean isASC,
			String sortType) {
		/*
		 * FcAdminUserExample example = new FcAdminUserExample();
		 * FcAdminUserExample.Criteria criteria = example.createCriteria(); if
		 * (!StringUtils.isEmpty(username)) { criteria.andUsernameLike("%" + username +
		 * "%"); } if (!StringUtils.isEmpty(mobile)) { criteria.andPhoneEqualTo(mobile);
		 * } criteria.andDeletedEqualTo(false);
		 * 
		 * if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
		 * example.setOrderByClause(sort + " " + order); } PageHelper.startPage(page,
		 * limit);
		 * 
		 * return userMapper.selectByExample(example);
		 */
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(username)) {
			wrapper.like(FcAdminUser.USERNAME, username);
		}
		if (!StringUtils.isEmpty(mobile)) {
			wrapper.eq(FcAdminUser.PHONE, mobile);
		}
		wrapper.eq(BaseEntity.DELETED, false);

		if (isASC && !StringUtils.isEmpty(sortType)) {
			wrapper.orderBy(true, isASC, sortType);
		}

		return baseService.findByPage(FcAdminUser.class, wrapper, page);
	}

	public int updateById(FcAdminUser admin) {
		admin.setUpdateTime(System.currentTimeMillis()/1000);
		/* return userMapper.updateByPrimaryKeySelective(admin); */
		return baseService.update(admin);
	}

	public void deleteById(Long id) {

		/* userMapper.logicalDeleteByPrimaryKey(id); */
		baseService.deleteById(FcAdminUser.class, id);
	}

	public void add(FcAdminUser admin) {
		admin.setCreateTime(System.currentTimeMillis()/1000);
		admin.setUpdateTime(System.currentTimeMillis()/1000);
		/* userMapper.insertSelective(admin); */
		baseService.save(admin);
	}

	public FcAdminUser findById(Long id) {

		/* return userMapper.selectByPrimaryKey(id); */
		return baseService.getById(FcAdminUser.class, id);
	}

	public List<FcAdminUser> all() {
		/*
		 * FcAdminUserExample example = new FcAdminUserExample();
		 * example.or().andDeletedEqualTo(false); return
		 * userMapper.selectByExample(example);
		 */
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		wrapper.or().eq(BaseEntity.DELETED, false);
		return baseService.findByCondition(FcAdminUser.class, wrapper);
	}

	/**
	 * 获取所有销售人员
	 *
	 * @return
	 */
	public List<FcAdminUser> selectAllSalesPeople() {/*
														 * FcAdminUserExample adminExample = new FcAdminUserExample();
														 * adminExample.createCriteria().andDeletedEqualTo(false).
														 * andRoleIdsEqualTo(new Long[]{2L}); return
														 * userMapper.selectByExample(adminExample);
														 */
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminUser.ROLE_IDS, new Long[] { 2L });
		return baseService.findByCondition(FcAdminUser.class, wrapper);
	}

	/**
	 * 获取所有主管人员
	 *
	 * @return
	 */
	public List<FcAdminUser> selectAllSalesMasterPeople() {/*
															 * FcAdminUserExample adminExample = new
															 * FcAdminUserExample();
															 * adminExample.createCriteria().andDeletedEqualTo(false).
															 * andJobIdEqualTo(164L); return
															 * userMapper.selectByExample(adminExample);
															 */
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminUser.JOB_ID, new Long[] { 164L });
		return baseService.findByCondition(FcAdminUser.class, wrapper);
	}

	/**
	 * 根据JobId获取管理
	 *
	 * @param jobId
	 *            JobId
	 * @return
	 */
	public FcAdminUser findByJobId(Long jobId) {
		/*
		 * FcAdminUserExample adminExample = new FcAdminUserExample();
		 * adminExample.createCriteria().andDeletedEqualTo(false).andJobIdEqualTo(jobId)
		 * ; return userMapper.selectOneByExample(adminExample);
		 */
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminUser.JOB_ID, jobId);
		return baseService.getByCondition(FcAdminUser.class, wrapper);
	}

	/**
	 * 根据组织ID和职位ID获取Admin记录
	 * 
	 * @param deparentmentId
	 * @param jobId
	 * @return
	 */
	public List<FcAdminUser> queryAdminsByOrganIdAndJobId(Long deparentmentId, Long jobId) {
		/*
		 * FcAdminUserExample adminExample = new FcAdminUserExample();
		 * adminExample.createCriteria().andDeletedEqualTo(false).andJobIdEqualTo(jobId)
		 * .andDepartmentIdEqualTo(organizedId); return
		 * userMapper.selectByExample(adminExample);
		 */
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminUser.JOB_ID, jobId)
				.eq(FcAdminUser.DEPARTMENT_ID, deparentmentId);
		return baseService.findByCondition(FcAdminUser.class, wrapper);
	}

	public FcAdminUser findManager(Long jobId, Long deparentmentId) {
		/*
		 * FcAdminUserExample adminExample = new FcAdminUserExample();
		 * adminExample.createCriteria().andDeletedEqualTo(false).andJobIdEqualTo(jobId)
		 * .andDepartmentIdEqualTo(DeparentmentId); return
		 * userMapper.selectOneByExample(adminExample);
		 */
		QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminUser.JOB_ID, jobId)
				.eq(FcAdminUser.DEPARTMENT_ID, deparentmentId);
		return baseService.getByCondition(FcAdminUser.class, wrapper);
	}

	/**
	 * 通过名字模糊查询
	 *
	 * @param name
	 *            名字
	 * @return
	 */
	public List<FcAdminUser> querySaleByNameWithLike(String name) {
		List<FcAdminUser> list = new ArrayList<>();
		/*
		 * if (!StringUtils.isEmpty(name)) { FcAdminUserExample adminExample = new
		 * FcAdminUserExample();
		 * adminExample.createCriteria().andDeletedEqualTo(false).andRoleIdsEqualTo(new
		 * Long[]{2L}).andNicknameLike("%" + name + "%"); list =
		 * userMapper.selectByExample(adminExample); }
		 */
		if (!StringUtils.isEmpty(name)) {
			QueryWrapper<FcAdminUser> wrapper = new QueryWrapper<>();
			wrapper.eq(BaseEntity.DELETED, false).eq(FcAdminUser.ROLE_IDS, new long[] { 2L })
					.like(FcAdminUser.NICKNAME, name);
			list = baseService.findByCondition(FcAdminUser.class, wrapper);
		}
		return list;
	}

	/**
	 * @param newPassword
	 * @param id
	 * @return
	 */
	public int updatePwd(String newPassword, Long id) {
		UpdateWrapper<FcAdminUser> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcAdminUser.ID, id);
		wrapper.set(FcAdminUser.PASSWORD, newPassword);
		return this.baseService.updateByCondition(FcAdminUser.class, wrapper);
	}


}
