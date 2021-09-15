package com.jc.system.api.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.api.dao.IDeptSynclogDao;
import com.jc.system.api.domain.DeptSynclog;
import com.jc.system.api.service.IDeptSynclogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class DeptSynclogServiceImpl extends BaseServiceImpl<DeptSynclog> implements IDeptSynclogService{

	private IDeptSynclogDao deptSyncDao;

	public DeptSynclogServiceImpl(){}

	@Autowired
	public DeptSynclogServiceImpl(IDeptSynclogDao deptSyncDao){
		super(deptSyncDao);
		this.deptSyncDao = deptSyncDao;
	}

	@Override
    @Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(DeptSynclog deptSync) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(deptSync,true);
			result = deptSyncDao.delete(deptSync);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(deptSync);
			throw ce;
		}
		return result;
	}

}