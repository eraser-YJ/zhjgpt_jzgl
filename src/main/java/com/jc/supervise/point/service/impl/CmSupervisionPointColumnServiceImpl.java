package com.jc.supervise.point.service.impl;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.supervise.point.dao.ICmSupervisionPointColumnDao;
import com.jc.supervise.point.domain.CmSupervisionPointColumn;
import com.jc.supervise.point.service.ICmSupervisionPointColumnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 建设管理-监察点数据来源管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmSupervisionPointColumnServiceImpl extends BaseServiceImpl<CmSupervisionPointColumn> implements ICmSupervisionPointColumnService {

	private ICmSupervisionPointColumnDao cmSupervisionPointColumnDao;

	public CmSupervisionPointColumnServiceImpl(){}

	@Autowired
	public CmSupervisionPointColumnServiceImpl(ICmSupervisionPointColumnDao cmSupervisionPointColumnDao){
		super(cmSupervisionPointColumnDao);
		this.cmSupervisionPointColumnDao = cmSupervisionPointColumnDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmSupervisionPointColumn entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmSupervisionPointColumnDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmSupervisionPointColumn entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			String content = null;
			boolean ifSql = false;
			if (entity.getDataSource().equals(SQL)) {
				ifSql = true;
				content = entity.getDataValue();
				entity.setDataValue(null);
			}
			this.save(entity);
			if (ifSql) {
				GlobalUtil.writeFile(entity.getId(), entity.getCreateDate(), content);
			}
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmSupervisionPointColumn entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			String content = null;
			boolean ifSql = false;
			if (entity.getDataSource().equals(SQL)) {
				ifSql = true;
				content = entity.getDataValue();
				entity.setDataValue(null);
			}
			Integer flag = this.update(entity);
			if (flag == 1) {
				if (ifSql) {
					CmSupervisionPointColumn db = this.getById(entity.getId());
					GlobalUtil.writeFile(db.getId(), db.getCreateDate(), content);
				}
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
	public Result updateSupervisionId(String newSupervisionId, String oldSupervisionId) {
		this.cmSupervisionPointColumnDao.updateSupervisionId(newSupervisionId, oldSupervisionId);
		return Result.success();
	}

	public CmSupervisionPointColumn getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmSupervisionPointColumn entity = new CmSupervisionPointColumn();
		entity.setId(id);
		return GlobalUtil.getFirstItem(this.cmSupervisionPointColumnDao.queryAll(entity));
	}
}
