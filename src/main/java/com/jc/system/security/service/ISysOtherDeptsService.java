package com.jc.system.security.service;

import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.SysOtherDepts;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISysOtherDeptsService extends IBaseService<SysOtherDepts>{

	/**
	 * 根据主键删除多条记录方法
	 * @param userId
	 * @return
	 */
	int deleteOtherDept(String userId);

	/**
	 * 修改删除状态
	 * @param sysOtherDepts
	 * @return
	 */
	Integer updateDeleteFlagByIds(SysOtherDepts sysOtherDepts) ;

	/**
	 * 恢复删除方法
	 * @param sysOtherDepts
	 * @return
	 */
	Integer deleteBack(SysOtherDepts sysOtherDepts);
}