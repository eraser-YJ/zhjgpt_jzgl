package com.jc.system.common.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface ICommonDao extends IBaseDao<BaseBean> {

	String getDBSysdate() throws CustomException;
}
