package com.jc.common.kit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 转换工具
 * 
 * @author lc  liubq
 * @since 2018年9月29日
 */
public class ArrayTool {

	/**
	 * 转为字符串数组
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> String[] toArray(final T... ts) {
		if (ts == null || ts.length == 0) {
			return new String[0];
		}
		String[] resArr = new String[ts.length];
		int index = 0;
		for (T t : ts) {
			resArr[index++] = String.valueOf(t);
		}
		return resArr;
	}

	/**
	 * 转为字符串数组
	 * 
	 * @param list
	 * @return
	 */
	public static <T> String[] toArray(final Collection<T> list) {
		if (list == null || list.size() == 0) {
			return new String[0];
		}
		String[] resArr = new String[list.size()];
		int index = 0;
		for (T t : list) {
			resArr[index++] = String.valueOf(t);
		}
		return resArr;
	}

	/**
	 * 转为Long数组
	 * 
	 * @param list
	 * @return
	 */
	public static <T> Long[] toLongArray(final Collection<T> list) {
		if (list == null || list.size() == 0) {
			return new Long[0];
		}
		Long[] resArr = new Long[list.size()];
		int index = 0;
		for (T t : list) {
			resArr[index++] = Long.valueOf(t.toString());
		}
		return resArr;
	}

	/**
	 * 转为Integer数组
	 * 
	 * @param list
	 * @return
	 */
	public static <T> Integer[] toIntegerArray(final Collection<T> list) {
		if (list == null || list.size() == 0) {
			return new Integer[0];
		}
		Integer[] resArr = new Integer[list.size()];
		int index = 0;
		for (T t : list) {
			resArr[index++] = Integer.valueOf(t.toString());
		}
		return resArr;
	}

	/**
	 * 转为字符串列表
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> List<String> toList(final T[] arr) {
		List<String> list = new ArrayList<String>();
		if (arr == null || arr.length == 0) {
			return list;
		}
		for (T t : arr) {
			list.add(String.valueOf(t));
		}
		return list;
	}

	/**
	 * 转为字符串列表
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> List<String> toList(final Collection<T> paramList) {
		List<String> list = new ArrayList<String>();
		if (paramList == null || paramList.size() == 0) {
			return list;
		}
		for (T t : paramList) {
			list.add(String.valueOf(t));
		}
		return list;
	}

	/**
	 * 转为Long列表
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> List<Long> toLongList(final T[] arr) {
		List<Long> list = new ArrayList<Long>();
		if (arr == null || arr.length == 0) {
			return list;
		}
		for (T t : arr) {
			list.add(Long.valueOf(t.toString()));
		}
		return list;
	}

	/**
	 * 转为Long列表
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> List<Long> toLongList(final Collection<T> paramList) {
		List<Long> list = new ArrayList<Long>();
		if (paramList == null || paramList.size() == 0) {
			return list;
		}
		for (T t : paramList) {
			list.add(Long.valueOf(t.toString()));
		}
		return list;
	}

	/**
	 * 转为Integer列表
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> List<Integer> toIntegerList(final T[] arr) {
		List<Integer> list = new ArrayList<Integer>();
		if (arr == null || arr.length == 0) {
			return list;
		}
		for (T t : arr) {
			list.add(Integer.valueOf(t.toString()));
		}
		return list;
	}

	/**
	 * 转为Integer列表
	 * 
	 * @param arr
	 * @return
	 */
	public static <T> List<Integer> toIntegerList(final Collection<T> paramList) {
		List<Integer> list = new ArrayList<Integer>();
		if (paramList == null || paramList.size() == 0) {
			return list;
		}
		for (T t : paramList) {
			list.add(Integer.valueOf(t.toString()));
		}
		return list;
	}
}
