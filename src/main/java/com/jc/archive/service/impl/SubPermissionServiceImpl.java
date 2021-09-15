package com.jc.archive.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.archive.dao.ISubPermissionDao;
import com.jc.archive.domain.SubPermission;
import com.jc.archive.service.ISubPermissionService;

/**
 * @title  GOA2.0源代码
 * @description  业务服务类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 闻瑜
 * @version  2014-06-19
 */
@Service
public class SubPermissionServiceImpl extends BaseServiceImpl<SubPermission> implements ISubPermissionService{

	private ISubPermissionDao subPermissionDao;
	
	public SubPermissionServiceImpl(){}
	
	@Autowired
	public SubPermissionServiceImpl(ISubPermissionDao subPermissionDao){
		super(subPermissionDao);
		this.subPermissionDao = subPermissionDao;
	}
	
	/**
	 * 物理删除权限
	 * @param Permission permission 实体类
	 * @author weny
	 * @version  2014-06-19
	 */
	@Override
	public Integer deleteSubPermission(SubPermission subpermission) {
		return subPermissionDao.deleteSubPermission(subpermission);
	}
	
	/**
	 * 修改部门人员权限
	 * @param Permission permission 实体类
	 * @author weny
	 * @version  2014-06-23
	 */
	@Override
	public List<SubPermission> updatePermission(SubPermission subpermission) {
		return subPermissionDao.updateSubPermission(subpermission);
	}
	
	/**
	 * 修改部门人员权限
	 * @param Permission permission 实体类
	 * @author weny
	 * @version  2014-06-23
	 */
	@Override
	public Integer updateBatchPermission(SubPermission subpermission) {
		return subPermissionDao.deleteSubPermission(subpermission);
	}
}