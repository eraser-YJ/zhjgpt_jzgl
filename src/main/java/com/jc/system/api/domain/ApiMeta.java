package com.jc.system.api.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.HttpClientUtil;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.api.ApiServiceContext;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class ApiMeta extends BaseBean{
	
	private static final long serialVersionUID = 1L;

	public ApiMeta() {
	}

	/**唯一编号*/
	private String uuid;
	/**子系统标识*/
	private String subsystem;
	/**服务名字*/
	private String appName;
	/**API名字*/
	private String apiName;
	/**请求URI*/
	private String uri;
	/**参数列表JSON*/
	private String params;
	/**参数与值*/
	private Map<String, String> parameters;

	public String getUuid(){
		return uuid;
	}
	public void setUuid(String uuid){
		this.uuid = uuid;
	}
	public String getAppName(){
		return appName;
	}
	public void setAppName(String appName){
		this.appName = appName;
	}
	public String getApiName(){
		return apiName;
	}
	public void setApiName(String apiName){
		this.apiName = apiName;
	}
	public String getUri(){
		return uri;
	}
	public void setUri(String uri){
		this.uri = uri;
	}
	public String getParams(){
		return params;
	}
	public void setParams(String params){
		this.params = params;
	}
	public Map<String, String> getParameters() {
		return this.parameters;
	}
	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}
	public String getSubsystem() {
		return subsystem;
	}
	public void setSubsystem(String subsystem) {
		this.subsystem = subsystem;
	}

	/**
	 * 发起服务调用
	 * @return
     */
	public JsonResult invokeInternal() {
		String res = HttpClientUtil.get(getUri(), getParameters());
		return  (JsonResult) JsonUtil.json2Java(res, JsonResult.class);
	}

	/**
	 * 心跳检测
	 * @return
     */
	public boolean heartBeat() {
		if(getApiName().equals(ApiServiceContext.API_HEARTBEAT)) {
			String res = HttpClientUtil.get(getUri(), null);
			JsonResult result = (JsonResult) JsonUtil.json2Java(res, JsonResult.class);
			if (result != null && result.getStatus() == 0) {
				return true;
			}
		}
		return false;
	}
}