package com.fingerchar.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "fc.errorlog")
public class ErrorMsgProperties {

	private static String projectName;
	
	private static String logSystemUrl;
	
	private static String hostname;

	public static String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		ErrorMsgProperties.projectName = projectName;
	}

	public static String getLogSystemUrl() {
		return logSystemUrl;
	}

	public void setLogSystemUrl(String logSystemUrl) {
		ErrorMsgProperties.logSystemUrl = logSystemUrl;
	}

	public static String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		ErrorMsgProperties.hostname = hostname;
	}
}
