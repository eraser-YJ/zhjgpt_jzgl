package com.jc.csmp.projectSgxk.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.projectSgxk.dao.ICompanyProjectsSgxkDao;
import com.jc.csmp.projectSgxk.domain.CompanyProjectsSgxk;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class CompanyProjectsSgxkDaoImpl extends BaseClientDaoImpl<CompanyProjectsSgxk> implements ICompanyProjectsSgxkDao{

	public CompanyProjectsSgxkDaoImpl(){}

}