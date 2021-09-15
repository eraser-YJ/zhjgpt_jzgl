package com.jc.mobile.log.enums.service;

import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.util.MobileApiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 移动端进度日志
 * @Author 常鹏
 * @Date 2020/8/7 16:04
 * @Version 1.0
 */
public class LogProjectPlanServiceImpl extends ILogBusinessTypeEnumService {
    private static ICmProjectPlanService cmProjectPlanService = null;
    public LogProjectPlanServiceImpl() {
        cmProjectPlanService = SpringContextHolder.getBean(ICmProjectPlanService.class);
    }
    @Override
    public MobileApiResponse detail(String id) {
        if (StringUtil.isEmpty(id)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        CmProjectPlan entity = cmProjectPlanService.getById(id);
        if (entity == null) {
            return MobileApiResponse.error(ResultCode.RESULE_DATA_NONE);
        }
        return MobileApiResponse.ok(entity);
    }

    @Override
    public Map<String, String> getIdAndContent(Object entity) {
        Map<String, String> resultMap = new HashMap<>(2);
        CmProjectPlan po = (CmProjectPlan) entity;
        if (po != null) {
            resultMap.put("businessId", po.getId());
            resultMap.put("content", "计划\"" + po.getPlanName() + "\"的完成进度更新为：" + (po.getCompletionRatio() == null ? "0" : po.getCompletionRatio()) + "%");
        }
        return resultMap;
    }
}
