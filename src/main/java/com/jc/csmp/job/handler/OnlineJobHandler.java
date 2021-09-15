package com.jc.csmp.job.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.jc.csmp.common.log.Uog;
import com.jc.csmp.exchange.adapter.HkPorxy;
import com.jc.csmp.exchange.service.IExchangeInfoService;
import com.jc.csmp.exchange.vo.MonitorAttVO;
import com.jc.csmp.job.annotation.XxlJobRegister;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@XxlJobRegister
@Component
public class OnlineJobHandler extends IJobHandler {
    //日志
    private Uog log = Uog.getInstanceOnjob();
    //升降机属性
    private MonitorAttVO vo = new MonitorAttVO();

    @XxlJob("onlineJobHandler")
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        IExchangeInfoService exchangeInfoService = SpringContextHolder.getBean(IExchangeInfoService.class);

        try {
            String json = HkPorxy.callOnline();
            log.info(json);
            JsonNode jsonNode = JsonUtil.createNode(json);
            String code = jsonNode.get("code").textValue();
            if ("0".equalsIgnoreCase(code)) {
                JsonNode data = jsonNode.get("data");
                if (data != null) {
                    JsonNode dataList = data.get("list");
                    if (dataList != null) {
                        List<Map<String, String>> listMap = new ArrayList<>();
                        dataList.forEach((JsonNode node) -> {
                            Map<String, String> item = new HashMap<>();
                            item.put("cameraIndexCode", getValue("indexCode",node.get("indexCode").toString()));
                            item.put("online_status", getValue("online",node.get("online").toString()));
                            listMap.add(item);
                        });
                        //保存数据
                        exchangeInfoService.updateDeviceOnline(listMap, vo);
                    } else {
                        log.info("list == null 跳过");
                    }

                } else {
                    log.info("data == null 跳过");
                }
            } else {
                log.info("code != 0跳过");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error(ex);
        }


        XxlJobLogger.log("XXL-JOB, Hello World.");
        System.out.println("ctil" + param);
        for (int i = 0; i < 5; i++) {
            XxlJobLogger.log("beat at:" + i);
            TimeUnit.SECONDS.sleep(2);
        }
        return SUCCESS;
    }


    public String getKey(String key) {
        if (key == null || "null".equalsIgnoreCase(key.toString())) {
            return null;
        }
        String inStr = key.toString();
        if (inStr.startsWith("\"")) {
            inStr = inStr.substring(1);
        }
        if (inStr.endsWith("\"")) {
            inStr = inStr.substring(0, inStr.length() - 1);
        }
        return inStr;
    }

    public String getValue(String key, Object value) {
        if (value == null || "null".equalsIgnoreCase(value.toString())) {
            return null;
        }
        String inStr = value.toString();
        if (inStr.startsWith("\"")) {
            inStr = inStr.substring(1);
        }
        if (inStr.endsWith("\"")) {
            inStr = inStr.substring(0, inStr.length() - 1);
        }
        String inKey = key.toUpperCase();
        if (inKey.indexOf("TIME") > 0 || inKey.indexOf("DATE") > 0) {
            if (inStr.length() >= 19) {
                inStr = inStr.replaceAll("T", " ");
                inStr = inStr.substring(0, 19);
            }
        }
        return inStr;
    }


}
