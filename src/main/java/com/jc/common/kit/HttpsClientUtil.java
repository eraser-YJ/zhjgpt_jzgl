package com.jc.common.kit;

import com.jc.csmp.common.log.Uog;
import com.jc.foundation.util.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import static java.net.URLDecoder.decode;

/**
 * http客户段
 */
public class HttpsClientUtil {
    //默认字符集合
    private static final String UTF_8 = "UTF-8";
    //日志
    private static final Uog log = Uog.getInstanceOnjob();

    /**
     * 发送post请求
     *
     * @param params
     * @param url
     * @return
     * @throws Exception
     */
    public static String post(List<BasicNameValuePair> params, String url) throws Exception {
        CloseableHttpClient httpClient = createSSLClientDefault();
        try {
            HttpEntity httpEntity = new UrlEncodedFormEntity(params, UTF_8);
            HttpPost post = new HttpPost(url);
            post.setEntity(httpEntity);
            HttpResponse response = httpClient.execute(post);
            HttpEntity entity = response.getEntity();
            String body = EntityUtils.toString(entity, Charset.forName(UTF_8));
            log.info("\r\n地址：" + url + "\r\n参数：" + getUtf8Params(httpEntity) + "\r\n返回结果：" + body);
            return body;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error(e);
            }
        }
    }

    /**
     * 创建ssl的http客户端
     *
     * @return
     */
    private static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new HostnameVerifier() {

                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e);
        }
        return HttpClients.createDefault();
    }

    /**
     * 打印参数
     *
     * @param httpEntity
     * @throws IOException
     */
    private static String getUtf8Params(HttpEntity httpEntity) throws IOException {
        BufferedInputStream bif = null;
        try {
            bif = new BufferedInputStream(httpEntity.getContent());
            byte[] byteArr = new byte[(int) httpEntity.getContentLength()];
            byte tmpByte[] = new byte[1024];
            int totalCounts = 0;
            int size = bif.read(tmpByte);
            while (size > 0) {
                System.arraycopy(tmpByte, 0, byteArr, totalCounts, size);
                totalCounts = totalCounts + size;
                size = bif.read(tmpByte);
            }
            return decode(new String(byteArr, UTF_8), UTF_8);
        } catch (Exception e) {
            log.error(e);
            return "";
        } finally {
            if (bif != null) {
                bif.close();
            }
        }
    }

}
