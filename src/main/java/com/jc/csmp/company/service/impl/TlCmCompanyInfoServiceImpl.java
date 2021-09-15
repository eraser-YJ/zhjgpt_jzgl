package com.jc.csmp.company.service.impl;

import com.jc.csmp.company.dao.ICmCompanyInfoDao;
import com.jc.csmp.company.domain.CmCompanyInfo;
import com.jc.csmp.company.service.ICmCompanyInfoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 建设管理-单位管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class TlCmCompanyInfoServiceImpl extends BaseServiceImpl<CmCompanyInfo> implements ICmCompanyInfoService {

	private ICmCompanyInfoDao cmCompanyInfoDao;

	public TlCmCompanyInfoServiceImpl(){}

	@Autowired
	public TlCmCompanyInfoServiceImpl(ICmCompanyInfoDao cmCompanyInfoDao){
		super(cmCompanyInfoDao);
		this.cmCompanyInfoDao = cmCompanyInfoDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmCompanyInfo entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmCompanyInfoDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmCompanyInfo entity) throws CustomException {
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
	public Result updateEntity(CmCompanyInfo entity) throws CustomException {
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
	public Result checkCreditCode(String id, String creditCode) {
		if (StringUtil.isEmpty(creditCode)) {
			return Result.success();
		}
		CmCompanyInfo param = new CmCompanyInfo();
		param.setCreditCode(creditCode);
		List<CmCompanyInfo> entityList = this.cmCompanyInfoDao.queryAll(param);
		if (entityList == null || entityList.size() == 0) {
			return Result.success();
		}
		boolean result = true;
		for (CmCompanyInfo entity : entityList) {
			if (entity.getCreditCode().equals(creditCode)) {
				if (id == null || !id.equals(entity.getId())) {
					result = false;
					break;
				}
			}
		}
		if (result) {
			return Result.success();
		}
		return Result.failure(1, "统一社会信用代码已存在");
	}
}
