package com.jc.csmp.exchange.web;

import com.jc.common.kit.vo.ResVO;
import com.jc.csmp.exchange.action.PreHandler;
import com.jc.csmp.exchange.service.IExchangeInfoService;
import com.jc.csmp.exchange.vo.CraneAttVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/towerCrane/")
public class TowerCraneController {

    //塔机属性
    private CraneAttVO vo = new CraneAttVO();
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
        ResVO res = PreHandler.check("realDeviceData", 5, request);
        if (res != null) {
            return res;
        }
        return exchangeInfoService.realDeviceDataEx(mapList, vo);
    }

}

