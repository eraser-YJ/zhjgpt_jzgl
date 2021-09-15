package com.jc.system.security.service;

import java.util.List;

import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.AdminSide;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IAdminSideService extends IBaseService<AdminSide>{

	/**
	 * 删除方法
	 * @param adminSide
	 * @return
	 */
	Integer deleteAdminSide(AdminSide adminSide);

	/**
	 * 查询管理员机构树
	 * @param adminSide
	 * @return
	 */
	List<AdminSide> queryManagerDeptRree(AdminSide adminSide);

	/**
	 * 根据部门ID查出AdminSide关联ID
	 * @param adminSide
	 * @return
	 */
	List<AdminSide> queryAdminSideIdByDeptId(AdminSide adminSide);

	/**
	 * 根据部门ID查出AdminSide关联IDS
	 * @param deptIds
	 * @return
	 */
	boolean queryAdminSideIdByDeptIds(String[] deptIds);

	/**
	 * 恢复删除方法
	 * @param adminSide
	 * @return
	 */
	Integer deleteBack(AdminSide adminSide);
	
}