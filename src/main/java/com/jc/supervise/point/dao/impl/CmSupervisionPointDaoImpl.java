package com.jc.supervise.point.dao.impl;

import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.supervise.point.dao.ICmSupervisionPointDao;
import com.jc.supervise.point.domain.CmSupervisionPoint;
import org.springframework.stereotype.Repository;

/**
 * 建设管理-监察点管理DaoImpl
 * @Author changpeng
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Repository
public class CmSupervisionPointDaoImpl extends BaseClientDaoImpl<CmSupervisionPoint> implements ICmSupervisionPointDao {
	public CmSupervisionPointDaoImpl(){}
}
