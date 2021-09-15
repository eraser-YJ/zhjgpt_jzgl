package com.jc.system.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.SysRole;
import com.jc.system.security.dao.ISysRoleDao;
import com.jc.system.security.service.ISysRoleService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements ISysRoleService{

	private ISysRoleDao sysRoleDao;
	
	@Autowired
	public SysRoleServiceImpl(ISysRoleDao dao) {
		super(dao);
		this.sysRoleDao = dao;
	}
	public SysRoleServiceImpl(){
		
	}

	@Override
    public PageManager query(SysRole sysRole, PageManager page) {
		return sysRoleDao.queryByPage(sysRole, page);
	}
	
	@Override
    public List<SysRole> queryAll(SysRole sysRole) {
		return sysRoleDao.queryAll(sysRole);
	}

	@Override
    @Transactional(rollbackFor=Exception.class)
	public Integer deleteByIds(SysRole sysRole) throws CustomException{
		Integer result = -1;
		try{
			result = sysRoleDao.delete(sysRole, false);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(sysRole);
			throw ce;
		}
		return result;
	}

	@Override
    @Transactional(rollbackFor=Exception.class)
	public Integer save(SysRole sysRole) throws CustomException{
		Integer result = -1;
		try{
			result = sysRoleDao.save(sysRole);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(sysRole);
			throw ce;
		}
		return result;
	}

	@Override
    @Transactional(rollbackFor=Exception.class)
	public Integer update(SysRole sysRole) throws CustomException{
		Integer result = -1;
		try{
			result = sysRoleDao.update(sysRole);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(sysRole);
			throw ce;
		}
		return result;
	}

	@Override
    public SysRole get(SysRole sysRole) throws CustomException{
		return sysRoleDao.get(sysRole);
	}

}