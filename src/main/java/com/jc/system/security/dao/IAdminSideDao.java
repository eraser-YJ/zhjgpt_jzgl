package com.jc.system.security.dao;

import java.util.List;

import com.jc.system.security.domain.AdminSide;
import com.jc.foundation.dao.IBaseDao;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IAdminSideDao extends IBaseDao<AdminSide>{

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
	 * 恢复删除用户
	 * @param adminSide
	 * @return
	 */
	Integer deleteBack(AdminSide adminSide);
}
