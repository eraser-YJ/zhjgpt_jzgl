package com.jc.util;

import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

//如果JDK版本低于1.8,请使用三方库提供Base64类

import com.jc.foundation.util.GlobalContext;

//如果JDK版本是1.8,可使用原生Base64类
//import java.util.Base64;

public class SendSmsUtil {

	/* 是否开启机关履职满意度评价信息同步 true：开启同步 */
	private static String url = GlobalContext.getProperty("sms.url");
	/* 获取token接口 */
	private static String appKey = GlobalContext.getProperty("sms.appKey");
	/* 机关履职满意度评价信息同步接口 */
	private static String appSecret = GlobalContext.getProperty("sms.appSecret");
	/* 客户端id */
	private static String sender = GlobalContext.getProperty("sms.sender");
	// 模板ID
	private static String templateId = GlobalContext.getProperty("sms.templateId");
	// 签名名称
	private static String signature = GlobalContext.getProperty("sms.signature");

	// 无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
	private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
	// 无需修改,用于格式化鉴权头域,给"Authorization"参数赋值
	private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

	public static String sendSms(String telno, String checkNum) {
		// 返回结果json串 格式 {"result":[{"originTo":"+8613894849844","createTime":"2019-04-18T08:24:06Z","from":"10690549122901772","smsMsgId":"0d03cb19-4f06-4898-bd87-4d7ba11bfc00_260038354","status":"000000"}],"code":"000000","description":"Success"}
		String result = "";
		String receiver = "+86" + telno; // 短信接收人号码
		String statusCallBack = "";
		String templateParas = "[" + checkNum + "]"; // 模板变量
		// 请求Body,不携带签名名称时,signature请填null
		String body = buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack, signature);
		if (null == body || body.isEmpty()) {
			System.out.println("body is null.");
			return "";
		}
		// 请求Headers中的X-WSSE参数值
		String wsseHeader = buildWsseHeader(appKey, appSecret);
		if (null == wsseHeader || wsseHeader.isEmpty()) {
			System.out.println("wsse header is null.");
			return "";
		}

		try {
			SSLContextBuilder builder = new SSLContextBuilder();
			builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
			CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
//请求方法POST
			HttpResponse response = client.execute(RequestBuilder.create("POST").setUri(url).addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded").addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE).addHeader("X-WSSE", wsseHeader).setEntity(new StringEntity(body)).build());

			System.out.println(response.toString()); // 打印响应头域信息
			result = EntityUtils.toString(response.getEntity());
			System.out.println(result); // 打印响应消息实体
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	public static void main(String[] args) throws Exception {
		SendSmsUtil.sendSms("13894849844", "123456");
		/*
		 * //必填,请参考"开发准备"获取如下数据,替换为实际值 // String url = "https://api.rtc.huaweicloud.com:10443/sms/batchSendSms/v1"; //APP接入地址+接口访问URI String url = "https://rtcsms.cn-north-1.myhuaweicloud.com:10743/sms/batchSendSms/v1"; //APP接入地址+接口访问URI String appKey = "3N9P0fuYnT682DN94ZfLCRp3J5gu"; //APP_Key String appSecret = "3mNgLT5xrW0I1dTqJP2A7l4xrv22"; //APP_Secret String sender = "10690549122901772"; //国内短信签名通道号或国际/港澳台短信通道号 String templateId = "633cdd1e9ffb46f681f36bbd1e11094a"; //模板ID //条件必填,国内短信关注,当templateId指定的模板类型为通用模板时生效且必填,必须是已审核通过的,与模板类型一致的签名名称 //国际/港澳台短信不用关注该参数 String signature = "华为云短信测试"; //签名名称
		 * 
		 * //必填,全局号码格式(包含国家码),示例:+8615123456789,多个号码之间用英文逗号分隔 String receiver = "+8613894849844"; //短信接收人号码
		 * 
		 * //选填,短信状态报告接收地址,推荐使用域名,为空或者不填表示不接收状态报告 String statusCallBack = "";
		 * 
		 *//**
			 * 选填,使用无变量模板时请赋空值 String templateParas = ""; 单变量模板示例:模板内容为"您的验证码是${NUM_6}"时,templateParas可填写为"[\"369751\"]" 双变量模板示例:模板内容为"您有${NUM_2}件快递请到${TXT_32}领取"时,templateParas可填写为"[\"3\",\"人民公园正门\"]" 查看更多模板变量规则:常见问题>业务规则>短信模板内容审核标准
			 *//*
				 * String templateParas = "[\"369751\"]"; //模板变量
				 * 
				 * //请求Body,不携带签名名称时,signature请填null String body = buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack, signature); if (null == body || body.isEmpty()) { System.out.println("body is null."); return; }
				 * 
				 * //请求Headers中的X-WSSE参数值 String wsseHeader = buildWsseHeader(appKey, appSecret); if (null == wsseHeader || wsseHeader.isEmpty()) { System.out.println("wsse header is null."); return; }
				 * 
				 * //如果JDK版本低于1.8,可使用如下代码 //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
				 */
		/*
		 * CloseableHttpClient client = HttpClients.custom().setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
		 * 
		 * @Override public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { return true; } }).build()).setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
		 *//*
			 * //如果JDK版本是1.8,可使用如下代码 //为防止因HTTPS证书认证失败造成API调用失败,需要先忽略证书信任问题
			 *//*
				 * CloseableHttpClient client = HttpClients.custom() .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, (x509CertChain, authType) -> true).build()) .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE) .build();
				 *//*
					 * 
					 * SSLContextBuilder builder = new SSLContextBuilder(); builder.loadTrustMaterial(null, new TrustSelfSignedStrategy()); SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build()); CloseableHttpClient client = HttpClients.custom().setSSLSocketFactory(sslsf).build();
					 * 
					 * HttpResponse response = client.execute(RequestBuilder.create("POST")//请求方法POST .setUri(url) .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded") .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE) .addHeader("X-WSSE", wsseHeader) .setEntity(new StringEntity(body)).build());
					 * 
					 * System.out.println(response.toString()); //打印响应头域信息 System.out.println(EntityUtils.toString(response.getEntity())); //打印响应消息实体
					 */
	}

	/**
	 * 构造请求Body体
	 *
	 * @param sender
	 * @param receiver
	 * @param templateId
	 * @param templateParas
	 * @param statusCallbackUrl
	 * @param signature         | 签名名称,使用国内短信通用模板时填写
	 * @return
	 */
	static String buildRequestBody(String sender, String receiver, String templateId, String templateParas, String statusCallbackUrl, String signature) {
		if (null == sender || null == receiver || null == templateId || sender.isEmpty() || receiver.isEmpty() || templateId.isEmpty()) {
			System.out.println("buildRequestBody(): sender, receiver or templateId is null.");
			return null;
		}
		List<NameValuePair> keyValues = new ArrayList<NameValuePair>();

		keyValues.add(new BasicNameValuePair("from", sender));
		keyValues.add(new BasicNameValuePair("to", receiver));
		keyValues.add(new BasicNameValuePair("templateId", templateId));
		if (null != templateParas && !templateParas.isEmpty()) {
			keyValues.add(new BasicNameValuePair("templateParas", templateParas));
		}
		if (null != statusCallbackUrl && !statusCallbackUrl.isEmpty()) {
			keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));
		}
		if (null != signature && !signature.isEmpty()) {
			keyValues.add(new BasicNameValuePair("signature", signature));
		}

		return URLEncodedUtils.format(keyValues, Charset.forName("UTF-8"));
	}

	/**
	 * 构造X-WSSE参数值
	 *
	 * @param appKey
	 * @param appSecret
	 * @return
	 */
	static String buildWsseHeader(String appKey, String appSecret) {
		if (null == appKey || null == appSecret || appKey.isEmpty() || appSecret.isEmpty()) {
			System.out.println("buildWsseHeader(): appKey or appSecret is null.");
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		String time = sdf.format(new Date()); // Created
		String nonce = UUID.randomUUID().toString().replace("-", ""); // Nonce

		byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
		String hexDigest = Hex.encodeHexString(passwordDigest);

		// 如果JDK版本是1.8,请加载原生Base64类,并使用如下代码
		// String passwordDigestBase64Str = Base64.getEncoder().encodeToString(hexDigest.getBytes()); //PasswordDigest
		// 如果JDK版本低于1.8,请加载三方库提供Base64类,并使用如下代码
		String passwordDigestBase64Str = Base64.encodeBase64String(hexDigest.getBytes(Charset.forName("utf-8"))); // PasswordDigest
		// 若passwordDigestBase64Str中包含换行符,请执行如下代码进行修正
		passwordDigestBase64Str = passwordDigestBase64Str.replaceAll("[\\s*\t\n\r]", "");

		return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
	}
}