package com.jc.csmp.company.dao.impl;

import com.jc.csmp.company.dao.ICmCompanyInfoDao;
import com.jc.csmp.company.domain.CmCompanyInfo;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-单位管理DaoImpl
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmCompanyInfoDaoImpl extends BaseClientDaoImpl<CmCompanyInfo> implements ICmCompanyInfoDao {
	public CmCompanyInfoDaoImpl(){}
}
