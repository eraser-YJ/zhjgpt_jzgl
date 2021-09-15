package com.jc.common.kit;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存数据工具类
 * 
 * @author lc  liubq
 * @since 2018年9月29日
 */
public class CacheObj<T> {
	// 存储空间
	private Map<String, CacheData<T>> cacheArea = new HashMap<>();

	/**
	 * 取得指定key的对应的数据
	 * 
	 * @param key 标识
	 * @return 数据
	 */
	public T get(String key) {
		CacheData<T> data = cacheArea.get(key);
		if (data != null && !isExpire(key, data)) {
			return data.getData();
		}
		return null;
	}

	/**
	 * 缓存指定数据
	 * 
	 * @param key    标识
	 * @param data   数据
	 * @param expire 缓存时间 单位:秒
	 */
	public void put(String key, T data, int expire) {
		// 赋值
		cacheArea.put(key, new CacheData<T>(data, expire));
	}

	/**
	 * 缓存指定数据 默认8小时
	 * 
	 * @param key  标识
	 * @param data 数据
	 */
	public void put(String key, T data) {
		put(key, data, 3600 * 8);
	}

	/**
	 * 清理单个值
	 * 
	 * @param key
	 */
	public void clear(String key) {
		cacheArea.remove(key);
	}

	/**
	 * 清理全部
	 */
	public void clearAll() {
		cacheArea.clear();
	}

	/**
	 * 是否过期
	 * 
	 * @param data
	 * @return true 过期了，false 没有过期
	 */
	private boolean isExpire(String key, CacheData<T> data) {
		if (data != null && data.getSaveTime() >= System.currentTimeMillis() + 10) {
			return false;
		}
		return true;
	}

	/**
	 * 缓存对象
	 * 
	 * @author lc  liubq
	 */
	private class CacheData<E> {

		/**
		 * 缓存对象
		 * 
		 * @param t      数据
		 * @param expire 缓存时间 单位:秒
		 */
		CacheData(E t, int expire) {
			this.data = t;
			this.expire = expire <= 0 ? 0 : expire * 1000;
			this.saveTime = System.currentTimeMillis() + this.expire;
		}

		// 数据
		private E data;
		// 过期时间
		private long saveTime;
		// 缓存时间
		private long expire;

		/**
		 * 数据
		 * 
		 * @return
		 */
		public E getData() {
			return data;
		}

		/**
		 * 过期时间
		 * 
		 * @return
		 */
		public long getSaveTime() {
			return saveTime;
		}
	}
}