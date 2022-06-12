package com.fingerchar.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.constant.SysConfConstant;
import com.fingerchar.core.manager.FcPayTokenManager;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcPayToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FcPayTokenService {

	@Autowired
	private IBaseService baseService;

	@Autowired
    private FcPayTokenManager payTokenManager;


	public IPage<FcPayToken> list(IPage<FcPayToken> page){
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		wrapper.orderByDesc(BaseEntity.ID);
		return this.baseService.findByPage(FcPayToken.class, wrapper, page);
	}

	public List<FcPayToken> findAll() {
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		wrapper.orderByDesc(BaseEntity.ID);
		return this.baseService.findByCondition(FcPayToken.class, wrapper);
	}


	/**
	 * @param address
	 * @return
	 */
	public Object delete(String address) {
		this.payTokenManager.delete(address);
		return ResponseUtil.ok();
	}


	/**
	 * @param payToken
	 * @return
	 */
	public Object update(FcPayToken payToken) {
		if(payToken.getType() != 0 && !DappWeb3jUtil.isValidAddress(payToken.getAddress())){
			return ResponseUtil.fail(-1, "address is incorrect");
		}
		if(hasPayToken(payToken)){
			return ResponseUtil.fail(-1, "已有对应的支付币种");
		}
		if(!checkDefaultPayToken(payToken)){
			return ResponseUtil.fail(-1, "已有默认的支付币种");
		}
		if(!checkTypePayToken(payToken)){
			return ResponseUtil.fail(-1, "已设置了主币支付币种");
		}

		payToken = this.payTokenManager.getPayTokenInfo(payToken);
		if(null == payToken){
			return ResponseUtil.fail(-1, "update paytoken fail");
		}
		this.payTokenManager.update(payToken);
		return ResponseUtil.ok();
	}
	
	public Object save(FcPayToken payToken) {
		if(payToken.getType() != 0 && !DappWeb3jUtil.isValidAddress(payToken.getAddress())){
			return ResponseUtil.fail(-1, "address is incorrect");
		}
		if(hasPayToken(payToken)){
			return ResponseUtil.fail(-1, "已有对应的支付币种");
		}
		if(!checkDefaultPayToken(payToken)){
			return ResponseUtil.fail(-1, "已有默认的支付币种");
		}
		if(!checkTypePayToken(payToken)){
			return ResponseUtil.fail(-1, "已设置了主币支付币种");
		}
		payToken = this.payTokenManager.getPayTokenInfo(payToken);
		if(null == payToken){
			return ResponseUtil.fail(-1, "save paytoken fail");
		}
		this.payTokenManager.save(payToken);
		return ResponseUtil.ok();
	}

	private Boolean hasPayToken(FcPayToken payToken){
		FcPayToken temp = this.payTokenManager.get(payToken.getAddress());
		if(null != temp && !temp.getId().equals(payToken.getId())){
			return true;
		}
		return false;
	}

	private Boolean checkDefaultPayToken(FcPayToken payToken){
		if(null != payToken.getIsDefault() && payToken.getIsDefault() == 1){
			FcPayToken token = this.payTokenManager.getDefault();
			if(null != token && !token.getId().equals(payToken.getId())){
				return false;
			}
		}
		return true;
	}

	private Boolean checkTypePayToken(FcPayToken payToken){
		if(null != payToken.getType() && payToken.getType() == 0){
			FcPayToken token = this.payTokenManager.getETH();
			if(null != token && !token.getId().equals(payToken.getId())){
				return false;
			}
		}
		return true;
	}
}
