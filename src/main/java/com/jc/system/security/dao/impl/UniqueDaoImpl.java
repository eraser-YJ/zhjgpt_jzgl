package com.jc.system.security.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.dao.IUniqueDao;
import com.jc.system.security.domain.Unique;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class UniqueDaoImpl extends BaseServerDaoImpl<Unique> implements IUniqueDao{

	public UniqueDaoImpl(){}

	@Override
	public Unique getOne(Unique t) throws CustomException {
		return (Unique) getTemplate().selectOne(getNameSpace(t) + ".getOne", t);
	}
}