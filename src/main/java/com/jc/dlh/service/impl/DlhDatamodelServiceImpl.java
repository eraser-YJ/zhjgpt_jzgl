package com.jc.dlh.service.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jc.common.db.DBType;
import com.jc.common.kit.ListTool;
import com.jc.dlh.dao.IDlhDatamodelDao;
import com.jc.dlh.domain.DlhDatamodel;
import com.jc.dlh.service.IDlhDatamodelService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.service.impl.BaseServiceImpl;

/**
 * @title 统一数据资源中心
 * @description 业务服务类
 * @author lc 
 * @version 2020-03-10
 */
@Service
public class DlhDatamodelServiceImpl extends BaseServiceImpl<DlhDatamodel> implements IDlhDatamodelService {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private IDlhDatamodelDao dlhDatamodelDao;

	public DlhDatamodelServiceImpl() {
	}

	@Autowired
	public DlhDatamodelServiceImpl(IDlhDatamodelDao dlhDatamodelDao) {
		super(dlhDatamodelDao);
		this.dlhDatamodelDao = dlhDatamodelDao;
	}

	/**
	 * @description 根据主键删除多条记录方法
	 * @param DlhDatamodel dlhDatamodel 实体类
	 * @return Integer 处理结果
	 * @author lc 
	 * @version 2020-03-10
	 */
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public Integer deleteByIds(DlhDatamodel dlhDatamodel) throws CustomException {
		Integer result = -1;
		try {

			String[] ids = dlhDatamodel.getPrimaryKeys();
			// 判断是否被引用
			String idsStr = ListTool.joinSQLString(Arrays.asList(ids));
			String sqlExists = "select count(*) from busi_dlh_dataobject_field where model_id in (" + idsStr + ")";
			Integer value = jdbcTemplate.queryForObject(sqlExists, Integer.class);
			if (value != null && value > 0) {
				throw new Exception("已经被引用不能删除！");
			}
			// 删除字表
			String sqlDel = "delete from busi_dlh_datamodel_item where model_id in (" + idsStr + ")";
			jdbcTemplate.execute(sqlDel);
			result = dlhDatamodelDao.delete(dlhDatamodel, false);
		} catch (Exception e) {
			e.printStackTrace();
			CustomException ce = new CustomException(e.getMessage());
			ce.setBean(dlhDatamodel);
			throw ce;
		}
		return result;
	}

	@Override
	public void publish(DlhDatamodel dlhDatamodel) throws CustomException {
		try {
			if (dlhDatamodel.getId() == null) {
				throw new CustomException("参数不正确");
			}
			DlhDatamodel head = dlhDatamodelDao.get(dlhDatamodel);
			if (head == null) {
				throw new CustomException("没有定义模型");
			}
			DBType dbtype = DBType.getType(head.getDbType());
			dbtype.getService().publish(head);
		} catch (Exception ex) {
			throw new CustomException(ex.getMessage(), ex);
		}
	}

}