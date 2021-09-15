package com.jc.system.dic.cache.init;

import java.util.List;

import org.apache.log4j.Logger;

import com.jc.system.dic.cache.CacheDate;
import com.jc.system.dic.domain.Dic;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public abstract class InitBase {
	protected final Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @description 初始化数据
	 * @param list 数据list
	 * @param cacheData 数据存放的bean
	 */
	public abstract void init(List<Dic> list, CacheDate cacheData);
}
