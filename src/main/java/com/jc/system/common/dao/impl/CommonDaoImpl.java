package com.jc.system.common.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.exception.CustomException;
import com.jc.system.common.dao.ICommonDao;
import org.springframework.stereotype.Repository;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class CommonDaoImpl extends BaseServerDaoImpl<BaseBean> implements ICommonDao {

	@Override
	public String getDBSysdate() throws CustomException {
		return getTemplate().selectOne("com.jc.system.getSysdate");
	}

}
