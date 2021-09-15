package com.jc.common.db.dialect;

import java.util.List;
import java.util.Map;

import com.jc.common.kit.vo.PageManagerEx;
import com.jc.dlh.domain.DlhDatamodel;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;

public interface IDBDialect {
	void publish(DlhDatamodel dlhDatamodel) throws CustomException;

	/*
	* dataList:[
	* 	(map){col1:value1,col1:value1,col1:value1},
	* 	(map){col1:value1,col1:value1,col1:value1}
	* ]   每条记录一个map*/
	boolean addData(DlhDataobject object, List<Map<String, Object>> dataList)throws CustomException;


	boolean updateData(DlhDataobject object, Map<String, Object> dataMap, String column, String uuid)throws CustomException;

	/*根据主键查询数据*/
	List<Map<String, String>> queryDataByPrimaryKey(DlhDataobject object, String uuid) throws CustomException;

	void modify(DlhDataobject cond, List<Map<String, Object>> dataList) throws CustomException;

	boolean deleteData(DlhDataobject object,List<Map> condMapList) throws CustomException;

	/*查询所有记录*/
	List<Map<String, Object>> queryAll(DlhDataobject object, String condJson)throws CustomException;

	PageManagerEx<Map<String, Object>> query(DlhDataobject condObj, PageManager page, String condJson) throws CustomException;

	List<Map<String, String>> queryById(DlhDataobject object, String uuid, List<DlhDataobjectField> fieldList,String yewuKey) throws CustomException;
}
