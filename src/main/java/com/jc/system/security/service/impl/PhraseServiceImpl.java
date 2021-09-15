package com.jc.system.security.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jc.system.security.SystemSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.service.impl.BaseServiceImpl;
import com.jc.system.security.dao.IPhraseDao;
import com.jc.system.security.domain.Phrase;
import com.jc.system.security.domain.User;
import com.jc.system.security.service.IPhraseService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Service
public class PhraseServiceImpl extends BaseServiceImpl<Phrase> implements IPhraseService{

	private IPhraseDao phraseDao;

	public PhraseServiceImpl(){}
	
	@Autowired
	public PhraseServiceImpl(IPhraseDao phraseDao){
		super(phraseDao);
		this.phraseDao = phraseDao;
	}
	
	/**
	 * 获取当前人的常用词(包括个人与公共)
	 * @param userId 查询人员id
	 * @return Map<String,List<Phrase>>查询结果  key:person为个人，key:common为公共
	 */
	@Override
    public Map<String,List<Phrase>> queryPhrase(String userId){
		Map<String,List<Phrase>> result = new HashMap<>(2);
		result.put("person", queryPersonPhrase(userId));
		result.put("common", queryCommonPhrase());
		return result;
	}
	
	/**
	 * 查询个人常用词
	 * @param userId 人员id
	 * @return	List<Phrase> 个人常用词列表
	 */
	private List<Phrase> queryPersonPhrase(String userId){
		Phrase phrase = new Phrase();
		phrase.setPhraseType("1");
		phrase.setCreateUser(userId);
		phrase.addOrderByFieldDesc("t.CREATE_DATE");
		return phraseDao.queryAll(phrase);
	}
	
	/**
	 * 查询公共常用词
	 * @param
	 * @return	List<Phrase> 公共常用词列表
	 */
	private List<Phrase> queryCommonPhrase(){
		User user = SystemSecurityUtils.getUser();
		Phrase phrase = new Phrase();
		phrase.setPhraseType("0");
		phrase.setCreateUserOrg(user.getOrgId());
		phrase.addOrderByFieldDesc("t.CREATE_DATE");
		return phraseDao.queryAll(phrase);
	}

	@Override
    public PageManager queryPhraseForUser(Phrase phrase, PageManager page) {
		return phraseDao.queryPhraseForUser(phrase,page);
	}
}