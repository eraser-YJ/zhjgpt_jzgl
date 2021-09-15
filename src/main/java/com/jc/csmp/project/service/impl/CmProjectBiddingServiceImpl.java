package com.jc.csmp.project.service.impl;

import com.jc.csmp.project.dao.ICmProjectBiddingDao;
import com.jc.csmp.project.domain.CmProjectBidding;
import com.jc.csmp.project.service.ICmProjectBiddingService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.content.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 建设管理-招投标管理serviceImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Service
public class CmProjectBiddingServiceImpl extends BaseServiceImpl<CmProjectBidding> implements ICmProjectBiddingService {

	private ICmProjectBiddingDao projectBiddingDao;
	@Autowired
	private IUploadService uploadService;

	public CmProjectBiddingServiceImpl(){}

	@Autowired
	public CmProjectBiddingServiceImpl(ICmProjectBiddingDao projectBiddingDao){
		super(projectBiddingDao);
		this.projectBiddingDao = projectBiddingDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(CmProjectBidding entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = projectBiddingDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(CmProjectBidding entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			this.save(entity);
			uploadService.managerForAttach(entity.getId(), "cm_project_bidding", entity.getAttachFile1(), entity.getDeleteAttachFile1(), 1);
			if (entity.getProjectApproval() != null && entity.getProjectApproval().equals(GlobalContext.SIGN_1)) {
				//选择了有立项批文
				uploadService.managerForAttach(entity.getId(), "cm_project_bidding_project_approval", entity.getAttachFile2(), entity.getDeleteAttachFile2(), 1);
			} else {
				uploadService.managerForAttach(entity.getId(), "cm_project_bidding_project_approval", null, entity.getDeleteAttachFile2(), 1);
				uploadService.managerForAttach(entity.getId(), "cm_project_bidding_project_approval", null, entity.getAttachFile2(),  1);
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
	public Result updateEntity(CmProjectBidding entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				uploadService.managerForAttach(entity.getId(), "cm_project_bidding", entity.getAttachFile1(), entity.getDeleteAttachFile1(), 1);
				if (entity.getProjectApproval() != null && entity.getProjectApproval().equals(GlobalContext.SIGN_1)) {
					//选择了有立项批文
					uploadService.managerForAttach(entity.getId(), "cm_project_bidding_project_approval", entity.getAttachFile2(), entity.getDeleteAttachFile2(), 1);
				} else {
					uploadService.managerForAttach(entity.getId(), "cm_project_bidding_project_approval", null, entity.getDeleteAttachFile2(), 1);
					uploadService.managerForAttach(entity.getId(), "cm_project_bidding_project_approval", null, entity.getAttachFile2(),  1);
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

}

