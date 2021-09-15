package com.jc.csmp.project.dao.impl;

import com.jc.csmp.project.dao.ICmProjectInfoDao;
import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalUtil;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-项目管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectInfoDaoImpl extends BaseClientDaoImpl<CmProjectInfo> implements ICmProjectInfoDao {
	public CmProjectInfoDaoImpl(){}

	@Override
	public int finishByProjectNumbers(String projectNumbers) {
		CmProjectInfo entity = new CmProjectInfo();
		entity.setPrimaryKeys(GlobalUtil.splitStr(projectNumbers, ','));
		return this.getTemplate().update(this.getNameSpace(entity) + ".finishByProjectNumbers", entity);
	}

	@Override
	public PageManager queryProjectSafety(CmProjectInfo o, PageManager page) {
			return this.queryByPage(o, page, "queryProjectSafetyCount", "queryProjectSafety");
	}
}
