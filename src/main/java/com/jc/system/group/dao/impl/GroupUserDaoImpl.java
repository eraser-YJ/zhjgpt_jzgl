package com.jc.system.group.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import org.springframework.stereotype.Repository;

import com.jc.system.group.domain.GroupUser;
import com.jc.system.group.dao.IGroupUserDao;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class GroupUserDaoImpl extends BaseServerDaoImpl<GroupUser> implements IGroupUserDao{

	public GroupUserDaoImpl(){}

}