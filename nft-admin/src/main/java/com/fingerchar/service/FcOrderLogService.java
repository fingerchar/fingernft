package com.fingerchar.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 *
 * @author zjm
 * */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcOrderLog;

@Service
public class FcOrderLogService {

	@Autowired
	private IBaseService baseService;

	/**
	 * 查询转手的nft
	 * 
	 * @param address
	 * @param staTime
	 * @return
	 */
	public List<FcOrderLog> findByToken(List<String> address, Long staTime) {
		QueryWrapper<FcOrderLog> wrapper = new QueryWrapper<>();
		if (null == address || address.size() > 0) {
			return new ArrayList<>();
		}
		wrapper.in(FcOrderLog.TOKEN, address);
		wrapper.ge(FcOrderLog.UPDATE_TIME, staTime).lt(FcOrderLog.UPDATE_TIME, staTime + 24 * 60 * 60);
		return baseService.findByCondition(FcOrderLog.class, wrapper);
	}

	/**
	 * 将token和数据做成map集合
	 * 
	 * @param data
	 * @return
	 */
	public Map<String, List<FcOrderLog>> packToken(List<FcOrderLog> data) {
		Map<String, List<FcOrderLog>> result = new HashMap<>();
		if (null == data) {
			return new HashMap<>();
		}
		data.stream().forEach(log -> {
			if (null == result.get(log.getToken().toLowerCase())) {
				result.put(log.getToken().toLowerCase(), new ArrayList<>());
			}
			result.get(log.getToken().toLowerCase()).add(log);
		});
		return result;
	}
}
