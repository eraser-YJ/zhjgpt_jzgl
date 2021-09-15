package com.jc.system.api.web;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonResult;
import com.jc.foundation.util.StringUtil;
import com.jc.system.api.ApiServiceContext;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.api.service.IApiMetaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping("/api/service")
public class ApiInvokeController {

    protected Logger logger = Logger.getLogger(this.getClass());
    @Resource
    private AsyncTaskExecutor taskExecutor;

    @Autowired
    private IApiMetaService apiMetaService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult invoke(String subsystem, String appname, final String apiname, String token, String jsonparams) throws ExecutionException, InterruptedException {
        if(StringUtil.trimIsEmpty(appname) || StringUtil.trimIsEmpty(apiname) || StringUtil.trimIsEmpty(token)){
            return new JsonResult(-1, "appname or apiname or token is empty");
        }
        ApiMeta apiMeta = new ApiMeta();
        if(!ApiServiceContext.APP_SERVICE.equals(appname)) {
            apiMeta.setSubsystem(subsystem);
        }
        apiMeta.setAppName(appname);
        apiMeta.setApiName(apiname);
        try {
            apiMeta = apiMetaService.get(apiMeta);
            if(apiMeta == null) {
                return new JsonResult(-1, "Cannot find app that named " + appname);
            }
            Map<String, String> params = new HashMap<>();
            params.put(ApiServiceContext.URL_PARAM_JSON_NAME, jsonparams);
            apiMeta.setParameters(params);
            final ApiMeta finalApiMeta = apiMeta;
            Callable<JsonResult> callableInvoke = new Callable<JsonResult>() {
                @Override
                public JsonResult call() throws Exception {
                    JsonResult result = finalApiMeta.invokeInternal();
                    if(result == null) {
                        return new JsonResult(-1, "api invokeInternal failed, please track in log files");
                    }
                    return result;
                }
            };
            Callable<Void> callableLog = new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                	logger.error("log to db: App[" + apiname + "." + apiname + "] has been invoked.");
                    return null;
                }
            };
            JsonResult result = taskExecutor.submit(callableInvoke).get();
            taskExecutor.submit(callableLog);
            return result;
        } catch (CustomException e) {
            logger.error(e);
        }
        return null;
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseBody
    public String test() throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        Callable<String> callable = new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "{\"status\":0, \"msg\":\"ok\"}";
            }
        };
        String result = taskExecutor.submit(callable).get();
        logger.info(result);
        long end = System.currentTimeMillis();
        logger.info((end - start) / 1000.0 + "s");
        return result;
    }

}
