package com.jc.system.security.dao;

import java.util.List;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.Setting;
import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISystemDao extends IBaseDao<User> {

	/**
	 * 查询登录用户
	 * @param loginName
	 * @return
	 */
	User get(String loginName);

	/**
	 * 查询菜单
	 * @param userId
	 * @return
	 */
	List<Menu> queryMenu(String userId);

	/**
	 * 查询系统参数
	 * @param setting
	 * @return
	 */
	List<Setting> queryServerSetting(Setting setting);

	Department queryParentDeptForAllDept(Department department);

	/**
	 * 查询父部门
	 * @param department
	 * @return
     */
	Department queryParentDept(Department department);

	Department getRoot(Department department);
}