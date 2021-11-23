package com.fingerchar.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.dao.ext.FcOrderExtMapper;
import com.fingerchar.domain.FcNftCategory;
import com.fingerchar.domain.FcOrder;
import com.fingerchar.domain.FcOrderLog;
import com.fingerchar.dto.FcOrderDto;
import com.fingerchar.vo.FcOrderLogVo;
import com.fingerchar.vo.FcOrderVo;

@Service
public class FcOrderService {

	@Autowired
	private FcOrderExtMapper fcOrderExtMapper;

	@Autowired
	private FcPayTokenService payTokenService;

	@Autowired
	private IBaseService baseService;

	/**
	 * 查询
	 * 
	 * @param address
	 * @param page
	 * @param isASC
	 * @param sortType
	 * @return
	 */
	public IPage<FcOrderLogVo> querySelective(String address, Long staTime, IPage<FcOrderLog> page, boolean isASC,
			String sortType) {
		QueryWrapper<FcOrderLog> wrapper = new QueryWrapper<>();
		if (!StringUtils.isEmpty(address)) {
			wrapper.and(i -> i.eq("log.type", 4).or().eq("log.type", 8))
					.and(a -> a.eq("log.from", address).or().eq("log.to", address));
		} else {
			wrapper.and(i -> i.eq("log.type", 4).or().eq("log.type", 8));
		}
		if (null == staTime) {
			staTime = System.currentTimeMillis() / 1000;
		}
		wrapper.ge("fc.update_time", staTime).lt("fc.update_time", staTime + 24 * 60 * 60).eq("fc.status", 1);
		if (!StringUtils.isEmpty(isASC) && !StringUtils.isEmpty(sortType)) {
			wrapper.orderBy(true, isASC, sortType);
		} else {
			wrapper.orderBy(true, false, "log.create_time");
		}
		IPage<FcOrderLog> iPage = fcOrderExtMapper.findByPage(page, wrapper);
		return this.packagingData(iPage);
	}

	/*
	 * 封装并处理数据
	 */
	private IPage<FcOrderLogVo> packagingData(IPage<FcOrderLog> iPage) {
		List<FcOrderLog> list = iPage.getRecords();
		List<FcOrderLogVo> resultList = new ArrayList<>();
		Map<String, BigDecimal> addressRateMap = payTokenService.selectAddressAndRate();
		FcOrder parse;
		if (null != list) {
			FcOrderLogVo data;
			BigDecimal rate = new BigDecimal("0");
			for (FcOrderLog orderLog : list) {
				data = new FcOrderLogVo();
				data.setId(orderLog.getId());
				data.setCreateTime(orderLog.getCreateTime());
				data.setFrom(orderLog.getFrom());
				data.setTo(orderLog.getTo());
				data.setType(orderLog.getType());
				data.setDeleted(orderLog.getDeleted());
				data.setUpdateTime(orderLog.getUpdateTime());
				parse = JSON.parseObject(orderLog.getContent(), FcOrder.class);
				if (null != parse) {
					data.setBuyerToken(parse.getBuyerToken());
					data.setSellToken(parse.getSellToken());
					if (orderLog.getType() == 4) {
						rate = addressRateMap.get(parse.getBuyerToken().toLowerCase());
					} else if (orderLog.getType() == 8) {
						rate = addressRateMap.get(parse.getSellToken().toLowerCase());
					}
					Long sells = parse.getSells();
					if (sells == null) {
						sells = 0L;
					}
					data.setAccumulatedMoney(
							new BigDecimal(parse.getBuyerValue()).divide(new BigDecimal(parse.getSellValue()), 3)
									.multiply(new BigDecimal(sells)).multiply(rate));
				}
				resultList.add(data);
			}
		}
		IPage<FcOrderLogVo> result = new Page<FcOrderLogVo>(iPage.getCurrent(), iPage.getSize());
		result.setPages(iPage.getPages());
		result.setRecords(resultList);
		result.setTotal(iPage.getTotal());
		return result;
	}

	public List<Map<String, String>> echartShow(FcOrderVo fcOrderVo) {
		List<Map<String, String>> maps = new LinkedList<>();
		List<FcNftCategory> fcNftCategories = baseService.find(new FcNftCategory());
		Map<String, String> map = new HashMap<>();
		Map<String, String> numMap = new HashMap<>();
		Map<String, String> avgMap = new HashMap<>();
		Map<String, String> NMTMap = new HashMap<>();
		List<FcOrderDto> orderDtos = listVo(null);// 查询所有成交数量以及价格
		BigDecimal allPrice = orderDtos.stream().map(x -> new BigDecimal(x.getUsdtPrice())).reduce(BigDecimal.ZERO,
				BigDecimal::add);

		for (FcNftCategory fcNftCategory : fcNftCategories) {
			map.put(fcNftCategory.getName() + "Price",
					addOrderDtos(orderDtos, Integer.parseInt(fcNftCategory.getId().toString()), allPrice));// 成交价格所占百分比
			numMap.put(fcNftCategory.getName() + "Num",
					orderNum(orderDtos, Integer.parseInt(fcNftCategory.getId().toString())));// 成交数量所占百分比
			avgMap.put(fcNftCategory.getName() + "AVG",
					orderAvg(orderDtos, Integer.parseInt(fcNftCategory.getId().toString())));// 成交均价
			NMTMap.put(fcNftCategory.getName() + "NMT",
					orderNMT(orderDtos, Integer.parseInt(fcNftCategory.getId().toString())));// 成交均价
		}
		maps.add(map);
		maps.add(numMap);
		maps.add(avgMap);
		maps.add(NMTMap);
		return maps;

	}

	/**
	 * 查询所有类别的成交价格
	 * 
	 * @param fcOrder
	 * @return
	 */
	public List<FcOrderDto> listVo(FcOrder fcOrder) {
		return fcOrderExtMapper.listVo(fcOrder);
	}

	/**
	 * 计算价格所占百分比
	 * 
	 * @param orderDtos
	 * @param type
	 * @param allPrice
	 * @return
	 */
	public String addOrderDtos(List<FcOrderDto> orderDtos, Integer type, BigDecimal allPrice) {
		if (!StringUtils.isEmpty(orderDtos)) {
			BigDecimal bigDecimal = orderDtos.stream().filter(x -> x.getType() == type)
					.map(x -> new BigDecimal(x.getUsdtPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
			return bigDecimal.divide(allPrice, 2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).toString()
					+ "%";
		}
		return null;
	}

	/**
	 * 获得各个类别的数量所占百分比
	 * 
	 * @param orderDtos
	 * @param type
	 * @return
	 */
	public String orderNum(List<FcOrderDto> orderDtos, Integer type) {
		if (!StringUtils.isEmpty(orderDtos)) {
			List<FcOrderDto> collect = orderDtos.stream().filter(x -> x.getType() == type).collect(Collectors.toList());
			BigDecimal bigDecimal = new BigDecimal(collect.size());
			BigDecimal bigDecimal1 = new BigDecimal(orderDtos.size());
			return bigDecimal.divide(bigDecimal1, 2, RoundingMode.HALF_EVEN).multiply(new BigDecimal(100)).toString()
					+ "%";
		}
		return null;
	}

	/**
	 * 计算各类别平均交易额
	 * 
	 * @param orderDtos
	 * @param type
	 * @return
	 */
	public String orderAvg(List<FcOrderDto> orderDtos, Integer type) {
		if (!StringUtils.isEmpty(orderDtos)) {
			List<FcOrderDto> collect = orderDtos.stream().filter(x -> x.getType() == type).collect(Collectors.toList());
			BigDecimal bigDecimal = collect.stream().map(x -> new BigDecimal(x.getUsdtPrice())).reduce(BigDecimal.ZERO,
					BigDecimal::add);
			if (!StringUtils.isEmpty(collect)) {
				return bigDecimal.divide(new BigDecimal(collect.size()), 2, RoundingMode.HALF_EVEN).toString();
			}
		}
		return null;
	}

	/**
	 * 计算各类别总数
	 * 
	 * @param orderDtos
	 * @param type
	 * @return
	 */
	public String orderNMT(List<FcOrderDto> orderDtos, Integer type) {
		if (!StringUtils.isEmpty(orderDtos)) {
			int size = orderDtos.stream().filter(x -> x.getType() == type).collect(Collectors.toList()).size();
			return String.valueOf(size);
		}
		return null;
	}

	/**
	 * 统计交易数量
	 * 
	 * @return
	 */
	public Map<String, Object> statTransaction(Long staTime) {
		Map<String, Object> dataMap = new HashMap<>();
		QueryWrapper<FcOrder> wrapper = new QueryWrapper<>();
		long oneDay = 24 * 60 * 60;
		if (null == staTime) {
			staTime = System.currentTimeMillis() / 1000;
		}
		// 24小时交易量
		wrapper.ge(FcOrder.UPDATE_TIME, staTime).lt(FcOrder.UPDATE_TIME, staTime + oneDay).eq(FcOrder.STATUS, 1);

		Integer oneDayCounts = baseService.counts(FcOrder.class, wrapper);
		dataMap.put("oneDayCounts", oneDayCounts);

		// 交易用户数
		Integer userCounts = fcOrderExtMapper.countUser(wrapper);
		dataMap.put("userCounts", userCounts);

		// 交易金额
		QueryWrapper<FcOrder> queryWrapper = new QueryWrapper<>();
		queryWrapper.and(i -> i.eq("log.type", 4).or().eq("log.type", 8));
		queryWrapper.ge("fc.update_time", staTime).lt("fc.update_time", staTime + 24 * 60 * 60).eq("fc.status", 1);
		List<FcOrderLog> dataList = fcOrderExtMapper.getAccumulatedMoney(queryWrapper);
		BigDecimal accumulatedMoney = this.sumMoney(dataList);
		dataMap.put("accumulatedMoney", accumulatedMoney);
		return dataMap;
	}

	/**
	 * 计算总价
	 */
	private BigDecimal sumMoney(List<FcOrderLog> dataList) {
		// 汇率集合
		Map<String, BigDecimal> addressRateMap = payTokenService.selectAddressAndRate();
		// 汇率
		BigDecimal rate = new BigDecimal("0");
		// 总价
		BigDecimal accumulatedMoney = new BigDecimal("0");
		FcOrder parse;
		Long sells;
		for (FcOrderLog orderLog : dataList) {
			parse = JSON.parseObject(orderLog.getContent(), FcOrder.class);
			if (orderLog.getType() == 4) {
				rate = addressRateMap.get(parse.getBuyerToken().toLowerCase());
			} else if (orderLog.getType() == 8) {
				rate = addressRateMap.get(parse.getSellToken().toLowerCase());
			}
			sells = parse.getSells();
			if (sells == null) {
				sells = 0L;
			}
			accumulatedMoney = accumulatedMoney.add(new BigDecimal(parse.getBuyerValue())
					.divide(new BigDecimal(parse.getSellValue()), 3).multiply(new BigDecimal(sells)).multiply(rate));
		}
		return accumulatedMoney;
	}

	/**
	 * 查询所有的类别
	 *
	 */
	public List<String> getAllToken(Long staTime) {
		QueryWrapper<FcOrderLog> wrapper = new QueryWrapper<>();
		wrapper.ge("log.update_time", staTime).lt("log.update_time", staTime + 24 * 60 * 60);
		return fcOrderExtMapper.getAll(wrapper);
	}
}
