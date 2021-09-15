package com.jc.system.security.dao;

import com.jc.foundation.exception.CustomException;
import com.jc.system.security.domain.Unique;

import com.jc.foundation.dao.IBaseDao;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IUniqueDao extends IBaseDao<Unique>{

    Unique getOne(Unique t) throws CustomException;
}
