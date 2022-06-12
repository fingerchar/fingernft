package com.fingerchar.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.core.constant.CommonStatus;
import com.fingerchar.core.constant.NoticeType;
import com.fingerchar.core.manager.FcNoticeManager;
import com.fingerchar.core.manager.FcUserFollowManager;
import com.fingerchar.core.manager.FcUserManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.domain.FcUserFollow;
import com.fingerchar.db.vo.UserBaseInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FcUserFollowService {

	@Autowired
	FcNoticeManager noticeManager;

	@Autowired
	FcUserManager userManager;

	@Autowired
	FcUserFollowManager userFollowManager;


	public Object follow(String userAddress, String followingAddress){
		FcUserFollow userFollow = this.userFollowManager.get(userAddress, followingAddress);
		if(null != userFollow){
			return ResponseUtil.ok();
		}
		userFollow = new FcUserFollow();
		userFollow.setUserAddress(userAddress);
		userFollow.setFollowingAddress(followingAddress);
		this.userFollowManager.save(userFollow);

		Integer type = CommonStatus.FOLLOWED.getType();
		Integer noticeType = NoticeType.FOLLOW.getType();
		Map<String, Object> map = new HashMap<>();
		map.put("address", followingAddress);
		this.noticeManager.add(map, followingAddress, type, noticeType, userAddress);

		return ResponseUtil.ok();
	}

	public Object unfollow(String userAddress, String followingAddress){
		FcUserFollow userFollow = this.userFollowManager.get(userAddress, followingAddress);
		if(null == userFollow){
			return ResponseUtil.ok();
		}
		this.userFollowManager.delete(userFollow);
		return ResponseUtil.ok();
	}

	public Object listbymulti(String userAddress, List<String> addressList){
		List<FcUserFollow> userFollowList = this.userFollowManager.listbymulti(userAddress, addressList);
		return ResponseUtil.ok(userFollowList);
	}

	public Object pageFollow(String address, IPage<FcUserFollow> pageInfo){
		IPage<FcUserFollow> followIPage = this.userFollowManager.pageFollow(address, pageInfo);
		List<FcUserFollow> followList = followIPage.getRecords();
		List<String> addressList = followList.stream().map(FcUserFollow::getFollowingAddress).collect(Collectors.toList());
		List<FcUser> userList = this.userManager.listByMulti(addressList);
		Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
		List<UserBaseInfoVo> infoVoList = addressList.stream().map(vo -> new UserBaseInfoVo(vo, userMap.get(vo))).collect(Collectors.toList());

		IPage<UserBaseInfoVo> infoVoIPage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
		infoVoIPage.setRecords(infoVoList);
		infoVoIPage.setPages(pageInfo.getPages());
		return ResponseUtil.okList(infoVoIPage);
	}

	public Object pageFollower(String address, IPage<FcUserFollow> pageInfo){
		IPage<FcUserFollow> followIPage = this.userFollowManager.pageFollower(address, pageInfo);
		List<FcUserFollow> followList = followIPage.getRecords();
		List<String> addressList = followList.stream().map(FcUserFollow::getUserAddress).collect(Collectors.toList());
		List<FcUser> userList = this.userManager.listByMulti(addressList);
		Map<String, FcUser> userMap = userList.stream().collect(Collectors.toMap(FcUser::getAddress, Function.identity()));
		List<UserBaseInfoVo> infoVoList = addressList.stream().map(vo -> new UserBaseInfoVo(vo, userMap.get(vo))).collect(Collectors.toList());

		IPage<UserBaseInfoVo> infoVoIPage = new Page<>(pageInfo.getCurrent(), pageInfo.getSize(), pageInfo.getTotal());
		infoVoIPage.setRecords(infoVoList);
		infoVoIPage.setPages(pageInfo.getPages());
		return ResponseUtil.okList(infoVoIPage);
	}

}
