package com.fingerchar.admin.service.event;

import com.fingerchar.core.constant.ContractType;
import com.fingerchar.core.manager.FcContractNftManager;
import com.fingerchar.core.util.DappEventUtils;
import com.fingerchar.db.dto.TransferLog;
import com.fingerchar.db.vo.EventValuesExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class Erc721EventService extends baseEventService{

	private static final Logger logger = LoggerFactory.getLogger(Erc721EventService.class);

    @Autowired
    FcContractNftManager contractNftManager;
	
	/**
     * @param addressList 合约地址列表
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public List<EventValuesExt> processTransferEvent(
            List<String> addressList,
            List<EthLog.LogResult> allLogs,
            Map<BigInteger, EthBlock.Block> blockMap
    ) throws Exception {
        //根据事件的合约地址与topic过滤logs
        List<EventValuesExt> valueList = this.getEventList(addressList, DappEventUtils.TRANSFER_TOPIC, DappEventUtils.TRANSFER_EVENT, allLogs, blockMap);
        if (valueList.isEmpty()) {
            return valueList;
        }
        for(EventValuesExt value: valueList){
            this.processTransferEvent(value);
        }
        return valueList;
    }

    /**
     * @param eventValues 日志信息
     * @throws Exception
     */
    private void processTransferEvent(EventValuesExt eventValues) throws Exception {
        logger.info("721转移事件");
        String from = (String) eventValues.getIndexedValues().get(0).getValue();
        String to = (String) eventValues.getIndexedValues().get(1).getValue();
        BigInteger tokenId = (BigInteger) eventValues.getIndexedValues().get(2).getValue();
        TransferLog log = new TransferLog(eventValues.getAddress(), tokenId, from, to, eventValues.getTxHash(), eventValues.getBlockTimestamp(), eventValues.getBlockNumber().longValue());
        contractNftManager.transfer(log);
    }

}
