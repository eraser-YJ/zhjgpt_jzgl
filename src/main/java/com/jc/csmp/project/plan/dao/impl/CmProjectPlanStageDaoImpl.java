package com.jc.csmp.project.plan.dao.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanStageDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.DBException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 建设管理-项目计划阶段管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectPlanStageDaoImpl extends BaseClientDaoImpl<CmProjectPlanStage> implements ICmProjectPlanStageDao {
	public CmProjectPlanStageDaoImpl(){}

	@Override
	public void saveByTemplateStage(List<CmProjectPlanTemplateStage> cmProjectPlanTemplateStageList) {

		//this.getTemplate().insert(this.getNameSpace(cmProjectPlanTemplateStageList) + "." + "insertByTemplateId", cmProjectPlanTemplateStageList);
	}

	@Override
	public List<CmProjectPlanStage> queryGenttCharts(CmProjectPlanStage cmProjectPlanStage) {

		return  this.getTemplate().selectList(this.getNameSpace(cmProjectPlanStage) + "." + "queryGenttCharts", cmProjectPlanStage);
	}
}
