package com.jc.dlh.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.jc.dlh.dao.IDlhDatamodelDao;
import com.jc.dlh.domain.DlhDatamodel;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.DBException;

/**
 * @title 统一数据资源中心
 * @description dao实现类 
 * @author lc 
 * @version 2020-03-10
 */
@Repository
public class DlhDatamodelDaoImpl extends BaseClientDaoImpl<DlhDatamodel> implements IDlhDatamodelDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public DlhDatamodelDaoImpl() {
	}

	public Integer save(DlhDatamodel o) throws DBException {
		Integer localInteger = Integer.valueOf(-1);
		try {
			// 自定义ID
			String idSql = " SELECT Max(CAST(id AS signed))+1 FROM busi_dlh_datamodel ";
			o.setId(String.valueOf(jdbcTemplate.queryForObject(idSql, Long.class)));
			localInteger = Integer.valueOf(getTemplate().insert(getNameSpace(o) + "." + "insert", o));
		} catch (Exception localException) {
			this.log.error(localException, localException);
			DBException localDBException = new DBException(localException);
			localDBException.setLogMsg("数据库添加数据发生错误");
			throw localDBException;
		}
		return localInteger;
	}

}