package com.fingerchar.api.web;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.FcUserFollowService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.utils.Str2ArrayUtils;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.domain.FcUserFollow;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/follow")
public class FcUserFollowController extends BaseController {

	@Autowired
	FcUserService userService;
	
	@Autowired
	FcUserFollowService userFollowService;

	@PostMapping("add")
	public Object add(String address) {
		String userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		if(null==address){
			return  ResponseUtil.fail(-1, "user address can not be empty");
		}
		FcUser user = userService.getUserByAddress(userAddress);
		FcUser followingUser = userService.getUserByAddress(address);
		if(followingUser == null){
			return ResponseUtil.fail(-1, "Invalid following user");
		}
		if(user.getId().longValue() == followingUser.getId().longValue()) {
			return  ResponseUtil.fail(-1, "Can not follow yourself");
		}
		FcUserFollow fcUserFollow = userFollowService.findByFAddrUAddr(followingUser.getAddress(), user.getAddress());
		if(fcUserFollow != null){
			return ResponseUtil.ok();
		} else {		
			this.userFollowService.add(user, followingUser);
		}
        return ResponseUtil.ok();
	}

	@PostMapping("delete")
	public Object delete(String address) {
		String userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		if(address == null){
			return ResponseUtil.fail(-1, "user address can not be empty");
		}
		FcUser fcUser=userService.getUserByAddress(address);
		if(fcUser==null){
			return ResponseUtil.fail(-1, "Invalid user");
		}
		FcUser user = userService.getUserByAddress(userAddress);
		FcUserFollow fcUserFollow = userFollowService.findByFAddrUAddr(fcUser.getAddress(), user.getAddress());
		if(fcUserFollow == null) {
			return ResponseUtil.ok();
		} else {
			this.userFollowService.delete(fcUserFollow.getId());
			return ResponseUtil.ok();
		}
	}
	
    //根据address列表，算出哪些address是我关注的
	//address,逗号分割
	@PostMapping("match")
	public Object match(String address, String userAddrs) {
		if(StringUtils.isEmpty(address) || StringUtils.isEmpty(userAddrs)) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		FcUser fcUser = userService.getUserByAddress(address);
		if (fcUser == null) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		List<String> paramsList = Str2ArrayUtils.sliceString2StringArray(userAddrs);
		if (null == paramsList || paramsList.isEmpty()) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		
		List<FcUser> fcUsers = userService.findListByAddrs(paramsList);
		if(null == fcUsers || fcUsers.isEmpty()) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		List<String> followAddrs = fcUsers.stream().map(FcUser::getAddress).collect(Collectors.toList());
		if(null == followAddrs || followAddrs.isEmpty()) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		List<FcUserFollow> fcUserFollows = userFollowService.findListByFollowAddrsAndUserAddr(followAddrs, fcUser.getAddress());
		if(null == fcUserFollows || fcUserFollows.isEmpty()) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		followAddrs = fcUserFollows.stream().map(f->f.getFollowingAddress()).collect(Collectors.toList());
		fcUsers = this.userService.findListByAddrs(followAddrs);
		List<String> userAddrList = fcUsers.stream().map(u->u.getAddress()).collect(Collectors.toList());
		return ResponseUtil.ok(userAddrList);	
	}
}
