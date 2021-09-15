package com.jc.dlh.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.dlh.dao.IDlhTableMapDao;
import com.jc.dlh.domain.DlhTableMap;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class DlhTableMapDaoImpl extends BaseClientDaoImpl<DlhTableMap> implements IDlhTableMapDao{

	public DlhTableMapDaoImpl(){}

}