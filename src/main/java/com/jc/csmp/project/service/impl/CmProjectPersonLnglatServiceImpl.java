package com.jc.csmp.project.service.impl;

import com.jc.csmp.common.enums.CompanyTypeConvertDepartmentEnums;
import com.jc.csmp.project.dao.ICmProjectPersonLnglatDao;
import com.jc.csmp.project.domain.CmProjectPersonLnglat;
import com.jc.csmp.project.service.ICmProjectPersonLnglatService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 建设管理-项目人员分配serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectPersonLnglatServiceImpl extends BaseServiceImpl<CmProjectPersonLnglat> implements ICmProjectPersonLnglatService {

	private ICmProjectPersonLnglatDao CmProjectPersonLnglatDao;

	public CmProjectPersonLnglatServiceImpl(){}

	@Autowired
	public CmProjectPersonLnglatServiceImpl(ICmProjectPersonLnglatDao CmProjectPersonLnglatDao){
		super(CmProjectPersonLnglatDao);
		this.CmProjectPersonLnglatDao = CmProjectPersonLnglatDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectPersonLnglat entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = CmProjectPersonLnglatDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectPersonLnglat entity) throws CustomException {
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
	public Result updateEntity(CmProjectPersonLnglat entity) throws CustomException {
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
	public CmProjectPersonLnglat getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmProjectPersonLnglat param = new CmProjectPersonLnglat();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.CmProjectPersonLnglatDao.queryAll(param));
	}
}

