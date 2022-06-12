package com.fingerchar.api.web;

import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcNftLikeService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.vo.NftParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/like")
public class FcNftLikeController extends BaseController {

	@Autowired
	FcNftLikeService nftLikeService;

	@Autowired
	FcUserService userService;

	@Autowired
	FcContractNftService contractNftService;

	@PostMapping("listuserlike")
	public Object listbyaddr(String[] nfts, String address) {
		if (nfts == null || nfts.length == 0) {
			return ResponseUtil.ok(new ArrayList<String>(0));
		}
		if (StringUtils.isEmpty(address)) {
			return ResponseUtil.ok(new ArrayList<String>(0));
		}
		FcUser user = this.userService.getUserByAddress(address);
		if (null == user) {
			return ResponseUtil.ok(new ArrayList<String>(0));
		}
		List<NftParamVO> paramVOList = new ArrayList<>();
		String[] _nft = null;
		for(String nft: nfts){
			_nft = nft.split(":");
			paramVOList.add(new NftParamVO(_nft[0], _nft[1]));
		}
		return ResponseUtil.ok(nftLikeService.listbymulti(address, paramVOList));
	}

	@PostMapping("add")
	public Object add(String address, String tokenId) {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		return this.nftLikeService.add(userAddress, address, tokenId);
	}

	@PostMapping("remove")
	public Object remove(String address, String tokenId) {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		return this.nftLikeService.delete(userAddress, address, tokenId);
	}

}
