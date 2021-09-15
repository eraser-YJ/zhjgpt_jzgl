package com.jc.csmp.exchange.web;

import com.jc.common.def.DefUtil;
import com.jc.csmp.exchange.action.RateCacheUtils;
import com.jc.csmp.exchange.adapter.ApiProxy;
import com.jc.csmp.exchange.adapter.DeviceProxy;
import com.jc.csmp.exchange.adapter.RealTimeProxy;
import com.jc.csmp.exchange.cache.CacheEquiInfo;
import com.jc.csmp.exchange.vo.CraneAttVO;
import com.jc.csmp.exchange.vo.HoistAttVO;
import com.jc.foundation.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 视频截图处理
 *
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/api/test")
public class ZTestScsController extends BaseController {

    public ZTestScsController() {
    }


    @RequestMapping(value = "do1.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> do1() throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        try {
            resMap.put("message", ApiProxy.getToken());
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

    @RequestMapping(value = "do2.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> do2() throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        try {
            resMap.put("message", DeviceProxy.getList1());
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }


    @RequestMapping(value = "posi.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> posi(HttpServletRequest request) throws Exception {
        //3504219
        //3502303
        Map<String, Object> resMap = new HashMap<>();
        try {
            //
            Map<String, Object> bean3 = new HashMap<>();
            bean3.put("crane_height", "30米");
            bean3.put("crane_range", "18.6米");
            bean3.put("id","106ce7b1a99e407e9c2544cc5636aa4c");
            Map<String, String> attMap = new CraneAttVO().getNumKey();
            if (attMap != null) {
                for (String key : attMap.keySet()) {
                    String value = DefUtil.getLastNum1(bean3.get(key));
                    if (value != null) {
                        bean3.put(attMap.get(key), value);
                    }
                }
            }
            CacheEquiInfo.put((String)bean3.get("id"),bean3);
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

    @RequestMapping(value = "posi1.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> posi1(HttpServletRequest request) throws Exception {
        //3504219
        //3502303
        Map<String, Object> resMap = new HashMap<>();
        try {
            //
            Map<String, Object> bean3 = new HashMap<>();
            bean3.put("real_time_height", "18.6米");
            bean3.put("id","eeccccc57692470aa38ae02c59a41560");
            Map<String, String> attMap = new HoistAttVO().getNumKey();
            if (attMap != null) {
                for (String key : attMap.keySet()) {
                    String value = DefUtil.getLastNum1(bean3.get(key));
                    if (value != null) {
                        bean3.put(attMap.get(key), value);
                    }
                }
            }
            CacheEquiInfo.put((String)bean3.get("id"),bean3);
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

    @RequestMapping(value = "do3.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> do3() throws Exception {
        //3504219
        //3502303
        Map<String, Object> resMap = new HashMap<>();
        try {
            Long start = System.currentTimeMillis() - 1000 * 3500;
            //resMap.put("升降机", RealTimeProxy.getRealInfo("3504219,3502303", new Date(start), new Date()));
            resMap.put("塔机", RealTimeProxy.getRealInfo1("12345678,9501407,9506381", new Date(start), new Date()));
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

    @RequestMapping(value = "do4.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> do4() throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        try {
            //resMap.put("升降机", DeviceProxy.getOnline("3504219,3502303"));
            //Thread.sleep(5*1000);
            resMap.put("塔机", DeviceProxy.getOnline1("9501407,9506381"));
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }

    @RequestMapping(value = "cache.action", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> cache() throws Exception {
        Map<String, Object> resMap = new HashMap<>();
        try {
            Object time = RateCacheUtils.get("time");
            if (time == null) {
                time = Integer.valueOf(1);
            } else {
                System.out.println(time.getClass());
                time = Integer.valueOf(time.toString()) + 1;
            }
            RateCacheUtils.put("time", time);
            resMap.put("time", time);
        } catch (Exception e) {
            e.printStackTrace();
            resMap.put("message", e.getMessage());
        }
        return resMap;
    }


}

