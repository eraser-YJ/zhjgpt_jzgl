package com.jc.system.security.util;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.foundation.util.StringUtil;
import com.jc.system.common.util.CacheUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.ISystemService;

import java.util.List;

/**
 * 部门缓存
 * @author Administrator
 * @date 2020-06-30
 */
public class DeptCacheUtil {

	private DeptCacheUtil() {
		throw new IllegalStateException("DeptCacheUtil class");
	}

	private static final String CACHE_DEPT_INFO = "dept_info_";
	private static IDepartmentService departmentService = SpringContextHolder.getBean(IDepartmentService.class);
	private static ISystemService systemService = SpringContextHolder.getBean(ISystemService.class);

	/**
	 * 缓存查询单个组织
	 * @param deptId
	 * @return
	 * @throws CustomException
	 */
	public static Department getDeptById(String deptId) throws CustomException {
		Department d = (Department) CacheUtils.get(CACHE_DEPT_INFO + deptId);
		if(d == null){
			Department dept = new Department();
			dept.setId(deptId);
			dept.setDeleteFlag(0);
			d = departmentService.get(dept);
			if(d != null){
				Department department = new Department();
				department.setId(d.getId());
				department = systemService.queryOrgIdAndName(department);
				if (department != null){
					d.setOrgId(department.getId());
					d.setOrgName(department.getName());
					CacheUtils.put(CACHE_DEPT_INFO + d.getId(), d);
				}
			}
		} else if(d.getOrgId() == null) {
			Department dept = new Department();
			dept.setId(deptId);
			dept.setDeleteFlag(0);
			d = departmentService.get(dept);
			if(d != null){
				Department department = new Department();
				department.setId(d.getId());
				department = systemService.queryOrgIdAndName(department);
				if (department != null){
					d.setOrgId(department.getId());
					d.setOrgName(department.getName());
					CacheUtils.put(CACHE_DEPT_INFO + d.getId(), d);
				}
			}
		}
		return d;
	}

	/**
	 * 缓存查询单个组织
	 * @param deptId
	 * @return
	 * @throws CustomException
	 */
	public static Department getNewDeptById(String deptId) throws CustomException {
		Department dept = new Department();
		dept.setId(deptId);
		dept.setDeleteFlag(null);
		Department d = departmentService.get(dept);
		if(d != null){
			Department department = new Department();
			department.setId(d.getId());
			department.setDeleteFlag(null);
			department = systemService.queryOrgIdAndNameForAllDept(department);
			d.setOrgId(department.getId());
			d.setOrgName(department.getName());
		}
		return d;
	}

	public static String[] getDeptIdsById(String id) {
		if (StringUtil.isEmpty(id)) {
			return new String[] {"0"};
		}
		Department param = new Department();
		param.setId(id);
		List<Department> deptList = departmentService.queryAllByDeptId(param);
		if (deptList != null) {
			int size = deptList.size();
			if (size > 0) {
				String[] array = new String[size];
				int index = 0;
				for (Department entity : deptList) {
					array[index++] = entity.getId();
				}
				return array;
			}
		}
		return new String[] {"0"};
	}

	/**
	 * 根据id获取code
	 * @param id
	 * @return
	 */
	public static String getCodeById(String id) {
		String code = "-1";
		try {
			Department department = getDeptById(id);
			if (department != null) {
				if (!StringUtil.isEmpty(department.getCode())) {
					code = department.getCode();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return code;
	}

	/**
	 * 根据id获取name
	 * @param id
	 * @return
	 */
	public static String getNameById(String id) {
		String name = "";
		if (StringUtil.isEmpty(id)) {
			return name;
		}
		try {
			Department department = getDeptById(id);
			if (department != null) {
				name = department.getName();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return name;
	}

	/**
	 * 根据id获取负责人
	 * @param id
	 * @return
	 */
	public static String getLeaderById(String id) {
		String name = "";
		if (StringUtil.isEmpty(id)) {
			return name;
		}
		try {
			Department department = getDeptById(id);
			if (department != null) {
				name = department.getLeaderId() == null ? "" :department.getLeaderId();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return name;
	}
}
