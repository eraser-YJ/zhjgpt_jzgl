package com.jc.mobile.log.enums.service;

import com.jc.csmp.project.plan.domain.CmProjectPlanImages;
import com.jc.csmp.project.plan.service.ICmProjectPlanImagesService;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.util.MobileApiResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * 形象上报日志
 * @Author 常鹏
 * @Date 2020/8/7 16:04
 * @Version 1.0
 */
public class LogPlanImagesServiceImpl extends ILogBusinessTypeEnumService {
    private static ICmProjectPlanImagesService cmProjectPlanImagesService = null;
    public LogPlanImagesServiceImpl() {
        cmProjectPlanImagesService = SpringContextHolder.getBean(ICmProjectPlanImagesService.class);
    }
    @Override
    public MobileApiResponse detail(String id) {
        if (StringUtil.isEmpty(id)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        CmProjectPlanImages entity = cmProjectPlanImagesService.getById(id);
        if (entity == null) {
            return MobileApiResponse.error(ResultCode.RESULE_DATA_NONE);
        }
        return MobileApiResponse.ok(entity);
    }

    @Override
    public Map<String, String> getIdAndContent(Object entity) {
        Map<String, String> resultMap = new HashMap<>(2);
        CmProjectPlanImages po = (CmProjectPlanImages) entity;
        if (po != null) {
            resultMap.put("businessId", po.getId());
            resultMap.put("content", "标题：" + po.getTitle());
        }
        return resultMap;
    }
}
