package com.jc.csmp.project.service.impl;

import com.jc.csmp.project.dao.ICmProjectDataAuthDao;
import com.jc.csmp.project.domain.CmProjectDataAuth;
import com.jc.csmp.project.service.ICmProjectDataAuthService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 建设管理-项目数据权限管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectDataAuthServiceImpl extends BaseServiceImpl<CmProjectDataAuth> implements ICmProjectDataAuthService {

	private ICmProjectDataAuthDao cmProjectDataAuthDao;

	public CmProjectDataAuthServiceImpl(){}

	@Autowired
	public CmProjectDataAuthServiceImpl(ICmProjectDataAuthDao cmProjectDataAuthDao){
		super(cmProjectDataAuthDao);
		this.cmProjectDataAuthDao = cmProjectDataAuthDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectDataAuth entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmProjectDataAuthDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveOrUpdate(String busId, String...deptIds) throws CustomException {
		if (StringUtil.isEmpty(busId)) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		this.cmProjectDataAuthDao.deleteByBusinessId(busId);
		for (String deptId : deptIds) {
			CmProjectDataAuth entity = new CmProjectDataAuth();
			entity.setBusinessId(busId);
			entity.setDeptId(deptId);
			entity.setDeptCode(DeptCacheUtil.getCodeById(deptId));
			this.save(entity);
		}
		return Result.success();
	}
}
