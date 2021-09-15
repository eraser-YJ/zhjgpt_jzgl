package com.jc.system.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.exception.CustomException;
import com.jc.system.api.dao.IUserSynclogDao;
import com.jc.system.api.domain.UserSynclog;
import com.jc.system.api.service.IUserSynclogService;

import com.jc.foundation.service.impl.BaseServiceImpl;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class UserSynclogServiceImpl extends BaseServiceImpl<UserSynclog> implements IUserSynclogService{

	private IUserSynclogDao userSyncDao;
	
	public UserSynclogServiceImpl(){}
	
	@Autowired
	public UserSynclogServiceImpl(IUserSynclogDao userSyncDao){
		super(userSyncDao);
		this.userSyncDao = userSyncDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(UserSynclog userSync) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(userSync,true);
			result = userSyncDao.delete(userSync);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(userSync);
			throw ce;
		}
		return result;
	}

}