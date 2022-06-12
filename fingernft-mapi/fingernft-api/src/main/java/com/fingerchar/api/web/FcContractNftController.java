package com.fingerchar.api.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcNftItemsService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcSystemConfigManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.dto.NftInfo;
import com.fingerchar.db.vo.FcOrderVo;
import com.fingerchar.db.vo.NftParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
	FcSystemConfigManager systemConfigManager;

	@Autowired
	FcNftItemsService nftItemsService;

	@PostMapping("add")
	public Object add(NftInfo nft, String address) {
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

		String temp = this.systemConfigManager.getKeyValue(SysConfConstant.NFT_DEFAULT_VERIFY);
		if(!StringUtils.isEmpty(temp) || "true".equals(temp)) {
			nft.setNftVerify(1);
		} else {
			nft.setNftVerify(0);
		}
		nft.setIsSync(false);

		FcContractNft contractNft = this.contractNftService.save(nft);
		if(null == contractNft) {
			return ResponseUtil.fail(-1, "create nft fail");
		} else {
			return ResponseUtil.ok(contractNft);
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
		List<NftParamVO> paramVOList = this.parseNftParam(info);
		if(paramVOList.isEmpty()){
			return ResponseUtil.ok(new ArrayList<>(0));
		}
		List<FcOrderVo> orderVoList = this.contractNftService.activebids(paramVOList);
		return ResponseUtil.ok(orderVoList);
	}

	@PostMapping("activesales")
	public Object activesales(String info) {
		if(StringUtils.isEmpty(info)) {
			return ResponseUtil.ok(new ArrayList<>(0));
		}
		List<NftParamVO> paramVOList = this.parseNftParam(info);
		if(paramVOList.isEmpty()){
			return ResponseUtil.ok(new ArrayList<>(0));
		}
		List<FcOrderVo> orderVoList = this.contractNftService.activesales(paramVOList);
		return ResponseUtil.ok(orderVoList);
	}

	private List<NftParamVO> parseNftParam(String info){
		String[] nftStrs = info.split(",");
		String[] nft = null;
		List<NftParamVO> paramVOList = new ArrayList<>();
		for(String nftStr : nftStrs){
			nft = nftStr.split(":");
			paramVOList.add(new NftParamVO(nft[0], nft[1]));
		}
		return paramVOList;
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
