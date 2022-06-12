package com.fingerchar.admin.service.event;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fingerchar.core.base.service.impl.BaseService;
import com.fingerchar.core.util.DappWeb3jUtil;
import com.fingerchar.db.domain.FcTxOrder;
import com.fingerchar.db.vo.EventValuesExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.datatypes.Event;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class baseEventService {

    @Autowired
    private BaseService baseService ;

    List<EventValuesExt> sortLog(List<EventValuesExt> list) {
        if (null == list) {
            return new ArrayList<>();
        }
        Collections.sort(list, new Comparator<EventValuesExt>() {
            public int compare(EventValuesExt val1, EventValuesExt val2) {
                return val1.getBlockNumber().compareTo(val2.getBlockNumber());
            }
        });
        return list;
    }

    List<EventValuesExt> filterValues(List<EventValuesExt> valueList, List<String> txList) {
        if(null == valueList) {
            return new ArrayList<>();
        }
        return valueList.stream().filter(value->!txList.contains(value.getTxHash())).collect(Collectors.toList());
    }

    List<String> getTxHashList(Set<String> hashList) {
        QueryWrapper<FcTxOrder> wrapper = new QueryWrapper<>();
        wrapper.in(FcTxOrder.TX_HASH, hashList);
        List<FcTxOrder> txList = this.baseService.findByCondition(FcTxOrder.class, wrapper);
        return txList.stream().map(FcTxOrder::getTxHash).collect(Collectors.toList());
    }

    List<EventValuesExt> getEventListByTopic(String filterTopic, String topic, Event event, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap){
        List<EthLog.LogResult> eventLogs = allLogs
                .stream()
                .filter(log-> ((Log) (log.get())).getTopics().contains(filterTopic))
                .collect(Collectors.toList());
        return this.parseEvent(eventLogs, topic, event, blockMap);
    }

    List<EventValuesExt> getEventList(String address, String topic, Event event, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) {
        String addressLowerCase = address.toLowerCase();
        //将allLogs过滤出此事件地址的log
        List<EthLog.LogResult> eventLogs = allLogs
                .stream()
                .filter(log->addressLowerCase.equals(((Log) (log.get())).getAddress()))
                .collect(Collectors.toList());
        return this.parseEvent(eventLogs, topic, event, blockMap);
    }

    List<EventValuesExt> getEventList(List<String> addressList, String topic, Event event, List<EthLog.LogResult> allLogs, Map<BigInteger, EthBlock.Block> blockMap) {
        Set<String> addressSet = addressList.stream().map(a -> a = a.toLowerCase()).collect(Collectors.toSet());
        //将allLogs过滤出此事件地址的log
        List<EthLog.LogResult> eventLogs = allLogs
                .stream()
                .filter(log->addressSet.contains(((Log) (log.get())).getAddress()))
                .collect(Collectors.toList());
        return this.parseEvent(eventLogs, topic, event, blockMap);
    }

    List<EventValuesExt> parseEvent(List<EthLog.LogResult> eventLogs, String topic, Event event, Map<BigInteger, EthBlock.Block> blockMap){
        if (eventLogs.isEmpty()) {
            return new ArrayList<EventValuesExt>();
        }
        //将log解析并过滤出此topic的log
        List<EventValuesExt> list = DappWeb3jUtil.decodeLog(eventLogs, topic, event, blockMap);
        if (list.isEmpty()) {
            return new ArrayList<EventValuesExt>();
        }
        //获得此事件log的所有txHash
        Set<String> TxHashSet = list.stream().map(log-> log.getTxHash()).collect(Collectors.toSet());

        //获得TxHashSet中已处理过得txHash
        List<String> txList = this.getTxHashList(TxHashSet);

        //根据txList过滤log得到本次需要处理的事件列表
        List<EventValuesExt> eventList = this.sortLog(this.filterValues(list,txList));
        return eventList;
    }

    List<String> filterInvalidContract(List<String> contracts) {
        Iterator<String> it = contracts.iterator();
        String contract = null;
        while(it.hasNext()) {
            contract = it.next();
            if(StringUtils.isEmpty(contract)) {
                it.remove();
            }else if(!DappWeb3jUtil.isValidAddress(contract)){
                it.remove();
            }
        }
        return contracts;
    }
}
