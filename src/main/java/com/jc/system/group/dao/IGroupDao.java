package com.jc.system.group.dao;

import java.util.List;

import com.jc.system.group.domain.Group;
import com.jc.foundation.dao.IBaseDao;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IGroupDao extends IBaseDao<Group>{

	/**
	 * 根据查询条件，所有的组及用户
	 * @param group
	 * @return
	 */
	List<Group> queryAllGroupUsers(Group group);

	/**
	 * 根据查询条件，查询该组名称是否存在
	 * @param group
	 * @return
	 */
	Group querySameGroupCount(Group group);
}
