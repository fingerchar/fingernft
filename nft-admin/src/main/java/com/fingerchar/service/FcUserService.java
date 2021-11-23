package com.fingerchar.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.base.entity.BaseEntity;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.dao.ext.FcUserExtMapper;
import com.fingerchar.domain.*;
import com.fingerchar.vo.FcUserVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FcUserService {

	@Autowired
	private FcUserExtMapper userExtMapper;

	@Autowired
	private FcPayTokenService payTokenService;

	@Autowired
	private IBaseService baseService;

	/**
	 * @param useraddress
	 * @param isverify
	 * @param name
	 * @param page
	 * @param isASC
	 * @param sortType
	 * @return
	 */
	public IPage<FcUserVo> querySelective(String useraddress, Integer isverify, String name, IPage<FcUserVo> page,
			boolean isASC, String sortType) {
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(name)) {
			wrapper.like("user.nickname", name);
		}
		if (!StringUtils.isEmpty(isverify)) {
			if (isverify != 0) {
				wrapper.eq("user.user_verify", isverify);
			} else {
				wrapper.and(i -> i.eq("user.user_verify", isverify).or().isNull("user.user_verify"));
			}

		}
		if (!StringUtils.isEmpty(useraddress)) {
			wrapper.eq("user.address", useraddress);
		}
		wrapper.eq("user.is_web", true);

		if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
			wrapper.orderBy(true, isASC, sortType);
		}
		/*
		 * 填装买卖nft数据等
		 */
		IPage<FcUserVo> iPage = userExtMapper.getList2(page, wrapper);
		List<FcUserVo> list = iPage.getRecords();

		IPage<FcUserVo> result = new Page<FcUserVo>(iPage.getCurrent(), iPage.getSize());
		result.setPages(iPage.getPages());
		// packFcUserVo(list)打包数据
		result.setRecords(packFcUserVo(list));
		result.setTotal(iPage.getTotal());
		return result;
	}

	/**
	 * 打包用户数据
	 *
	 * @param list
	 *            用户视图集合
	 * @return
	 */
	private List<FcUserVo> packFcUserVo(List<FcUserVo> list) {
		// 将用户地址取出装入集合中
		List<String> nftAddress = list.stream().map(vo -> vo.getAddress()).collect(Collectors.toList());
		if (nftAddress.isEmpty()) {
			// 如果为空直接返回
			return list;
		}
		QueryWrapper<FcContractNft> nftWrapper = new QueryWrapper<>();
		nftWrapper.in(FcContractNft.CREATOR, nftAddress);
		// 得到用户与之关联的的nft
		List<FcContractNft> contractNfts = baseService.findByCondition(FcContractNft.class, nftWrapper);
		// 形成以用户地址为key，nft为值的集合
		Map<String, List<FcContractNft>> nftMap = new HashMap<>();
		contractNfts.stream().forEach(item -> {
			if (null == nftMap.get(item.getCreator())) {
				nftMap.put(item.getCreator(), new ArrayList<>());
			}
			nftMap.get(item.getCreator()).add(item);
		});

		// 将nft对应id取出
		List<Long> ids = contractNfts.stream().map(nft -> nft.getId()).collect(Collectors.toList());
		if (ids.isEmpty()) {
			ids = new ArrayList<>();
			ids.add(-1L);
		}
		QueryWrapper<FcNftItems> itemsWrapper = new QueryWrapper<>();
		itemsWrapper.eq(FcNftItems.DELETED, false).in(FcNftItems.NFT_ID, ids);
		// 获得nft对应的所有Items集合
		List<FcNftItems> itemsList = this.baseService.findByCondition(FcNftItems.class, itemsWrapper);
		Map<Long, List<FcNftItems>> itemsMap = new HashMap<>();
		itemsList.stream().forEach(item -> {
			if (null == itemsMap.get(item.getNftId())) {
				itemsMap.put(item.getNftId(), new ArrayList<>());
			}
			itemsMap.get(item.getNftId()).add(item);
		});

		List<FcNftItems> tempList;
		List<FcContractNft> nftList;
		FcNftItems item;
		int itemLen = 0;
		Long quantity = 0L;
		Long createNFT = 0L;
		Long haveNFT = 0L;
		FcContractNft contractNft;
		Map<String, BigDecimal> addressRateMap = payTokenService.selectAddressAndRate();
		// 买入nft集合
		QueryWrapper<FcOrderLog> orderWrapper = new QueryWrapper<>();
		orderWrapper.in(FcOrderLog.FROM, nftAddress);
		orderWrapper.eq(FcOrderLog.TYPE, 4);
		List<FcOrderLog> buyNft = baseService.findByCondition(FcOrderLog.class, orderWrapper);
		Map<String, List<FcOrderLog>> orderBuyMap = new HashMap<>();
		buyNft.stream().forEach(buy -> {
			if (null == orderBuyMap.get(buy.getFrom())) {
				orderBuyMap.put(buy.getFrom(), new ArrayList<>());
			}
			orderBuyMap.get(buy.getFrom()).add(buy);
		});

		// 卖出nft数量
		QueryWrapper<FcOrderLog> saleWrapper = new QueryWrapper<>();
		saleWrapper.in(FcOrderLog.FROM, nftAddress);
		saleWrapper.eq(FcOrderLog.TYPE, 8);
		List<FcOrderLog> saleNft = baseService.findByCondition(FcOrderLog.class, saleWrapper);
		Map<String, List<FcOrderLog>> orderSaleMap = new HashMap<>();
		saleNft.stream().forEach(sale -> {
			if (null == orderSaleMap.get(sale.getFrom())) {
				orderSaleMap.put(sale.getFrom(), new ArrayList<>());
			}
			orderSaleMap.get(sale.getFrom()).add(sale);
		});

		if (null != list) {
			for (FcUserVo fcUserVo : list) {
				nftList = nftMap.get(fcUserVo.getAddress());
				buyNft = orderBuyMap.get(fcUserVo.getAddress());
				if (null == buyNft) {
					continue;
				}
				sumMoney(buyNft, fcUserVo, addressRateMap, 4);
				saleNft = orderSaleMap.get(fcUserVo.getAddress());
				if (null == saleNft) {
					continue;
				}
				sumMoney(saleNft, fcUserVo, addressRateMap, 8);
				if (null == nftList) {
					continue;
				}
				for (int n = 0; n < nftList.size(); n++) {
					contractNft = nftList.get(n);
					quantity = contractNft.getQuantity();
					if (null != quantity) {
						createNFT += quantity;
					}
					tempList = itemsMap.get(contractNft.getId());
					if (null == tempList) {
						continue;
					}
					itemLen = tempList.size();
					for (int i = 0; i < itemLen; i++) {
						item = tempList.get(i);
						if (null != item.getQuantity()) {
							haveNFT += item.getQuantity();
						}
					}
				}
				fcUserVo.setCreateNFT(createNFT);
				fcUserVo.setHaveNFT(haveNFT);
				createNFT = 0L;
				haveNFT = 0L;
			}
		}
		return list;
	}

	/**
	 * 计算金额
	 *
	 * @param list
	 *            订单日志
	 * @return
	 */
	private List<FcOrderLog> sumMoney(List<FcOrderLog> list, FcUserVo fcUserVo, Map<String, BigDecimal> addressRateMap,
			int flag) {
		BigDecimal rate = new BigDecimal(0);
		for (FcOrderLog fcOrderLog : list) {
			FcOrder parse = JSON.parseObject(fcOrderLog.getContent(), FcOrder.class);
			Long sells = parse.getSells();
			if (null == sells || sells <= 0) {
				continue;
			}
			if (4 == flag) {
				fcUserVo.setBuyNFT((fcUserVo.getBuyNFT() == null ? 0L : fcUserVo.getBuyNFT()) + sells);
				rate = addressRateMap.get(parse.getBuyerToken().toLowerCase());
			} else {
				fcUserVo.setSaleNFT((fcUserVo.getSaleNFT() == null ? 0L : fcUserVo.getSaleNFT()) + sells);
				rate = addressRateMap.get(parse.getSellToken().toLowerCase());
			}

			if (rate == null) {
				rate = new BigDecimal("0");
			}
			String buyerValue = parse.getBuyerValue();
			String sellValue = parse.getSellValue();
			BigDecimal money;
			if (buyerValue == null || "".equals(buyerValue)) {
				buyerValue = "0";
			}
			if (sellValue == null || "".equals(sellValue)) {
				money = new BigDecimal("0");
			} else {
				money = new BigDecimal(buyerValue).divide(new BigDecimal(sellValue), 6, BigDecimal.ROUND_DOWN)
						.multiply(new BigDecimal(sells)).multiply(rate);
			}
			BigDecimal accumulatedMoney = fcUserVo.getAccumulatedMoney();
			if (accumulatedMoney == null) {
				accumulatedMoney = new BigDecimal("0");
			}
			fcUserVo.setAccumulatedMoney(accumulatedMoney.add(money));
			fcUserVo.setAccumulatedVolume(
					(fcUserVo.getAccumulatedVolume() == null ? 0L : fcUserVo.getAccumulatedVolume()) + sells);
		}
		return list;
	}

	/**
	 * 根据ID更新Verify
	 *
	 * @param contract
	 *            Verify记录
	 * @return 更新成功返回true，否则返回false
	 */
	public boolean verifyContract(FcUser contract, Integer userVerify) {
		contract.setUserVerify(userVerify);
		contract.setUpdateTime(System.currentTimeMillis() / 1000);
		return baseService.update(contract) > 0;
	}

	/**
	 * 根据ID禁用
	 *
	 * @param contract
	 * @return 更新成功返回true，否则返回false
	 */
	public boolean disableContract(FcUser contract) {
		contract.setDeleted(true);
		contract.setUpdateTime(System.currentTimeMillis() / 1000);
		return baseService.update(contract) > 0;
	}

	/**
	 * 根据ID启用
	 *
	 * @param contract
	 * @return 更新成功返回true，否则返回false
	 */
	public boolean enableContract(FcUser contract) {
		contract.setDeleted(false);
		contract.setUpdateTime(System.currentTimeMillis() / 1000);
		return baseService.update(contract) > 0;
	}

	/**
	 * findById
	 *
	 * @param id
	 * @return
	 */
	public FcUser findById(Long id) {
		return baseService.getById(FcUser.class, id);
	}

	/**
	 * 统计新增用户数
	 *
	 * @param staTime
	 * @return
	 */
	public Map<String, Object> newCreateList(Long staTime) {
		Map<String, Object> map = new HashMap<>();
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		long oneDay = 24 * 60 * 60;
		wrapper.lt(FcUser.CREATE_TIME, staTime + oneDay).ge(FcUser.CREATE_TIME, staTime).eq(FcUser.IS_WEB, true)
				.eq(FcUser.DELETED, false);
		Integer toDayCounts = baseService.counts(FcUser.class, wrapper);
		QueryWrapper<FcUser> beforeWrapper = new QueryWrapper<>();
		beforeWrapper.lt(FcUser.CREATE_TIME, staTime).ge(FcUser.CREATE_TIME, staTime - oneDay).eq(FcUser.IS_WEB, true);
		Integer beforeCounts = baseService.counts(FcUser.class, beforeWrapper);
		map.put("todayAdd", toDayCounts);
		map.put("beforeAdd", beforeCounts);
		return map;
	}

	/**
	 * 统计用户
	 */
	public Map<String, Object> statUser() {
		Map<String, Object> map = new HashMap<>();
		QueryWrapper<FcUser> wrapper = new QueryWrapper<>();
		wrapper.ge(BaseEntity.CREATE_TIME, System.currentTimeMillis() / 1000).eq(FcUser.IS_WEB, true);
		Integer todayCounts = baseService.counts(FcUser.class, wrapper);
		QueryWrapper<FcUser> beforeWrapper = new QueryWrapper<>();
		beforeWrapper.ge(BaseEntity.CREATE_TIME, System.currentTimeMillis() / 1000 - 24 * 60 * 60)
				.lt(BaseEntity.CREATE_TIME, System.currentTimeMillis() / 1000).eq(FcUser.IS_WEB, true);
		Integer beforeCounts = baseService.counts(FcUser.class, beforeWrapper);
		QueryWrapper<FcUser> verifyWrapper = new QueryWrapper<>();
		verifyWrapper.eq("user_verify", 1).eq(FcUser.IS_WEB, true);
		Integer verifyCounts = baseService.counts(FcUser.class, verifyWrapper);
		QueryWrapper<FcUser> notVerifyWrapper = new QueryWrapper<>();
		notVerifyWrapper.isNull("user_verify").or().eq("user_verify", 0).eq(FcUser.IS_WEB, true);
		Integer notVerifyCounts = baseService.counts(FcUser.class, notVerifyWrapper);
		map.put("todayAdd", todayCounts);
		map.put("yesterdayAdd", beforeCounts);
		map.put("verifyUsers", verifyCounts);
		map.put("notVerifyUser", notVerifyCounts);
		return map;
	}

}
