package com.jc.system.session.util;

import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.session.service.ISessionServiceService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class sessionUtil {

    private static ISessionServiceService sessionService = SpringContextHolder.getBean(ISessionServiceService.class);

    /**
     * 获取登出用户信息
     */
    public static int getLogoutUser(String loginName) {
       return sessionService.getLogoutUser(loginName);
    }
}
