package com.jc.csmp.dic.dao.impl;

import com.jc.csmp.dic.dao.ICmCustomDicDao;
import com.jc.csmp.dic.domain.CmCustomDic;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-自定义字典项DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmCustomDicDaoImpl extends BaseClientDaoImpl<CmCustomDic> implements ICmCustomDicDao {
	public CmCustomDicDaoImpl(){}

	@Override
	public long queryCountByParentIds(CmCustomDic entity) {
		Object rowsCountObject = this.getTemplate().selectOne(this.getNameSpace(entity) + ".queryCountByParentIds", entity);
		long rowsCount;
		if (rowsCountObject instanceof Long) {
			rowsCount = (Long) rowsCountObject;
		} else {
			rowsCount = new Long((Integer) rowsCountObject);
		}
		return rowsCount;
	}
}
