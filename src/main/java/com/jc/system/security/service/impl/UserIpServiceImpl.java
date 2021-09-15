package com.jc.system.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IUserIpDao;
import com.jc.system.security.domain.UserIp;
import com.jc.system.security.service.IUserIpService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class UserIpServiceImpl extends BaseServiceImpl<UserIp> implements IUserIpService{

	public UserIpServiceImpl(){}
	
	@Autowired
	public UserIpServiceImpl(IUserIpDao userIpDao){
		super(userIpDao);
	}

}