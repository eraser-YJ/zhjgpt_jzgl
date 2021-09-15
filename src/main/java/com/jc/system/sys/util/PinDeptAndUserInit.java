package com.jc.system.sys.util;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.sys.domain.PinDepartment;
import com.jc.system.sys.domain.PinReDepartment;
import com.jc.system.sys.domain.PinReUser;
import com.jc.system.sys.domain.PinUser;
import com.jc.system.sys.service.IPinDepartmentService;
import com.jc.system.sys.service.IPinUserService;
import com.jc.system.sys.service.impl.PinDepartmentServiceImpl;
import com.jc.system.sys.service.impl.PinUserServiceImpl;
import com.jc.system.util.InitClazzUtils;
import org.apache.log4j.Logger;

import java.util.List;

/***
 * @author Administrator
 * @date 2020-07-01
 */
public class PinDeptAndUserInit {

    private PinDeptAndUserInit() {
        throw new IllegalStateException("PinDeptAndUserInit class");
    }

    private transient static final Logger log = Logger.getLogger(PinDeptAndUserInit.class);
    private static IPinDepartmentService pinDepartmentService = null;
    private static IPinUserService pinUserService = null;

    /**
     * 初始化函数
     */
    public static void init(){
        log.debug("初始化系统参数开始");
        pinDepartmentService = SpringContextHolder.getBean(PinDepartmentServiceImpl.class);
        pinUserService = SpringContextHolder.getBean(PinUserServiceImpl.class);
        refresh();
        log.debug("初始化系统参数结束");
    }

    /**
     * 刷新系统参数内存
     */
    public static void refresh(){
        try {
            List<PinReDepartment> pinDepartmentList = pinDepartmentService.queryPinDepartment(new PinDepartment());
            List<PinReUser> pinUserList = pinUserService.queryPinUser(new PinUser());
            CacheClient.putCache("cache_department_info_pins", JsonUtil.java2Json(pinDepartmentList));
            CacheClient.putCache("cache_user_info_pins", JsonUtil.java2Json(pinUserList));
        } catch (CustomException e) {
            log.error(e);
        }
    }
}
