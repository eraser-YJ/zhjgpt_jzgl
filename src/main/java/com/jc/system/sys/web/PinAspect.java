package com.jc.system.sys.web;

import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.security.web.DepartmentController;
import com.jc.system.security.web.UserController;
import com.jc.system.sys.service.impl.MyDeptServiceImpl;
import com.jc.system.sys.service.impl.MyUserServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class PinAspect implements AfterReturningAdvice {
    private static Logger logger = Logger.getLogger(PinAspect.class);
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        try {
            if (target instanceof UserController) {
                MyUserServiceImpl myUserService = SpringContextHolder.getBean(MyUserServiceImpl.class);
                myUserService.afterMethod(args, method);
            }
            if (target instanceof DepartmentController) {
                MyDeptServiceImpl myDeptService = SpringContextHolder.getBean(MyDeptServiceImpl.class);
                myDeptService.afterMethod(args, method);
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
    }
}
