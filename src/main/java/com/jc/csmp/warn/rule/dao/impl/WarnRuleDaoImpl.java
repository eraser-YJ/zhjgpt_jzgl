package com.jc.csmp.warn.rule.dao.impl;

import org.springframework.stereotype.Repository;

import com.jc.csmp.warn.rule.dao.IWarnRuleDao;
import com.jc.csmp.warn.rule.domain.WarnRule;
import com.jc.foundation.dao.impl.BaseClientDaoImpl;
import com.jc.foundation.exception.ConcurrentException;
import com.jc.foundation.exception.DBException;

/**
 * @title   
 * @version
 */
@Repository
public class WarnRuleDaoImpl extends BaseClientDaoImpl<WarnRule> implements IWarnRuleDao{

	public WarnRuleDaoImpl(){}

}