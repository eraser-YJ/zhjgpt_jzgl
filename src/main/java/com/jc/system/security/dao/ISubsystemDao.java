package com.jc.system.security.dao;

import java.util.List;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.security.domain.Subsystem;

/**
 * 子系统维护dao
 * @author Administrator
 * @date 2020-06-29
 */
public interface ISubsystemDao extends IBaseDao<Subsystem>{

	/**
	 * 查询删除子系统关联的一级菜单
	 * @param subsystem
	 * @return
	 */
	List<Subsystem> queryDeleteMenu(Subsystem subsystem);
}
