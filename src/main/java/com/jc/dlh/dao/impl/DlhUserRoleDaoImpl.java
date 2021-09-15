package com.jc.dlh.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.dlh.dao.IDlhUserRoleDao;
import com.jc.dlh.domain.DlhUserRole;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title 统一数据资源中心
 * @description  dao实现类
 * Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved
 * Company 长春奕迅
 * @author lc  
 * @version  2020-03-18
 */
@Repository
public class DlhUserRoleDaoImpl extends BaseClientDaoImpl<DlhUserRole> implements IDlhUserRoleDao{

	public DlhUserRoleDaoImpl(){}

	public static final String SQL_DELETEBYYSERID= "deleteByUserId";

	public Integer deleteByUserId(DlhUserRole dlhUserRole) throws DBException {
		Integer result = -1;
		try {
			result = getTemplate().update(getNameSpace(dlhUserRole)   + "."+SQL_DELETEBYYSERID, dlhUserRole);
		} catch (Exception e) {
			DBException exception = new DBException(e);
			exception.setLogMsg("数据库更新数据发生错误");
			throw exception;
		}
		if(result == 0){
			throw new ConcurrentException("数据已被修改，请刷新后重新操作");
		}
		return result;
	}
}