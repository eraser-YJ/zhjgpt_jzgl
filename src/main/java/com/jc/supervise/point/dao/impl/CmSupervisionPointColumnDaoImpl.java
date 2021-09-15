package com.jc.supervise.point.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.util.Result;
import com.jc.supervise.point.dao.ICmSupervisionPointColumnDao;
import com.jc.supervise.point.domain.CmSupervisionPointColumn;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-监察点数据来源管理DaoImpl
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmSupervisionPointColumnDaoImpl extends BaseClientDaoImpl<CmSupervisionPointColumn> implements ICmSupervisionPointColumnDao {
	public CmSupervisionPointColumnDaoImpl(){}

	@Override
	public Integer updateSupervisionId(String newSupervisionId, String oldSupervisionId) {
		CmSupervisionPointColumn entity = new CmSupervisionPointColumn();
		entity.setNewSupervisionId(newSupervisionId);
		entity.setSupervisionId(oldSupervisionId);
		return this.getTemplate().update(this.getNameSpace(entity) + "." + "updateSupervisionId", entity);
	}
}
