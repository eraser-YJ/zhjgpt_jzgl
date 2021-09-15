package com.jc.system.api.domain;

import com.jc.foundation.domain.BaseBean;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class ApiLog extends BaseBean{
	
	private static final long serialVersionUID = 1L;
	
	public ApiLog() {
	}

	/**UUID*/
	private String uuid;
	/**子系统标识*/
	private String subsystem;
	/**插件名称*/
	private String pluginName;
	/**接口子系统*/
	private String apiSubsystem;
	/**api接口对应模块名称*/
	private String apiAppname;
	/**接口名称*/
	private String apiApiname;
	/**api接口地址*/
	private String apiUri;
	/**请求接口参数*/
	private String apiParameter;
	/**返回状态*/
	private String status;
	/**返回信息*/
	private String msg;
	/**IP地址*/
	private String ip;

	public String getUuid(){
		return uuid;
	}
	public void setUuid(String uuid){
		this.uuid = uuid;
	}
	public String getSubsystem(){
		return subsystem;
	}
	public void setSubsystem(String subsystem){
		this.subsystem = subsystem;
	}
	public String getPluginName(){
		return pluginName;
	}
	public void setPluginName(String pluginName){
		this.pluginName = pluginName;
	}
	public String getApiSubsystem(){
		return apiSubsystem;
	}
	public void setApiSubsystem(String apiSubsystem){
		this.apiSubsystem = apiSubsystem;
	}
	public String getApiAppname(){
		return apiAppname;
	}
	public void setApiAppname(String apiAppname){
		this.apiAppname = apiAppname;
	}
	public String getApiApiname(){
		return apiApiname;
	}
	public void setApiApiname(String apiApiname){
		this.apiApiname = apiApiname;
	}
	public String getApiUri(){
		return apiUri;
	}
	public void setApiUri(String apiUri){
		this.apiUri = apiUri;
	}
	public String getApiParameter(){
		return apiParameter;
	}
	public void setApiParameter(String apiParameter){
		this.apiParameter = apiParameter;
	}
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status = status;
	}
	public String getMsg(){
		return msg;
	}
	public void setMsg(String msg){
		this.msg = msg;
	}
	public String getIp(){
		return ip;
	}
	public void setIp(String ip){
		this.ip = ip;
	}
}