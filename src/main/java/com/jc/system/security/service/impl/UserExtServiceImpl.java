package com.jc.system.security.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IUserExtDao;
import com.jc.system.security.domain.UserExt;
import com.jc.system.security.service.IUserExtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class UserExtServiceImpl extends BaseServiceImpl<UserExt> implements IUserExtService {

	
	private IUserExtDao userExtDao;
	
	public UserExtServiceImpl(){}
	
	@Autowired
	public UserExtServiceImpl(IUserExtDao userExtDao){
		super(userExtDao);
		this.userExtDao = userExtDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(UserExt userExt) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(userExt,true);
			result = userExtDao.delete(userExt);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(userExt);
			throw ce;
		}
		return result;
	}

}