package com.fingerchar.api.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.dto.NftItemsInfo;
import com.fingerchar.api.dto.UserVo;
import com.fingerchar.api.service.FcContractNftService;
import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcNftItemsService;
import com.fingerchar.api.service.FcUserFollowService;
import com.fingerchar.api.service.FcUserService;
import com.fingerchar.api.utils.ListUtils;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcContract;
import com.fingerchar.db.domain.FcContractNft;
import com.fingerchar.db.domain.FcNftItems;
import com.fingerchar.db.domain.FcOrder;
import com.fingerchar.db.domain.FcUser;
import com.fingerchar.db.domain.FcUserFollow;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/home")
public class FcHomeController extends BaseController {

	@Autowired
	FcContractNftService contractNftService;

	@Autowired
	FcContractService contractService;

	@Autowired
	FcUserService userService;

	@Autowired
	FcUserFollowService userFollowService;

	@Autowired
	FcNftItemsService  nftItemsService;
	
	@Autowired
	IBaseService baseService;

	@PostMapping("list")
	public Object list(String address, Long cate,String symbol, String sort, String order) {
		List<String> contractAddrList = null;
		if (!StringUtils.isEmpty(symbol)) {
			List<FcContract> fcContracts = contractService.findBySymbol(symbol);
			if (fcContracts != null) {
				contractAddrList = fcContracts.stream().map(FcContract::getAddress).collect(Collectors.toList());
			}
		}
		IPage<FcContractNft> list = contractNftService.findListForIndex(this.getPageInfo(), address, cate, contractAddrList, sort, order, true);
		return ResponseUtil.okList(this.processNft(list), list);
	}

	@PostMapping("search")
	public Object search(String search) {

		if(StringUtils.isEmpty(search)){
			search = "";
		}
		IPage<FcContractNft> list = contractNftService.findSearch(search, this.getPageInfo());
		return ResponseUtil.okList(this.processNft(list), list);
	}
	
	private List<Map<String, Object>> processNft(IPage<FcContractNft> list) {
		List<Map<String, Object>> result = new ArrayList<>(list.getRecords().size());
		if (list != null && !list.getRecords().isEmpty()) {	
			List<Long> nftIds = list.getRecords().stream().map(FcContractNft::getId).collect(Collectors.toList());
			List<String> contractAddrs = list.getRecords().stream().map(FcContractNft::getAddress).collect(Collectors.toList());
			List<FcContract> contractList = this.contractService.findListByAdress(contractAddrs);
			List<FcNftItems> itemsList = this.nftItemsService.findListByNftIds(nftIds);
			List<String> userAddrs = itemsList.stream().map(FcNftItems::getItemOwner).collect(Collectors.toList());
			List<FcUser> userList = this.userService.findListByAddrs(userAddrs);
			Map<String, Object> res = null;
			FcContract fcContract = null;
			FcUser fcUser = null;
			List<FcNftItems>  tempItemsList = null;
			Map<String, Object> itemsMap = null;
			List<Map<String, Object>> tempList = null;
			QueryWrapper<FcOrder> orderWrapper = null;
			FcOrder order = null;
			for (FcContractNft nft : list.getRecords()) {
				res = new HashMap<>(3);
				fcContract = ListUtils.getByPredicate(contractList, contract -> contract.getAddress().toLowerCase().equals(nft.getAddress().toLowerCase()));
				tempItemsList = itemsList.stream().filter(item->item.getNftId().equals(nft.getId())).collect(Collectors.toList());
				if(!tempItemsList.isEmpty()) {					
					tempList = new ArrayList<>();
					for(FcNftItems item: tempItemsList) {
						itemsMap = new HashMap<>(2);
						NftItemsInfo itemInfo = new NftItemsInfo(item);
						
						fcUser = ListUtils.getByPredicate(userList, user -> user.getAddress().toLowerCase().equals(item.getItemOwner().toLowerCase()));
						if(null != fcUser) {							
							itemsMap.put("user", new UserVo(fcUser));
						} else {
							itemsMap.put("user", new UserVo(item.getItemOwner()));
						}
						if(null != item.getOnsell() && item.getOnsell().booleanValue()) {
							orderWrapper = new QueryWrapper<>();
							orderWrapper.eq(FcOrder.OWNER, item.getItemOwner())
								.eq(FcOrder.SELL_TOKEN, nft.getAddress())
								.eq(FcOrder.SELL_TOKEN_ID, nft.getTokenId())
								.eq(FcOrder.STATUS, 0)
								.eq(FcOrder.EXPIRED, false)
								.eq(BaseEntity.DELETED, false);
							order = this.baseService.getByCondition(FcOrder.class, orderWrapper);
							if(null != order) {
								itemInfo.setCompleted(order.getSells());
								itemInfo.setSellValue(order.getSellValue());
								itemInfo.setBuyValue(order.getBuyerValue());
								itemInfo.setPayTokenAddress(order.getBuyerToken());
							} else {
								itemInfo.setCompleted(0L);
								itemInfo.setSellValue("0");
								itemInfo.setBuyValue("");
								itemInfo.setPayTokenAddress("");
							}
						} else {
							itemInfo.setCompleted(0L);
							itemInfo.setSellValue("0");
							itemInfo.setBuyValue("");
							itemInfo.setPayTokenAddress("");
						}
						itemsMap.put("item", itemInfo);
						tempList.add(itemsMap);
					} 
				}
				res.put("contract", fcContract);
				res.put("nft", nft);
				res.put("itemList", tempList);
				result.add(res);
			}
		}
		return result;
	}

	@PostMapping("searchuser")
	public Object searchUser(String search) {
		if(search == null){
			return ResponseUtil.okList(this.getPageInfo());
		}

		IPage<FcUser> list = this.userService.findByName(search, this.getPageInfo());
		return ResponseUtil.okList(list);
	}

	@PostMapping("following")
	public Object follows(String address,Long categoryId) {
		String userAddress1 = (String) request.getAttribute("userAddress");
		if(StringUtils.isEmpty(userAddress1)) {
			return ResponseUtil.unlogin();
		}
		FcUser user = userService.getUserByAddress(userAddress1);
		List<FcUserFollow> fcUserFollows = userFollowService.findListByUserAddress(user.getAddress());
		List<String> userFollowAddrs = fcUserFollows.stream().map(FcUserFollow::getFollowingAddress)
				.collect(Collectors.toList());
		List<Map<String, Object>> result = new ArrayList<>(fcUserFollows.size());
		if (!fcUserFollows.isEmpty() && userFollowAddrs.size() > 0) {
			List<FcUser> fcUsers = userService.findListByAddrs(userFollowAddrs);
			List<String> userAddress = fcUsers.stream().map(FcUser::getAddress).collect(Collectors.toList());
			if (userAddress.size() > 0) {
				IPage<FcNftItems> fcContractNfts = nftItemsService.findListByOwners(userAddress, categoryId, this.getPageInfo());
				if (!fcContractNfts.getRecords().isEmpty()) {
					Map<String, Object> res = null;
					FcContractNft fcContractNft = null;
					FcContract fcContract = null;
					FcUser fcUser = null;
					for (FcNftItems fcNftItems : fcContractNfts.getRecords()) {
						res = new HashMap<>(3);
						fcContractNft = contractNftService.findById(fcNftItems.getNftId());
						fcContract = contractService.getByAddress(fcNftItems.getAddress());
						fcUser = userService.getUserByAddress(fcNftItems.getItemOwner());
						res.put("contract", fcContract);
						res.put("nft", fcContractNft);
						res.put("user", fcUser);
						res.put("item", fcNftItems);
						result.add(res);
					}
				}
			}
		}
		return ResponseUtil.okList(result);
	}

}
