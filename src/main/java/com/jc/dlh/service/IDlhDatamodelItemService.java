package com.jc.dlh.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.jc.dlh.domain.DlhDatamodelItem;
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

public interface IDlhDatamodelItemService extends IBaseService<DlhDatamodelItem>{
	/**
	* @description 根据主键删除多条记录方法
	* @param DlhDatamodelItem dlhDatamodelItem 实体类
	* @return Integer 处理结果
	* @author
	* @version  2020-03-10 
	*/
	public Integer deleteByIds(DlhDatamodelItem dlhDatamodelItem) throws CustomException;

	/**
	 * @description 导入excel
	 * @param file,request
	 * @return Map<String, Object> 处理结果
	 * @author lc 
	 * @version  2020-03-19
	 */
	public Map<String, Object> importExcel(MultipartFile file, HttpServletRequest request) throws Exception;


}