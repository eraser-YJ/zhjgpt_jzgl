package com.jc.dlh.service;

import java.util.List;
import java.util.Map;

import com.jc.common.kit.vo.PageManagerEx;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;

import javax.servlet.http.HttpServletResponse;

/**
 * @title 统一数据资源中心
 * @description 业务接口类 Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved Company 长春奕迅
 * @author lc 
 * @version 2020-03-10
 */

public interface IDlhDateQueryService {
	/**
	 * @document 列表数据
	 * @param condObj
	 * @param page
	 * @param condJson
	 * @return
	 * @throws CustomException
	 */
	public PageManagerEx query(DlhDataobject condObj, final PageManager page, String condJson) throws CustomException;

	/**
	 * @document 表单数据
	 * @param uuid
	 * @param fieldList
	 * @return
	 * @throws CustomException
	 */
	public List<Map<String, String>> queryById(String uuid, List<DlhDataobjectField> fieldList,String yewuKey) throws CustomException;

    void exportExcelFile(String objUrl, String condJson, HttpServletResponse response) throws CustomException;
}