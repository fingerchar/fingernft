package com.fingerchar.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.constant.CommonStatus;
import com.fingerchar.api.constant.NoticeType;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.domain.FcUserFollow;

@Service
public class FcUserFollowService {

	@Autowired
	FcNoticeService noticeService;
	
	@Autowired
	IBaseService baseService;

	/**
	 * 获取我关注的人的个数
	 *
	 * @param uid
	 * @return
	 */
	public Integer countByOwner(String userAddress) {
		QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUserFollow.USER_ADDRESS, userAddress)
		.eq(BaseEntity.DELETED, false);
		return this.baseService.counts(FcUserFollow.class, wrapper);
	}

	/**
	 * 获取关注我的人数
	 *
	 * @param followingId
	 * @return
	 */
	public Integer countByFollowingAddress(String followingAddress) {
		QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUserFollow.FOLLOWING_ADDRESS, followingAddress)
		.eq(BaseEntity.DELETED, false);
		return this.baseService.counts(FcUserFollow.class, wrapper);
	}

	/**
	 * 查找关注用户的
	 *
	 * @param followingId
	 * @param page
	 * @param limit
	 * @return
	 */
	public IPage<FcUserFollow> findListByFollowingAddress(String followingAddress, IPage<FcUserFollow> pageInfo) {
		QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUserFollow.FOLLOWING_ADDRESS, followingAddress)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.findByPage(FcUserFollow.class, wrapper, pageInfo);
	}

	/**
	 * 查找用户关注的
	 *
	 * @param userId
	 * @param page
	 * @param limit
	 * @return
	 */
	public IPage<FcUserFollow> findListByUserAddress(String userAddress, IPage<FcUserFollow> pageInfo) {
		QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUserFollow.USER_ADDRESS, userAddress)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.findByPage(FcUserFollow.class, wrapper, pageInfo);
	}

	/**
	 * 获取follow信息
	 *
	 * @param fid
	 * @param uid
	 * @return
	 */
	public FcUserFollow findByFAddrUAddr(String faddress, String uaddress) {
		QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUserFollow.FOLLOWING_ADDRESS, faddress)
			.eq(FcUserFollow.USER_ADDRESS, uaddress)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.getByCondition(FcUserFollow.class, wrapper);
	}

	/**
	 * 更新follow信息
	 *
	 * @param fcUserFollow
	 * @return
	 */
	public int update(FcUserFollow fcUserFollow) {
		return this.baseService.update(fcUserFollow);
	}

	public int delete(Long id) {
		return this.baseService.deleteById(FcUserFollow.class, id);
	}

	/**
	 * 新增follow
	 *
	 * @param followId
	 * @param userId
	 * @return
	 */
	public int add(String followingAddress, String userAddress) {
		FcUserFollow fcUserFollow = new FcUserFollow();
		fcUserFollow.setFollowingAddress(followingAddress);
		fcUserFollow.setUserAddress(userAddress);
		return this.baseService.save(fcUserFollow);
	}

	/**
	 * 新增follow 同时添加通知
	 *
	 * @param user
	 * @param followingUser
	 */
	@Transactional(rollbackFor = Exception.class)
	public void add(FcUser user, FcUser followingUser) {
		add(followingUser.getAddress(), user.getAddress());
		Map<String, Object> map = new HashMap<>();
		map.put("followingId", followingUser.getId());
		map.put("followingName", followingUser.getNickname());
		map.put("followingImage", followingUser.getAvatar());
		map.put("followingAddr", followingUser.getAddress());
		map.put("followedId", user.getId());
		map.put("followedName", user.getNickname());
		map.put("followedImage", user.getAvatar());
		map.put("followedAddr", user.getAddress());
		String name = user.getNickname();
		if (StringUtils.isEmpty(name)) {
			name = user.getAddress();
		}
		String type = CommonStatus.getStatusByName("Followed").getType().toString();
		String noticeType = NoticeType.getNoticeTypeByName("Follow").getType().toString();
		this.noticeService.insertNotice(map, followingUser.getAddress(), type, user.getAvatar(), name, noticeType, user.getAddress());
	}

	/**
	 * 根据followingid集合和用户id查找follow
	 *
	 * @param fid
	 * @param uid
	 * @return
	 */
	public List<FcUserFollow> findListByFollowAddrsAndUserAddr(List<String> faddrs, String uaddr) {
		QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
		wrapper.in(FcUserFollow.FOLLOWING_ADDRESS, faddrs)
			.eq(FcUserFollow.USER_ADDRESS, uaddr)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.findByCondition(FcUserFollow.class, wrapper);
	}

	/**
	 * 根据用户id集合查找follow
	 *
	 * @param userId
	 * @return
	 */
	public List<FcUserFollow> findListByUserAddress(String userAddress) {
		QueryWrapper<FcUserFollow> wrapper = new QueryWrapper<>();
		wrapper.eq(FcUserFollow.USER_ADDRESS, userAddress).eq(BaseEntity.DELETED, false);
		return this.baseService.findByCondition(FcUserFollow.class, wrapper);
	}
}
