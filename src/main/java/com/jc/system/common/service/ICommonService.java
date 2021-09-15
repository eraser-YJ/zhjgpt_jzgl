package com.jc.system.common.service;

import java.util.List;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.beans.UserBean;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Role;
import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface ICommonService {

	String getDBSysdate() throws CustomException;

	/**
	 * 获得部门树和人员数据
	 * @return
	 * @throws Exception
	 */
	List<Department> getDeptsAndUser() throws Exception;

	/**
	 * 查询管理员部门树
	 * @return
	 */
	List getDeptTree();

	/**
	 * 查询管理员机构树(包括部门)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List queryManagerDeptRree();

	/**
	 * @param orgId
	 * @return
	 */
	List queryManagerDeptRree(String orgId);

	/**
	 * 查询管理员机构树(不包括部门)
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List queryManagerOrgRree();

	/**
	 * 根据部门ID获取本部门及所有子部门及人员信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<Department> getDeptAndUserByDeptId(String id) throws Exception ;

	/**
	 * 获取在线用户登录名
	 * @return
	 */
	List<UserBean> getOnlineUserBean();

	/**
	 * 获取在线用户数
	 * @return
	 */
	int getOnlineUserCount();

	/**
	 * 根据用户ID获取用户信息
	 * @param id
	 * @return
	 */
	User getUserById(String id);

	/**
	 * 用户管理角色列表
	 * @return
	 * @throws CustomException
	 */
	List<Role> getRolesForUser() throws CustomException;

	/**
	 * 用户管理角色列表
	 * @param orgId
	 * @return
	 * @throws CustomException
	 */
	List<Role> getRolesForUser(String orgId) throws CustomException;

	/**
	 * 根据部门ID获取本部门及所有子部门司机信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	List<User> getDriverByDeptId(String id) throws Exception ;

	/**
	 * 根据机构ID获取所有领导信息
	 * @param user
	 * @return
	 * @throws Exception
	 */
	List<User> getLeaderUserByDeptId(User user) throws Exception ;

	/**
	 * 根据登录人所以在机构ID获取本机构所有子部门司机信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	List<User> getDriverByOrgId(String orgId) throws Exception ;

	/**
	 * 根据登录人所以在机构ID获取本机构所有子部门信息
	 * @param deptId
	 * @return
	 * @throws Exception
	 */
	List<Department> getDeptByDeptId(String deptId) throws Exception ;

	/**
	 * 根据机构ID获取本机构所有子部门人员信息
	 * @param orgId
	 * @return
	 * @throws Exception
	 */
	List<User> getUsersByOrgId(String orgId) throws Exception ;

	/**
	 * 根据用户角色查询信息栏目
	 * @param orgReult
	 * @return
	 * @throws CustomException
	 */
	String[] getPermissionOrg(String[] orgReult) throws CustomException;

}
