package com.jc.system.security.dao;

import java.util.List;

import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;
import com.jc.system.security.domain.Department;

/**
 * 组织dao
 * @author Administrator 
 * @date 2020-06-29
 */
public interface IDepartmentDao extends IBaseDao<Department> {
	PageManager searchQuery(Department department, PageManager page);
	List<Department> queryTree(Department department);
	Department queryOne(Department department);
	List<Department> queryAllByDeptId(Department department);
	Integer updateByDeptIds(Department department);
	List<Department> getDeptAndUserByDeptId(String id);
	List<Department> getDeptByDeptId(String id);
	List<Department> getDeptByOrgId(String id);
	List<Department> queryManagerDeptTree(String userId);
	List<Department> getQueryDeptPidByOrgId(String id) ;
	List<Department> getAllOrgNoDeptTree();
	List<Department> getDeptByUserIds(Department dept);
	List<Department> getDeptByDeptIdForDel(String id);
	List<Department> getSameLevelDeptByParentId(String parentId);
	List<Department> getUserCountForAll();
}
