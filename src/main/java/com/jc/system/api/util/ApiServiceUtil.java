package com.jc.system.api.util;

import com.jc.foundation.util.HttpClientUtil;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.api.ApiServiceContext;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public final class ApiServiceUtil {

	private ApiServiceUtil() {
		throw new IllegalStateException("ApiServiceUtil class");
	}

    public static JsonResult invoke(final String subsystem,final String appName, final String apiName, final String jsonParams) {
        JsonResult result = null;

        Map<String, String> params = new HashMap<>();
        params.put(ApiServiceContext.URL_PARAM_SUBSYSTEM, subsystem);
        params.put(ApiServiceContext.URL_PARAM_APP_NAME, appName);
        params.put(ApiServiceContext.URL_PARAM_API_NAME, apiName);
        params.put(ApiServiceContext.URL_PARAM_JSON_NAME, jsonParams);
        params.put(ApiServiceContext.URL_PARAM_TOKEN_NAME, "123");

        String ret = HttpClientUtil.get(ApiServiceContext.getServerUrl(), params);
        if(!"".equals(ret)){
            result = (JsonResult) JsonUtil.json2Java(ret, JsonResult.class);
        }

        return result;
    }
    
	public static String getUserPhotoAbsolutePath(HttpServletRequest request, String relativePath) {
		String absolutePath = "";
		if(request.getProtocol().contains("HTTPS") || request.getProtocol().contains("https")){
			if("".equals(relativePath) || relativePath == null){
				absolutePath = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/images/demoimg/userPhoto.png";
			} else {
				absolutePath = "https://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + relativePath;
			}
		} else {
			if("".equals(relativePath) || relativePath == null){
				absolutePath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/images/demoimg/userPhoto.png";
			} else {
				absolutePath = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + relativePath;
			}
		}
		
		return absolutePath;
	}
    
}
