package com.jc.system.security.service;

import java.util.List;
import java.util.Map;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.service.IBaseService;
import com.jc.system.security.domain.Phrase;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public interface IPhraseService extends IBaseService<Phrase>{
	/**
	  * 获取当前人的常用词(包括个人与公共)
	  * @param userId 查询人员id
	  * @return Map<String,List<Phrase>>查询结果  key:person为个人，key:common为公共
	*/
	Map<String,List<Phrase>> queryPhrase(String userId);

	/**
	 * 查询所有常用词
	 * @param phrase
	 * @param page
	 * @return
	 */
	PageManager queryPhraseForUser(Phrase phrase,PageManager page);
	
}