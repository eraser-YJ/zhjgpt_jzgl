package com.jc.system.api.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.PluginUtil;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.api.service.IApiMetaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping("/api/service")
public class ApiRegisterController {

    private static Logger logger = Logger.getLogger(PluginUtil.class);

    @Autowired
    IApiMetaService apiMetaService;

    /**
     * @description 注册服务元数据
     */
    @RequestMapping(value = "reg.action",method=RequestMethod.GET)
    @ResponseBody
    public JsonResult regApiMeta(String jsonparams) {
        JsonResult result = null;
        Map<String, String> params = (Map<String, String>) JsonUtil.json2Java(jsonparams, Map.class);
        if(params == null) {
            return new JsonResult(-1, "jsonparams is empty");
        }
        List<String> requiredParamsKeys = new ArrayList<>();
    	requiredParamsKeys.add("uuid");
    	requiredParamsKeys.add("subsystem");
    	requiredParamsKeys.add("appname");
    	requiredParamsKeys.add("apiname");
    	requiredParamsKeys.add("extStr1");
    	requiredParamsKeys.add("extStr2");
    	requiredParamsKeys.add("uri");
    	requiredParamsKeys.add("params");
        for (String pkey : requiredParamsKeys) {
            if (!params.containsKey(pkey)) {
                return new JsonResult(-1, pkey + " not exist");
            }
        }
        ApiMeta apiMeta = new ApiMeta();
        apiMeta.setUuid(params.get("uuid"));
        apiMeta.setSubsystem(params.get("subsystem"));
        apiMeta.setAppName(params.get("appname"));
        apiMeta.setApiName(params.get("apiname"));
        apiMeta.setExtStr1(params.get("extStr1"));
        apiMeta.setExtStr2(params.get("extStr2"));
        apiMeta.setUri(params.get("uri"));
        apiMeta.setParams(params.get("params"));
        try {
            Integer ret = apiMetaService.save(apiMeta);
            if(ret > 0) {
                result = new JsonResult(0, "Success");
            }
        } catch (CustomException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
