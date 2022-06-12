package com.fingerchar.core.util;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

	private static final OkHttpClient client = new OkHttpClient.Builder()
			.sslSocketFactory(createSSLSocketFactory(), new TrustAllCerts())
			.hostnameVerifier(new TrustAllHostnameVerifier()).retryOnConnectionFailure(false)
			.readTimeout(30000, TimeUnit.MILLISECONDS).build();

	private static final MediaType type = MediaType.parse("application/json; charset=utf-8");

	public static String post(String url, String params) throws IOException {
		RequestBody stringBody = RequestBody.Companion.create(params, type);
		Request request = new Request.Builder().url(url).post(stringBody).build();
		try {
			Response resp = client.newCall(request).execute();
			if(resp.isSuccessful()) {
				return resp.body().string();
			} else {
				throw new IOException("okhttp3 post error");
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	public static String postForm(String url, Map<String, Object> params) throws IOException {
		FormBody.Builder builder = new FormBody.Builder();
		if(null != params && !params.isEmpty()) {
			Iterator<String> it = params.keySet().iterator();
			String paramName = null;
			while(it.hasNext()) {
				paramName = it.next();
				builder.add(paramName, params.get(paramName).toString());
			}
		}
		Request request = new Request.Builder().url(url).post(builder.build()).build();
		try {
			Response resp = client.newCall(request).execute();
			if(resp.isSuccessful()) {
				return resp.body().string();
			} else {
				throw new IOException("okhttp3 post error");
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	public static String post(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		try {
			Response resp = client.newCall(request).execute();
			if(resp.isSuccessful()) {
				return resp.body().string();
			} else {
				throw new IOException("okhttp3 post error");
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	public static String get(String url, String params) throws IOException {
		RequestBody stringBody = RequestBody.Companion.create(params, type);
		Request request = new Request.Builder().url(url).post(stringBody).build();
		try {
			Response resp = client.newCall(request).execute();
			if(resp.isSuccessful()) {
				return resp.body().string();
			} else {
				throw new IOException("okhttp3 get error");
			}
		} catch (IOException e) {
			throw e;
		}
	}
	
	public static String get(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();
		try {
			Response resp = client.newCall(request).execute();
			if(resp.isSuccessful()) {
				return resp.body().string();
			} else {
				throw new IOException("okhttp3 get error");
			}
		} catch (IOException e) {
			throw e;
		}
	}

	private static SSLSocketFactory createSSLSocketFactory() {
		SSLSocketFactory ssfFactory = null;
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, new TrustManager[] { new TrustAllCerts() }, new SecureRandom());
			ssfFactory = sc.getSocketFactory();
		} catch (Exception e) {
		}

		return ssfFactory;
	}

	private static class TrustAllCerts implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
	}

	private static class TrustAllHostnameVerifier implements HostnameVerifier {
		@Override
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
}
