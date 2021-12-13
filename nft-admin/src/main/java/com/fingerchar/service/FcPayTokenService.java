package com.fingerchar.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fingerchar.base.service.IBaseService;
import com.fingerchar.domain.FcPayToken;
import com.fingerchar.utils.ResponseUtil;

@Service
public class FcPayTokenService {

	@Autowired
	private IBaseService baseService;

	public List<FcPayToken> findAll() {
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		return this.baseService.findByCondition(FcPayToken.class, wrapper);
	}

	// 封装address和汇率
	public Map<String, BigDecimal> selectAddressAndRate() {
		Map<String, BigDecimal> addressRateMap = new ConcurrentHashMap<>();
		List<FcPayToken> all = this.findAll();
		for (FcPayToken payToken : all) {
			addressRateMap.put(payToken.getAddress().toLowerCase(), payToken.getRate());
		}
		return addressRateMap;
	}

	/**
	 * @param address
	 * @return
	 */
	public Object delete(String address) {
		UpdateWrapper<FcPayToken> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcPayToken.ADDRESS, address);
		this.baseService.deleteByCondition(FcPayToken.class, wrapper);
		return ResponseUtil.ok();
	}

	/**
	 * @param address
	 * @return
	 */
	public Object enable(String address) {
		UpdateWrapper<FcPayToken> wrapper = new UpdateWrapper<>();
		wrapper.eq(FcPayToken.ADDRESS, address);
		wrapper.set(FcPayToken.DELETED, false);
		this.baseService.updateByCondition(FcPayToken.class, wrapper);
		return ResponseUtil.ok();
	}

	/**
	 * @param payToken
	 * @return
	 */
	public Object update(FcPayToken payToken) {
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		wrapper.eq(FcPayToken.ADDRESS, payToken.getAddress());
		FcPayToken temp = this.baseService.getByCondition(FcPayToken.class, wrapper);
		if(null != temp && !temp.getId().equals(payToken.getId())) {
			return ResponseUtil.fail(-1, "已有对应的支付币种");
		}
		if(null != payToken.getIsDefault() && payToken.getIsDefault() == 1) {
			wrapper = new QueryWrapper<>();
			wrapper.eq(FcPayToken.IS_DEFAULT, true);
			temp = this.baseService.getByCondition(FcPayToken.class, wrapper);
			if(null != temp && !temp.getId().equals(payToken.getId())) {
				return ResponseUtil.fail(-1, "已有默认的支付币种");
			}
		}
		if(null != payToken.getType() && payToken.getType() == 0) {
			wrapper = new QueryWrapper<>();
			wrapper.eq(FcPayToken.TYPE, 0);
			temp = this.baseService.getByCondition(FcPayToken.class, wrapper);
			if(null != temp && !temp.getId().equals(payToken.getId())) {
				return ResponseUtil.fail(-1, "已设置了主币支付币种");
			}
		}
		this.baseService.update(payToken);
		return ResponseUtil.ok();
	}
	
	public Object save(FcPayToken payToken) {
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		wrapper.eq(FcPayToken.ADDRESS, payToken.getAddress());
		FcPayToken temp = this.baseService.getByCondition(FcPayToken.class, wrapper);
		if(null != temp) {
			return ResponseUtil.fail(-1, "已有对应的支付币种");
		}
		if(null != payToken.getIsDefault() && payToken.getIsDefault() == 1) {
			wrapper = new QueryWrapper<>();
			wrapper.eq(FcPayToken.IS_DEFAULT, true);
			temp = this.baseService.getByCondition(FcPayToken.class, wrapper);
			if(null != temp) {
				return ResponseUtil.fail(-1, "已有默认的支付币种");
			}
		}
		if(null != payToken.getType() && payToken.getType() == 0) {
			wrapper = new QueryWrapper<>();
			wrapper.eq(FcPayToken.TYPE, 0);
			temp = this.baseService.getByCondition(FcPayToken.class, wrapper);
			if(null != temp) {
				return ResponseUtil.fail(-1, "已设置了主币支付币种");
			}
		}
		this.baseService.save(payToken);
		return ResponseUtil.ok();
	}
	
	public List<FcPayToken> findSync() {
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		wrapper.eq(FcPayToken.DELETED, false);
		return this.baseService.findByCondition(FcPayToken.class, wrapper);
	}
}
