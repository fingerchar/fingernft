package com.fingerchar.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "fc.keyprop")
@Configuration
public class KeyProperties {

	private static String keyPath;
	
	private static String rsaPubKey;
	
	private static String rsaPriKey;

	public static String getKeyPath() {
		return keyPath;
	}

	public void setKeyPath(String keyPath) {
		KeyProperties.keyPath = keyPath;
	}

	public static String getRsaPubKey() {
		return rsaPubKey;
	}

	public void setRsaPubKey(String rsaPubKey) {
		KeyProperties.rsaPubKey = rsaPubKey;
	}

	public static String getRsaPriKey() {
		return rsaPriKey;
	}

	public void setRsaPriKey(String rsaPriKey) {
		KeyProperties.rsaPriKey = rsaPriKey;
	}
}
