package com.jc.archive.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.archive.ArchiveException;
import com.jc.archive.dao.IPermissionDao;
import com.jc.archive.domain.Permission;

/**
 * @title  GOA2.0源代码
 * @description  dao实现类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 闻瑜
 * @version  2014-06-19
 */
@Repository
public class PermissionDaoImpl extends BaseClientDaoImpl<Permission> implements IPermissionDao{

	public PermissionDaoImpl(){}

	/**
	 * 物理删除权限
	 * @param Permission permission 实体类
	 * @author weny
	 * @version  2014-06-19
	 */
	public Integer deletePermission(Permission permission) {
		// 查询行数
		Integer result = getTemplate().delete(getNameSpace(permission) + "."+SQL_DELETE_PHYSICAL, permission);
		return result;
	}

	/**
	 * 权限查询
	 * @param Permission permission 实体类
	 * @author weny
	 * @version  2014-06-23
	 */
	public List<Permission> queryPermission(Permission permission) {
		// 查询行数
		try{
			List<Permission> result = getTemplate().selectList(getNameSpace(permission) + "."+"queryPermission", permission);
			return result;
		} catch (Exception e){
			e.getStackTrace();
		}
		return null;
	}

	@Override
	public Long getFolderPermissionCount(Permission permission)
			throws ArchiveException {
		// TODO Auto-generated method stub
		Object rowsCountObject = getTemplate().selectOne(getNameSpace(permission) + ".queryFolderPermissionCount", permission);
		Long rowsCount = (Long) rowsCountObject;
		return rowsCount;
	}
	
	
}