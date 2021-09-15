package com.jc.pin.util;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.pin.domain.PinReSubDepartment;
import com.jc.pin.domain.PinReSubUser;
import com.jc.pin.domain.PinSubDepartment;
import com.jc.pin.domain.PinSubUser;
import com.jc.pin.service.IPinSubDepartmentService;
import com.jc.pin.service.IPinSubUserService;
import com.jc.pin.service.impl.PinSubDepartmentServiceImpl;
import com.jc.pin.service.impl.PinSubUserServiceImpl;
import com.jc.system.sys.domain.PinDepartment;
import com.jc.system.sys.domain.PinReDepartment;
import com.jc.system.sys.domain.PinReUser;
import com.jc.system.sys.domain.PinUser;
import com.jc.system.sys.service.IPinDepartmentService;
import com.jc.system.sys.service.IPinUserService;
import com.jc.system.sys.service.impl.PinDepartmentServiceImpl;
import com.jc.system.sys.service.impl.PinUserServiceImpl;
import com.jc.system.sys.util.PinDeptAndUserInit;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class PinSubDeptAndUserInit {

    private transient static final Logger log = Logger.getLogger(PinSubDeptAndUserInit.class);
    private static IPinSubDepartmentService pinDepartmentService = null;
    private static IPinSubUserService pinUserService = null;

    private PinSubDeptAndUserInit() {
        throw new IllegalStateException("PinSubDeptAndUserInit class");
    }

    /**
     * 初始化函数
     */
    public static void init(){
        log.debug("初始化系统参数开始");
        pinDepartmentService = SpringContextHolder.getBean(PinSubDepartmentServiceImpl.class);
        pinUserService = SpringContextHolder.getBean(PinSubUserServiceImpl.class);
        refresh();
        log.debug("初始化系统参数结束");
    }

    /**
     * 刷新系统参数内存
     */
    public static void refresh(){
        try {
            List<PinReSubDepartment> pinDepartmentList = pinDepartmentService.queryPinDepartment(new PinSubDepartment());
            List<PinReSubUser> pinUserList = pinUserService.queryPinUser(new PinSubUser());
            CacheClient.putCache("cache_sub_department_info_pins", JsonUtil.java2Json(pinDepartmentList));
            CacheClient.putCache("cache_sub_user_info_pins", JsonUtil.java2Json(pinUserList));
        } catch (CustomException e) {
            log.error(e);
        }
    }
}
