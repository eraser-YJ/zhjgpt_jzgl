package com.jc.system.security.service;

import java.lang.reflect.Method;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IClientServiceExt{
    /**
     * 在执行功能操作后执行
     * 注：Controller层拦截，request可用可通过getRequest调用
     *     Service层拦截，request不可用
     * @param o
     */
    void afterMethod(Object[] o,Method method);
}
