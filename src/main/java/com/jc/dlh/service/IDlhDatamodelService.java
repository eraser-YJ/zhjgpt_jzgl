package com.jc.dlh.service;

import com.jc.dlh.domain.DlhDatamodel;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;

/**
 * @title 统一数据资源中心
 * @description  业务接口类
 * Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved
 * Company 长春奕迅
 * @author lc  
 * @version  2020-03-10
 */

public interface IDlhDatamodelService extends IBaseService<DlhDatamodel>{
	/**
	* @description 根据主键删除多条记录方法
	* @param DlhDatamodel dlhDatamodel 实体类
	* @return Integer 处理结果
	* @author
	* @version  2020-03-10 
	*/
	public Integer deleteByIds(DlhDatamodel dlhDatamodel) throws CustomException;
	
	public void publish(DlhDatamodel dlhDatamodel) throws CustomException;

	
}