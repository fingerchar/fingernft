package com.fingerchar.api.util;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.EthLog.LogResult;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;

import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.FcSystemConfigService;
import com.fingerchar.api.vo.EventValuesExt;
import com.fingerchar.core.util.SpringContextUtil;

public class NftDappEventUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(NftDappEventUtils.class);

	public static final Event BUY_EVENT = new Event("Buy", Arrays.asList(new TypeReference<Address>(true) {
	}, new TypeReference<Uint256>(true) {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Address>() {
	}, new TypeReference<Address>() {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Address>() {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Uint256>() {
	}));
	public static final Event CANCEL_EVENT = new Event("Cancel", Arrays.asList(new TypeReference<Address>(true) {
	}, new TypeReference<Uint256>(true) {
	}, new TypeReference<Address>() {
	}, new TypeReference<Address>() {
	}, new TypeReference<Uint256>() {
	}, new TypeReference<Uint256>() {
	}));

	public static final Event TRANSFER_EVENT = new Event("Transfer", Arrays.asList(new TypeReference<Address>(true) {
	}, new TypeReference<Address>(true) {
	}, new TypeReference<Uint256>(true) {
	}));

	public static final Event SECONDARYSALEFEES_EVENT = new Event("SecondarySaleFees",
			Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {
			}, new TypeReference<DynamicArray<Address>>() {
			}, new TypeReference<DynamicArray<Uint256>>() {
			}));;

	public static final String TRANSFER_TOPIC = EventEncoder.encode(TRANSFER_EVENT);

	public static final String CANCEL_TOPIC = EventEncoder.encode(CANCEL_EVENT);

	public static final String BUY_TOPIC = EventEncoder.encode(BUY_EVENT);

	public static final String SECONDARYSALEFEES_TOPIC = EventEncoder.encode(SECONDARYSALEFEES_EVENT);
	
	private static Web3j web3j = null;

	private static HttpService service = null;

	@SuppressWarnings("serial")
	public static final HashMap<String, Event> eventMap = new HashMap<String, Event>() {
		{
			put(TRANSFER_TOPIC, TRANSFER_EVENT);
			put(CANCEL_TOPIC, CANCEL_EVENT);
			put(BUY_TOPIC, BUY_EVENT);
			put(SECONDARYSALEFEES_TOPIC, SECONDARYSALEFEES_EVENT);
		}
	};
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, List<EventValuesExt>> decodeLog( List<LogResult> logList, Map<String, Event> eventMap) {
		Map<String, List<EventValuesExt>> map = new HashMap<>();
		if (null != logList && !logList.isEmpty()) {
			logList.stream().forEach(log-> {
				eventMap.keySet().stream().forEach(topic-> {
					if(((Log) log.get()).getTopics().contains(topic)) {
						
						if(null == map.get(eventMap.get(topic).getName())) {
							map.put(eventMap.get(topic).getName(), new ArrayList<>());
						}
						map.get(eventMap.get(topic).getName()).add(decodeLog(log, eventMap.get(topic)));
					}
				});
			});
		}
		return map;
	}
	
	public EventValuesExt decodeLog(LogResult<Log> logResult, Event event) {
		EventValues eventValues = Contract.staticExtractEventParameters(event, logResult.get());
		Web3j web3j = this.initWeb3j();
		BigInteger timestamp;
		try {
			timestamp = web3j.ethGetBlockByHash(logResult.get().getBlockHash(), false).send().getBlock().getTimestamp();
		} catch (IOException e) {
			throw new RuntimeException("get block timestamp error");
		}
		EventValuesExt val = new EventValuesExt(eventValues, logResult.get().getTransactionHash(), logResult.get().getAddress(),
				logResult.get().getBlockNumber(), timestamp);
		return val;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<EventValuesExt> decodeLog(List<LogResult> logList, String topic, Event event) {
		List<EventValuesExt> list = new ArrayList<>();
		if (null != logList && !logList.isEmpty()) {
			logList.stream().forEach(log-> {
				if(((Log) log.get()).getTopics().contains(topic)) {
					list.add(decodeLog(log, event));
				}
			});
		}
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public List<LogResult> getEthLogs(BigInteger start, BigInteger end, List<String> address) throws InterruptedException, ExecutionException, IOException {
		List<LogResult> list = new ArrayList<>();
		List<LogResult> temp = null;
		for(String addr : address) {
			temp = getEthLogs(start, end, addr);
			if(null != temp && !temp.isEmpty()) {				
				list.addAll(temp);
			}
		}
		return list;
		
	}
	
	@SuppressWarnings("rawtypes")
	public List<LogResult> getEthLogs(BigInteger start, BigInteger end, String address) throws InterruptedException, ExecutionException, IOException {
		EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(start), new DefaultBlockParameterNumber(end),
				address);
		Web3j web3j = this.initWeb3j();
		EthLog log = web3j.ethGetLogs(filter).send();
		if(log.hasError()) {
			logger.error(log.getError().getMessage());
			throw new RuntimeException(log.getError().getMessage());
		} else {
			return log.getLogs();
		}
	}
	
	public BigInteger getLastBlock() throws InterruptedException, ExecutionException, IOException {
		Web3j web3j = this.initWeb3j();
		EthBlockNumber ebn = web3j.ethBlockNumber().send();
		if(ebn.hasError()) {
			throw new RuntimeException("get block number error");
		} else {
			return ebn.getBlockNumber();
		}
	}
	
	public HttpService getService() {
		if (service == null) {
			FcSystemConfigService configService = SpringContextUtil.getBean(FcSystemConfigService.class);
			String url = configService.get(SysConfConstant.CHAIN_API_URL);
			if (!StringUtils.isEmpty(url)) {
				logger.info(url);
				service = new HttpService(url);
			} else {
				logger.error("unset chain api url");
			}
		}
		return service;
	}

	public Web3j initWeb3j() {
		if (null == web3j) {
			return Web3j.build(getService());
		}
		return web3j;
	}
}
