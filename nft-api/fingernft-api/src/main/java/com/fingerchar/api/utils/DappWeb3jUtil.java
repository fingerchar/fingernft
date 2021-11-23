package com.fingerchar.api.utils;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ReadonlyTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import com.alibaba.fastjson.JSONObject;
import com.fingerchar.api.utils.contract.ERC721;
import com.fingerchar.api.utils.contract.Royalties;
import com.fingerchar.api.vo.ERCTokenInfo;
import com.fingerchar.core.storage.IpfsStorage;

public class DappWeb3jUtil {

	private static final Logger logger = LoggerFactory.getLogger(DappWeb3jUtil.class);
	
	public static final String SUPPORT_ROYALTIES_CODE = "0xb7799584";
	
	private static Web3j web3j;
	
	public static void initWeb3j(String url) {
		web3j = Web3j.build(new HttpService(url));
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
	
	public static String getName(String token) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		ERC721 contract721 = ERC721.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return contract721.name().sendAsync().get();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getSymbol(String token) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		ERC721 contract721 = ERC721.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return contract721.symbol().sendAsync().get();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static ERCTokenInfo processUri(String uri) throws IOException {
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

	/**
	 * @param token
	 * @param tokenId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<BigInteger> getRoyalties(String token, String tokenId) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		Royalties royalties = Royalties.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return royalties.getFeeBps(new BigInteger(tokenId)).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {	
			logger.error("获取版权信息异常", e);
			return null;
		}
	}

	/**
	 * @param token
	 * @return
	 */
	public static Boolean isSupportRoyalties(String token) {
		TransactionManager transactionManager = new ReadonlyTransactionManager(web3j, token);
		Royalties royalties = Royalties.load(token, web3j, transactionManager, new DefaultGasProvider());
		try {
			return royalties.supportsInterface(Numeric.hexStringToByteArray(SUPPORT_ROYALTIES_CODE)).sendAsync().get();
		} catch (InterruptedException | ExecutionException e) {	
			logger.error("获取版权信息异常", e);
			return false;
		}
	}
}
