package com.jc.dlh.service;

import com.jc.dlh.domain.DlhUserRole;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;

/**
 * @title 统一数据资源中心
 * @description 业务接口类 
 * @author lc 
 * @version 2020-03-18
 */

public interface IDlhUserRoleService extends IBaseService<DlhUserRole> {
	/**
	 * @description 根据主键删除多条记录方法
	 * @param DlhUserRole dlhUserRole 实体类
	 * @return Integer 处理结果
	 * @author lc 
	 * @version 2020-03-18
	 */
	public Integer deleteByIds(DlhUserRole dlhUserRole) throws CustomException;
}