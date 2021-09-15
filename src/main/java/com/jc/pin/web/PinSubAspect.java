package com.jc.pin.web;

import com.jc.foundation.util.PluginUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.pin.service.impl.MySubDeptServiceImpl;
import com.jc.pin.service.impl.MySubUserServiceImpl;
import com.jc.sys.web.SubDepartmentController;
import com.jc.sys.web.SubUserController;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class PinSubAspect implements AfterReturningAdvice {

    private static Logger logger = Logger.getLogger(PluginUtil.class);

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        try {
            if (target instanceof SubUserController) {
                MySubUserServiceImpl myUserService = SpringContextHolder.getBean(MySubUserServiceImpl.class);
                myUserService.afterMethod(args, method);
            }
            if (target instanceof SubDepartmentController) {
                MySubDeptServiceImpl myDeptService = SpringContextHolder.getBean(MySubDeptServiceImpl.class);
                myDeptService.afterMethod(args, method);
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }
}
