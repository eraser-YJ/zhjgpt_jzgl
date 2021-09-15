package com.jc.csmp.plan.ctrl.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.plan.ctrl.dao.IProjectYearPlanCtrlDao;
import com.jc.csmp.plan.ctrl.domain.ProjectYearPlanCtrl;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class ProjectYearPlanCtrlDaoImpl extends BaseClientDaoImpl<ProjectYearPlanCtrl> implements IProjectYearPlanCtrlDao{

	public ProjectYearPlanCtrlDaoImpl(){}

}