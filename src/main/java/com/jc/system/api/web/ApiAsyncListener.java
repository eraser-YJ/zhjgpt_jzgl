package com.jc.system.api.web;

import com.jc.foundation.exception.CustomException;
import com.jc.system.api.domain.ApiInvokeStatus;
import com.jc.system.api.domain.ApiLog;
import com.jc.system.api.service.IApiLogService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebListener;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@WebListener
public class ApiAsyncListener implements AsyncListener {
    protected Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private IApiLogService apiLogService;

    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {
    	logger.info("ApiAsyncListener onComplete");
        logApiInvoke(asyncEvent, ApiInvokeStatus.COMPLETE);
    }

    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
    	logger.info("ApiAsyncListener onError");
        logApiInvoke(asyncEvent, ApiInvokeStatus.ERROR);
    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {
    	logger.info("ApiAsyncListener onStartAsync");
        logApiInvoke(asyncEvent, ApiInvokeStatus.START);
    }

    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
    	logger.info("ApiAsyncListener onTimeout");
        ServletResponse response = asyncEvent.getAsyncContext().getResponse();
        PrintWriter out = response.getWriter();
        out.write("TimeOut Error in Processing");
        logApiInvoke(asyncEvent, ApiInvokeStatus.TIMEOUT);
    }

    private void logApiInvoke(AsyncEvent asyncEvent, ApiInvokeStatus status) {
        asyncEvent.getAsyncContext().getRequest();
        ApiLog log = new ApiLog();
        try {
            apiLogService.save(log);
        } catch (CustomException e) {
            logger.error(e.getMessage());
        }
    }
}

