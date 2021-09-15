package com.jc.csmp.exchange.web;

import com.jc.csmp.exchange.adapter.HkPorxy;
import com.jc.foundation.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 视频截图处理
 *
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/api/test")
public class ZTestHkController extends BaseController {


    @RequestMapping(value = "hkList.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> hkList() throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        try {
            resMap.put("message", HkPorxy.callResourceList());
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

    @RequestMapping(value = "hk.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> hk() throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        try {
            resMap.put("message", HkPorxy.callPreviewURLs());
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

    @RequestMapping(value = "hktest.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> hktest() throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        try {
            resMap.put("message", HkPorxy.callTest());
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

}

