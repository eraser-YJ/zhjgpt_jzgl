package com.jc.csmp.safe.supervision.service.impl;

import com.jc.csmp.common.WordUtil;
import com.jc.csmp.safe.supervision.domain.SafetyUnit;
import com.jc.csmp.safe.supervision.dao.ISafetyUnitDao;
import com.jc.csmp.safe.supervision.domain.SafetyUnit;
import com.jc.csmp.safe.supervision.service.ISafetyUnitService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @Version 1.0
 */
@Service
public class SafetyUnitServiceImpl extends BaseServiceImpl<SafetyUnit> implements ISafetyUnitService {

	private ISafetyUnitDao safetyUnitDao;

	public SafetyUnitServiceImpl(){}

	@Autowired
	public SafetyUnitServiceImpl(ISafetyUnitDao safetyUnitDao){
		super(safetyUnitDao);
		this.safetyUnitDao = safetyUnitDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(SafetyUnit entity,boolean logicDelete) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = safetyUnitDao.delete(entity,logicDelete);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(SafetyUnit entity) throws CustomException {
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
	public Result updateEntity(SafetyUnit entity) throws CustomException {
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

