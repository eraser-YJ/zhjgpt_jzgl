package com.jc.system.group.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.group.dao.IGroupUserDao;
import com.jc.system.group.domain.GroupUser;
import com.jc.system.group.service.IGroupUserService;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Service
public class GroupUserServiceImpl extends BaseServiceImpl<GroupUser> implements IGroupUserService{

	//private IGroupUserDao groupUserDao;
	
	public GroupUserServiceImpl(){}
	
	@Autowired
	public GroupUserServiceImpl(IGroupUserDao groupUserDao){
		super(groupUserDao);
		//this.groupUserDao = groupUserDao;
	}

}