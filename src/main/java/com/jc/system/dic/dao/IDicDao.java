package com.jc.system.dic.dao;

import java.util.List;

import com.jc.foundation.dao.IBaseDao;
import com.jc.system.dic.domain.Dic;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IDicDao extends IBaseDao<Dic> {

	List<Dic> getDicByDuty(Dic dic);
	
	Integer delForDicList(Dic dic);
}
