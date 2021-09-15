package com.jc.system.security.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.foundation.dao.impl.BaseServerDaoImpl;
import com.jc.foundation.domain.PageManager;
import com.jc.system.security.dao.IPhraseDao;
import com.jc.system.security.domain.Phrase;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Repository
public class PhraseDaoImpl extends BaseServerDaoImpl<Phrase> implements IPhraseDao{

	public PhraseDaoImpl(){}

	@Override
    public PageManager queryPhraseForUser(Phrase phrase, PageManager page) {
		return queryByPage(phrase,page,"queryCountForUser","queryForUser");
	}

}