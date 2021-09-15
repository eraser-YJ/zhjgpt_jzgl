package com.jc.sys.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.sys.dao.ISubDepartmentDao;
import com.jc.sys.domain.SubDepartment;

import java.util.List;

/**
 *@ClassName SubDepartmentDaoImpl
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Repository
public class SubDepartmentDaoImpl extends BaseClientDaoImpl<SubDepartment> implements ISubDepartmentDao{

	public SubDepartmentDaoImpl(){}
	public static final String SQL_QUERY_GET = "queryOne";					//获取部门信息
	public static final String SQL_QUERY_BY_DEPTID = "queryByDeptId";

	@Override
	public SubDepartment queryOne(SubDepartment department){
		return getTemplate().selectOne(getNameSpace(department) + "."+SQL_QUERY_GET, department);
	}

	@Override
	public List<SubDepartment> getDeptsByDeptId(String id) {
		return getTemplate().selectList(getNameSpace(new SubDepartment()) + "." + SQL_QUERY_BY_DEPTID, id);
	}

	@Override
	public void deleteAll(SubDepartment department) {
		getTemplate().delete(getNameSpace(new SubDepartment()) + ".deleteAll",department);
	}
}