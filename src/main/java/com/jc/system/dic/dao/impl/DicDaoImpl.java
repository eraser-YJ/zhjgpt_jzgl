package com.jc.system.dic.dao.impl;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.system.dic.dao.IDicDao;
import com.jc.system.dic.domain.Dic;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 * @date 2020-06-30
 */
@Repository
public class DicDaoImpl extends BaseServerDaoImpl<Dic> implements IDicDao {

	@Override
    public List<Dic> getDicByDuty(Dic dic) {
		return getTemplate().selectList(getNameSpace(dic)+".queryDuty", dic);
	}

	@Override
    public Integer delForDicList(Dic dic) {
		return getTemplate().delete(getNameSpace(dic)+".deleteLogic", dic);
	}

	
}