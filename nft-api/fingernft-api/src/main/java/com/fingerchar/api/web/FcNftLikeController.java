package com.fingerchar.api.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcNftLikeService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftLike;
import com.fingerchar.db.domain.FcUser;


@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/like")
public class FcNftLikeController extends BaseController {

	@Autowired
	FcNftLikeService nftLikeService;

	@Autowired
	FcUserService userService;

	@Autowired
	FcContractNftService contractNftService;

	@PostMapping("list")
	public Object listAll(String[] nfts) {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}

		if (nfts == null || nfts.length == 0) {
			return ResponseUtil.ok(new ArrayList<String>(0));
		}
		List<Long> nftIds = new ArrayList<>(nfts.length);
		String[] _nft = null;
		String address = null;
		String tokenId = null;
		FcContractNft fcContract = null;
		for (String nft : nfts) {
			_nft = nft.split(":");
			address = _nft[0].toLowerCase();
			tokenId = _nft[1];
			fcContract = contractNftService.findByAddressTokenId(address, tokenId);
			if (null != fcContract) {
				nftIds.add(fcContract.getId());
			}
		}
		if (nftIds.isEmpty()) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		return ResponseUtil.ok(nftLikeService.list(userAddress, nftIds));
	}

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
		List<Long> nftIds = new ArrayList<>(nfts.length);
		String[] _nft = null;
		String token = null;
		String tokenId = null;
		FcContractNft fcContract = null;
		for (String nft : nfts) {
			_nft = nft.split(":");
			token = _nft[0].toLowerCase();
			tokenId = _nft[1];
			fcContract = contractNftService.findByAddressTokenId(token, tokenId);
			if (null != fcContract) {
				nftIds.add(fcContract.getId());
			}
		}
		if (nftIds.isEmpty()) {
			return ResponseUtil.ok(new ArrayList<>());
		}
		return ResponseUtil.ok(nftLikeService.list(user.getAddress(), nftIds));
	}

	@PostMapping("add")
	public Object add(String address, String tokenId) {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		if (tokenId == null) {
			return ResponseUtil.badArgument();
		}
		if (StringUtils.isEmpty(address)) {
			return ResponseUtil.badArgument();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		FcContractNft fcContractNft = contractNftService.findByAddressTokenId(address, tokenId);
		if (fcContractNft == null) {
			return ResponseUtil.NotFoud();
		}

		FcNftLike fcNftLike = nftLikeService.queryByUserIdNftId(fcContractNft.getId(), user.getAddress());
		if (fcNftLike == null) {
			nftLikeService.add(fcContractNft, user);
		}
		return ResponseUtil.ok();
	}

	@PostMapping("remove")
	public Object remove(String address, String tokenId) {
		String userAddress = (String) request.getAttribute("userAddress");
		if (StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		if (tokenId == null) {
			return ResponseUtil.badArgument();
		}
		if (StringUtils.isEmpty(address)) {
			return ResponseUtil.badArgument();
		}
		FcContractNft fcContractNft = contractNftService.findByAddressTokenId(address, tokenId);
		if (fcContractNft == null) {
			return ResponseUtil.NotFoud();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		FcNftLike fcNftLike = nftLikeService.queryByUserIdNftId(fcContractNft.getId(), user.getAddress());
		if (fcNftLike != null) {
			nftLikeService.delete(fcNftLike);
		}
		return ResponseUtil.ok();
	}

	@PostMapping("listbyaddr")
	public Object listByAddress(List<String> addressList) {
		return this.nftLikeService.listByAddress(addressList);
	}

}
