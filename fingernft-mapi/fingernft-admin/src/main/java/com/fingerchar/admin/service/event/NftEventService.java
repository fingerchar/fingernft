package com.fingerchar.admin.service.event;

import com.fingerchar.admin.service.FcContractService;
import com.fingerchar.db.vo.EventValuesExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class NftEventService extends baseEventService{

    @Autowired
    Erc721EventService erc721EventService;

    @Autowired
    FcContractService contractService;

    public List<EventValuesExt> processEvent(List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) throws Exception{
        //获取以下事件相关的合约列表
        List<String> addressList = this.getAllAddr();
        addressList = this.filterInvalidContract(addressList);
        //721 transfer事件
        List<EventValuesExt> list = this.erc721EventService.processTransferEvent(addressList, allLogs, blockMap);
        return list;
    }

    private List<String> getAllAddr() {
        List<String> list = this.contractService.findAllAddress();
        return list;
    }
}
