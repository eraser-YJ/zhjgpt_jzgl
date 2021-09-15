package com.jc.dlh.service.impl;

import java.util.List;
import java.util.Map;

import com.jc.common.kit.StringUtil;

import com.jc.dlh.ResourceAttachInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.common.db.DBType;
import com.jc.dlh.dao.IDlhDataobjectDao;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;

/**
 * @title 统一数据资源中心
 * @description 业务服务类
 * @author lc
 * @version 2020-03-10
 */
@Service
public class DlhDataobjectServiceImpl extends BaseServiceImpl<DlhDataobject> implements IDlhDataobjectService {
	// 日志
	private IDlhDataobjectDao dlhDataobjectDao;
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public DlhDataobjectServiceImpl() {
	}

	@Autowired
	public DlhDataobjectServiceImpl(IDlhDataobjectDao dlhDataobjectDao) {
		super(dlhDataobjectDao);
		this.dlhDataobjectDao = dlhDataobjectDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param DlhDataobject dlhDataobject 实体类
	 * @return Integer 处理结果
	 * @author lc
	 * @version 2020-03-10
	 */
	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer deleteByIds(DlhDataobject dlhDataobject) throws CustomException {
		Integer result = -1;
		try {
			propertyService.fillProperties(dlhDataobject, true);
			String[] ids = dlhDataobject.getPrimaryKeys();
			for (String id : ids) {
				String sqlDel = "delete from busi_dlh_dataobject_field where object_id='" + id + "'";
				jdbcTemplate.execute(sqlDel);
			}
			result = dlhDataobjectDao.delete(dlhDataobject, false);
		} catch (Exception e) {
			CustomException ce = new CustomException(e);
			ce.setBean(dlhDataobject);
			throw ce;
		}
		return result;
	}

	@Override
	public void modify(DlhDataobject cond, List<Map<String, Object>> dataList) throws CustomException {
		try {
			if (dataList == null || dataList.size() == 0) {
				return;
			}
			List<DlhDataobject> objList = dlhDataobjectDao.queryAll(cond);
			if (objList == null || objList.size() == 0) {
				throw new CustomException("地址：" + cond.getObjUrl() + "没有定义交换对象");
			} else if (objList.size() > 1) {
				throw new CustomException("地址：" + cond.getObjUrl() + "交换对象有多个");
			}
			DlhDataobject object = objList.get(0);
			DBType dbtype = DBType.getType(object.getDbType());
			dbtype.getService().modify(object, dataList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public void deleteData(ResourceAttachInfo resourceAttachInfo) throws CustomException {
		try {
			DlhDataobject object = this.getDataObjectByObjUrl(resourceAttachInfo.getObjUrl());


			DBType dbtype = DBType.getType(object.getDbType());
			boolean su = true;
			if(!StringUtil.isEmpty(resourceAttachInfo.getDelFileIds())){
				List<Map> condMapList = resourceAttachInfo.getConMapList();
				su = dbtype.getService().deleteData(object, condMapList);
			}
			if(su){
				dbtype.getService().addData(object,resourceAttachInfo.getDataList());
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new CustomException(e.getMessage());
		}
	}

	@Override
	public List<Map<String, String>> getPrimaryKey(String objUrl,String uuid) throws CustomException {
		DlhDataobject object = this.getDataObjectByObjUrl(objUrl);
		DBType dbtype = DBType.getType(object.getDbType());
		List<Map<String, String>> mapList = dbtype.getService().queryDataByPrimaryKey(object, uuid);
		return mapList;
	}

	private DlhDataobject getDataObjectByObjUrl(String objUrl)throws CustomException{
		DlhDataobject cond = new DlhDataobject();
		cond.setObjUrl(objUrl);
		List<DlhDataobject> objList = dlhDataobjectDao.queryAll(cond);
		if(objList==null||objList.size()<=0){
			throw new CustomException("对象url没有找到对应数据记录"+objUrl);
		}
		return objList.get(0);
	}

	public void addData(ResourceAttachInfo resourceAttachInfo) throws CustomException {
		DlhDataobject object = this.getDataObjectByObjUrl(resourceAttachInfo.getObjUrl());
		DBType dbtype = DBType.getType(object.getDbType());
		dbtype.getService().addData(object,resourceAttachInfo.getDataList());
	}

	@Override
	public void updateData(Map<String, Object> dataMap, String objUrl, String uuid, String column) throws CustomException {
		DlhDataobject object = this.getDataObjectByObjUrl(objUrl);
		DBType dbtype = DBType.getType(object.getDbType());
		dbtype.getService().updateData(object,dataMap,column,uuid);
	}

	@Override
	public void init(DlhDataobject dlhDataobject) throws CustomException {
		try {
			// 查询数据对象
			DlhDataobject obj = dlhDataobjectDao.get(dlhDataobject);
			// 判断是否生成过
			String sqlCount = "select count(*) from busi_dlh_dataobject_field where object_id='" + obj.getId() + "'";
			Integer number = jdbcTemplate.queryForObject(sqlCount, Integer.class);
			if (number != null && number.intValue() > 0) {
				throw new CustomException("已经添加过数据，不能再次初始化");
			}
			// 批量插入
			StringBuilder sql = new StringBuilder();
			sql.append(" INSERT INTO busi_dlh_dataobject_field");
			sql.append(" (id,");
			sql.append("  object_id,");
			sql.append("  model_id,");
			sql.append("  field_code,");
			sql.append("  field_name,");
			sql.append("  item_id,");
			sql.append("  field_seq,");
			sql.append("  DELETE_FLAG,");
			sql.append("  modify_date,");
			sql.append("  create_date)");
			sql.append(" SELECT");
			sql.append(" replace(uuid(),'-',''),");
			sql.append(" '" + obj.getId() + "',");
			sql.append(" item.model_id,");
			sql.append(" item.item_name,");
			sql.append(" item.item_comment,");
			sql.append(" item.ID,");
			sql.append(" item.item_seq,");
			sql.append(" 0,");
			sql.append(" NOW(),");
			sql.append(" NOW()");
			sql.append(" FROM busi_dlh_datamodel_item item");
			sql.append(" WHERE item.model_id = '" + obj.getModelId() + "' ");
			jdbcTemplate.execute(sql.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new CustomException(ex.getMessage());
		}

	}

}