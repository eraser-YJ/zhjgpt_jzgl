package com.jc.csmp.project.dao.impl;

import com.jc.csmp.project.dao.ICmProjectRelationOrderDao;
import com.jc.csmp.project.domain.CmProjectRelationOrder;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.DBException;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * 建设管理-项目工程联系单Dao实现
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectRelationOrderDaoImpl extends BaseClientDaoImpl<CmProjectRelationOrder> implements ICmProjectRelationOrderDao {

	public CmProjectRelationOrderDaoImpl(){}

	@Override
	public Integer insertRelationDept(String relationId, String personType, String deptId) throws DBException {
		Integer result = -1;
		CmProjectRelationOrder entity = new CmProjectRelationOrder();
		try {
			entity.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			entity.setRelationId(relationId);
			entity.setPersonType(personType);
			entity.setDeptId(deptId);
			result = this.getTemplate().insert(this.getNameSpace(entity) + "." + "insertRelationDept", entity);
			return result;
		} catch (Exception var5) {
			this.log.error(var5, var5);
			DBException exception = new DBException(var5);
			exception.setLogMsg("数据库添加数据发生错误");
			throw exception;
		}
	}

}