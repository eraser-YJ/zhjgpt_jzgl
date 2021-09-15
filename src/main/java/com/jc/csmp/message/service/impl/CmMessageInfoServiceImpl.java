package com.jc.csmp.message.service.impl;

import com.jc.csmp.message.dao.ICmMessageInfoDao;
import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.csmp.message.service.ICmMessageInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.workflow.external.WorkflowBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 建设管理-消息管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmMessageInfoServiceImpl extends BaseServiceImpl<CmMessageInfo> implements ICmMessageInfoService {

	private ICmMessageInfoDao cmMessageInfoDao;

	public CmMessageInfoServiceImpl(){}

	@Autowired
	public CmMessageInfoServiceImpl(ICmMessageInfoDao cmMessageInfoDao){
		super(cmMessageInfoDao);
		this.cmMessageInfoDao = cmMessageInfoDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmMessageInfo entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = cmMessageInfoDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmMessageInfo entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
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
	public Result updateEntity(CmMessageInfo entity) throws CustomException {
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
	public CmMessageInfo getById(String id) {
		if (StringUtil.isEmpty(id)) {
			return null;
		}
		CmMessageInfo param = new CmMessageInfo();
		param.setId(id);
		return GlobalUtil.getFirstItem(this.cmMessageInfoDao.queryAll(param));
	}

	@Override
	public Result sendMessage(String title, String content, String receiveId, String receiveDeptId, String receiveDeptCode) {
		CmMessageInfo entity = new CmMessageInfo();
		entity.setTitle(title);
		entity.setContent(content);
		entity.setReceiveId(receiveId);
		entity.setReadStatus("0");
		entity.setReceiveDeptId(receiveDeptId);
		entity.setReceiveDeptCode(receiveDeptCode);
		try {
			return this.saveEntity(entity);
		} catch (CustomException e) {
			e.printStackTrace();
			return Result.failure(1, e.getMessage());
		}
	}

	@Override
	public PageManager workflowTodoQuery(CmMessageInfo param, PageManager page) {
		return this.cmMessageInfoDao.queryByPage(param, page, "workflowTodoQueryCount", "workflowTodoQuery");
	}

	@Override
	public PageManager workflowDoneQuery(CmMessageInfo param, PageManager page) {
		return this.cmMessageInfoDao.queryByPage(param, page, "workflowDoneQueryCount", "workflowDoneQuery");
	}
}
