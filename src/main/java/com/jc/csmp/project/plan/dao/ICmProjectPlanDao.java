package com.jc.csmp.project.plan.dao;

import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.foundation.dao.IBaseDao;


/**
 * 建设管理-项目计划管理Dao
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectPlanDao extends IBaseDao<CmProjectPlan>{
    /**
     * 根据模板将模板的阶段保存到项目中
     * @param templateId
     * @param projectId
     */
    void saveByTemplateStage(String templateId, String projectId);
}
