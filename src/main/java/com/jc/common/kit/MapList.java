package com.jc.common.kit;

import java.util.*;

/**
 * MapList工具类
 * 
 * @author lc  liubq
 * @since 20170711
 */
public class MapList<K, V> {

	// 存数据
	private Map<K, List<V>> dataMap = new HashMap<K, List<V>>();

	/**
	 * 添加数据 空对象不能添加
	 * 
	 * @param K key
	 * @param V value
	 */
	public void put(K key, V value) {
		List<V> items = dataMap.get(key);
		if (items == null) {
			items = new ArrayList<V>();
		}
		items.add(value);
		dataMap.put(key, items);
	}

	/**
	 * 添加数据 空对象不能添加
	 * 
	 * @param key
	 * @param list
	 */
	public void putList(K key, List<V> list) {
		List<V> items = dataMap.get(key);
		if (items == null) {
			items = new ArrayList<V>();
		}
		if (list != null) {
			items.addAll(list);
		}
		dataMap.put(key, items);
	}

	/**
	 * 取得key对应的值
	 * 
	 * @param key
	 * @return 值
	 */
	public List<V> get(K key) {
		return dataMap.get(key);
	}

	/**
	 * 取得所有的Keys
	 * 
	 * @return Keys
	 */
	public List<K> getKeys() {
		Set<K> set = dataMap.keySet();
		List<K> list = new ArrayList<K>();
		list.addAll(set);
		return list;
	}

	/**
	 * 添加参数对象的所有的MapList值对
	 * 
	 * @param map MapList对象
	 */
	@SuppressWarnings("unchecked")
	public void putAll(MapList<K, V> map) {
		if (map != null) {
			dataMap.putAll((Map<? extends K, ? extends List<V>>) map);
		}
	}

	/**
	 * 取得所有的MapList值对
	 * 
	 * @return 所有的MapList值对
	 */
	public Set<Map.Entry<K, List<V>>> entrySet() {
		return dataMap.entrySet();
	}
}
