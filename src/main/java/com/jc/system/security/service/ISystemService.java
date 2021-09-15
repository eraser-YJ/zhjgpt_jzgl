package com.jc.system.security.service;

import javax.servlet.http.HttpServletRequest;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.Setting;
import com.jc.system.security.domain.User;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISystemService {

	User get(String loginName);
	
	Department queryOrgIdAndName(Department department);

	Department queryOrgIdAndName(Department department,List<Department> deptList);

	Department queryOrgIdAndNameForAllDept(Department department);
	
	void loginCallBack(HttpServletRequest request) throws CustomException;
	
	void logoutCallBack(HttpServletRequest request) throws CustomException;
	
	void loginCallBack4M(User user, HttpServletRequest request) throws CustomException;
	
	void logoutCallBack4M(HttpServletRequest request) throws CustomException;

	/**
	 * 查询菜单
	 * @return List<Menu>
	 */
	List<Menu> queryMenu(String userId);

	/**
	 * 查询系统参数
	 * @return List<Setting>
	 */
	List<Setting> queryServerSetting(Setting setting);

	/**
	 * 查询父部门
	 * @param department
	 * @return
	 */
	Department queryParentDept(Department department);

	Department getRoot(Department department);
}