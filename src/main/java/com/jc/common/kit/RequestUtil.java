package com.jc.common.kit;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class RequestUtil {


    /**
     * 参数
     *
     * @param request
     * @return
     */
    public static Map<String, Object> getPara(HttpServletRequest request){
        // 查询参数
        Map<String, Object> paraMap = new HashMap<>();
        Enumeration<String> keys = request.getParameterNames();
        String key;
        while (keys.hasMoreElements()) {
            key = keys.nextElement();
            paraMap.put(key, request.getParameter(key));
        }
        return paraMap;
    }
}
