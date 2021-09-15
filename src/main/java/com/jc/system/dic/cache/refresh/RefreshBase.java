package com.jc.system.dic.cache.refresh;

import com.jc.system.dic.cache.CacheDate;
import com.jc.system.dic.domain.Dic;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public abstract class RefreshBase {
	public abstract void refresh(Dic dic, Dic oldDic, CacheDate cacheData);
}
