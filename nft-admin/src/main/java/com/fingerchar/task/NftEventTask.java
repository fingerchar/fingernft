package com.fingerchar.task;

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

import com.fingerchar.base.service.IBaseService;
import com.fingerchar.constant.SysConfConstant;
import com.fingerchar.service.FcContractService;
import com.fingerchar.service.FcSystemConfigService;
import com.fingerchar.service.NftEventService;
import com.fingerchar.utils.NftDappEventUtils;
import com.fingerchar.vo.EventValuesExt;

@Configuration
@EnableScheduling
public class NftEventTask {

	private static final Logger logger = LoggerFactory.getLogger(NftEventTask.class);
	
	public static Boolean isProcessing = false;

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
			BigInteger from = getFromBlock();
			if(null == from) {
				logger.warn("未配置起始块高度");
				return;
			}
			BigInteger start = from.add(BigInteger.ONE);
	
			BigInteger end = NftDappEventUtils.getLastBlock().subtract(new BigInteger("12"));
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
			List<LogResult> allLogs = NftDappEventUtils.getEthLogs(start, end, allAddr);
			Map<String, List<EventValuesExt>> map = NftDappEventUtils.decodeLog(allLogs, NftDappEventUtils.eventMap);
			nftEventService.processEvent(map, end.toString());
		} catch (Exception e) {
			logger.error("nft event task error", e);
		} finally {
			isProcessing = false;
		}
    }

	private String getNftExchangeAddress() {
		return this.configService.get(SysConfConstant.NFT_EXCHANGE);
	}
	
	private List<String> getAllAddr() {
		return this.contractService.findAllAddress();
	}

	private BigInteger getFromBlock() {
		String value = this.configService.get(SysConfConstant.LAST_BLOCK);
		if(StringUtils.isEmpty(value)) {
			return null;
		} else {
			return new BigInteger(value);
		}
	}
}
