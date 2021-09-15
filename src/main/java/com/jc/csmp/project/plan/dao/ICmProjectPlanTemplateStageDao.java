package com.jc.csmp.project.plan.dao;

import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.foundation.dao.IBaseDao;

import java.util.List;

/**
 * 建设管理-项目计划模板阶段管理Dao
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public interface ICmProjectPlanTemplateStageDao extends IBaseDao<CmProjectPlanTemplateStage> {
    List<CmProjectPlanTemplateStage> selectByprojectId(CmProjectPlanTemplateStage cmProjectPlanTemplateStage);
}
