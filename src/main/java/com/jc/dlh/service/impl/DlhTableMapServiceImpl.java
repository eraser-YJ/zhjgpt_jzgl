package com.jc.dlh.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.Result;
import com.jc.foundation.util.ResultCode;
import com.jc.foundation.util.StringUtil;
import com.jc.dlh.dao.IDlhTableMapDao;
import com.jc.dlh.domain.DlhTableMap;
import com.jc.dlh.service.IDlhTableMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @Version 1.0
 */
@Service
public class DlhTableMapServiceImpl extends BaseServiceImpl<DlhTableMap> implements IDlhTableMapService {

	private IDlhTableMapDao dlhTableMapDao;

	public DlhTableMapServiceImpl(){}

	@Autowired
	public DlhTableMapServiceImpl(IDlhTableMapDao dlhTableMapDao){
		super(dlhTableMapDao);
		this.dlhTableMapDao = dlhTableMapDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(DlhTableMap entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = dlhTableMapDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(DlhTableMap entity) throws CustomException {
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
	public Result updateEntity(DlhTableMap entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				return Result.success(MessageUtils.getMessage("JC_SYS_003"));
			} else {
				return null;//Result.failure(1, MessageUtils.getMessage("JC_SYS_004"));
			}
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

}

