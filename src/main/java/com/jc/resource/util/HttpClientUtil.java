package com.jc.resource.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.resource.enums.vo.ResourceAttachInfo;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author changpeng
 * @version 2019/12/5
 */
public class HttpClientUtil {
    private static int SUCCESSCODE = 200;
    private static String POST = "post";
    private static String GET = "get";
    private static String CHARTSET = "utf-8";

    public static void main(String[] args) {
        /*String url = "http://192.168.0.128:1111/dh/webapi/queryTable/savePhyData.action";
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> result1 = new HashMap<>();
        result.put("objUrl", "1");
        result1.put("aaaa", "1");
        result1.put("bbb", "1");
        result.put("info", result1);
        HttpClientUtil.post(url, result);*/
        String url = "http://192.168.0.93:8888/personnel/rest/dingtalk/system/test?name=1";
        Result result = HttpClientUtil.get(url);
        System.out.println(result.getData());

        System.out.println("******************");
        Map<String, Object> param = new HashMap<>(1);
        param.put("name", "cp");
        Result result1 = HttpClientUtil.post("http://192.168.0.93:8888/personnel/rest/dingtalk/system/test", param);

        System.out.println(result1.getData());
    }

    private static CloseableHttpClient getHttpClient() {
        RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder.create();
        ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
        registryBuilder.register("http", plainSF);
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            TrustStrategy anyTrustStrategy = new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { return true; }
            };
            SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(trustStore, anyTrustStrategy).build();
            LayeredConnectionSocketFactory sslSF = new SSLConnectionSocketFactory(sslContext, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            registryBuilder.register("https", sslSF);
        } catch (KeyStoreException var6) {
            throw new RuntimeException(var6);
        } catch (KeyManagementException var7) {
            throw new RuntimeException(var7);
        } catch (NoSuchAlgorithmException var8) {
            throw new RuntimeException(var8);
        }
        Registry<ConnectionSocketFactory> registry = registryBuilder.build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(registry);
        return HttpClientBuilder.create().setConnectionManager(connManager).build();
    }

    private static Object getHttp(String type, String url) {
        if (GET.equals(type)) {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            httpGet.setConfig(requestConfig);
            return httpGet;
        } else if (POST.equals(type)) {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
            httpPost.setConfig(requestConfig);
            return httpPost;
        }
        return null;
    }

    /**
     * get请求，参数跟在url后面
     * @param url
     * @return
     */
    public static Result get(String url) {
        String result = "";
        try {
            CloseableHttpClient httpClient = getHttpClient();
            HttpGet httpGet = (HttpGet)getHttp(GET, url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int status = response.getStatusLine().getStatusCode();
            if (status != SUCCESSCODE) {
                return Result.failure(ResultCode.INTERFACE_OUTTER_INVOKE_ERROR);
            }
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
            httpClient.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Result.success(ResultCode.SUCCESS.message(), result);
    }

    /**
     * 不带附件的POST请求
     * @param url
     * @param params
     * @return
     */
    public static Result post(String url, Map<String, Object> params) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = (HttpPost)getHttp(POST, url);
        try {
            String data = new ObjectMapper().writeValueAsString(params);
            StringEntity requestEntity = new StringEntity(data, Charset.forName(CHARTSET));
            requestEntity.setContentEncoding(CHARTSET);
            requestEntity.setContentType("application/json");
            httpPost.setEntity(requestEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != SUCCESSCODE) {
                System.out.println(response.getStatusLine());
                return Result.failure(ResultCode.INTERFACE_OUTTER_INVOKE_ERROR);
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            response.close();
            httpClient.close();
            System.out.println(result);
            return Result.success(ResultCode.SUCCESS.message(), result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failure(ResultCode.INTERFACE_OUTTER_INVOKE_ERROR);
        }
    }

    public static Result post(String url, String data) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = (HttpPost)getHttp(POST, url);
        System.out.println(url);
        System.out.println(data);
        try {
            StringEntity requestEntity = new StringEntity(data, Charset.forName(CHARTSET));
            requestEntity.setContentEncoding(CHARTSET);
            requestEntity.setContentType("application/json");
            httpPost.setEntity(requestEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != SUCCESSCODE) {
                System.out.println(response.getStatusLine());
                return Result.failure(ResultCode.INTERFACE_OUTTER_INVOKE_ERROR);
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            response.close();
            httpClient.close();
            System.out.println(result);
            return Result.success(ResultCode.SUCCESS.message(), result);
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failure(ResultCode.INTERFACE_OUTTER_INVOKE_ERROR);
        }
    }

    /**
     * 带附件的post请求
     * @param url
     * @param params
     * @param fileMap
     * @return
     */
    public static Result post(String url, Map<String, String> params, Map<String, File> fileMap) {
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = (HttpPost)getHttp(POST, url);
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        ContentType contentType = ContentType.create("text/plain",Charset.forName(CHARTSET));
        /**普通参数*/
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> paramEntry : params.entrySet()) {
                multipartEntityBuilder.addPart(paramEntry.getKey(), new StringBody(paramEntry.getValue() == null ? "" : paramEntry.getValue(), contentType));
            }
        }
        /**附件参数*/
        if (fileMap != null && fileMap.keySet().size() > 0) {
            for (Map.Entry<String, File> fileEntry : fileMap.entrySet()) {
                multipartEntityBuilder = multipartEntityBuilder.addPart(fileEntry.getKey(), new FileBody(fileEntry.getValue()));
            }
        }
        try {
            HttpEntity reqEntity = multipartEntityBuilder.setCharset(Charset.forName(CHARTSET)).build();
            httpPost.setEntity(reqEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() != SUCCESSCODE) {
                System.out.println(EntityUtils.toString(response.getEntity()));
                return Result.failure(ResultCode.INTERFACE_OUTTER_INVOKE_ERROR);
            }
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            response.close();
            httpClient.close();
            return Result.success(ResultCode.SUCCESS.message(), result);
        } catch (IOException ex) {
            ex.printStackTrace();
            return Result.failure(ResultCode.INTERFACE_OUTTER_INVOKE_ERROR);
        }
    }
}
