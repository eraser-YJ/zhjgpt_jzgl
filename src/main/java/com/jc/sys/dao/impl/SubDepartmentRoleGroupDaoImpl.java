package com.jc.sys.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.sys.dao.ISubDepartmentRoleGroupDao;
import com.jc.sys.domain.SubDepartmentRoleGroup;

/**
 *@ClassName SubDepartmentRoleGroupDaoImpl
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Repository
public class SubDepartmentRoleGroupDaoImpl extends BaseClientDaoImpl<SubDepartmentRoleGroup> implements ISubDepartmentRoleGroupDao{

	public SubDepartmentRoleGroupDaoImpl(){}

	@Override
	public void deleteAll(SubDepartmentRoleGroup vo) {
		getTemplate().delete(getNameSpace(new SubDepartmentRoleGroup()) + ".deleteAll",vo);
	}
}