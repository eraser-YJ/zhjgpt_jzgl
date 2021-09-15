package com.jc.sys.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.sys.dao.ISubRoleDao;
import com.jc.sys.domain.SubRole;

/**
 *@ClassName SubRoleDaoImpl
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Repository
public class SubRoleDaoImpl extends BaseClientDaoImpl<SubRole> implements ISubRoleDao{

	public SubRoleDaoImpl(){}

	@Override
	public void deleteAll(SubRole vo) {
		getTemplate().delete(getNameSpace(new SubRole()) + ".deleteAll",vo);
	}
}