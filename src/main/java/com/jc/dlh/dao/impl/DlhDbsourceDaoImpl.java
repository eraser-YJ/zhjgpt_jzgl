package com.jc.dlh.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.dlh.dao.IDlhDbsourceDao;
import com.jc.dlh.domain.DlhDbsource;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;

/**
 * @title 统一数据资源中心
 * @author lc  
 * @version  2020-04-07
 */
@Repository
public class DlhDbsourceDaoImpl extends BaseClientDaoImpl<DlhDbsource> implements IDlhDbsourceDao{

	public DlhDbsourceDaoImpl(){}

}