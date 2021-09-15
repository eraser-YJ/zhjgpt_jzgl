package com.jc.csmp.safe.supervision.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.safe.supervision.dao.ISafetyUnitDao;
import com.jc.csmp.safe.supervision.domain.SafetyUnit;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class SafetyUnitDaoImpl extends BaseClientDaoImpl<SafetyUnit> implements ISafetyUnitDao{

	public SafetyUnitDaoImpl(){}

}