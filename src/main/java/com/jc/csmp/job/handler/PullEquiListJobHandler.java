package com.jc.csmp.job.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.jc.csmp.common.log.Uog;
import com.jc.csmp.exchange.adapter.HkPorxy;
import com.jc.csmp.exchange.service.IExchangeInfoService;
import com.jc.csmp.exchange.vo.HoistAttVO;
import com.jc.csmp.exchange.vo.MonitorAttVO;
import com.jc.csmp.job.annotation.XxlJobRegister;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.service.IUserService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@XxlJobRegister
@Component
public class PullEquiListJobHandler extends IJobHandler {
    //日志
    private Uog log = Uog.getInstanceOnjob();
    //升降机属性
    private MonitorAttVO vo = new MonitorAttVO();

    @XxlJob("scsJobHandler")
    @Override
    public ReturnT<String> execute(String param) throws Exception {
        IExchangeInfoService exchangeInfoService = SpringContextHolder.getBean(IExchangeInfoService.class);

        try {
            String json = HkPorxy.callResourceList();
            //String json = "{\"code\":\"0\",\"msg\":\"success\",\"data\":{\"total\":2,\"pageNo\":1,\"pageSize\":100,\"list\":[{\"altitude\":null,\"cameraIndexCode\":\"ede5eb4513d74886b6c23776a0e0d7c4\",\"cameraName\":\"IPC-大华\",\"cameraType\":1,\"cameraTypeName\":\"半球\",\"capabilitySet\":\"io,remote_vss,event_vss,vss,record,ptz,maintenance,status\",\"capabilitySetName\":\"IO能力,视频设备远程获取能力,视频事件能力,视频能力,录像能力,云台能力,设备维护能力,状态能力\",\"intelligentSet\":null,\"intelligentSetName\":null,\"channelNo\":\"1\",\"channelType\":\"analog\",\"channelTypeName\":\"模拟通道\",\"createTime\":\"2020-08-17T13:27:39.774+08:00\",\"encodeDevIndexCode\":\"ab1820a9ff224f2895dbcbb23b063888\",\"encodeDevResourceType\":null,\"encodeDevResourceTypeName\":null,\"gbIndexCode\":null,\"installLocation\":\"\",\"keyBoardCode\":null,\"latitude\":\"\",\"longitude\":\"\",\"pixel\":null,\"ptz\":null,\"ptzName\":null,\"ptzController\":null,\"ptzControllerName\":null,\"recordLocation\":\"1\",\"recordLocationName\":\"设备存储\",\"regionIndexCode\":\"root000000\",\"status\":null,\"statusName\":null,\"transType\":1,\"transTypeName\":\"TCP\",\"treatyType\":null,\"treatyTypeName\":null,\"viewshed\":null,\"updateTime\":\"2020-08-17T13:29:32.764+08:00\"},{\"altitude\":null,\"cameraIndexCode\":\"de68161ea21e4d9e9f251392c92745ed\",\"cameraName\":\"IPC-海康\",\"cameraType\":0,\"cameraTypeName\":\"枪机\",\"capabilitySet\":\"event_face_detect_alarm,event_audio,io,event_gis,event_rule,gis,event_ias,event_vss,record,vss,event_io,net,maintenance,event_device,status\",\"capabilitySetName\":\"人脸侦测告警,音频事件能力,IO能力,可视域事件能力,行为分析事件能力,可视域能力,入侵报警事件能力,视频事件能力,录像能力,视频能力,IO事件能力,网络参数配置能力,设备维护能力,设备事件能力,状态能力\",\"intelligentSet\":null,\"intelligentSetName\":null,\"channelNo\":\"1\",\"channelType\":\"analog\",\"channelTypeName\":\"模拟通道\",\"createTime\":\"2020-08-17T13:27:01.932+08:00\",\"encodeDevIndexCode\":\"09ff5facf5a749909e6adaafb333c624\",\"encodeDevResourceType\":null,\"encodeDevResourceTypeName\":null,\"gbIndexCode\":null,\"installLocation\":\"\",\"keyBoardCode\":null,\"latitude\":\"\",\"longitude\":\"\",\"pixel\":null,\"ptz\":null,\"ptzName\":null,\"ptzController\":null,\"ptzControllerName\":null,\"recordLocation\":\"1\",\"recordLocationName\":\"设备存储\",\"regionIndexCode\":\"root000000\",\"status\":null,\"statusName\":null,\"transType\":1,\"transTypeName\":\"TCP\",\"treatyType\":null,\"treatyTypeName\":null,\"viewshed\":null,\"updateTime\":\"2020-08-17T13:29:52.801+08:00\"}]}}";
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
                            Iterator<Map.Entry<String, JsonNode>> jsonNodes = node.fields();
                            String key;
                            String value;
                            while (jsonNodes.hasNext()) {
                                Map.Entry<String, JsonNode> subNode = jsonNodes.next();
                                key = getKey(subNode.getKey());
                                value = getValue(key, subNode.getValue());
                                if (value != null) {
                                    item.put(key, value);
                                }
                            }
                            listMap.add(item);
                        });
                        //保存数据
                        exchangeInfoService.addDeviceInfoEx(listMap, vo);
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
