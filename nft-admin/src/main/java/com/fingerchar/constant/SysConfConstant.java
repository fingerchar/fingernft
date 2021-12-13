package com.fingerchar.constant;

import java.util.ArrayList;
import java.util.List;

public class SysConfConstant {

	public static final String IPFS_URL = "ipfsUrl";
	
	public static final String SELLER_FEE = "sellerFee";
	
	public static final String BUYER_FEE = "buyerFee";
	
	public static final String NFT721 = "NFT721";
	
	public static final String TRANSFER_PROXY = "TransferProxy";
	
	public static final String TRANSFER_PROXY_DEPRECATED = "TransferProxyForDeprecated";
	
	public static final String ERC20_TRANSFER_PROXY = "ERC20TransferProxy";
	
	public static final String NFT_EXCHANGE = "NftExchange";
	
	public static final String STATIC_PATH = "StaticPath";
	
	public static final String LAST_BLOCK = "LastBlock";
	
	public static final String WEB_SITE = "WebSite";
	
	public static final String NFT_DEFAULT_VERIFY = "NftDefaultVerify";
	
	public static final String CHAIN_API_URL = "ChainApiUrl";

	public static final String IPFS_SERVER_IP = "IpfsServerIp";
	
	public static final String IPFS_SERVER_PORT = "IpfsServerPort";
	
	public static final String IPFS_REMOTE_SERVER = "IpfsRemoteServer";
	
	public static final String STORAGE_LOCAL_PATH = "StorageLocalPath";
	
	public static final String STORAGE_REQUEST_BASE = "StorageRequestBase";
	
	public static final String MINT_KEY = "MintKey";
	
	public static final String TRANS_KEY = "TransKey";
	
	public static final List<String> REFRESH_KEYS = new ArrayList<String>() {/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{add(IPFS_SERVER_IP); add(IPFS_SERVER_PORT); add(IPFS_REMOTE_SERVER); add(STORAGE_LOCAL_PATH); add(STORAGE_REQUEST_BASE); add(MINT_KEY); add(TRANS_KEY); add(CHAIN_API_URL);}};

}
