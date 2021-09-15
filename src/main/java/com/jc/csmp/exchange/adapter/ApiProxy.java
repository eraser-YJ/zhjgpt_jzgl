package com.jc.csmp.exchange.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.common.kit.HttpsClientUtil;
import com.jc.foundation.util.JsonUtil;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class ApiProxy {

    private static String cacheToken = null;

    private static final String userName = "hfykcs";

    private static final String password = "hfykcs1";

    private static final String token_action = "https://101.37.149.55/SmartSite/v2/getToken.action";

    /**
     * 取得token
     *
     * @return
     * @throws Exception
     */
    public static String getToken() throws Exception {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("password", password));
        String resultStr = HttpsClientUtil.post(params, token_action);
        JsonNode jsonNode = JsonUtil.createNode(resultStr);
        JsonNode code = jsonNode.get("code");
        if (code.asText().equals("200")) {
            JsonNode token = jsonNode.get("data").get("token");
            cacheToken = token.asText();
            return cacheToken;
        }
        throw new Exception(resultStr);
    }

    /**
     * 取得缓存token
     *
     * @return
     * @throws Exception
     */
    public static String getCacheToken() throws Exception {
        if (cacheToken == null) {
            try {
                cacheToken = getToken();
                return cacheToken;
            } catch (Exception e) {
            }
            //休眠三秒
            Thread.sleep(1000 * 5);
            cacheToken = getToken();
            return cacheToken;
        }
        return cacheToken;
    }

    /**
     * 取得缓存token
     *
     * @return
     * @throws Exception
     */
    public static String getRefreshToken() throws Exception {
        cacheToken = null;
        return getCacheToken();
    }


    /**
     * 发送数据
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static JsonNode post(String url, List<BasicNameValuePair> params) throws Exception {
        String resultStr = postAction(url, params, userName, getCacheToken());
        JsonNode jsonNode = JsonUtil.createNode(resultStr);
        JsonNode code = jsonNode.get("code");
        if (code.asText().equals("200")) {
            return jsonNode.get("data");
        } else if (code.toString().equals("402")) {
            resultStr = postAction(url, params, userName, getRefreshToken());
            if (code.toString().equals("200")) {
                ObjectMapper mapper = new ObjectMapper();
                return jsonNode.get("data");
            } else if (code.toString().equals("402")) {
                throw new Exception("获取token失败");
            } else {

            }
        }
        throw new Exception(resultStr);
    }

    /**
     * 发送数据
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    private static String postAction(String url, List<BasicNameValuePair> params, String userName, String token) throws Exception {
        //添加一个动态参数
        if (params == null) {
            params = new ArrayList<>();
        }
        params.add(new BasicNameValuePair("userName", userName));
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("n_", String.valueOf(System.currentTimeMillis())));
        String resultStr = HttpsClientUtil.post(params, url);
        return resultStr;
    }
}
