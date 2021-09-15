package com.jc.system.util;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.system.api.domain.ApiLog;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.api.service.IApiCallback;
import com.jc.system.api.service.IApiLogService;
import com.jc.system.api.service.IApiMetaService;
import com.jc.system.common.util.Utils;
import com.jc.system.plugin.domain.Plugin;
import com.jc.system.plugin.domain.PluginDependencies;
import com.jc.system.plugin.service.IPluginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class PluginServiceUtil {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PluginServiceUtil.class);
	
	static SimpleAsyncTaskExecutor asyncTaskExecutor = null;
	
	private static IPluginService pluginService = SpringContextHolder.getBean(IPluginService.class);
	
	private static IApiMetaService apiMetaService = SpringContextHolder.getBean(IApiMetaService.class);
	
	@Autowired(required=false)
	private IApiCallback apiCallback;
	
	@Autowired
	private IApiLogService apiService;
	
	public static List<JsonResult> callApiService(String pluginName, String apiName, String params){
		return callApiService(pluginName, apiName, params, true);
	}
	
	public static List<JsonResult> callApiService(String pluginName, String apiName, String params, boolean sync){
		String ip = "";
		if(RequestContextHolder.getRequestAttributes() != null){
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			ip = Utils.getIpAddr(request);
		}else{
			ip = "127.0.0.1";
		}
		PluginServiceUtil pluginServiceUtil = new PluginServiceUtil();
		MessageSender messageSender = pluginServiceUtil.new MessageSender(pluginName, apiName, params, ip);
		
		if(asyncTaskExecutor == null) {
            asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        }
		if(sync){
			asyncTaskExecutor.submit(messageSender);
			return new ArrayList<>();
		}else{
			messageSender.call();
			return messageSender.resultList;
		}
		
	}
	
	public static List<JsonResult> callApiService(String pluginName, String apiName, String params, Map<String,File> fileMap){
		return callApiService(pluginName, apiName, params, fileMap, true);
	}
	
	public static List<JsonResult> callApiService(String pluginName, String apiName, String params, Map<String,File> fileMap, boolean sync){
		String ip = "";
		if(RequestContextHolder.getRequestAttributes() != null){
			HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
			ip = Utils.getIpAddr(request);
		}else{
			ip = "127.0.0.1";
		}
		PluginServiceUtil pluginServiceUtil = new PluginServiceUtil();
		MessageSender messageSender = pluginServiceUtil.new MessageSender(pluginName, apiName, params, ip, fileMap);
		if(asyncTaskExecutor == null) {
            asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        }
		if(sync){
			asyncTaskExecutor.submit(messageSender);
			return new ArrayList<>();
		}else{
			messageSender.call();
			return messageSender.resultList;
		}
		
	}
	

	private class MessageSender implements Callable<Boolean> {

		private String pluginName;
		
		private String apiName;
		
        private String params;
        
        private Map<String,File> fileMap;
        
        private String ip;
        
        private List<JsonResult> resultList;

        public MessageSender(String pluginName, String apiName, String params, String ip) {
            this.pluginName = pluginName;
            this.apiName = apiName;
            this.params = params;
            this.ip = ip;
        }
        
        public MessageSender(String pluginName, String apiName, String params, String ip, Map<String,File> fileMap) {
        	this.pluginName = pluginName;
        	this.apiName = apiName;
        	this.params = params;
        	this.ip = ip;
        	this.fileMap = fileMap;
        }

        @Override
        public Boolean call(){
        	resultList = new ArrayList<>();
        	boolean flag = true;
        	try {
    			Plugin pluginBean = new Plugin();
    			pluginBean.setName(pluginName);
    			pluginBean = pluginService.get(pluginBean);
    			List<PluginDependencies> pluginDependenciesList =  pluginService.queryPluginService(String.valueOf(pluginBean.getId()));
    			ApiMeta apiMetaBean = new ApiMeta();
    			apiMetaBean.setApiName(apiName);
    			List<ApiMeta> apiMetaList =  apiMetaService.queryAll(apiMetaBean);
    			for(ApiMeta apiMeta : apiMetaList){
    				for(PluginDependencies pluginDependencies : pluginDependenciesList){
    					if(pluginDependencies.getServiceId().equals(apiMeta.getId())){
    						JsonResult jsonResult = new JsonResult();
//    						jsonResult = (JsonResult) JsonUtil.json2Java(HttpClientUtil.post(apiMeta.getUri(), null, null),JsonResult.class);
    						if(StringUtil.isEmpty(apiMeta.getParams())){
    							if(this.fileMap == null){
    								jsonResult = (JsonResult) JsonUtil.json2Java(HttpClientUtil.post(apiMeta.getUri(), null, null),JsonResult.class);
    							}else{
    								jsonResult = (JsonResult) JsonUtil.json2Java(HttpClientUtil.post(apiMeta.getUri(), null, null, fileMap),JsonResult.class);
    							}
    						}else{
    							List<String> paramList = JsonUtil.json2Array(apiMeta.getParams(), String.class);
    							if(StringUtil.isEmpty(params)){
    								jsonResult.setStatus(1);
    								jsonResult.setMsg("参数不能为空");
    								flag = false;
    							}else{
    								Map<String,String> parameterMap = (Map<String, String>) JsonUtil.json2Java(params, Map.class);
    								if(parameterMap.keySet().containsAll(paramList)){
    									if(this.fileMap == null){
    										jsonResult = (JsonResult) JsonUtil.json2Java(HttpClientUtil.post(apiMeta.getUri(), null, parameterMap),JsonResult.class);
    	    							}else{
    	    								jsonResult = (JsonResult) JsonUtil.json2Java(HttpClientUtil.post(apiMeta.getUri(), null, parameterMap, fileMap),JsonResult.class);
    	    							}
    								}else{
    									jsonResult.setStatus(2);
        								jsonResult.setMsg("参数不正确");
    									flag = false;
    								}
    							}
    						}
    						Map<String, Object> callMsg = new HashMap<>();
    						String subsystem = GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD);
    						if(GlobalContext.isSysCenter()) {
    							subsystem = GlobalContext.SYS_CENTER;
    						}
    						callMsg.put("uuid", UUID.randomUUID().toString());
    						callMsg.put("subsystem", subsystem);
    						callMsg.put("pluginName", pluginName);
    						callMsg.put("apiSubsystem", apiMeta.getSubsystem());
    						callMsg.put("apiAppname", apiMeta.getAppName());
    						callMsg.put("apiApiname", apiMeta.getApiName());
    						callMsg.put("apiUri", apiMeta.getUri());
    						callMsg.put("apiParameter", params);
    						callMsg.put("ip", ip);
    						if(jsonResult == null) {
                                jsonResult = new JsonResult();
                            }
    						jsonResult.setCallMsg(callMsg);
    						resultList.add(jsonResult);
    					}
    				}
    			}
    		} catch (CustomException e) {
    			flag = false;
    			LOGGER.error("callApiService error {}", e);
    		} catch (Exception e) {
    			flag = false;
    			LOGGER.error("callApiService error {}", e);
    		}
        	if(PluginServiceUtil.this.apiCallback != null){
        		apiCallback.callback(resultList);
        	}
        	writeLog(resultList);
        	return flag;
        }
        
        private void writeLog(List<JsonResult> resultList){
        	apiService = SpringContextHolder.getBean(IApiLogService.class);
        	List<ApiLog> list = new ArrayList<>();
        	for(JsonResult jsonResult : resultList){
        		Map<String,Object> map = jsonResult.getCallMsg();
        		if(map == null) {
                    continue;
                }
        		map.put("status", jsonResult.getStatus());
        		map.put("msg", jsonResult.getMsg());
        		ApiLog apiLog = (ApiLog) JsonUtil.json2Java(JsonUtil.java2Json(map),ApiLog.class);
        		list.add(apiLog);
        	}
        	try {
        		apiService.saveList(list);
        	} catch (Exception e) {
        		LOGGER.error("apiLog write error {}", e);
        	}
        }
    }

	public static boolean hasPlugin(String pluginName){
		Plugin pluginBean = new Plugin();
		pluginBean.setName(pluginName);
		pluginBean.setState(1);
		try {
			pluginBean = pluginService.get(pluginBean);
			if(pluginBean == null){
				return false;
			}else{
				return true;
			}
		} catch (CustomException e) {
			LOGGER.error(e.toString());
		}
		return false;
	}
	
}
