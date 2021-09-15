package com.jc.csmp.item.service.impl;

import com.jc.csmp.item.domain.ItemClassifyAttach;
import com.jc.csmp.item.dao.IItemClassifyAttachDao;
import com.jc.csmp.item.domain.ItemClassifyAttach;
import com.jc.csmp.item.service.IItemClassifyAttachService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/** 
 * @Version 1.0
 */
@Service
public class ItemClassifyAttachServiceImpl extends BaseServiceImpl<ItemClassifyAttach> implements IItemClassifyAttachService {

	private IItemClassifyAttachDao itemClassifyAttachDao;

	public ItemClassifyAttachServiceImpl(){}

	@Autowired
	public ItemClassifyAttachServiceImpl(IItemClassifyAttachDao itemClassifyAttachDao){
		super(itemClassifyAttachDao);
		this.itemClassifyAttachDao = itemClassifyAttachDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(ItemClassifyAttach entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = itemClassifyAttachDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(ItemClassifyAttach entity) throws CustomException {
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
	public Result updateEntity(ItemClassifyAttach entity) throws CustomException {
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

}

