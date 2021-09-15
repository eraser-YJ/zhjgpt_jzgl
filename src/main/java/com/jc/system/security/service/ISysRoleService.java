package com.jc.system.security.service;

import java.util.List;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.SysRole;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISysRoleService {
	PageManager query(SysRole sysRole,PageManager page) ;
	List<SysRole> queryAll(SysRole sysRole) ;
	Integer deleteByIds(SysRole sysRole) throws CustomException;
	Integer save(SysRole sysRole) throws CustomException;
	Integer update(SysRole sysRole) throws CustomException;
	SysRole get(SysRole sysRole) throws Exception;
}