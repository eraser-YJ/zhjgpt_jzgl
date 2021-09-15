package com.jc.csmp.projectSgxk.service.impl;

import com.jc.csmp.projectSgxk.domain.CompanyProjectsSgxk;
import com.jc.csmp.projectSgxk.dao.ICompanyProjectsSgxkDao;
import com.jc.csmp.projectSgxk.domain.CompanyProjectsSgxk;
import com.jc.csmp.projectSgxk.service.ICompanyProjectsSgxkService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @Version 1.0
 */
@Service
public class CompanyProjectsSgxkServiceImpl extends BaseServiceImpl<CompanyProjectsSgxk> implements ICompanyProjectsSgxkService {

	private ICompanyProjectsSgxkDao companyProjectsSgxkDao;

	public CompanyProjectsSgxkServiceImpl(){}

	@Autowired
	public CompanyProjectsSgxkServiceImpl(ICompanyProjectsSgxkDao companyProjectsSgxkDao){
		super(companyProjectsSgxkDao);
		this.companyProjectsSgxkDao = companyProjectsSgxkDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CompanyProjectsSgxk entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = companyProjectsSgxkDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CompanyProjectsSgxk entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CompanyProjectsSgxk entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				return Result.success(MessageUtils.getMessage("JC_SYS_003"));
			} else {
				return Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
			}
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

}

