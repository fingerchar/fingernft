package com.fingerchar.api.web;

import com.fingerchar.api.service.FcUserFollowService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.utils.DappWeb3jUtil;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.core.util.Str2ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
		if(StringUtils.isEmpty(address)){
			return ResponseUtil.fail(-1, "address is empty");
		}
		if(userAddress.equalsIgnoreCase(address)){
			return ResponseUtil.fail(-1, "address is yourself");
		}
		if(!DappWeb3jUtil.isValidAddress(address)){
			return ResponseUtil.fail(-1, "address is incorrect");
		}
		return this.userFollowService.follow(userAddress, address);
	}

	@PostMapping("delete")
	public Object delete(String address) {
		String userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		if(address == null){
			return ResponseUtil.fail(-1, "address is empty");
		}
		if(!DappWeb3jUtil.isValidAddress(address)){
			return ResponseUtil.fail(-1, "address is incorrect");
		}
		return this.userFollowService.unfollow(userAddress, address);
	}

	@PostMapping("follows")
	public Object follows(String address){
		if(StringUtils.isEmpty(address) || !DappWeb3jUtil.isValidAddress(address)){
			return ResponseUtil.okList(new ArrayList<>());
		}
		return this.userFollowService.pageFollow(address, this.getPageInfo());
	}

	@PostMapping("followers")
	public Object followers(String address){
		if(StringUtils.isEmpty(address) || !DappWeb3jUtil.isValidAddress(address)){
			return ResponseUtil.okList(new ArrayList<>());
		}
		return this.userFollowService.pageFollower(address, this.getPageInfo());

	}

    //根据address列表，算出哪些address是我关注的
	//address,逗号分割
	@PostMapping("match")
	public Object match(String address, String userAddrs) {
		if(StringUtils.isEmpty(address) || StringUtils.isEmpty(userAddrs)) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		if(!DappWeb3jUtil.isValidAddress(address)){
			return ResponseUtil.fail(-1, "address is correct");
		}

		List<String> paramsList = Str2ListUtils.sliceString2StringArray(userAddrs);
		if (null == paramsList || paramsList.isEmpty()) {
			return ResponseUtil.ok(new ArrayList<>());
		}

		return this.userFollowService.listbymulti(address, paramsList);
	}
}
