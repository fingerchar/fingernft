package com.fingerchar.api.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.api.constant.ContractType;
import com.fingerchar.api.utils.TokenExchangeCompute;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.db.base.BaseEntity;
import com.fingerchar.db.domain.FcPayToken;

@Service
public class FcPayTokenService {

	@Autowired
	IBaseService baseService;

	public List<FcPayToken> findAll() {
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		wrapper.eq(BaseEntity.DELETED, false);
		return this.baseService.findByCondition(FcPayToken.class, wrapper);
	}

	//封装address和名称
	public Map<String,String> getAddrAndName(){
		Map<String,String> addressNameMap = new ConcurrentHashMap<>();
		List<FcPayToken> all = this.findAll();
		for (FcPayToken payToken : all) {
			addressNameMap.put(payToken.getAddress().toLowerCase(), payToken.getName());
		}
		return addressNameMap;
	}
	
	public String getErcValue(Integer type, String value, String address) {
		ContractType ctype = ContractType.getContractType(type);
		String result = null;
		switch(ctype) {
			case ETH:
			case ERC20:
				FcPayToken token = this.getPayToken(address);
				if(null == token) {
					return "";
				}
				result = TokenExchangeCompute.computeErc20OrEth(value, token.getDecimals());
				if(result.indexOf(".") > -1) {
					result = result.substring(0, result.indexOf("."));
				}
				break;
			case ERC1155:
				result = value;
				break;
			case ERC721:
				result = value;
				break;
			case ERC721Deprecated:
				result = value;
				break;
			default:
				result = null;
		}
		return result;
	}

	public FcPayToken getPayToken(String address) {
		QueryWrapper<FcPayToken> wrapper = new QueryWrapper<>();
		wrapper.eq(FcPayToken.ADDRESS, address)
			.eq(BaseEntity.DELETED, false);
		return this.baseService.getByCondition(FcPayToken.class, wrapper);
	}

}
