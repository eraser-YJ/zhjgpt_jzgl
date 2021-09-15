package com.jc.system.security.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IRoleExtsDao;
import com.jc.system.security.domain.RoleExts;
import com.jc.system.security.service.IRoleExtsService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class RoleExtsServiceImpl extends BaseServiceImpl<RoleExts> implements IRoleExtsService {

	@Autowired
	public RoleExtsServiceImpl(IRoleExtsDao roleExtsDao) {
		super(roleExtsDao);
		this.roleExtsDao= roleExtsDao;
	}

	public RoleExtsServiceImpl(){
		
	}

	private IRoleExtsDao roleExtsDao;

	public List<RoleExts> query(RoleExts roleExts) {
		return roleExtsDao.queryAll(roleExts);
	}

}
