package com.jc.sys.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.sys.dao.ISubUserDao;
import com.jc.sys.domain.SubUser;

/**
 *@ClassName SubUserDaoImpl
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Repository
public class SubUserDaoImpl extends BaseClientDaoImpl<SubUser> implements ISubUserDao{

	public SubUserDaoImpl(){}

	@Override
	public void deleteAll(SubUser vo) {
		getTemplate().delete(getNameSpace(new SubUser()) + ".deleteAll",vo);
	}

	@Override
	public void reTheme(SubUser vo) {
		getTemplate().update(getNameSpace(new SubUser()) + ".reTheme",vo);
	}
}