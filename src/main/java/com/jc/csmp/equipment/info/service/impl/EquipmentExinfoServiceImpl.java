package com.jc.csmp.equipment.info.service.impl;

import com.jc.csmp.equipment.info.domain.EquipmentExinfo;
import com.jc.csmp.equipment.info.dao.IEquipmentExinfoDao;
import com.jc.csmp.equipment.info.domain.EquipmentExinfo;
import com.jc.csmp.equipment.info.service.IEquipmentExinfoService;
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
public class EquipmentExinfoServiceImpl extends BaseServiceImpl<EquipmentExinfo> implements IEquipmentExinfoService {

	private IEquipmentExinfoDao equipmentExinfoDao;

	public EquipmentExinfoServiceImpl(){}

	@Autowired
	public EquipmentExinfoServiceImpl(IEquipmentExinfoDao equipmentExinfoDao){
		super(equipmentExinfoDao);
		this.equipmentExinfoDao = equipmentExinfoDao;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(EquipmentExinfo entity) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(entity,true);
			result = equipmentExinfoDao.delete(entity);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(entity);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteById(String id) throws CustomException{
		Integer result = -1;
		try{
			EquipmentExinfo entity = new EquipmentExinfo();
			entity.setPrimaryKeys(new String[]{id});
			result = equipmentExinfoDao.delete(entity,false);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			throw ce;
		}
		return result;
	}

	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Result saveEntity(EquipmentExinfo entity) throws CustomException {
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
	public Result updateEntity(EquipmentExinfo entity) throws CustomException {
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

}

