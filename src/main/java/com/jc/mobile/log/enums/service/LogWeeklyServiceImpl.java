package com.jc.mobile.log.enums.service;

import com.jc.csmp.project.domain.CmProjectWeekly;
import com.jc.csmp.project.service.ICmProjectWeeklyService;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.util.MobileApiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 移动端周报 日志
 * @Author 常鹏
 * @Date 2020/8/7 16:04
 * @Version 1.0
 */
public class LogWeeklyServiceImpl extends ILogBusinessTypeEnumService {
    private static ICmProjectWeeklyService cmProjectWeeklyService = null;
    public LogWeeklyServiceImpl() {
        cmProjectWeeklyService = SpringContextHolder.getBean(ICmProjectWeeklyService.class);
    }
    @Override
    public MobileApiResponse detail(String id) {
        if (StringUtil.isEmpty(id)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        CmProjectWeekly entity = cmProjectWeeklyService.getById(id);
        if (entity == null) {
            return MobileApiResponse.error(ResultCode.RESULE_DATA_NONE);
        }
        return MobileApiResponse.ok(entity);
    }

    @Override
    public Map<String, String> getIdAndContent(Object entity) {
        Map<String, String> resultMap = new HashMap<>(2);
        CmProjectWeekly po = (CmProjectWeekly) entity;
        if (po != null) {
            resultMap.put("businessId", po.getId());
            resultMap.put("content", "周报名称为：" + po.getReportName());
        }
        return resultMap;
    }
}
