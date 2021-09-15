package com.jc.dlh.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.dlh.dao.IDlhDataobjectFieldDao;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.dlh.service.IDlhDataobjectFieldService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;


/**
 * @title 统一数据资源中心
 * @description  业务服务类
 * Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved
 * Company 长春奕迅
 * @author lc  
 * @version  2020-03-10
 */
@Service
public class DlhDataobjectFieldServiceImpl extends BaseServiceImpl<DlhDataobjectField> implements IDlhDataobjectFieldService{

	
	
	private IDlhDataobjectFieldDao dlhDataobjectFieldDao;
	
	public DlhDataobjectFieldServiceImpl(){}
	
	@Autowired
	public DlhDataobjectFieldServiceImpl(IDlhDataobjectFieldDao dlhDataobjectFieldDao){
		super(dlhDataobjectFieldDao);
		this.dlhDataobjectFieldDao = dlhDataobjectFieldDao;
	}


	

	/**
	* @description 根据主键删除多条记录方法
	* @param DlhDataobjectField dlhDataobjectField 实体类
	* @return Integer 处理结果
	* @author
	* @version  2020-03-10 
	*/
	@Override
	@Transactional(rollbackFor=Exception.class,propagation=Propagation.REQUIRED)
	public Integer deleteByIds(DlhDataobjectField dlhDataobjectField) throws CustomException{
		Integer result = -1;
		try{
			propertyService.fillProperties(dlhDataobjectField,true);
			result = dlhDataobjectFieldDao.delete(dlhDataobjectField,false);
		}catch(Exception e){
			CustomException ce = new CustomException(e);
			ce.setBean(dlhDataobjectField);
			throw ce;
		}
		return result;
	}

}