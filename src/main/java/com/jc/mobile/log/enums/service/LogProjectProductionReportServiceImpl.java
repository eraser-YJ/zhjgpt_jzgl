package com.jc.mobile.log.enums.service;

import com.jc.csmp.productionReport.domain.ProjectProductionReport;
import com.jc.csmp.productionReport.service.IProjectProductionReportService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.mobile.util.MobileApiResponse;

import java.util.HashMap;
import java.util.Map;

public class LogProjectProductionReportServiceImpl extends ILogBusinessTypeEnumService {

    private static IProjectProductionReportService projectProductionReportService = null;
    public LogProjectProductionReportServiceImpl() {
        projectProductionReportService = SpringContextHolder.getBean(IProjectProductionReportService.class);
    }

    @Override
    public MobileApiResponse detail(String id) {
        if (StringUtil.isEmpty(id)) {
            return MobileApiResponse.error(ResultCode.PARAM_IS_BLANK);
        }
        ProjectProductionReport entity = null;
        try {
            ProjectProductionReport entityPam = new ProjectProductionReport();
            entityPam.setId(id);
            entity = projectProductionReportService.get(entityPam);
        } catch (CustomException e) {
            e.printStackTrace();
        }
        if (entity == null) {
            return MobileApiResponse.error(ResultCode.RESULE_DATA_NONE);
        }
        return MobileApiResponse.ok(entity);
    }

    @Override
    public Map<String, String> getIdAndContent(Object entity) {
        Map<String, String> resultMap = new HashMap<>(2);
        ProjectProductionReport po = (ProjectProductionReport) entity;
        if (po != null) {
            resultMap.put("businessId", po.getId());
            resultMap.put("content", "标题：" + po.getProjectName() +"产值 "+po.getCompletedInvestmentAmount());
        }
        return resultMap;
    }
}
