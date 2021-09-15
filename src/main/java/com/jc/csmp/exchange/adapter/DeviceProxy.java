package com.jc.csmp.exchange.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceProxy {


    private static final String url = "http://118.31.23.115:8080/Hoist/v3/listDeviceInfo";

    private static final String url1 = "http://118.31.23.115:8080/TowerCrane/v3/listDeviceInfo";
    /**
     * 取得token
     *
     * @return
     * @throws Exception
     */
    public static String getList() throws Exception {
        return ApiProxy.post(url, null).toString();
    }
    /**
     * 取得token
     *
     * @return
     * @throws Exception
     */
    public static String getList1() throws Exception {
        return ApiProxy.post(url1, null).toString();
    }

    /**
     * 取得token
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getOnline(String codeList) throws Exception {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("listDeviceId", codeList));
        params.add(new BasicNameValuePair("fields", "1,0,0,0,0,0,0"));
        params.add(new BasicNameValuePair("extraFields", "1,1,1,1"));
        JsonNode jsonNode = ApiProxy.post(url, params);
        Map<String, Object> resMap = new HashMap<>();
        jsonNode.forEach((JsonNode node) -> {
            JsonNode tempNode = node.get("hoist_box_id");
            if (tempNode != null) {
                String key = tempNode.asText();
                JsonNode tempNode2 = node.get("online_status");
                if (tempNode2 != null) {
                    String status = tempNode2.asText();
                    resMap.put(key, status);
                } else {
                    //没有在线状态，则为离线
                    resMap.put(key, "-1");
                }
            }

        });
        return resMap;
    }

    /**
     * 取得token
     *
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getOnline1(String codeList) throws Exception {
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("listDeviceId", codeList));
        params.add(new BasicNameValuePair("fields", "1,0,0,0,0,0,0"));
        params.add(new BasicNameValuePair("extraFields", "1,1,1,1"));
        JsonNode jsonNode = ApiProxy.post(url1, params);
        Map<String, Object> resMap = new HashMap<>();
        jsonNode.forEach((JsonNode node) -> {
            JsonNode tempNode = node.get("black_box_id");
            if (tempNode != null) {
                String key = tempNode.asText();
                JsonNode tempNode2 = node.get("online_status");
                if (tempNode2 != null) {
                    String status = tempNode2.asText();
                    resMap.put(key, status);
                } else {
                    //没有在线状态，则为离线
                    resMap.put(key, "-1");
                }
            }

        });
        return resMap;
    }
}
