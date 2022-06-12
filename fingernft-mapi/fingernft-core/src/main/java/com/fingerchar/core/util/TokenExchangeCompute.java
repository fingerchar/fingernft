package com.fingerchar.core.util;

import java.math.BigDecimal;

public class TokenExchangeCompute {

	/**
	 * 计算usdt价格
	 * @param fromPrice
	 * @param rate
	 * @return
	 */
	public static String computeUsdt(String fromPrice, BigDecimal rate) {
		BigDecimal bPrice = new BigDecimal(fromPrice);
		bPrice = bPrice.multiply(rate);
		return bPrice.toString();
	}
	
	/**
	 * 计算usdt价格 
	 * @param fromPrice
	 * @param rate
	 * @param hisTotal
	 * @param curTotal
	 * @return
	 */
	public static String computeUsdtTotal(String fromPrice, BigDecimal rate, Integer hisTotal, Integer curTotal) {
		BigDecimal bPrice = new BigDecimal(fromPrice);
		bPrice = bPrice.multiply(rate).multiply(new BigDecimal(curTotal)).divide(new BigDecimal(hisTotal));
		return bPrice.toString();
	}
	
	/**
	 * 计算usdt单价
	 * @param fromPrice
	 * @param rate
	 * @param hisTotal
	 * @return
	 */
	public static String computeUsdtSingle(String fromPrice, BigDecimal rate, Integer hisTotal) {
		BigDecimal bPrice = new BigDecimal(fromPrice);
		bPrice = bPrice.multiply(rate).divide(new BigDecimal(hisTotal));
		return bPrice.toString();
	}
	
	/**
	 * 计算其他币种价格
	 * @param fromPrice
	 * @param hisTotal
	 * @param curTotal
	 * @return
	 */
	public static String computeAnyTotal(String fromPrice, Integer hisTotal, Integer curTotal) {
		BigDecimal bPrice = new BigDecimal(fromPrice);
		bPrice = bPrice.multiply(new BigDecimal(curTotal)).divide(new BigDecimal(hisTotal));
		return bPrice.toString();
	}
	
	/**
	 * 计算ERC20或ETH最小单位值
	 * @param price
	 * @param decimal
	 * @return
	 */
	public static String computeErc20OrEth(String price, Integer decimal) {
		BigDecimal bPrice = new BigDecimal(price);
		bPrice = bPrice.multiply(new BigDecimal(10).pow(decimal));
		String result = bPrice.toBigInteger().toString();
		if(result.indexOf(".") > -1) {
			result = result.substring(0, result.indexOf("."));
		}
		return result;
	}
}
