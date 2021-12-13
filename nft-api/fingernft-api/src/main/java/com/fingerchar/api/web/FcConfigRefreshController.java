package com.fingerchar.api.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fingerchar.api.constant.SysConfConstant;
import com.fingerchar.api.service.StorageService;
import com.fingerchar.api.utils.DappCryptoUtil;
import com.fingerchar.api.utils.DappWeb3jUtil;
import com.fingerchar.core.base.controller.BaseController;
import com.fingerchar.core.storage.IpfsStorage;
import com.fingerchar.core.util.ResponseUtil;
import com.fingerchar.core.util.SpringContextUtil;

@RestController
@RequestMapping(SysConfConstant.URL_PREFIX + "/refresh")
public class FcConfigRefreshController extends BaseController {
	
	public static final Logger logger = LoggerFactory.getLogger(FcConfigRefreshController.class);
	
	@PostMapping("refreshconfig")
	public Object refreshConfig(@RequestBody String data) {
		if(StringUtils.isEmpty(data)) {
			return ResponseUtil.fail(-1, "refresh configs can not be empty");
		}
		JSONObject obj = JSON.parseObject(data);
		obj = obj.getJSONObject("data");
		DappCryptoUtil.setMintKey(obj.getString(SysConfConstant.MINT_KEY));
		DappCryptoUtil.setTransKey(obj.getString(SysConfConstant.TRANS_KEY));
		StorageService storageService = SpringContextUtil.getBean(StorageService.class);
		IpfsStorage storage = new IpfsStorage();
		storage.setHost(obj.getString(SysConfConstant.IPFS_SERVER_IP));
		storage.setPort(obj.getInteger(SysConfConstant.IPFS_SERVER_PORT));
		storage.setLoclLocation(obj.getString(SysConfConstant.STORAGE_LOCAL_PATH));
		storage.setRemoteService(obj.getString(SysConfConstant.IPFS_REMOTE_SERVER));
		storage.setRequestBase(obj.getString(SysConfConstant.STORAGE_REQUEST_BASE));
		storageService.setStorage(storage);
		DappWeb3jUtil.initWeb3j(obj.getString(SysConfConstant.CHAIN_API_URL));
		return ResponseUtil.ok();
	}
}
