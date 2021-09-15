package com.jc.system.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.system.security.dao.IAdminSideDao;
import com.jc.system.security.domain.AdminSide;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.service.IAdminSideService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class AdminSideServiceImpl extends BaseServiceImpl<AdminSide> implements IAdminSideService{

	private IAdminSideDao adminSideDao;
	
	public AdminSideServiceImpl(){
		
	}
	
	@Autowired
	public AdminSideServiceImpl(IAdminSideDao adminSideDao){
		super(adminSideDao);
		this.adminSideDao = adminSideDao;
	}

	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer deleteAdminSide(AdminSide adminSide) {
		return adminSideDao.deleteAdminSide(adminSide);
	}

	@Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public List<AdminSide> queryManagerDeptRree(AdminSide adminSide) {
		return adminSideDao.queryManagerDeptRree(adminSide);
	}

	@Override
	public List<AdminSide> queryAdminSideIdByDeptId(AdminSide adminSide) {
		return adminSideDao.queryAdminSideIdByDeptId(adminSide);
	}

	@Override
    public boolean queryAdminSideIdByDeptIds(String[] deptIds){
		for(String deptId : deptIds){
			AdminSide adminSide = new AdminSide();
			adminSide.setDeptId(deptId);
			adminSide = adminSideDao.get(adminSide);
			if(adminSide != null){
				return true;
			}
		}
		return false;
	}

	@Override
	public Integer deleteBack(AdminSide adminSide) {
		return adminSideDao.deleteBack(adminSide);
	}
}