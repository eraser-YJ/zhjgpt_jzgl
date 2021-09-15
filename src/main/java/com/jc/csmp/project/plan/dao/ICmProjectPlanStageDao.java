package com.jc.csmp.project.plan.dao;

import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.foundation.dao.IBaseDao;

import java.util.List;

/**
 * 建设管理-项目计划阶段管理Dao
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectPlanStageDao extends IBaseDao<CmProjectPlanStage> {
    /**
     * 根据模板将模板的阶段保存到项目中
     * @param
     * @param
     */
    void saveByTemplateStage(List<CmProjectPlanTemplateStage> cmProjectPlanTemplateStageList);

    List<CmProjectPlanStage> queryGenttCharts(CmProjectPlanStage cmProjectPlanStage);
}
