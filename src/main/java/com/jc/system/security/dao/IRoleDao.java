package com.jc.system.security.dao;

import java.util.List;

import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.Role;

/**
 * 角色
 * @author Administrator
 * @date 2020-07-01
 */
public interface IRoleDao extends IBaseDao<Role> {

	/**
	 * 用户管理角色列表
	 * @param role
	 * @return
	 * @throws CustomException
	 */
	List<Role> getRolesForUser(Role role) throws CustomException;

	Role getRoleById(Role role) throws CustomException;

	/**
	 * 根据用户信息获取角色Id信息
	 * @param role
	 * @return
	 */
	List<Role> getRolesByUserId(Role role);

	/**
	 * 根据部门名称或角色名称模糊查询
	 * @param role
	 * @return
	 */
	List<Role> getRolesByRoleOrDept(Role role);

	/**
	 * 根据用户id及菜单id获取角色列表
	 * @param role
	 * @return
	 */
	List<Role> getRolesByUserIdAndMenuId(Role role);
}