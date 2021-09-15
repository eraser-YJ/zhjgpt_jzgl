package com.jc.system.api.task;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.system.api.domain.ApiMeta;
import com.jc.system.api.ApiServiceContext;
import com.jc.system.api.service.IApiMetaService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Component
public class DetectHeartBeat {
    protected Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private IApiMetaService apiMetaService;

    @Resource
    private AsyncTaskExecutor taskExecutor;

    /**
     * 检测服务的心跳
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Scheduled(cron = "0 */5 * * * ?")
    public void detect() throws ExecutionException, InterruptedException {
        if(!GlobalContext.isSysCenter()) {
            logger.debug("business system do not run service's heartbeat detect");
            return;
        }
        Long start = System.currentTimeMillis();
        logger.info(DateUtils.getTime() + "\tDetectHeartBeat任务开始");

        ApiMeta apiMeta = new ApiMeta();
        apiMeta.setApiName(ApiServiceContext.API_HEARTBEAT);
        try {
            List<ApiMeta> apiMetas = apiMetaService.queryAll(apiMeta);

            Map<ApiMeta, Future<Boolean>> result = new HashMap<>();
            for (final ApiMeta meta : apiMetas) {
                Callable<Boolean> callable = new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return meta.heartBeat();
                    }
                };
                result.put(meta, taskExecutor.submit(callable));
            }

            for (Map.Entry<ApiMeta, Future<Boolean>> entry : result.entrySet()) {
                ApiMeta app = entry.getKey();
                boolean isAlive = entry.getValue().get();
                if (!isAlive) {
                    apiMetaService.disableApp(app);
                } else {
                    apiMetaService.enableApp(app);
                }

            }

        } catch (CustomException e) {
            logger.error(e.getMessage());
        }
        Long end = System.currentTimeMillis();
        Long cost = end - start;
        logger.info(DateUtils.getTime() + "\tDetectHeartBeat任务结束, 耗时:" + cost);
    }
}
