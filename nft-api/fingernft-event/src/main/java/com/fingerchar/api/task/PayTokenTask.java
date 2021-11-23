package com.fingerchar.api.task;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fingerchar.api.service.FcPayTokenService;
import com.fingerchar.api.util.HttpUtils;
import com.fingerchar.api.vo.RateVo;
import com.fingerchar.db.domain.FcPayToken;

//@Component
public class PayTokenTask {
	private static final Log logger = LogFactory.getLog(PayTokenTask.class);

	@Autowired
	private FcPayTokenService payTokenService;

	//private static final String _URL = "https://api.coincap.io/v2/rates/binance-coin";
	private static final String _URL = "https://dncapi.bqiapp.com/api/coin/web-coinrank";

	/**
	 * 每隔一个分钟同步检查
	 */
	//@Scheduled(fixedDelay = 1 * 60 * 1000)
	public void updateRate() {
		logger.info("start get rate");
		
		String result = null;;
		try {
			result = HttpUtils.post(_URL);
		} catch (IOException e) {
			result = null;
		}
		if(!StringUtils.isEmpty(result)) {
			JSONObject obj = JSON.parseObject(result);
			String data = obj.getString("data");
			List<RateVo> list = JSON.parseArray(data, RateVo.class);
			if(null != list && !list.isEmpty()) {
				List<FcPayToken> payTokenList = this.payTokenService.findAll();
				if(null != payTokenList && !payTokenList.isEmpty()) {
					payTokenList.stream().forEach(payToken-> {
						RateVo rate = list.stream().filter(vo->vo.getName().toLowerCase().equals(payToken.getName().toLowerCase()) || ("W" + vo.getName()).toLowerCase().equals(payToken.getName().toLowerCase())).findFirst().orElse(null);
						if(null != rate) {
							payToken.setRate(new BigDecimal(rate.getCurrent_price_usd()));
							this.payTokenService.updatePayToken(payToken);
						}
					});
				}
			}
		}
		logger.info("end get rate");
	}
}
