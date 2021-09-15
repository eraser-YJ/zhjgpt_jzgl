package com.jc.system.group.dao.impl;

import java.util.List;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import org.springframework.stereotype.Repository;

import com.jc.system.group.dao.IGroupDao;
import com.jc.system.group.domain.Group;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class GroupDaoImpl extends BaseServerDaoImpl<Group> implements IGroupDao{

	public GroupDaoImpl(){}
	
	/**
	 * 条件查询SQL ID
	 */
	public static final String SQL_ALLGROUPUSERS = "queryAllGroupUsers";
	
	/**
	 * 条件查询SQL ID
	 */
	public static final String SQL_SAMEGROUPCOUNT = "querySameGroupCount";

	/**
	 * 根据查询条件，所有的组及用户
	 * @param group
	 * @return
	 */
	@Override
    public List<Group> queryAllGroupUsers(Group group) {
		return getTemplate().selectList(getNameSpace(group) + "." + SQL_ALLGROUPUSERS, group);
	}

	/**
	 * 根据查询条件，查询该组名称是否存在
	 * @param group
	 * @return
	 */
	@Override
    public Group querySameGroupCount(Group group) {
		Group sameGroup = (Group)getTemplate().selectOne(getNameSpace(group) + "." + SQL_SAMEGROUPCOUNT, group);
		if(null == sameGroup){
			sameGroup = new Group();
		}
		return sameGroup;
	}

}