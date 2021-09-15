package com.jc.csmp.item.service.impl;

import com.jc.csmp.item.domain.ItemClassify;
import com.jc.cmsp.item.dao.IItemClassifyDao;
import com.jc.csmp.item.domain.ItemClassify;
import com.jc.csmp.item.domain.ItemClassifyAttach;
import com.jc.csmp.item.service.IItemClassifyAttachService;
import com.jc.csmp.item.service.IItemClassifyService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.foundation.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 
 * @Version 1.0
 */
@Service
public class ItemClassifyServiceImpl extends BaseServiceImpl<ItemClassify> implements IItemClassifyService {

	private IItemClassifyDao itemClassifyDao;

	public ItemClassifyServiceImpl(){}
	@Autowired
	private IItemClassifyAttachService itemClassifyAttachService;
	@Autowired
	public ItemClassifyServiceImpl(IItemClassifyDao itemClassifyDao){
		super(itemClassifyDao);
		this.itemClassifyDao = itemClassifyDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(ItemClassify entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = itemClassifyDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(ItemClassify entity) throws CustomException {
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
	public Result updateEntity(ItemClassify entity) throws CustomException {
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
	public ItemClassify get(ItemClassify entity) throws CustomException {
		try {
		entity = itemClassifyDao.get(entity);
		ItemClassifyAttach itemClassifyAttach = new ItemClassifyAttach();
		itemClassifyAttach.setItemId(entity.getId());
		itemClassifyAttach.addOrderByField("t.ext_num1");
		List<ItemClassifyAttach> itemClassifyAttachList = itemClassifyAttachService.queryAll(itemClassifyAttach);
		entity.setItemClassifyAttachList(itemClassifyAttachList);
	} catch (Exception e) {
		CustomException ce = new CustomException(e);
		ce.setBean(entity);
		throw ce;
	}
		return entity;
	}

	@Override
	public ItemClassify getByItemCode(String itemCode) {
		ItemClassify entity = new ItemClassify();
		entity.setItemCode(itemCode);
		try {
			entity = itemClassifyDao.get(entity);
			ItemClassifyAttach itemClassifyAttach = new ItemClassifyAttach();
			itemClassifyAttach.setItemId(entity.getId());
			itemClassifyAttach.addOrderByField("t.ext_num1");
			List<ItemClassifyAttach> itemClassifyAttachList = itemClassifyAttachService.queryAll(itemClassifyAttach);
			entity.setItemClassifyAttachList(itemClassifyAttachList);
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
		return entity;
	}
}

