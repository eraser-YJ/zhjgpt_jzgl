package com.jc.system.security.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IUserRecycleDao;
import com.jc.system.security.domain.UserRecycle;
import com.jc.system.security.service.IUserRecycleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class UserRecycleServiceImpl extends BaseServiceImpl<UserRecycle> implements IUserRecycleService {

	private IUserRecycleDao userRecycleDao;
	
	public UserRecycleServiceImpl(){}
	
	@Autowired
	public UserRecycleServiceImpl(IUserRecycleDao userRecycleDao){
		super(userRecycleDao);
		this.userRecycleDao = userRecycleDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(UserRecycle userRecycle) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(userRecycle,true);
			result = userRecycleDao.delete(userRecycle,false);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(userRecycle);
			throw ce;
		}
		return result;
	}

}