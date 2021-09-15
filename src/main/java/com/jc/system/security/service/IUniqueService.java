package com.jc.system.security.service;

import com.jc.foundation.service.IBaseService;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.exception.DBException;
import com.jc.foundation.domain.PageManager;
import com.jc.system.security.domain.Unique;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IUniqueService extends IBaseService<Unique>{
	Integer deleteByIds(Unique unique) throws CustomException;
	Unique getOne(Unique t) throws CustomException;
}