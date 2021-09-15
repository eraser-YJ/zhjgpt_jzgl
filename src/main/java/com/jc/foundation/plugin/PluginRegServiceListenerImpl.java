package com.jc.foundation.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.api.ApiServiceContext;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.api.service.IApiMetaService;
import com.jc.system.api.util.ApiServiceUtil;
import com.jc.system.plugin.PluginRegServiceListener;
import com.jc.system.plugin.domain.Plugin;
import com.jc.system.plugin.domain.PluginService;
import com.jc.system.plugin.service.IPluginService;

public class PluginRegServiceListenerImpl implements PluginRegServiceListener{

	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	public void regService(Plugin plugin) {
		IApiMetaService apiService = SpringContextHolder.getBean(IApiMetaService.class);
		try {
			ApiMeta temp = new ApiMeta();
			temp.setUuid(plugin.getUuid());
			temp.setSubsystem(GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD));
			List<ApiMeta> list = apiService.queryAll(temp);
			if (list.size() > 0) {
				String[] ids = new String[list.size()];
				int index = 0;
				for (ApiMeta tempVo : list) {
					ids[index++] = String.valueOf(tempVo.getId());
					IPluginService pluginService = SpringContextHolder.getBean(IPluginService.class);
					pluginService.deleteByServiceId(String.valueOf(tempVo.getId()));
				}
				temp.setPrimaryKeys(ids);
				apiService.delete(temp, false);

			}
		} catch (CustomException e) {
			logger.error(e);
			logger.error("未能注册服务:" + plugin.getName() + "插件删除服务失败,请检查后重试");
			System.exit(-1);
		}

		if (null != plugin.getServices() && plugin.getServices().size() > 0) {
			for (final PluginService ps : plugin.getServices()) {

				if (GlobalContext.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD) == null || GlobalContext
						.getProperty(GlobalContext.SYS_SUBSYSTEM_FIELD).equals(GlobalContext.SYS_CENTER)) {
					ApiMeta service = PluginServiceToApiMeta.toApiMeta(ps);
					service.setSubsystem(GlobalContext.SYS_CENTER);
					try {
						service.setUuid(plugin.getUuid());
						apiService.save(service);
					} catch (CustomException e) {
						logger.error(e);
						logger.error("未能注册服务:" + ps + ",请检查后重试");
						System.exit(-1);
					}

				} else {
					if (!"Plugin initial".equals(plugin.getName())) {

						Map<String, String> params = new HashMap<>();
						//================
						params.put("uuid", plugin.getUuid());
						params.put("subsystem", GlobalContext.isSysCenter()?GlobalContext.SYS_CENTER:GlobalContext.getProperty("subsystem.id"));
						params.put("appname", ps.getAppName());
						params.put("apiname", ps.getApiName());
						params.put("extStr1", ps.getExtStr1());
						params.put("extStr2", ps.getExtStr2());
						params.put("uri", ps.getUri());
						params.put("params", JsonUtil.java2Json(ps.getParams()));
						//=================
						JsonResult result = ApiServiceUtil.invoke(params.get("subsystem"),ApiServiceContext.APP_SERVICE,
								ApiServiceContext.API_SERVICE_REG, JsonUtil.java2Json(params));

						if (result == null || result.getStatus() != 0) {
							logger.error("未能注册服务" + ps + ",请检查后重试");
							System.exit(-1);
						}
					}
				}
			}
		}
	}

}
