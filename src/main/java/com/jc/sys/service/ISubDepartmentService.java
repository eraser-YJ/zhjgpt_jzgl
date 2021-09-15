package com.jc.sys.service;

import com.jc.foundation.service.IBaseService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.domain.PageManager;
import com.jc.sys.domain.SubDepartment;
import com.jc.system.security.domain.User;

import java.util.List;
import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public interface ISubDepartmentService extends IBaseService<SubDepartment>{
	/**
	* @description 根据主键删除多条记录方法
	* @param  subDepartment 实体类
	* @return Integer 处理结果
	* @author
	* @version  2018-04-04 
	*/
	public Map<String, Object> deleteByIds(SubDepartment subDepartment) throws CustomException;

	/**
	 * @description 查询管理员机构树(包括部门)
	 * @author
	 * @version 2018-04-04
	 */
	public List queryManagerDeptRree(User user);

	/**
	 * 获取全部部门及人员
	 * @return
	 * @throws Exception
	 * @author 张继伟
	 * @version 1.0 2014年5月15日 上午8:27:24
	 */
	public String getAllDeptAndUser(User user) throws Exception ;

	/**
	 * 查询部门信息(带上级部门)
	 * @param department
	 * @author
	 * @version 2014-04-15
	 */
	public SubDepartment queryOne(SubDepartment department) throws CustomException;

	public List<SubDepartment> getDeptsByDeptId(String id);

	public void deleteAll(SubDepartment department);
}