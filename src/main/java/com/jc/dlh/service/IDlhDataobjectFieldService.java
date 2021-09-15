package com.jc.dlh.service;

import com.jc.dlh.domain.DlhDataobjectField;
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

public interface IDlhDataobjectFieldService extends IBaseService<DlhDataobjectField>{
	/**
	* @description 根据主键删除多条记录方法
	* @param DlhDataobjectField dlhDataobjectField 实体类
	* @return Integer 处理结果
	* @author
	* @version  2020-03-10 
	*/
	public Integer deleteByIds(DlhDataobjectField dlhDataobjectField) throws CustomException;

	
}