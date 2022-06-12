package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.dao.ext.FcOrderLogExtMapper;
import com.fingerchar.db.dao.ext.FcUserExtMapper;
import com.fingerchar.db.domain.FcOrderLog;
import com.fingerchar.db.domain.FcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FcUserService {

	@Autowired
	FcOrderLogExtMapper orderLogExtMapper;
	
	@Autowired
	FcUserExtMapper userExtMapper;
	
	@Autowired
	IBaseService baseService;


	/**
	 * 更新用户
	 * @param user
	 * @return
	 */
	public Integer updateById(FcUser user) {
		return this.baseService.update(user);
	}

	/**
	 * 新增用户
	 * @param user
	 * @return
	 */
	public Integer add(FcUser user) {
		return this.baseService.save(user);
	}

	/**
	 * 根据地址和登陆类型查找用户
	 * @param address
	 * @return
	 */
	public List<FcUser> queryUserByAddrAndType(String address) {
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUser.ADDRESS, address);
		return this.baseService.findByCondition(FcUser.class, wrapper);
	}

	/**
	 * 根据用户地址查找用户（特定几个列）
	 * @param address 用户地址
	 * @return
	 */
	public FcUser getUserByAddress(String address) {
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUser.ADDRESS, address);
		return this.baseService.getByCondition(FcUser.class, wrapper);
	}

	/**
	 * 根据用户地址查找用户（特定几个列）
	 * @param id 用户地址
	 * @return
	 */
	public FcUser findById(Long id) {
		return this.baseService.getById(FcUser.class, id);
	}


	public List<FcUser> findListByAddrs(List<String> addrs){
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		wrapper.select(FcUser.ADDRESS,FcUser.NICKNAME)
				.in(FcUser.ADDRESS, addrs);
		return this.baseService.findByCondition(FcUser.class, wrapper);
	}



	/**
	 *
	 * @param user
	 * @return
	 */
	public Object bid(FcUser user) {
		FcOrderLog log = this.orderLogExtMapper.getOne(user.getAddress(), 5);
		return ResponseUtil.ok(log);
	}


	/**
	 * 更新用户信息
	 * @param userInfo
	 * @return
	 */
	public Object updateUserinfo(FcUser userInfo) {
		return this.baseService.update(userInfo);
	}

	/**
	 * 根据用户名模糊查找用户
	 * @param search
	 * @param pageInfo
	 * @return
	 */
	public IPage<FcUser> findByName(String search, IPage<FcUser> pageInfo) {
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		wrapper.like(FcUser.NICKNAME, search);
		wrapper.orderByDesc(BaseEntity.CREATE_TIME);
		return this.baseService.findByPage(FcUser.class, wrapper, pageInfo);
	}

	/**
	 * @param url
	 */
	public void setBanner(Long userId, String url) {
		UpdateWrapper<FcUser> wrapper = new UpdateWrapper<>();
		wrapper.eq(BaseEntity.ID, userId);
		wrapper.set(FcUser.BANNER_URL, url);
		this.baseService.updateByCondition(FcUser.class, wrapper);
	}


}
