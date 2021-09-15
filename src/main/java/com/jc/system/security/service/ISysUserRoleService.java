package com.jc.system.security.service;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.SysUserRole;
import com.jc.foundation.service.IBaseService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISysUserRoleService extends IBaseService<SysUserRole>{
	int deleteSysUserRole(SysUserRole sysUserRole) throws CustomException;
	Integer deleteBack(SysUserRole sysUserRole);
}