package com.jc.csmp.exchange.action;

import com.jc.common.kit.vo.ResVO;
import com.jc.foundation.util.SpringContextHolder;
import io.swagger.models.auth.In;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

import javax.servlet.http.HttpServletRequest;

public class PreHandler {

    /**
     * 检查
     * @param key
     * @param num
     * @param request
     * @return
     */
    public static ResVO check(String key, int num, HttpServletRequest request) {
        //客户端ip
        String ip = getIpAddr(request);
        String cacheKey = ip + "_" + key;
        Object timeObject = RateCacheUtils.get(cacheKey);
        Integer time = 1;
        if (timeObject != null) {
            time = Integer.valueOf(timeObject.toString()) + 1;
        }
        if (time > num) {
            return ResVO.buildFail("405", "五秒内访问该接口次数超过规定（" + num + "）,不允许访问", null);
        }
        RateCacheUtils.put(cacheKey, time);
        return null;
    }

    /**
     * 客户端IP
     * @param request
     * @return
     */
    private static String getIpAddr(HttpServletRequest request) {
        String var2 = request.getHeader("x-forwarded-for");
        if (var2 == null || var2.length() == 0 || "unknown".equalsIgnoreCase(var2)) {
            var2 = request.getHeader("Proxy-Client-IP");
        }

        if (var2 == null || var2.length() == 0 || "unknown".equalsIgnoreCase(var2)) {
            var2 = request.getHeader("WL-Proxy-Client-IP");
        }

        if (var2 == null || var2.length() == 0 || "unknown".equalsIgnoreCase(var2)) {
            var2 = request.getRemoteAddr();
        }

        return var2;
    }
}
