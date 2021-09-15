package com.jc.csmp.project.plan.dao.impl;

import com.jc.csmp.project.plan.dao.ICmProjectPlanTemplateDao;
import com.jc.csmp.project.plan.domain.CmProjectPlanTemplate;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-项目计划模板管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectPlanTemplateDaoImpl extends BaseClientDaoImpl<CmProjectPlanTemplate> implements ICmProjectPlanTemplateDao {
	public CmProjectPlanTemplateDaoImpl(){}
}
