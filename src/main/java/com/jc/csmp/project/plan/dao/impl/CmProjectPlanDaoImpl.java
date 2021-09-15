package com.jc.csmp.project.plan.dao.impl;

import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import org.springframework.stereotype.Repository;

import com.jc.csmp.project.plan.dao.ICmProjectPlanDao;
import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;

/**
 * 建设管理-项目计划管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectPlanDaoImpl extends BaseClientDaoImpl<CmProjectPlan> implements ICmProjectPlanDao {

	public CmProjectPlanDaoImpl(){}

	@Override
	public void saveByTemplateStage(String templateId, String projectId) {
		CmProjectPlan entity = new CmProjectPlan();
		entity.setTemplateId(templateId);
		entity.setProjectId(projectId);
		this.getTemplate().insert(this.getNameSpace(entity) + "." + "insertByTemplateId", entity);
	}

}