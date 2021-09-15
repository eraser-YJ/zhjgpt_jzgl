package com.jc.common.kit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title http请求
 *
 * @author liubq
 */
public class HttpUtil {

	// 日志
	private final static Logger log = LoggerFactory.getLogger("largeTransport");

	/**
	 * @title 连接超时时间
	 *
	 * @return
	 */
	private static RequestConfig getConfig() {
		int timeOut = 1000 * 15;
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeOut).setConnectionRequestTimeout(timeOut).setSocketTimeout(timeOut).build();
		return config;
	}

	/**
	 * @title 发送FROM中只有jsonData的情况的数据
	 *
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String get(String url) throws Exception {
		Long start = System.currentTimeMillis();
		HttpGet httpget = new HttpGet(url);
		httpget.setConfig(getConfig());
		CloseableHttpClient httpclient = HttpClients.custom().setRetryHandler(new MyRetryHandler()).build();
		CloseableHttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String resultStr = EntityUtils.toString(entity, "UTF-8");
			log.debug("\r\nGet请求耗时：{}，地址：{}\r\n结果：{}", (System.currentTimeMillis() - start), url, resultStr);
			return resultStr;
		}
		return null;
	}

	/**
	 * @title 发送FROM中只有jsonData的情况的数据
	 *
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public static String postJsonData(String url, Map<String, Object> paramMap) throws Exception {
		Long start = System.currentTimeMillis();
		JSONObject jsonObject = new JSONObject();
		if (paramMap != null) {
			for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				jsonObject.put(entry.getKey(), entry.getValue());
			}
		}
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String paramStr = jsonObject.toString();
		list.add(new BasicNameValuePair("jsonData", paramStr));
		HttpPost httpRequst = new HttpPost(url);
		httpRequst.setConfig(getConfig());
		httpRequst.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httpRequst);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String resultStr = EntityUtils.toString(entity, "UTF-8");
			log.debug("\r\nPostJsonData耗时：{}， 地址：{}\r\n参数：{}\r\n结果：{}", (System.currentTimeMillis() - start), url, paramStr, resultStr);
			return resultStr;
		}
		return null;
	}

	//	/**
	//	 * @title 发送JSON
	//	 * 
	//	 * @param url
	//	 * @param paramMap
	//	 * @return
	//	 * @throws Exception
	//	 */
	//	public static String postJson(String url, Map<String, String> paramMap) throws Exception {
	//		JSONObject jsonObject = new JSONObject();
	//		if (paramMap != null) {
	//			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
	//				jsonObject.put(entry.getKey(), entry.getValue());
	//			}
	//		}
	//		HttpPost httpRequst = new HttpPost(url);
	//		httpRequst.setHeader("Content-Type", "application/json;charset=UTF-8");
	//		String parameter = jsonObject.toString();
	//		StringEntity se = new StringEntity(parameter);
	//		se.setContentType("text/json");
	//		httpRequst.setEntity(se);
	//		CloseableHttpClient httpclient = HttpClients.createDefault();
	//		CloseableHttpResponse response = httpclient.execute(httpRequst);
	//		HttpEntity entity = response.getEntity();
	//		if (entity != null) {
	//			String resultStr = EntityUtils.toString(entity, "UTF-8");
	//			log.debug("postJson地址：{}，参数：{}，结果：{}", url, paramMap, resultStr);
	//			return resultStr;
	//		}
	//		return "";
	//	}

	/**
	 * @title 发送表单
	 *
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public static String postForm(String url, Map<String, String> paramMap) throws Exception {
		Long start = System.currentTimeMillis();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		if (paramMap != null) {
			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}

		HttpPost httpRequst = new HttpPost(url);
		httpRequst.setConfig(getConfig());
		httpRequst.setEntity(new UrlEncodedFormEntity(list, "UTF-8"));
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = httpclient.execute(httpRequst);
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String resultStr = EntityUtils.toString(entity, "UTF-8");
			log.info("PostForm耗时：{}，地址：{}，参数：{}，结果：{}", (System.currentTimeMillis() - start), url, paramMap, resultStr);
			return resultStr;
		}
		return null;

	}

}
