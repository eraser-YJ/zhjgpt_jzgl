package com.jc.system.api;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.HttpClientUtil;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.JsonUtil;

import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class ApiServiceContext {
    public static final String APP_MENU = "common.menu";
    public static final String APP_SERVICE = "common.apiservice";
    public static final String API_MENU_ADD = "add";
    public static final String API_MENU_GET = "get";
    public static final String API_SERVICE_REG = "reg";
    public static final String API_HEARTBEAT = "heartbeat";
    public static final String SERVICE_URI = "/api/service";
    public static final String URL_PARAM_SUBSYSTEM = "subsystem";
    public static final String URL_PARAM_APP_NAME = "appname";
    public static final String URL_PARAM_API_NAME = "apiname";
    public static final String URL_PARAM_TOKEN_NAME = "token";
    public static final String URL_PARAM_JSON_NAME = "jsonparams";
    private ApiServiceContext() {
        throw new IllegalStateException("ApiServiceContext class");
    }
    public static String getServerUrl() {
        String apiServer = GlobalContext.getProperty("api.dataServer");
        return apiServer + SERVICE_URI;
    }
}
