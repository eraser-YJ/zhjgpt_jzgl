package com.jc.csmp.doc.dept.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import org.springframework.stereotype.Repository;

import com.jc.csmp.doc.dept.dao.ISysDepartmentDao;
import com.jc.csmp.doc.dept.domain.SysDepartment;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class SysDepartmentDaoImpl extends BaseServerDaoImpl<SysDepartment> implements ISysDepartmentDao{

	public SysDepartmentDaoImpl(){}

}