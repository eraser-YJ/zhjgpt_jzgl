package com.jc.csmp.project.dao.impl;

import com.jc.csmp.project.dao.ICmProjectWeeklyItemDao;
import com.jc.csmp.project.domain.CmProjectWeeklyItem;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.util.Result;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-周报事项DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectWeeklyItemDaoImpl extends BaseClientDaoImpl<CmProjectWeeklyItem> implements ICmProjectWeeklyItemDao {
	public CmProjectWeeklyItemDaoImpl(){}
	@Override
	public Integer updateWeeklyByIds(CmProjectWeeklyItem entity) {
		return this.getTemplate().update(this.getNameSpace(entity) + "." + "updateWeeklyByIds", entity);
	}
}
