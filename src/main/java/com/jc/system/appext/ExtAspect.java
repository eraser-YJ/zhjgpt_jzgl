package com.jc.system.appext;

import com.jc.system.util.InitClazzUtils;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class ExtAspect implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        String key = target.getClass().getSimpleName()+"-"+method.getName();
        if (InitClazzUtils.getClazzStr(key) != null){
            new InitClazzUtils().getUserServiceExt(key).afterMethod(args,method);
        }
    }
}
