package com.jc.system.api.service.impl;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.system.api.dao.IApiMetaDao;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.api.service.IApiMetaService;
import com.jc.system.plugin.domain.PluginDependencies;
import com.jc.system.plugin.service.IPluginService;
import com.jc.system.security.beans.UserBean;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Subsystem;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.ISubsystemService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class ApiMetaServiceImpl extends BaseServiceImpl<ApiMeta> implements IApiMetaService {

	private IApiMetaDao apiServiceDao;
	
	@Autowired
	private ISubsystemService subsystemService;
	
	@Autowired
	private IPluginService pluginService;
	
	public ApiMetaServiceImpl(){}
	
	@Autowired
	public ApiMetaServiceImpl(IApiMetaDao apiServiceDao){
		super(apiServiceDao);
		this.apiServiceDao = apiServiceDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(ApiMeta apiMeta) throws CustomException {
		Integer result = -1;
		try{
			propertyService.fillProperties(apiMeta,true);
			result = apiServiceDao.delete(apiMeta);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(apiMeta);
			throw ce;
		}
		return result;
	}

	@Override
	public void disableApp(ApiMeta app) {

	}

	@Override
	public void enableApp(ApiMeta app) {

	}

	@Override
	public String getAllSubsystemAndApi(String pluginId) {
		List<Subsystem> subsystemList ;
		try {
			subsystemList = subsystemService.queryAll(new Subsystem());
			List<ApiMeta> list = apiServiceDao.queryAll(new ApiMeta());
			if(subsystemList==null || subsystemList.size()<1 || list==null || list.size()<1 ){
				return "";
			}
			List<PluginDependencies> pluginDependencies = pluginService.queryPluginService(pluginId);
			ObjectNode pluginObj = (ObjectNode) JsonUtil.createNode();
			ArrayNode array = JsonUtil.createArrayNode();
			for(PluginDependencies plugin : pluginDependencies){
				ObjectNode obj = (ObjectNode) JsonUtil.createNode();
				obj.put("serviceId", plugin.getServiceId());
				array.add(obj);
			}
			pluginObj.put("services", array);
			return JsonUtil.java2Json(pottingAllSubsystemAndApi(subsystemList,list,pluginObj));
		} catch (CustomException e) {
			return "";
		}
	}
	
	private ArrayNode pottingAllSubsystemAndApi(List<Subsystem> subsystemList,List<ApiMeta> list,ObjectNode pluginObj){

		Map<String,List<ApiMeta>> appMap = new HashMap<>();
		Map<String,String> appSubsystemMap = new HashMap<>();
		Map<String,String> appDescriptionMap = new HashMap<>();
		for (int i = 0; i < list.size(); i++) {
			ApiMeta apiTmp = list.get(i);
			List<ApiMeta> appList = appMap.get(apiTmp.getAppName());
			if(appList==null){
				appList = new ArrayList<>();
				appSubsystemMap.put(apiTmp.getAppName(), apiTmp.getSubsystem());
				appDescriptionMap.put(apiTmp.getAppName(),apiTmp.getExtStr2());
			}
			appList.add(apiTmp);
			appMap.put(apiTmp.getAppName(), appList);
		}
				
		ArrayNode subsystemArray = JsonUtil.createArrayNode();
		ObjectNode subsystemObj = (ObjectNode) JsonUtil.createNode();
		ArrayNode array = JsonUtil.createArrayNode();
		for (Subsystem subsystem : subsystemList) {
				ObjectNode obj = (ObjectNode) JsonUtil.createNode();
				obj.put("id", subsystem.getId());
				obj.put("name", subsystem.getName());
				obj.put("deleteFlag", subsystem.getDeleteFlag());
				obj.put("subsystem", subsystem.getPermission());
				ArrayNode appArray = JsonUtil.createArrayNode();
				int i = 0;
				for(String appKey : appSubsystemMap.keySet()){
					if(subsystem.getPermission() != null && subsystem.getPermission().equals(appSubsystemMap.get(appKey))){
						ObjectNode sonobj = (ObjectNode) JsonUtil.createNode();
						sonobj.put("id", i++);
						sonobj.put("name", appKey);
						sonobj.put("description", appDescriptionMap.get(appKey));
						sonobj.put("parentId", subsystem.getPermission());
						sonobj.putPOJO("api", appMap.get(appKey));
						appArray.add(sonobj);
					}
				}
				obj.put("subApp", appArray);
				array.add(obj);
		}
		subsystemObj.put("subsystem", array);
		subsystemArray.add(subsystemObj);
		subsystemArray.add(pluginObj);
		return subsystemArray;
	}

}