package com.fingerchar.core.util;


import com.alibaba.fastjson.JSONObject;
import com.fingerchar.core.storage.IpfsStorage;
import com.fingerchar.core.util.contract.ERC1155;
import com.fingerchar.core.util.contract.ERC20;
import com.fingerchar.core.util.contract.ERC721;
import com.fingerchar.db.vo.ERCTokenInfo;
import com.fingerchar.db.vo.EventValuesExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.web3j.abi.EventValues;
import org.web3j.abi.datatypes.Event;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.BatchRequest;
import org.web3j.protocol.core.BatchResponse;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class DappWeb3jUtil {

    private static final Logger logger = LoggerFactory.getLogger(DappWeb3jUtil.class);

    public static Web3j web3j;

    public static void initWeb3j(String url) {
        web3j = Web3j.build(new HttpService(url));
    }

    public static ERCTokenInfo getErc20Info(String address) throws Exception {
        TransactionManager transactionManager = new ClientTransactionManager(web3j, address);
        ERC20 erc20 = ERC20.load(address, web3j, transactionManager, new DefaultGasProvider());
        ERCTokenInfo info = new ERCTokenInfo();
        String symbol = erc20.symbol().sendAsync().get();
        info.setContractSymbol(symbol);
        String name = erc20.name().sendAsync().get();
        info.setContractName(name);
        BigInteger deceimals = erc20.decimals().sendAsync().get();
        info.setContractDecimals(deceimals.intValue());
        return info;
    }

    public static Boolean isValidAddress(String address){
        return WalletUtils.isValidAddress(address);
    }


    public static String getErc721Uri(String token, String tokenId) {
        TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
        ERC721 contract721 = ERC721.load(token, web3j, transactionManager, new DefaultGasProvider());
        try {
            return contract721.tokenURI(new BigInteger(tokenId)).sendAsync().get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error("获取721uri异常", e);
            return null;
        }
    }

    public static ERCTokenInfo processNftUri(String uri) throws IOException {
        if (null != uri) {
            ERCTokenInfo info = new ERCTokenInfo();
            info.setUri(uri);
            if (uri.toLowerCase().startsWith("ipfs:/")) {
                String[] arr = uri.split("/");
                uri = arr[arr.length - 1];
                if (uri.length() == 46) {
                    String tokenInfoStr = null;
                    try {
                        tokenInfoStr = IpfsStorage.getIpfsData(uri);
                    } catch (Exception e) {
                        logger.error("获取ipfs信息异常", e);
                        return null;
                    }
                    if (null != tokenInfoStr) {
                        info.setContent(tokenInfoStr);
                        try {
                            JSONObject obj = JSONObject.parseObject(tokenInfoStr);
                            info.setProperties(obj.getString("attributes"));
                            info.setName(obj.getString("name"));
                            info.setDescription(obj.getString("description"));
                        } catch (Exception e) {
                            logger.info("获取到的ipfs非json数据=>" + uri);
                        }
                    }
                }
            } else if (uri.toLowerCase().startsWith("http")) {
                String content = HttpUtils.post(uri);
                if (StringUtils.isEmpty(content)) {
                    info.setContent("");
                } else {
                    info.setContent(content);
                    try {
                        JSONObject obj = JSONObject.parseObject(content);
                        info.setProperties(obj.getString("attributes"));
                        info.setName(obj.getString("name"));
                        info.setDescription(obj.getString("description"));
                    } catch (Exception e) {
                        logger.info("获取到的ipfs非json数据=>" + uri);
                    }
                }
            }
            return info;
        }
        return null;
    }

    public static List<EthBlock.Block> getBlockList(BigInteger start, BigInteger end) throws Exception{
        return DappWeb3jUtil.getBlockList(start, end, false);
    }

    public static List<EthBlock.Block> getBlockList(BigInteger start, BigInteger end, Boolean full) throws Exception{
        BatchRequest batchRequest = web3j.newBatch();
        Integer _start = Integer.valueOf(start.toString());
        Integer _end = Integer.valueOf(end.toString());
        for(int i = _start; i < _end + 1; i++){
            DefaultBlockParameter param = DefaultBlockParameter.valueOf(BigInteger.valueOf(i));
            batchRequest.add(web3j.ethGetBlockByNumber(param, full));
        }
        List<EthBlock.Block> blockList;
        try {
            BatchResponse batchResponse = batchRequest.send();
            blockList = batchResponse.getResponses().stream().map(r -> ((EthBlock) r).getBlock())
                    .sorted(Comparator.comparing(b -> b.getNumber().longValue())).collect(Collectors.toList());
        }catch (Exception e){
            throw new Exception(e);
        }
        return blockList;
    }


    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static List<EventValuesExt> decodeLog(List<EthLog.LogResult> logList, String topic, Event event, Map<BigInteger, EthBlock.Block> blockMap) {
        List<EventValuesExt> list = new ArrayList<>();
        if (null != logList && !logList.isEmpty()) {
            logList.stream().forEach(log-> {
                if(((Log) log.get()).getTopics().contains(topic)) {
                    list.add(decodeLog(log, event, blockMap));
                }
            });
        }
        return list;
    }

    private static EventValuesExt decodeLog(EthLog.LogResult<Log> logResult, Event event, Map<BigInteger, EthBlock.Block> blockMap) {
        EventValues eventValues = Contract.staticExtractEventParameters(event, logResult.get());
        EthBlock.Block block = blockMap.get(logResult.get().getBlockNumber());
        EventValuesExt val = new EventValuesExt(eventValues, logResult.get().getTransactionHash(), logResult.get().getAddress(),
                logResult.get().getBlockNumber(), block.getTimestamp());
        return val;
    }

    @SuppressWarnings("rawtypes")
    public static EthLog getEthLogs(BigInteger start, BigInteger end) throws InterruptedException, ExecutionException, IOException {
        EthFilter filter = new EthFilter(new DefaultBlockParameterNumber(start), new DefaultBlockParameterNumber(end),
                new ArrayList<>());
        EthLog log = web3j.ethGetLogs(filter).send();
        return log;
    }




    public static BigInteger getLastBlock() throws IOException {
        if(null == web3j){
            throw new RuntimeException("web3j is null");
        }
        EthBlockNumber ebn = web3j.ethBlockNumber().send();
        if(ebn.hasError()) {
            throw new RuntimeException("get block number error");
        } else {
            return ebn.getBlockNumber();
        }
    }

}
