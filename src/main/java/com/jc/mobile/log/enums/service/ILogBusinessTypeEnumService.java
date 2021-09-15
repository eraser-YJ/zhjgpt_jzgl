package com.jc.mobile.log.enums.service;

import com.jc.mobile.util.MobileApiResponse;

import java.util.Map;

/**
 * @Author 常鹏
 * @Date 2020/8/7 15:58
 * @Version 1.0
 */
public abstract class ILogBusinessTypeEnumService {
    /**
     * 详细信息
     * @param id
     * @return
     */
    public abstract MobileApiResponse detail(String id);

    /**
     * 获取存入日志表的内容
     * @param entity
     * @return
     */
    public abstract Map<String, String> getIdAndContent(Object entity);
}
