package com.jc.supervise.warning.service.impl;

import com.jc.csmp.message.service.ICmMessageInfoService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.supervise.warning.dao.ICmSupervisionMessageDao;
import com.jc.supervise.warning.domain.CmSupervisionMessage;
import com.jc.supervise.warning.service.ICmSupervisionMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 建设管理-督办管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmSupervisionMessageServiceImpl extends BaseServiceImpl<CmSupervisionMessage> implements ICmSupervisionMessageService {

	private ICmSupervisionMessageDao cmSupervisionMessageDao;
	@Autowired
	private ICmMessageInfoService cmMessageInfoService;

	public CmSupervisionMessageServiceImpl(){}

	@Autowired
	public CmSupervisionMessageServiceImpl(ICmSupervisionMessageDao cmSupervisionMessageDao){
		super(cmSupervisionMessageDao);
		this.cmSupervisionMessageDao = cmSupervisionMessageDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmSupervisionMessage entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmSupervisionMessageDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmSupervisionMessage entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			cmMessageInfoService.sendMessage(entity.getTitle(), entity.getContent(), entity.getReceiveId(), entity.getReceiveDeptId(), entity.getReceiveDeptCode());
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			e.printStackTrace();
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(CmSupervisionMessage entity) throws CustomException {
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
	public CmSupervisionMessage getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmSupervisionMessage param = new CmSupervisionMessage();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmSupervisionMessageDao.queryAll(param));
	}
}
