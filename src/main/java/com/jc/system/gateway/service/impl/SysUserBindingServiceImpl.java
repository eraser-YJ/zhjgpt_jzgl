package com.jc.system.gateway.service.impl;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.gateway.dao.ISysUserBindingDao;
import com.jc.system.gateway.domain.SysUserBinding;
import com.jc.system.gateway.service.ISysUserBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class SysUserBindingServiceImpl extends BaseServiceImpl<SysUserBinding> implements ISysUserBindingService{

	@Resource
	private ISysUserBindingDao sysUserBindingDao;

	@Autowired
	public SysUserBindingServiceImpl(ISysUserBindingDao dao) {
		super(dao);
		this.sysUserBindingDao = dao;
	}

	public SysUserBindingServiceImpl(){
		
	}

}