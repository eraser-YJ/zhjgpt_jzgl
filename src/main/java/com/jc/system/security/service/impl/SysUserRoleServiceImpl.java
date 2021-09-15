package com.jc.system.security.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.dao.ISysUserRoleDao;
import com.jc.system.security.service.ISysUserRoleService;
import com.jc.system.security.domain.SysUserRole;
import com.jc.foundation.service.impl.BaseServiceImpl;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class SysUserRoleServiceImpl extends BaseServiceImpl<SysUserRole> implements ISysUserRoleService{

	@Resource
	private ISysUserRoleDao sysUserRoleDao;
	
	@Autowired
	public SysUserRoleServiceImpl(ISysUserRoleDao dao) {
		super(dao);
		this.sysUserRoleDao = dao;
	}

	public SysUserRoleServiceImpl(){
		
	}

	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int deleteSysUserRole(SysUserRole sysUserRole) throws CustomException {
		return sysUserRoleDao.deleteSysUserRole(sysUserRole);
	}

	@Override
	public Integer deleteBack(SysUserRole sysUserRole) {
		return sysUserRoleDao.deleteBack(sysUserRole);
	}
}