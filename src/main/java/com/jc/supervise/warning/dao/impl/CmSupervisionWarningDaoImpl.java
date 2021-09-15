package com.jc.supervise.warning.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.supervise.warning.dao.ICmSupervisionWarningDao;
import com.jc.supervise.warning.domain.CmSupervisionWarning;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-监管预警管理DaoImpl
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmSupervisionWarningDaoImpl extends BaseClientDaoImpl<CmSupervisionWarning> implements ICmSupervisionWarningDao {
	public CmSupervisionWarningDaoImpl(){}
}
