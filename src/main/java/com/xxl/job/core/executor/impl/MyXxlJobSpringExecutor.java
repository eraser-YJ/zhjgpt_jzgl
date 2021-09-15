package com.xxl.job.core.executor.impl;

import com.jc.foundation.util.GlobalContext;
import com.xxl.job.core.handler.IJobHandler;

/**
 * 代理类，免得控制台不停报错
 */
public class MyXxlJobSpringExecutor extends XxlJobSpringExecutor {

    private static String jobRun = GlobalContext.getProperty("job.run");


    public void afterPropertiesSet() throws Exception {
        if ("true".equalsIgnoreCase(jobRun)) {
            super.afterPropertiesSet();
        }
    }

    public void start() throws Exception {
        if ("true".equalsIgnoreCase(jobRun)) {
            super.start();
        }

    }

    public void destroy() {
        if ("true".equalsIgnoreCase(jobRun)) {
            super.destroy();
        }
    }
}
