package com.fingerchar.api.utils.contract;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class NftExchange extends Contract {
    public static final String BINARY = "";

    public static final String FUNC_BENEFICIARY = "beneficiary";

    public static final String FUNC_BUYERFEESIGNER = "buyerFeeSigner";

    public static final String FUNC_ERC20TRANSFERPROXY = "erc20TransferProxy";

    public static final String FUNC_ORDERSHOLDER = "ordersHolder";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_RENOUNCEOWNERSHIP = "renounceOwnership";

    public static final String FUNC_STATE = "state";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final String FUNC_TRANSFERPROXY = "transferProxy";

    public static final String FUNC_TRANSFERPROXYFORDEPRECATED = "transferProxyForDeprecated";

    public static final String FUNC_SETBENEFICIARY = "setBeneficiary";

    public static final String FUNC_SETBUYERFEESIGNER = "setBuyerFeeSigner";

    public static final String FUNC_EXCHANGE = "exchange";

    public static final String FUNC_CANCEL = "cancel";

    public static final String FUNC_PREPAREBUYERFEEMESSAGE = "prepareBuyerFeeMessage";

    public static final String FUNC_PREPAREMESSAGE = "prepareMessage";

    public static final Event BUY_EVENT = new Event("Buy", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event CANCEL_EVENT = new Event("Cancel", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Uint256>(true) {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>(true) {}, new TypeReference<Address>(true) {}));
    ;

    @Deprecated
    protected NftExchange(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NftExchange(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected NftExchange(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected NftExchange(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<BuyEventResponse> getBuyEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BUY_EVENT, transactionReceipt);
        ArrayList<BuyEventResponse> responses = new ArrayList<BuyEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BuyEventResponse typedResponse = new BuyEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sellToken = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.sellTokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.sellValue = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.buyToken = (String) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.buyValue = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.buyer = (String) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            typedResponse.salt = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BuyEventResponse> buyEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, BuyEventResponse>() {
            @Override
            public BuyEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BUY_EVENT, log);
                BuyEventResponse typedResponse = new BuyEventResponse();
                typedResponse.log = log;
                typedResponse.sellToken = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.sellTokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.sellValue = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.buyToken = (String) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.buyValue = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.buyer = (String) eventValues.getNonIndexedValues().get(5).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
                typedResponse.salt = (BigInteger) eventValues.getNonIndexedValues().get(7).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BuyEventResponse> buyEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BUY_EVENT));
        return buyEventFlowable(filter);
    }

    public List<CancelEventResponse> getCancelEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(CANCEL_EVENT, transactionReceipt);
        ArrayList<CancelEventResponse> responses = new ArrayList<CancelEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CancelEventResponse typedResponse = new CancelEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sellToken = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.sellTokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.buyToken = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.salt = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CancelEventResponse> cancelEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, CancelEventResponse>() {
            @Override
            public CancelEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(CANCEL_EVENT, log);
                CancelEventResponse typedResponse = new CancelEventResponse();
                typedResponse.log = log;
                typedResponse.sellToken = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.sellTokenId = (BigInteger) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.owner = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.buyToken = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.buyTokenId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.salt = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CancelEventResponse> cancelEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(CANCEL_EVENT));
        return cancelEventFlowable(filter);
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<OwnershipTransferredEventResponse> ownershipTransferredEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventFlowable(filter);
    }

    public RemoteFunctionCall<String> beneficiary() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BENEFICIARY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> buyerFeeSigner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_BUYERFEESIGNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> erc20TransferProxy() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ERC20TRANSFERPROXY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> ordersHolder() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ORDERSHOLDER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> owner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_OWNER, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> renounceOwnership() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_RENOUNCEOWNERSHIP, 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> state() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_STATE, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> transferOwnership(String newOwner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_TRANSFEROWNERSHIP, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newOwner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> transferProxy() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSFERPROXY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> transferProxyForDeprecated() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSFERPROXYFORDEPRECATED, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> setBeneficiary(String newBeneficiary) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETBENEFICIARY, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newBeneficiary)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setBuyerFeeSigner(String newBuyerFeeSigner) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETBUYERFEESIGNER, 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, newBuyerFeeSigner)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> exchange(Order order, Sig sig, BigInteger buyerFee, Sig buyerFeeSig, BigInteger amount, String buyer, BigInteger weiValue) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_EXCHANGE, 
                Arrays.<Type>asList(order, 
                sig, 
                new org.web3j.abi.datatypes.generated.Uint256(buyerFee), 
                buyerFeeSig, 
                new org.web3j.abi.datatypes.generated.Uint256(amount), 
                new org.web3j.abi.datatypes.Address(160, buyer)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteFunctionCall<TransactionReceipt> cancel(OrderKey key) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_CANCEL, 
                Arrays.<Type>asList(key), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> prepareBuyerFeeMessage(Order order, BigInteger fee) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PREPAREBUYERFEEMESSAGE, 
                Arrays.<Type>asList(order, 
                new org.web3j.abi.datatypes.generated.Uint256(fee)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<String> prepareMessage(Order order) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_PREPAREMESSAGE, 
                Arrays.<Type>asList(order), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    @Deprecated
    public static NftExchange load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NftExchange(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static NftExchange load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NftExchange(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static NftExchange load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new NftExchange(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static NftExchange load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new NftExchange(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<NftExchange> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _transferProxy, String _transferProxyForDeprecated, String _erc20TransferProxy, String _state, String _ordersHolder, String _beneficiary, String _buyerFeeSigner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _transferProxy), 
                new org.web3j.abi.datatypes.Address(160, _transferProxyForDeprecated), 
                new org.web3j.abi.datatypes.Address(160, _erc20TransferProxy), 
                new org.web3j.abi.datatypes.Address(160, _state), 
                new org.web3j.abi.datatypes.Address(160, _ordersHolder), 
                new org.web3j.abi.datatypes.Address(160, _beneficiary), 
                new org.web3j.abi.datatypes.Address(160, _buyerFeeSigner)));
        return deployRemoteCall(NftExchange.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<NftExchange> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _transferProxy, String _transferProxyForDeprecated, String _erc20TransferProxy, String _state, String _ordersHolder, String _beneficiary, String _buyerFeeSigner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _transferProxy), 
                new org.web3j.abi.datatypes.Address(160, _transferProxyForDeprecated), 
                new org.web3j.abi.datatypes.Address(160, _erc20TransferProxy), 
                new org.web3j.abi.datatypes.Address(160, _state), 
                new org.web3j.abi.datatypes.Address(160, _ordersHolder), 
                new org.web3j.abi.datatypes.Address(160, _beneficiary), 
                new org.web3j.abi.datatypes.Address(160, _buyerFeeSigner)));
        return deployRemoteCall(NftExchange.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<NftExchange> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _transferProxy, String _transferProxyForDeprecated, String _erc20TransferProxy, String _state, String _ordersHolder, String _beneficiary, String _buyerFeeSigner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _transferProxy), 
                new org.web3j.abi.datatypes.Address(160, _transferProxyForDeprecated), 
                new org.web3j.abi.datatypes.Address(160, _erc20TransferProxy), 
                new org.web3j.abi.datatypes.Address(160, _state), 
                new org.web3j.abi.datatypes.Address(160, _ordersHolder), 
                new org.web3j.abi.datatypes.Address(160, _beneficiary), 
                new org.web3j.abi.datatypes.Address(160, _buyerFeeSigner)));
        return deployRemoteCall(NftExchange.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<NftExchange> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _transferProxy, String _transferProxyForDeprecated, String _erc20TransferProxy, String _state, String _ordersHolder, String _beneficiary, String _buyerFeeSigner) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _transferProxy), 
                new org.web3j.abi.datatypes.Address(160, _transferProxyForDeprecated), 
                new org.web3j.abi.datatypes.Address(160, _erc20TransferProxy), 
                new org.web3j.abi.datatypes.Address(160, _state), 
                new org.web3j.abi.datatypes.Address(160, _ordersHolder), 
                new org.web3j.abi.datatypes.Address(160, _beneficiary), 
                new org.web3j.abi.datatypes.Address(160, _buyerFeeSigner)));
        return deployRemoteCall(NftExchange.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class Asset extends StaticStruct {
        public String token;

        public BigInteger tokenId;

        public BigInteger assetType;

        public Asset(String token, BigInteger tokenId, BigInteger assetType) {
            super(new org.web3j.abi.datatypes.Address(token),new org.web3j.abi.datatypes.generated.Uint256(tokenId),new org.web3j.abi.datatypes.generated.Uint8(assetType));
            this.token = token;
            this.tokenId = tokenId;
            this.assetType = assetType;
        }

        public Asset(Address token, Uint256 tokenId, Uint8 assetType) {
            super(token,tokenId,assetType);
            this.token = token.getValue();
            this.tokenId = tokenId.getValue();
            this.assetType = assetType.getValue();
        }
    }

    public static class Sig extends StaticStruct {
        public BigInteger v;

        public byte[] r;

        public byte[] s;

        public Sig(BigInteger v, byte[] r, byte[] s) {
            super(new org.web3j.abi.datatypes.generated.Uint8(v),new org.web3j.abi.datatypes.generated.Bytes32(r),new org.web3j.abi.datatypes.generated.Bytes32(s));
            this.v = v;
            this.r = r;
            this.s = s;
        }

        public Sig(Uint8 v, Bytes32 r, Bytes32 s) {
            super(v,r,s);
            this.v = v.getValue();
            this.r = r.getValue();
            this.s = s.getValue();
        }
    }

    public static class OrderKey extends StaticStruct {
        public String owner;

        public BigInteger salt;

        public Asset sellAsset;

        public Asset buyAsset;

        public OrderKey(String owner, BigInteger salt, Asset sellAsset, Asset buyAsset) {
            super(new org.web3j.abi.datatypes.Address(owner),new org.web3j.abi.datatypes.generated.Uint256(salt),sellAsset,buyAsset);
            this.owner = owner;
            this.salt = salt;
            this.sellAsset = sellAsset;
            this.buyAsset = buyAsset;
        }

        public OrderKey(Address owner, Uint256 salt, Asset sellAsset, Asset buyAsset) {
            super(owner,salt,sellAsset,buyAsset);
            this.owner = owner.getValue();
            this.salt = salt.getValue();
            this.sellAsset = sellAsset;
            this.buyAsset = buyAsset;
        }
    }

    public static class Order extends StaticStruct {
        public OrderKey key;

        public BigInteger selling;

        public BigInteger buying;

        public BigInteger sellerFee;

        public Order(OrderKey key, BigInteger selling, BigInteger buying, BigInteger sellerFee) {
            super(key,new org.web3j.abi.datatypes.generated.Uint256(selling),new org.web3j.abi.datatypes.generated.Uint256(buying),new org.web3j.abi.datatypes.generated.Uint256(sellerFee));
            this.key = key;
            this.selling = selling;
            this.buying = buying;
            this.sellerFee = sellerFee;
        }

        public Order(OrderKey key, Uint256 selling, Uint256 buying, Uint256 sellerFee) {
            super(key,selling,buying,sellerFee);
            this.key = key;
            this.selling = selling.getValue();
            this.buying = buying.getValue();
            this.sellerFee = sellerFee.getValue();
        }
    }

    public static class BuyEventResponse extends BaseEventResponse {
        public String sellToken;

        public BigInteger sellTokenId;

        public BigInteger sellValue;

        public String owner;

        public String buyToken;

        public BigInteger buyTokenId;

        public BigInteger buyValue;

        public String buyer;

        public BigInteger amount;

        public BigInteger salt;
    }

    public static class CancelEventResponse extends BaseEventResponse {
        public String sellToken;

        public BigInteger sellTokenId;

        public String owner;

        public String buyToken;

        public BigInteger buyTokenId;

        public BigInteger salt;
    }

    public static class OwnershipTransferredEventResponse extends BaseEventResponse {
        public String previousOwner;

        public String newOwner;
    }
}
