package com.jc.csmp.project.dao.impl;

import com.jc.csmp.project.dao.ICmProjectDataAuthDao;
import com.jc.csmp.project.domain.CmProjectDataAuth;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-项目数据权限管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmProjectDataAuthDaoImpl extends BaseClientDaoImpl<CmProjectDataAuth> implements ICmProjectDataAuthDao {
	public CmProjectDataAuthDaoImpl(){}

	@Override
	public Integer deleteByBusinessId(String businessId) {
		CmProjectDataAuth param = new CmProjectDataAuth();
		param.setBusinessId(businessId);
		return this.getTemplate().delete(this.getNameSpace(param) + "." + "deleteByBusinessId", param);
	}
}
