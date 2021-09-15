package com.jc.system.dic.cache.refresh;

import com.jc.system.dic.cache.CacheDate;
import com.jc.system.dic.domain.BaseDic;
import com.jc.system.dic.domain.Dic;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class DicRefreshManage {
	public void refresh(Dic dic, Dic oldDic, CacheDate cacheData) {
		Dic dictemp = new Dic();
		dictemp.setId(dic.getId());
		dictemp.setCode(dic.getCode());
		dictemp.setParentId(dic.getParentId());
		dictemp.setParentType(dic.getParentType());
		new DicTypeRefresh().refresh(dictemp, oldDic, cacheData);
		new DicItemRefresh().refresh(dictemp, oldDic, cacheData);
	}

	public void refresh(String flag, BaseDic dic, BaseDic oldDic,
			CacheDate cacheData) {
	}
}
