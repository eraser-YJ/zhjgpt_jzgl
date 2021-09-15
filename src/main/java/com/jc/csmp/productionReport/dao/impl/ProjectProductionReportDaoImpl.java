package com.jc.csmp.productionReport.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.productionReport.dao.IProjectProductionReportDao;
import com.jc.csmp.productionReport.domain.ProjectProductionReport;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class ProjectProductionReportDaoImpl extends BaseClientDaoImpl<ProjectProductionReport> implements IProjectProductionReportDao{

	public ProjectProductionReportDaoImpl(){}

}