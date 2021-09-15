package com.jc.system.security.dao.impl;

import java.util.List;

import com.jc.system.security.DepartmentConstant;
import com.jc.system.security.domain.Setting;
import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.ISystemDao;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.Menu;
import com.jc.system.security.domain.User;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class SystemDaoImpl extends BaseServerDaoImpl<User> implements ISystemDao {

	@Override
    public User get(String loginName) {
		return getTemplate().selectOne("com.jc.system.systemMap.query", loginName);
	}

	@Override
    public List<Menu> queryMenu(String userId) {
		return getTemplate().selectList("com.jc.system.systemMap.queryMenus", userId);
	}

	@Override
	public List<Setting> queryServerSetting(Setting setting) {
		return getTemplate().selectList("com.jc.system.systemMap.querySetting", setting);
	}

	@Override
	public Department queryParentDeptForAllDept(Department department) {
		return (Department)getTemplate().selectOne("com.jc.system.systemMap.queryParentDeptForAllDept", department);
	}

	@Override
	public Department queryParentDept(Department department) {
		return (Department)getTemplate().selectOne("com.jc.system.systemMap.getParentDept", department);
	}

	@Override
    public Department getRoot(Department department){
		return (Department)getTemplate().selectOne("com.jc.system.systemMap.getRoot", department.getId());
	}
}