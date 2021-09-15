package com.jc.dlh.service.impl;

import java.util.*;

import com.jc.common.db.dialect.mysql.MysqlColumnType;
import com.jc.common.kit.ExcelBuilder;
import com.jc.dlh.service.IDlhDataobjectFieldService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.common.db.DBType;
import com.jc.common.kit.vo.PageManagerEx;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.dlh.service.IDlhDateQueryService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;

/**
 * @title 统一数据资源中心
 * @description 业务服务类
 * @author lc 
 * @version 2020-03-10
 */
@Service
public class DlhDataQueryServiceImpl implements IDlhDateQueryService {

	@Autowired
	private IDlhDataobjectService dlhDataobjectService;
	@Autowired
	private IDlhDataobjectFieldService dlhDataobjectFieldService;

	@Override
	public PageManagerEx query(DlhDataobject condObj, PageManager page, String condJson) throws CustomException {
		DlhDataobject object = dlhDataobjectService.get(condObj);
		if (object == null) {
			throw new CustomException("数据对象不存在");
		}
		DBType dbtype = DBType.getType(object.getDbType());
		return dbtype.getService().query(object, page, condJson);
	}

	@Override
	public List<Map<String, String>> queryById(String uuid, List<DlhDataobjectField> fieldList,String yewuKey) throws CustomException {
		DlhDataobject cond = new DlhDataobject();
		cond.setId(fieldList.get(0).getObjectId());
		DlhDataobject object = dlhDataobjectService.get(cond);
		if (object == null) {
			throw new CustomException("数据对象不存在");
		}
		DBType dbtype = DBType.getType(object.getDbType());
		return dbtype.getService().queryById(object, uuid, fieldList,yewuKey);
	}


	@Override
	public void exportExcelFile(String objUrl, String condJson, HttpServletResponse response) throws CustomException{
		DlhDataobject cond = new DlhDataobject();
		cond.setObjUrl(objUrl);
		try {
			List<DlhDataobject> list = dlhDataobjectService.queryAll(cond);
			if(list==null||list.size()<0){
				throw new CustomException("未找到数据对象");
			}
			cond = list.get(0);

			DlhDataobjectField field = new DlhDataobjectField();
			field.setObjectId(cond.getId());
			List<DlhDataobjectField> dlhDataobjectFields = dlhDataobjectFieldService.queryAll(field);

			Map<String, DlhDataobjectField> fieldMap = fieldList2Map(dlhDataobjectFields);

			DBType dbtype = DBType.getType(cond.getDbType());
			List<Map<String, Object>> dataList = dbtype.getService().queryAll(cond, condJson);
			this.createExcelFile(cond,dataList,fieldMap,response);

		} catch (CustomException e) {
			e.printStackTrace();
			throw e;
		}
	}
	private void createExcelFile(DlhDataobject object,List<Map<String, Object>> mapList,Map<String, DlhDataobjectField> fieldMap, HttpServletResponse response){
		ExcelBuilder builder = new ExcelBuilder();
		try {
			builder.openFile(object.getObjName());
			int rowIndex = 0;

			HSSFCell cell;
			int colIndex = 0;

			DlhDataobjectField field = null;
			List<DlhDataobjectField> titleList = new ArrayList<>();

			/*输出标题行*/
			HSSFRow row = builder.getRow(rowIndex++, 30);
			colIndex=0;
			for(String key:fieldMap.keySet()){
				field=fieldMap.get(key);
				if(field.getFieldListShow()!=null&&field.getFieldListShow()>0){
					titleList.add(field);
					cell = builder.getTitle(row, colIndex++);
					cell.setCellValue(field.getFieldName());

				}
			}



			HSSFRow rowData = null;

			for (Map<String,Object> lineMap:mapList){
				rowData = builder.getRow(rowIndex++, 20);
				colIndex=0;
				for(DlhDataobjectField field1:titleList){
					field=field1;
					if(field==null){
						continue;
					}
					Object value = lineMap.get(field1.getItemName());
					MysqlColumnType ctype = MysqlColumnType.getType(field.getItemType());
					cell = builder.getCell(rowData, colIndex++);
					cell.setCellValue(ctype.getService().getDisplayValue(value));


				}
			}

			builder.saveFile(object.getObjName()+".xlsx", response);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String,DlhDataobjectField> fieldList2Map(List<DlhDataobjectField> fieldList){
		Collections.sort(fieldList, new Comparator<DlhDataobjectField>() {
			@Override
			public int compare(DlhDataobjectField o1, DlhDataobjectField o2) {
				return o1.getFieldSeq().compareTo(o2.getFieldSeq());
			}

		});
		Map<String, DlhDataobjectField> fieldMap = new HashMap<>();
		if (fieldList != null&&fieldList.size()>0) {
			for (DlhDataobjectField f : fieldList) {
				fieldMap.put(f.getItemName(), f);
			}
		}
		return fieldMap;
	}
	/**
	 * @document 排序
	 * @param fieldList
	 * @return
	 */
	private List<DlhDataobjectField> sort(List<DlhDataobjectField> fieldList) {

		Collections.sort(fieldList, new Comparator<DlhDataobjectField>() {
			@Override
			public int compare(DlhDataobjectField o1, DlhDataobjectField o2) {
				if (o1.getFieldFormShow() == null && o2.getFieldFormShow() == null) {
					return 0;
				}
				if (o1.getFieldFormShow() == null) {
					return -1;
				}
				if (o2.getFieldFormShow() == null) {
					return 1;
				}
				return o1.getFieldFormShow().compareTo(o2.getFieldFormShow());

			}

		});
		return fieldList;

	}

}