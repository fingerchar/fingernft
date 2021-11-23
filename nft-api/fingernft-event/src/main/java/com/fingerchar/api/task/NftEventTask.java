package com.fingerchar.api.task;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.EthLog.LogResult;

import com.fingerchar.api.constant.RedisConstant;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.FcContractService;
import com.fingerchar.api.service.FcRedisService;
import com.fingerchar.api.service.FcSystemConfigService;
import com.fingerchar.api.service.NftEventService;
import com.fingerchar.api.util.NftDappEventUtils;
import com.fingerchar.api.vo.EventValuesExt;
import com.fingerchar.core.base.service.IBaseService;
import com.fingerchar.core.util.ErrorMsgUtils;

@Configuration
@EnableScheduling
public class NftEventTask {

	private static final Logger logger = LoggerFactory.getLogger(NftEventTask.class);
	
	NftDappEventUtils eventUtils = new NftDappEventUtils();

	public static Boolean isProcessing = false;

	@Autowired
	FcRedisService redisService;

	@Autowired
	FcSystemConfigService configService;

	@Autowired
	FcContractService contractService;
	
	@Autowired
	NftEventService nftEventService;
	
	@Autowired
	IBaseService baseService;
	
	@Scheduled(cron = "*/6 * * * * ?")
    private void startProcessEvent() {
		if(isProcessing.booleanValue()) {
			logger.info("task is in processing status");
			return;
		}
		synchronized (isProcessing) {
			if(isProcessing.booleanValue()) {
				logger.info("task is in processing status");
				return;
			}
			isProcessing = true;			
		}
		logger.info("task starting");
		try {
			BigInteger start = getFromBlock().add(BigInteger.ONE);
	
			BigInteger end = eventUtils.getLastBlock().subtract(new BigInteger("2"));
			if(start.compareTo(end) >=0) {
				return;
			}
			
			if(end.subtract(start).longValue() > 999L) {
				end = start.add(BigInteger.valueOf(999L));
			}
			List<String> allAddr = this.getAllAddr();
			String nftExchangeAddr = this.getNftExchangeAddress();

			allAddr.add(nftExchangeAddr);
			@SuppressWarnings("rawtypes")
			List<LogResult> allLogs = eventUtils.getEthLogs(start, end, allAddr);
			Map<String, List<EventValuesExt>> map = eventUtils.decodeLog(allLogs, NftDappEventUtils.eventMap);
			nftEventService.processEvent(map, end.toString());
		} catch (Exception e) {
			ErrorMsgUtils.addErrorMsg("nft task", 4, e.toString());
			logger.error("nft event task error", e);
		} finally {
			isProcessing = false;
		}
    }

	private String getNftExchangeAddress() {
		Object temp = this.redisService.get(RedisConstant.SYS_CONFIG_PRE + SysConfConstant.NFT_EXCHANGE);
		if(null == temp) {
			return this.configService.get(SysConfConstant.NFT_EXCHANGE);
		} else {
			return (String) temp;
		}
	}
	
	private List<String> getAllAddr() {
		return this.contractService.findAllAddress();
	}

	private BigInteger getFromBlock() {
		Object temp = this.redisService.get(RedisConstant.SYS_CONFIG_PRE + SysConfConstant.LAST_BLOCK);
		if(null == temp) {
			String value = this.configService.get(SysConfConstant.LAST_BLOCK);
			if(StringUtils.isEmpty(value)) {
				return null;
			} else {
				return new BigInteger(value);
			}
		} else {
			return new BigInteger((String)temp);
		}
	}
}