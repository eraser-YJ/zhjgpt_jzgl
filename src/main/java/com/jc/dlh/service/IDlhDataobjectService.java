package com.jc.dlh.service;

import java.util.List;
import java.util.Map;

import com.jc.dlh.ResourceAttachInfo;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.IBaseService;

/**
 * @title 统一数据资源中心
 * @description 业务接口类 Copyright (c) 2020 Jiachengnet.com Inc. All Rights
 *              Reserved Company 长春奕迅
 * @author lc 
 * @version 2020-03-10
 */

public interface IDlhDataobjectService extends IBaseService<DlhDataobject> {
	/**
	 * @description 根据主键删除多条记录方法
	 * @param DlhDataobject dlhDataobject 实体类
	 * @return Integer 处理结果
	 * @author lc 
	 * @version 2020-03-10
	 */
	public Integer deleteByIds(DlhDataobject dlhDataobject) throws CustomException;

	/**
	 * @description 插入或修改数据
	 * @param cond
	 * @param dataList
	 * @throws CustomException
	 */
	public void modify(DlhDataobject cond, List<Map<String, Object>> dataList) throws CustomException;

    void deleteData(ResourceAttachInfo resourceAttachInfo) throws CustomException;

    List<Map<String, String>> getPrimaryKey(String objUrl, String uuid) throws CustomException;

    void updateData(Map<String, Object> dataMap, String objUrl, String uuid, String column) throws CustomException;

    /**
	 * @description 初始
	 * @param dlhDataobject
	 * @throws CustomException
	 */
	public void init(DlhDataobject dlhDataobject) throws CustomException;

}