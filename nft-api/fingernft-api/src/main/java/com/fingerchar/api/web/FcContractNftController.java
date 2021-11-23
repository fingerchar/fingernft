package com.fingerchar.api.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.constant.RedisConstant;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.dto.ActiveBidInfo;
import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcNftItemsService;
import com.fingerchar.api.service.FcRedisService;
import com.fingerchar.api.service.FcSystemConfigService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcUser;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/nft")
public class FcContractNftController extends BaseController {

	@Autowired
	FcContractNftService contractNftService;

	@Autowired
	FcContractService contractService;

	@Autowired
	FcUserService userService;

	@Autowired
	FcRedisService redisService;

	@Autowired
	FcSystemConfigService configService;

	@Autowired
	IBaseService baseService;

	@Autowired
	FcNftItemsService nftItemsService;

	@PostMapping("add")
	public Object add(FcContractNft nft,String address) {
		String userAddress = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress)) {
			return ResponseUtil.unlogin();
		}
		if(StringUtils.isEmpty(address)) {
			return ResponseUtil.badArgumentValue();
		}
		FcContract contract = this.contractService.getByAddress(address);
		if(null == contract) {
			return ResponseUtil.badArgumentValue();
		}
		FcUser user = this.userService.getUserByAddress(userAddress);
		if(null == user) {
			return ResponseUtil.NotFoud();
		}
		if(StringUtils.isEmpty(nft.getRoyalties())) {
			nft.setRoyalties("");
		} else {
			List<BigDecimal> royalties = JSON.parseArray(nft.getRoyalties(), BigDecimal.class);
			if(null == royalties || royalties.isEmpty()) {
				nft.setRoyalties("");
			} else {
				for(BigDecimal roy : royalties) {
					int result = roy.compareTo(new BigDecimal("1000"));
					if(result > 0) {
						return ResponseUtil.fail(-1, "Royalties can not be greater then 10");
					}
					result = new BigDecimal("0").compareTo(roy);
					if(result > 0) {
						return ResponseUtil.fail(-1, "Royalties can not be lower then 0");
					}
				}
			}
		}
		nft.setContractId(contract.getId());
		nft.setCreator(user.getAddress());
		nft.setType(contract.getType());

		Object isVerifyTemp = this.redisService.get(RedisConstant.SYS_CONFIG_PRE + SysConfConstant.NFT_DEFAULT_VERIFY);
		if(null == isVerifyTemp) {
			String temp = this.configService.get(SysConfConstant.NFT_DEFAULT_VERIFY);
			if(StringUtils.isEmpty(temp)) {
				nft.setNftVerify(0);
			} else {
				nft.setNftVerify(1);
			}
		} else {
			Boolean isVerify = Boolean.parseBoolean((String)isVerifyTemp);
			nft.setNftVerify(isVerify?1:0);
		}
		nft.setIsSync(false);


		String ipfsHash = this.contractNftService.save(nft, user);
		if(StringUtils.isEmpty(ipfsHash)) {
			return ResponseUtil.fail();
		} else {
			return ResponseUtil.ok(ipfsHash);
		}
	}

	@PostMapping("detail")
	public Object detail(String token, String tokenId) {
		if(StringUtils.isEmpty(token) || null == tokenId) {
			return ResponseUtil.ok(new JSONObject());
		}
		return this.contractNftService.detail(token, tokenId);
	}

	@PostMapping("owners")
	public Object owner(String token, String tokenId, HttpServletRequest request) {
		if(StringUtils.isEmpty(token) || null == tokenId) {
			return ResponseUtil.ok(new ArrayList<>(0));
		}
		return this.contractNftService.owners(token, tokenId);
	}

	@PostMapping("bids")
	public Object bids(String token, String tokenId) {
		if(StringUtils.isEmpty(token) || null == tokenId) {
			return ResponseUtil.ok(new ArrayList<>(0));
		}
		return this.contractNftService.bids(token, tokenId);
	}

	@PostMapping("activebids")
	public Object activebids(String info) {
		if(StringUtils.isEmpty(info)) {
			return ResponseUtil.ok(new ArrayList<>(0));
		}
		String[] queryArr = info.split(",");
		String[] qinfo = null;
		List<Map<String, Object>> list =  new ArrayList<>();
		Map<String, Object> map = null;
		String token = null;
		String tokenId = null;
		List<FcOrder> tempList = null;
		for(String temp :queryArr) {
			qinfo = temp.split(":");
			if(qinfo.length == 2) {
				token = qinfo[0];
				tokenId = qinfo[1];
				tempList = this.contractNftService.activebids(token, tokenId);
				if(null != tempList && !tempList.isEmpty()) {
					map = new HashMap<>();
					map.put("address", token);
					map.put("tokenId", tokenId);
					map.put("bids", tempList.stream().map(order->new ActiveBidInfo(order)).collect(Collectors.toList()));
					list.add(map);
				}
			}
		}
		return ResponseUtil.ok(list);
	}

	@PostMapping("history")
	public Object history(String token, String tokenId) {
		if(StringUtils.isEmpty(token) || null == tokenId) {
			return ResponseUtil.ok(new ArrayList<>(0));
		}
		return this.contractNftService.history(token, tokenId);
	}

	@PostMapping("collections")
	public Object collections() {
		IPage<FcContractNft> list = this.contractNftService.findCollections(this.getPageInfo());
		return ResponseUtil.ok(list);
	}
	
	
	@PostMapping("getmedia")
	public Object getmedia(String[] info) {
		if(null == info || info.length <= 0) {
				return ResponseUtil.okList(new ArrayList<>());
		}
		Map<String, String> map = this.contractNftService.getMedia(info);
		return ResponseUtil.ok(map);
	}
	
	@PostMapping("getroyalties")
	public Object getRoyalties(String[] info) {
		if(null == info || info.length <= 0) {
			return ResponseUtil.okList(new ArrayList<>());
		}
		Map<String, String> map = this.contractNftService.getRoyalties(info);
		return ResponseUtil.ok(map);
	}

}
