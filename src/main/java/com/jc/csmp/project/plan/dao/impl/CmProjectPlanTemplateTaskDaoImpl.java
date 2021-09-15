package com.jc.csmp.project.plan.dao.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanTemplateTaskDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateTask;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-项目计划模板阶段管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectPlanTemplateTaskDaoImpl extends BaseClientDaoImpl<CmProjectPlanTemplateTask> implements ICmProjectPlanTemplateTaskDao {
	public CmProjectPlanTemplateTaskDaoImpl(){}
}
