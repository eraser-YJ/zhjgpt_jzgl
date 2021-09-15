package com.jc.digitalchina.service.impl;

import com.jc.digitalchina.dao.ICmUserRelationDao;
import com.jc.digitalchina.domain.CmUserRelation;
import com.jc.digitalchina.service.ICmUserRelationService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 建设管理-自定义字典项serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmUserRelationServiceImpl extends BaseServiceImpl<CmUserRelation> implements ICmUserRelationService {

	private ICmUserRelationDao cmDicLandNatureDao;
	@Autowired
	private IUserService userService;

	public CmUserRelationServiceImpl(){}

	@Autowired
	public CmUserRelationServiceImpl(ICmUserRelationDao cmDicLandNatureDao){
		super(cmDicLandNatureDao);
		this.cmDicLandNatureDao = cmDicLandNatureDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmUserRelation entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmDicLandNatureDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmUserRelation entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"), entity);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmUserRelation entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
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
	public CmUserRelation getById(String id) {
		CmUserRelation entity = new CmUserRelation();
		entity.setId(id);
		return GlobalUtil.getFirstItem(this.cmDicLandNatureDao.queryAll(entity));
	}

	@Override
	public User getByUuid(String uuid) {
		CmUserRelation entity = new CmUserRelation();
		entity.setUuid(uuid);
		entity = GlobalUtil.getFirstItem(this.cmDicLandNatureDao.queryAll(entity));
		if (entity == null || StringUtil.isEmpty(entity.getUserId())) {
			return null;
		}
		User user = this.userService.getUser(entity.getUserId());
		return user;
	}
}
