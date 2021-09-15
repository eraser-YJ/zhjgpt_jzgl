package com.jc.csmp.project.plan.dao.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanTemplateStageDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 建设管理-项目计划模板阶段管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectPlanTemplateStageDaoImpl extends BaseClientDaoImpl<CmProjectPlanTemplateStage> implements ICmProjectPlanTemplateStageDao {
	public CmProjectPlanTemplateStageDaoImpl(){}

	@Override
	public List<CmProjectPlanTemplateStage> selectByprojectId(CmProjectPlanTemplateStage cmProjectPlanTemplateStage) {
		List<CmProjectPlanTemplateStage> list = this.getTemplate().selectList(this.getNameSpace(cmProjectPlanTemplateStage) + ".selectByprojectId", cmProjectPlanTemplateStage);
		return list;
	}
}
