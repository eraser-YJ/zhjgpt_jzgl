package com.jc.system.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IRoleBlocksDao;
import com.jc.system.security.domain.RoleBlocks;
import com.jc.system.security.service.IRoleBlocksService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class RoleBlocksServiceImpl extends BaseServiceImpl<RoleBlocks> implements IRoleBlocksService {
	@Autowired
	public RoleBlocksServiceImpl(IRoleBlocksDao roleBlocksDao) {
		super(roleBlocksDao);
	}

	public RoleBlocksServiceImpl(){
		
	}
}
