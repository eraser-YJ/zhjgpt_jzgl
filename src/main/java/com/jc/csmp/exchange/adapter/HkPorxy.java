package com.jc.csmp.exchange.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.hikvision.artemis.sdk.ArtemisHttpUtil;
import com.hikvision.artemis.sdk.config.ArtemisConfig;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class HkPorxy {

    public static final String m_appKey = GlobalContext.getProperty("videoConfig.appKey"); // artemis网关服务器ip端口
    public static final String m_regEvent = GlobalContext.getProperty("videoConfig.regEvent"); // artemis网关服务器ip端口
    public static final String m_appSecret = GlobalContext.getProperty("videoConfig.secret");  // 秘钥appkey
    public static final String m_host = GlobalContext.getProperty("videoConfig.ip");// 秘钥appSecret
    public static final String m_host2 = GlobalContext.getProperty("videoConfig.ip2");// 秘钥appSecret

    /**
     * 请根据自己的appKey和appSecret更换static静态块中的三个参数. [1 host]
     * 如果你选择的是和现场环境对接,host要修改为现场环境的ip,https端口默认为443，http端口默认为80.例如10.33.25.22:443 或者10.33.25.22:80
     * appKey和appSecret请按照或得到的appKey和appSecret更改.
     * TODO 调用前先要清楚接口传入的是什么，是传入json就用doPostStringArtemis方法，下载图片doPostStringImgArtemis方法
     */
    static {
        ArtemisConfig.host = m_host; // artemis网关服务器ip端口
        ArtemisConfig.appKey = m_appKey;  // 秘钥appkey
        ArtemisConfig.appSecret = m_appSecret;// 秘钥appSecret
    }

    /**
     * 能力开放平台的网站路径
     * TODO 路径不用修改，就是/artemis
     */
    private static final String ARTEMIS_PATH = "/artemis";


    /**
     * 请求设备列表
     *
     * @return
     */
    public static String callResourceList() {
        Map<String, String> path = new HashMap<String, String>(2);
        path.put("http://", ARTEMIS_PATH + "/api/resource/v1/cameras");

        Map<String, Object> jsonBody = new HashMap<String, Object>();
        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 100);
        String body = JsonUtil.java2Json(jsonBody);
        System.out.println("body: " + body);
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        return result;
    }

    /**
     * 请求预览接口
     *
     * @return
     */
    public static String callPreviewURLs() {
        Map<String, String> path = new HashMap<String, String>(2);
        path.put("http://", ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs");

        Map<String, Object> jsonBody = new HashMap<String, Object>();
        jsonBody.put("cameraIndexCode", "de68161ea21e4d9e9f251392c92745ed");
        jsonBody.put("streamType", 0);
        jsonBody.put("protocol", "hls");
        jsonBody.put("transmode", 1);
        jsonBody.put("expand", "streamform=ps");
        String body = JsonUtil.java2Json(jsonBody);
        System.out.println("body: " + body);
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        return result;
    }

    /**
     * 请求预览接口
     *
     * @return
     */
    public static String callPreviewURLByCode(String code) {

        Map<String, String> path = new HashMap<String, String>(2);
        path.put("http://", ARTEMIS_PATH + "/api/video/v1/cameras/previewURLs");

        Map<String, Object> jsonBody = new HashMap<String, Object>();
        jsonBody.put("cameraIndexCode", code);
        jsonBody.put("streamType", 0);
        jsonBody.put("protocol", "rtmp");
        jsonBody.put("transmode", 1);
        jsonBody.put("expand", "streamform=ps");
        String body = JsonUtil.java2Json(jsonBody);
        System.out.println("body: " + body);
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        //{"code":"0","msg":"success","data":{"url":"rtmp://192.168.50.250:1935/live/openUrl/JOaT7mE"}}
        JsonNode jsonNode = JsonUtil.createNode(result);
        JsonNode dataNode = jsonNode.get("data");
        String url = dataNode.get("url").asText();
        return url;


    }

    /**
     * 按事件类型订阅事件
     *
     * @return
     */
    public static String callEventSubscriptionByEventTypes(Long[] eventCodes) {

        Map<String, String> path = new HashMap<String, String>(2);
        path.put("http://", ARTEMIS_PATH + "/api/eventService/v1/eventSubscriptionByEventTypes");

        Map<String, Object> jsonBody = new HashMap<String, Object>();
        jsonBody.put("eventTypes", eventCodes);
        jsonBody.put("eventDest", m_regEvent);
        jsonBody.put("subType", 1);
        jsonBody.put("eventLvl", new Long[]{2L} );
        String body = JsonUtil.java2Json(jsonBody);
        System.out.println("body: " + body);
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        System.out.println("result: " + result);
        return result;


    }

    public static String callEventSubscriptionView() {

        Map<String, String> path = new HashMap<String, String>(2);
        path.put("http://", ARTEMIS_PATH + "/api/eventService/v1/eventSubscriptionView");

        Map<String, Object> jsonBody = new HashMap<String, Object>();
        String body = JsonUtil.java2Json(jsonBody);
        System.out.println("body: " + body);
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        System.out.println("result: " + result);
        return result;
    }


    public static String callEventUnSubscriptionByEventTypes(Long[] eventCodes) {

        Map<String, String> path = new HashMap<String, String>(2);
        path.put("http://", ARTEMIS_PATH + "/api/eventService/v1/eventUnSubscriptionByEventTypes");

        Map<String, Object> jsonBody = new HashMap<String, Object>();
        jsonBody.put("eventTypes", eventCodes);
        String body = JsonUtil.java2Json(jsonBody);
        System.out.println("body: " + body);
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        System.out.println("result: " + result);
        return result;


    }

    /**
     * 是否在线
     *
     * @return
     */
    public static String callOnline() {
        Map<String, String> path = new HashMap<String, String>(2);
        path.put("http://", ARTEMIS_PATH + "/api/nms/v1/online/camera/get");

        Map<String, Object> jsonBody = new HashMap<String, Object>();
        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 100);
        String body = JsonUtil.java2Json(jsonBody);
        System.out.println("body: " + body);
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        return result;
    }


    public static String callTest() {
        Map<String, String> path = new HashMap<String, String>(2);
        path.put("http://", ARTEMIS_PATH + "/api/nms/v1/online/camera/get");

        Map<String, Object> jsonBody = new HashMap<String, Object>();
        jsonBody.put("pageNo", 1);
        jsonBody.put("pageSize", 100);
        String body = JsonUtil.java2Json(jsonBody);
        System.out.println("body: " + body);
        // post请求application/json类型参数
        String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, "application/json", null);
        return result;
    }
}
