package com.jc.sys.service;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;
import com.jc.sys.domain.SubRoleGroupMenu;

import java.util.Map;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public interface ISubRoleGroupMenuService extends IBaseService<SubRoleGroupMenu> {
	/**
	 * @description 根据主键删除多条记录方法
	 * @param  subRoleGroupMenu 实体类
	 * @return Integer 处理结果
	 * @author
	 * @version 2018-04-12
	 */
	public Integer deleteByIds(SubRoleGroupMenu subRoleGroupMenu) throws CustomException;

	/**
	 * @description 按照MenuIds保存数据
	 * @param  subRoleGroupMenu 实体类
	 * @return void
	 * @author
	 * @version 2018-04-12
	 */
	public void saveByMenuIds(SubRoleGroupMenu subRoleGroupMenu) throws CustomException;
	

	/**
	 * 按照roleGroupIds删除信息
	 *
	 * @param subRoleGroupMenu
	 * @return
	 */
	public Integer deleteByRoleGroupIds(SubRoleGroupMenu subRoleGroupMenu);

	/**
	 * @description 根据部门ID返回子系统菜单组集合
	 * @param  deptId 部门Id
	 * @return void
	 * @author
	 * @version 2018-04-12
	 */
	public Map<String, Object> getMenuIdsForDeptId(String deptId) throws CustomException;

}