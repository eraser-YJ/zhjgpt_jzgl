package com.jc.csmp.dic.service.impl;

import com.jc.csmp.company.domain.CmCompanyInfo;
import com.jc.csmp.dic.dao.ICmCustomDicDao;
import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.csmp.dic.service.ICmCustomDicService;
import com.jc.csmp.dic.util.CmCustomDicCacheUtil;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 建设管理-自定义字典项serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class TlCmCustomDicServiceImpl extends BaseServiceImpl<CmCustomDic> implements ICmCustomDicService {

	private ICmCustomDicDao cmDicLandNatureDao;

	public TlCmCustomDicServiceImpl(){}

	@Autowired
	public TlCmCustomDicServiceImpl(ICmCustomDicDao cmDicLandNatureDao){
		super(cmDicLandNatureDao);
		this.cmDicLandNatureDao = cmDicLandNatureDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmCustomDic entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmDicLandNatureDao.delete(entity);
			String[] array = entity.getPrimaryKeys();
			if (array != null) {
				for (String s : array) {
					CmCustomDicCacheUtil.delete(s);
				}
			}
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmCustomDic entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			CmCustomDicCacheUtil.add(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"), entity);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmCustomDic entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				CmCustomDicCacheUtil.update(entity.getId());
				return Result.success(MessageUtils.getMessage("JC_SYS_003"), entity);
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
	public Result checkCode(String id, String code, String dataType) {
		if (StringUtil.isEmpty(code)) {
			return Result.success();
		}
		CmCustomDic param = new CmCustomDic();
		param.setCode(code);
		param.setDataType(dataType);
		List<CmCustomDic> entityList = this.cmDicLandNatureDao.queryAll(param);
		if (entityList == null || entityList.size() == 0) {
			return Result.success();
		}
		boolean result = true;
		for (CmCustomDic entity : entityList) {
			if (entity.getCode().equals(code)) {
				if (id == null || !id.equals(entity.getId())) {
					result = false;
					break;
				}
			}
		}
		if (result) {
			return Result.success();
		}
		return Result.failure(1, "编码已存在");
	}

	@Override
	public long queryCountByParentIds(CmCustomDic entity) {
		return this.cmDicLandNatureDao.queryCountByParentIds(entity);
	}

	@Override
	public CmCustomDic getById(String id) {
		CmCustomDic entity = new CmCustomDic();
		entity.setId(id);
		return GlobalUtil.getFirstItem(this.cmDicLandNatureDao.queryAll(entity));
	}

	@Override
	public void initCache() {
		List<CmCustomDic> dicList = this.cmDicLandNatureDao.queryAll(null);
		if (dicList != null) {
			for (CmCustomDic entity : dicList) {
				CmCustomDicCacheUtil.add(entity);
			}
		}
	}
}
