package com.jc.csmp.exchange.web;

import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.common.log.Uog;
import com.jc.csmp.exchange.action.PreHandler;
import com.jc.csmp.exchange.adapter.HkPorxy;
import com.jc.csmp.exchange.service.IExchangeInfoService;
import com.jc.csmp.exchange.vo.MonitorAttVO;
import com.jc.csmp.exchange.vo.MonitorsEvent;
import com.jc.csmp.exchange.vo.MonitorsUtil;
import com.jc.csmp.exchange.vo.XyMonitorsEventType;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

@Controller
@RequestMapping(value = "/monitors/")
public class MonitorsController {
    //日志
    private Uog log = Uog.getInstanceOnPush();
    //属性
    private MonitorAttVO vo = new MonitorAttVO();
    @Autowired
    private IExchangeInfoService exchangeInfoService;

    @RequestMapping(value = "addDeviceInfo.action")
    @ResponseBody
    public ResVO addDeviceInfo(@RequestBody List<Map<String, String>> mapList, HttpServletRequest request) throws Exception {

        if (mapList == null || mapList.size() == 0) {
            return ResVO.buildSuccess("空参数");
        }
        //检查
        ResVO res = PreHandler.check("addDeviceInfoEx", 2, request);
        if (res != null) {
            return res;
        }

        return exchangeInfoService.addDeviceInfoEx(mapList, vo);
    }

    @RequestMapping(value = "updateDeviceOnlineStatus.action")
    @ResponseBody
    public ResVO updateDeviceOnlineStatus(@RequestBody List<Map<String, String>> mapList, HttpServletRequest request) throws Exception {
        if (mapList == null || mapList.size() == 0) {
            return ResVO.buildSuccess("空参数");
        }
        //检查
        ResVO res = PreHandler.check("updateDeviceOnline", 5, request);
        if (res != null) {
            return res;
        }
        return exchangeInfoService.updateDeviceOnline(mapList, vo);
    }

    @RequestMapping(value = "updateDeviceAlarm.action")
    @ResponseBody
    public ResVO updateDeviceAlarm(@RequestBody List<Map<String, String>> mapList, HttpServletRequest request) throws Exception {
        if (mapList == null || mapList.size() == 0) {
            return ResVO.buildSuccess("空参数");
        }
        //检查
        ResVO res = PreHandler.check("updateDeviceOnline", 5, request);
        if (res != null) {
            return res;
        }
        return exchangeInfoService.updateDeviceOnline(mapList, vo);
    }

    @RequestMapping(value = "realDeviceData.action")
    @ResponseBody
    public ResVO realDeviceData(@RequestBody List<Map<String, String>> mapList, HttpServletRequest request) throws Exception {
        if (mapList == null || mapList.size() == 0) {
            return ResVO.buildSuccess("空参数");
        }
        //检查
        ResVO res = PreHandler.check("updateDeviceOnline", 5, request);
        if (res != null) {
            return res;
        }
        return exchangeInfoService.realDeviceDataEx(mapList, vo);
    }

    @RequestMapping(value = "registService.action")
    @ResponseBody
    public ResVO registService(HttpServletRequest request) throws Exception {
        String initStr = HkPorxy.callEventSubscriptionView();
        if (initStr.indexOf("recvEvent.action") < 0) {
            String result = HkPorxy.callEventSubscriptionByEventTypes(MonitorsUtil.eventMap.keySet().toArray(new Long[0]));
            return ResVO.buildSuccess(result);
        } else {
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("registInfo", initStr);
            resMap.put("info", "已经注册过，不用重复注册");
            return ResVO.buildSuccess(resMap);
        }

    }

    @RequestMapping(value = "registServiceList.action")
    @ResponseBody
    public ResVO registServiceList(HttpServletRequest request) throws Exception {
        List<Long> codeList = new ArrayList<>();
        String result = HkPorxy.callEventSubscriptionView();
        return ResVO.buildSuccess(result);
    }

    @RequestMapping(value = "cancelServiceList.action")
    @ResponseBody
    public ResVO cancelServiceList(HttpServletRequest request) throws Exception {
        List<Long> codeList = new ArrayList<>();
        String result = HkPorxy.callEventUnSubscriptionByEventTypes(MonitorsUtil.eventMap.keySet().toArray(new Long[0]));
        return ResVO.buildSuccess(result);
    }

    @RequestMapping(value = "recvEvent1.action")
    @ResponseBody
    public ResVO recvEvent(HttpServletRequest request) throws Exception {
        String json = new String(Files.readAllBytes(Paths.get("C:\\Users\\Administrator\\Desktop\\111\\111.txt")));
        return recvEvent((Map<String, Object>) JsonUtil.json2Java(json, Map.class), request);
    }

    @RequestMapping(value = "recvEvent.action")
    @ResponseBody
    public ResVO recvEvent(@RequestBody Map<String, Object> dataMap, HttpServletRequest request) throws Exception {

       log.info("****************recvEvent****************");
        Enumeration<String> keySet = request.getParameterNames();
        while (keySet.hasMoreElements()) {
            String key = keySet.nextElement();
            System.out.print(key + " : ");
           log.info(request.getParameter(key));
        }
       log.info(JsonUtil.java2Json(dataMap));

       log.info("****************recvEvent****************");
        if (dataMap == null || dataMap.size() == 0) {
            return ResVO.buildSuccess("空参数");
        }
        Map<String, Object> params = (Map<String, Object>) dataMap.get("params");
        if (params != null) {
            List<Map<String, Object>> events = (List<Map<String, Object>>) params.get("events");
            if (events != null) {
                log.info("_____________________1__________________________________");
                List<Map<String, String>> eventList = new ArrayList<Map<String, String>>();
                for (Map<String, Object> item : events) {
                   log.info("_________________2______________________________________");
                    Map<String, String> newItem = new HashMap<>();
                    for (Map.Entry<String, Object> entry : item.entrySet()) {
                        if (entry.getValue() != null) {
                            newItem.put(entry.getKey(), entry.getValue().toString());
                        }
                    }
                   log.info("_________________3______________________________________");
                    List<Map<String, Object>> eventDetails = (List<Map<String, Object>>) item.get("eventDetails");
                    int index = 0;
                    for (Map<String, Object> eventDetail : eventDetails) {
                        for (Map.Entry<String, Object> entry : eventDetail.entrySet()) {
                            if (entry.getValue() != null) {
                                newItem.put(entry.getKey() + "_" + index, entry.getValue().toString().trim());
                            }

                        }
                        index++;
                    }
                   log.info("_________________4______________________________________");
                    MonitorsEvent event = MonitorsUtil.eventMap.get(Long.valueOf(newItem.get("eventType_0")));

                    if (event != null) {
                       log.info("________________5______________________________________");
                        newItem.put("eventXyName", event.getXyName());
                        newItem.put("eventXyType", event.getXyCode());
                        newItem.put("eventType", event.getHkCode() + "");
                        newItem.put("eventName", event.getHkName() + "");
                    } else {
                        newItem.put("eventXyName", XyMonitorsEventType.work_warn.toString());
                        newItem.put("eventXyType", XyMonitorsEventType.work_warn.getDisName());
                        newItem.put("eventType", event.getHkCode() + "");
                        newItem.put("eventName", event.getHkName() + "");
                    }
                    newItem.put("equiCode", newItem.get("srcIndex_0"));
                    newItem.put("equiName", newItem.get("srcName_0"));

                    newItem.put("event_time", DateUtils.getDateTime());

                   log.info("________________________6_______________________________");
                   eventList.add(newItem);
                }
                //保存数据
                log.info("________________________接口数据begin_______________________________");
                log.info(JsonUtil.java2Json(eventList));
                log.info("________________________接口数据end_______________________________");
                exchangeInfoService.realDeviceDataEx(eventList, vo);
            }
        }
        return ResVO.buildSuccess();
    }

//参数名称	参数类型	参数描述	是否必须	备注
//method	string	方法名，用于标识报文用途	是	事件固定OnEventNotify
//params	string	事件参数信息	是	具体参数信息
//+sendTime	string	事件从接收者（如设备接入框架）发出的时间，格式 YYYY-mm-dd hh:MM:ss	是
//+ability	string	事件类别，如视频事件、门禁事件	是	符合事件类型定义规范
//+uids	string	指定的事件接收用户列表，用于事件源发起组件指定接收用户，如指定用户接收手动事件、在部分应用中可以设置事件到指定用户接收	否	通配所有用户
//+events	object[]	事件信息	是	最大支持50条事件数据
//++eventId	string	事件Id，唯一标识事件的一次发生，同一事件发送多次需要ID相同	是	唯一标识,最大64字节
//++srcIndex	string	事件源编号，物理设备是资源编号	是	最大64字节
//++srcType	string	事件源类型	是	资源类型码
//++srcName	string	事件源名称，utf8	否	透传，应用自定义
//++eventType	int	事件类型	是
//++status	int	事件状态, 0-瞬时 1-开始 2-停止 3-事件脉冲 4-事件联动结果更新 5-异步图片上传	是	0,1,2,3,4,5
//            ++eventLvl	int	事件等级：0-未配置 1-低 2-中 3-高，注意，此处事件等级是指在事件联动中配置的等级	否	数字整型，0、1、2 、3，默认0
//++timeout	int	脉冲超时时间，一个持续性的事件，上报的间隔	是
//++happenTime	string	事件发生时间	是
//++srcParentIndex	string	事件发生的事件源父设备，无-空字符串	否	最大64字节
//++data	json	事件其它扩展信息	否	不同类型的事件，扩展信息不同，具体信息可查看具体事件的报文

//    {
//        "method": "OnEventNotify",
//            "params": {
//        "sendTime": "2018-06-28 17:04:31",
//                "ability": "event_pms",
//                "uids": "",
//                "events": [{
//            "eventId": "6DF7CA9A-7000-4AD5-8E73-17A83038BB4C",
//                    "eventType": 771764226,
//                    "happenTime": "2018-06-28 17:04:31",
//                    "srcIndex": "d0de2c6a62ee428498a9a42c2db39ebc",
//                    "srcName": "",
//                    "srcParentIdex": "",
//                    "srcType": "parkspace",
//                    "status": 0,
//                    "eventLvl": 0,
//                    "timeout": 30,
//                    "data": {}
//        }]
//    }
//    }


}
