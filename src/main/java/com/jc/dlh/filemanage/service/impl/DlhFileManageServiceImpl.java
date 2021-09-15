package com.jc.dlh.filemanage.service.impl;

import com.jc.dlh.filemanage.domain.DlhFile;
import com.jc.dlh.filemanage.dao.IDlhFileManageDao;
import com.jc.dlh.filemanage.service.IDlhFileManageService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import com.jc.system.content.service.IUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @Version 1.0
 */
@Service
public class DlhFileManageServiceImpl extends BaseServiceImpl<DlhFile> implements IDlhFileManageService {

	private IDlhFileManageDao dlhFileManageDao;

	@Autowired
	private IUploadService uploadService;

	public DlhFileManageServiceImpl(){}

	@Autowired
	public DlhFileManageServiceImpl(IDlhFileManageDao dlhFileDao){
		super(dlhFileDao);
		this.dlhFileManageDao = dlhFileDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(DlhFile entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = dlhFileManageDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(DlhFile entity) throws CustomException {
		if (!StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer result = this.save(entity);
			uploadService.managerForAttach(entity.getId(), "busi_dlh_file", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
			return Result.success(MessageUtils.getMessage("JC_SYS_001"));
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result updateEntity(DlhFile entity) throws CustomException {
		if (StringUtil.isEmpty(entity.getId())) {
			return Result.failure(ResultCode.PARAM_IS_INVALID);
		}
		try {
			Integer flag = this.update(entity);
			if (flag == 1) {
				uploadService.managerForAttach(entity.getId(), "cm_safety_supervision", entity.getAttachFile(), entity.getDeleteAttachFile(), 1);
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

