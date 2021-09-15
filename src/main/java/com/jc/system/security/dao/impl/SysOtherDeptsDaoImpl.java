package com.jc.system.security.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.ISysOtherDeptsDao;
import com.jc.system.security.domain.SysOtherDepts;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class SysOtherDeptsDaoImpl extends BaseServerDaoImpl<SysOtherDepts> implements ISysOtherDeptsDao{

	@Override
	public Integer save(List<SysOtherDepts> list) {
		return getTemplate().insert(getNameSpace(new SysOtherDepts()) +".insert", list);
	}

	@Override
	public List<SysOtherDepts> query(SysOtherDepts sysOtherDepts) {
		return getTemplate().selectList(getNameSpace(new SysOtherDepts()) +".query", sysOtherDepts);
	}

	@Override
	public int deleteOtherDept(String userId) {
		return getTemplate().delete(getNameSpace(new SysOtherDepts()) + ".deleteOtherDept" , userId);
	}

	@Override
	public Integer updateDeleteFlagByIds(SysOtherDepts sysOtherDepts) {
		return getTemplate().update(getNameSpace(new SysOtherDepts()) + ".updateDeleteFlagByIds" , sysOtherDepts);
	}

	@Override
	public Integer deleteBack(SysOtherDepts sysOtherDepts) {
		return getTemplate().update(getNameSpace(sysOtherDepts) + ".deleteBackByIds", sysOtherDepts);
	}

}