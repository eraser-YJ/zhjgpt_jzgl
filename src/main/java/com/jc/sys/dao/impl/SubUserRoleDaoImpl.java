package com.jc.sys.dao.impl;

import com.jc.foundation.exception.CustomException;
import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.sys.dao.ISubUserRoleDao;
import com.jc.sys.domain.SubUserRole;

import java.util.List;

/**
 *@ClassName SubUserRoleDaoImpl
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Repository
public class SubUserRoleDaoImpl extends BaseClientDaoImpl<SubUserRole> implements ISubUserRoleDao{

	public SubUserRoleDaoImpl(){}

	@Override
	public List<SubUserRole> getRolesByUserAndDeptId(SubUserRole subUserRole) throws CustomException {
		return getTemplate().selectList(getNameSpace(subUserRole) + ".getRolesByUserAndDeptId", subUserRole);
	}

	@Override
	public void deleteAll(SubUserRole vo) {
		getTemplate().delete(getNameSpace(new SubUserRole()) + ".deleteAll",vo);
	}
}