package com.jc.foundation.plugin;

import com.jc.foundation.util.JsonUtil;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.plugin.domain.PluginService;

public class PluginServiceToApiMeta {

	public static ApiMeta toApiMeta(PluginService ps) {
		ApiMeta meta = new ApiMeta();
        meta.setAppName(ps.getAppName());
        meta.setApiName(ps.getApiName());
        meta.setExtStr1(ps.getExtStr1());
        meta.setExtStr2(ps.getExtStr2());
        meta.setUri(ps.getUri());
        meta.setParams(JsonUtil.java2Json(ps.getParams()));
        return meta;
	}

}
