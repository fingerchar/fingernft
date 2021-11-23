package com.fingerchar.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.fingerchar.config.ErrorMsgProperties;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ErrorMsgUtils {

	private static final Logger logger = LoggerFactory.getLogger(ErrorMsgUtils.class);
	
	private static final  String NAME = "name";
	
	private static final String PROJECT_NAME = "projectName";
	
	private static final String LEVEL = "level";
	
	private static final String CONTENT = "content";
	
	private static final String MEDIA_TYPE = "application/json; charset=utf-8";

	public static void addErrorMsg(String name, Integer level, String content) {
		try {
			List<Map<String, Object>> list = new ArrayList<>();
			Map<String, Object> map = new HashMap<>(4);
			map.put(NAME, name);
			map.put(PROJECT_NAME, ErrorMsgProperties.getProjectName());
			map.put(LEVEL, level);
			map.put(CONTENT, content);
			list.add(map);
			Response resp = sendRequest(JSON.toJSONString(list));
			if(resp.isSuccessful()) {
				logger.info(resp.body().string());
			} else {
				logger.info("error log save error");
			}
		} catch (Exception e) {
			logger.error("发送错误日志异常", e);
		}
	}

	public static Response sendRequest(String params) throws IOException {
		logger.info(params);
		OkHttpClient client = new OkHttpClient.Builder().hostnameVerifier(new TrustLogSystemVerifier()).retryOnConnectionFailure(false)
				.readTimeout(30000, TimeUnit.MILLISECONDS).build();
		MediaType type = MediaType.parse(MEDIA_TYPE);
		RequestBody stringBody = RequestBody.Companion.create(params, type);
		Request request = new Request.Builder().url(ErrorMsgProperties.getLogSystemUrl()).post(stringBody).build();
		try {
			return client.newCall(request).execute();
		} catch (IOException e) {
			throw e;
		}
	}

	private static class TrustLogSystemVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			if(ErrorMsgProperties.getHostname().equals(hostname)) {
				return true;
			}
			return false;
		}
	}
}
