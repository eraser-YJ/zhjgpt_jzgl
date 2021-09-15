package com.jc.common.kit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 缓存数据工具类
 * 
 * @author lc  liubq
 * @since 2018年9月29日
 */
public class MemoryCache {

	// 存储空间
	private static Map<String, CacheData<Object>> cacheArea = new ConcurrentHashMap<String, CacheData<Object>>();

	// 清理线程
	private static ScheduledExecutorService cleaner;

	/**
	 * 取得指定key的对应的数据
	 * 
	 * @param key 标识
	 * @return 数据
	 */
	public static Object get(String key) {
		CacheData<Object> data = cacheArea.get(key);
		if (data != null && !isExpire(data)) {
			return data.getData();
		}
		return null;
	}

	/**
	 * 缓存指定数据
	 * 
	 * @param key 标识
	 * @param data 数据
	 * @param expire 缓存时间 单位:秒
	 */
	public static void put(String key, Object data, int expire) {
		// 初始化
		init();
		// 赋值
		cacheArea.put(key, new CacheData<Object>(data, expire));
	}

	/**
	 * 缓存指定数据 默认8小时
	 * 
	 * @param key 标识
	 * @param data 数据
	 */
	public static void put(String key, Object data) {
		put(key, data, 3600 * 1);
	}

	/**
	 * 清理单个值
	 * 
	 * @param key
	 */
	public static void clear(String key) {
		cacheArea.remove(key);
	}

	/**
	 * 清理全部
	 */
	public static void clearAll() {
		cacheArea.clear();
	}

	/**
	 * 是否过期
	 * 
	 * @param data
	 * @return true 过期了，false 没有过期
	 */
	private static boolean isExpire(CacheData<Object> data) {
		if (data != null && data.getSaveTime() >= System.currentTimeMillis() + 10) {
			return false;
		}
		return true;
	}

	/**
	 * 初始化
	 */
	private static void init() {
		if (cleaner == null || cleaner.isShutdown() || cleaner.isTerminated()) {
			synchronized (MemoryCache.class) {
				if (cleaner == null || cleaner.isShutdown() || cleaner.isTerminated()) {
					cleaner = Executors.newScheduledThreadPool(1);
					cleaner.scheduleWithFixedDelay(new Runnable() {
						@Override
						public void run() {
							try {
								List<String> rmKeyList = new ArrayList<String>();
								for (Map.Entry<String, CacheData<Object>> entry : cacheArea.entrySet()) {
									if (isExpire(entry.getValue())) {
										rmKeyList.add(entry.getKey());
									}
								}
								for (String key : rmKeyList) {
									cacheArea.remove(key);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}, 120, 120, TimeUnit.SECONDS);
				}
			}
		}
	}

	/**
	 * 缓存对象
	 * 
	 * @author lc  liubq
	 */
	private static class CacheData<T> {

		/**
		 * 缓存对象
		 * 
		 * @param t 数据
		 * @param expire 缓存时间 单位:秒
		 */
		CacheData(T t, int expire) {
			this.data = t;
			this.expire = expire <= 0 ? 0 : expire * 1000;
			this.saveTime = System.currentTimeMillis() + this.expire;
		}

		// 数据
		private T data;
		// 过期时间
		private long saveTime;
		// 缓存时间
		private long expire;

		/**
		 * 数据
		 * 
		 * @return
		 */
		public T getData() {
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