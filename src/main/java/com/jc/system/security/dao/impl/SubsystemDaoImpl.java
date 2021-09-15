package com.jc.system.security.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.security.dao.ISubsystemDao;
import com.jc.system.security.domain.Subsystem;

/**
 * 子系统维护dao实现类
 * @author Administrator
 * @date 2020-06-29
 */
@Repository
public class SubsystemDaoImpl extends BaseServerDaoImpl<Subsystem> implements ISubsystemDao{

	public SubsystemDaoImpl(){}

	@Override
	public List<Subsystem> queryDeleteMenu(Subsystem subsystem) {
		return getTemplate().selectList(getNameSpace(subsystem) + ".queryDeleteMenu", subsystem);
	}

}