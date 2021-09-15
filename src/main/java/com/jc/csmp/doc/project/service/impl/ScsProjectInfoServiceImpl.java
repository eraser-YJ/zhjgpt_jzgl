package com.jc.csmp.doc.project.service.impl;

import com.jc.csmp.doc.project.domain.ScsProjectInfo;
import com.jc.csmp.doc.project.dao.IScsProjectInfoDao;
import com.jc.csmp.doc.project.service.IScsProjectInfoService;
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
public class ScsProjectInfoServiceImpl extends BaseServiceImpl<ScsProjectInfo> implements IScsProjectInfoService {

	private IScsProjectInfoDao projectInfoDao;

	public ScsProjectInfoServiceImpl(){}

	@Autowired
	public ScsProjectInfoServiceImpl(IScsProjectInfoDao projectInfoDao){
		super(projectInfoDao);
		this.projectInfoDao = projectInfoDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(ScsProjectInfo entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = projectInfoDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(ScsProjectInfo entity) throws CustomException {
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
	public Result updateEntity(ScsProjectInfo entity) throws CustomException {
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

	@Override
	public ScsProjectInfo queryByCode(String code) {
		ScsProjectInfo cond = new ScsProjectInfo();
		cond.setProjectNumber(code);
		return this.projectInfoDao.get(cond);
	}

}

