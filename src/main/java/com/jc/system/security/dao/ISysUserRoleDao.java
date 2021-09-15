package com.jc.system.security.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.security.domain.SysUserRole;
import com.jc.system.security.domain.User;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISysUserRoleDao extends IBaseDao<SysUserRole>{

	/**
	  * 删除方法
	  * @param sysUserRole
	  * @return int
	*/
	int deleteSysUserRole(SysUserRole sysUserRole);

	/**
	  * 恢复删除用户
	  * @param sysUserRole
	  * @return
	*/
	Integer deleteBack(SysUserRole sysUserRole);

	List<User> queryUserByRole(SysUserRole sysUserRole);
}
