package com.jc.csmp.plan.dao.impl;

import com.jc.foundation.exception.DBException;
import org.springframework.stereotype.Repository;

import com.jc.csmp.plan.dao.IProjectYearPlanItemDao;
import com.jc.csmp.plan.domain.ProjectYearPlanItem;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;

/**
 * @title   
 * @version
 */
@Repository
public class ProjectYearPlanItemDaoImpl extends BaseClientDaoImpl<ProjectYearPlanItem> implements IProjectYearPlanItemDao {

	public ProjectYearPlanItemDaoImpl(){}

	public Integer save(ProjectYearPlanItem o) throws DBException {
		Integer result = -1;

		try {
			result = this.getTemplate().insert(this.getNameSpace(o) + "." + "insert", o);
			return result;
		} catch (Exception var5) {
			this.log.error(var5, var5);
			DBException exception = new DBException(var5);
			exception.setLogMsg("数据库添加数据发生错误");
			throw exception;
		}
	}

}