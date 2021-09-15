package com.jc.system.security.service.impl;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.ISysOtherDeptsDao;
import com.jc.system.security.domain.SysOtherDepts;
import com.jc.system.security.service.ISysOtherDeptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class SysOtherDeptsServiceImpl extends BaseServiceImpl<SysOtherDepts> implements ISysOtherDeptsService{
	
	@Autowired
	public SysOtherDeptsServiceImpl(ISysOtherDeptsDao dao) {
		super(dao);
		this.iSysOtherDeptsDao = dao;
	}
	public SysOtherDeptsServiceImpl(){
		
	}
	private ISysOtherDeptsDao iSysOtherDeptsDao;

	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public int deleteOtherDept(String userId) {
		return iSysOtherDeptsDao.deleteOtherDept(userId);
	}

	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer updateDeleteFlagByIds(SysOtherDepts sysOtherDepts) {
		return iSysOtherDeptsDao.updateDeleteFlagByIds(sysOtherDepts);
	}

	@Override
	public Integer deleteBack(SysOtherDepts sysOtherDepts) {
		return iSysOtherDeptsDao.deleteBack(sysOtherDepts);
	}
}