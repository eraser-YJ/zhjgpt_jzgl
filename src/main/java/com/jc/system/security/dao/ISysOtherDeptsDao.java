package com.jc.system.security.dao;

import java.util.List;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.security.domain.SysOtherDepts;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface ISysOtherDeptsDao extends IBaseDao<SysOtherDepts>{

	/**
	 * 保存方法
	 * @param list
	 * @return
	 */
	@Override
    Integer save(List<SysOtherDepts> list);

	/**
	 * 查询方法
	 * @param sysOtherDepts
	 * @return
	 */
	List<SysOtherDepts> query(SysOtherDepts sysOtherDepts);

	/**
	 * 删除方法
	 * @param userId
	 * @return
	 */
	int deleteOtherDept(String userId);

	/**
	 * 修改删除状态
	 * @param sysOtherDepts
	 * @return
	 */
	Integer updateDeleteFlagByIds(SysOtherDepts sysOtherDepts);

	/**
	 * 恢复删除用户
	 * @param sysOtherDepts
	 * @return
	 */
	Integer deleteBack(SysOtherDepts sysOtherDepts);
}
