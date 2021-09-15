package com.jc.system.security.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IUniqueDao;
import com.jc.system.security.domain.Unique;
import com.jc.system.security.service.IUniqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class UniqueServiceImpl extends BaseServiceImpl<Unique> implements IUniqueService{

	private IUniqueDao uniqueDao;
	
	public UniqueServiceImpl(){}
	
	@Autowired
	public UniqueServiceImpl(IUniqueDao uniqueDao){
		super(uniqueDao);
		this.uniqueDao = uniqueDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(Unique unique) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(unique,true);
			result = uniqueDao.delete(unique);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(unique);
			throw ce;
		}
		return result;
	}

	@Override
	public Unique getOne(Unique t) throws CustomException {
		return uniqueDao.getOne(t);
	}

}