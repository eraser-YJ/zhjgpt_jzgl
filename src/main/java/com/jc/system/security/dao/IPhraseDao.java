package com.jc.system.security.dao;

import com.jc.foundation.dao.IBaseDao;
import com.jc.foundation.domain.PageManager;
import com.jc.system.security.domain.Phrase;

/**
 * 常用词
 * @author Administrator
 * @date 2020-07-01
 */
public interface IPhraseDao extends IBaseDao<Phrase>{

	/**
	 * 查询所有常用词
	 * @param phrase
	 * @param page
	 * @return
	 */
	PageManager queryPhraseForUser(Phrase phrase,PageManager page);
	
}
