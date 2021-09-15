package com.jc.common.kit;

import java.lang.reflect.Method;
import java.util.*;

/**
 * 分组排序工具
 * 
 * 
 * @author lc  liubq
 * @since 2017年12月21日
 */
public class ListTool {

	/**
	 * 分组
	 * 
	 * @param list
	 * @param attName
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, V> Map<T, V> getMap(final Collection<V> list, final String attName, final Class<T> clazz) {
		Map<T, V> dataMap = new HashMap<T, V>();
		if (list == null || list.size() == 0) {
			return dataMap;
		}
		// 取得第一个
		V obj = null;
		for (V e : list) {
			obj = e;
			break;
		}
		Method[] methods = obj.getClass().getMethods();
		Method targetMethod = findMethod(methods, attName);
		if (targetMethod == null) {
			throw new RuntimeException("参数错误属性" + attName + "未定义");
		}
		Object keyValue;
		for (V item : list) {
			try {
				keyValue = targetMethod.invoke(item);
				dataMap.put((T) keyValue, item);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return dataMap;
	}

	/**
	 * 分组
	 * 
	 * @param list
	 * @param attName
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, V> MapList<T, V> getMapList(final Collection<V> list, final String attName, final Class<T> clazz) {
		MapList<T, V> dataMap = new MapList<T, V>();
		if (list == null || list.size() == 0) {
			return dataMap;
		}
		// 取得第一个
		V obj = null;
		for (V e : list) {
			obj = e;
			break;
		}
		Method[] methods = obj.getClass().getMethods();
		Method targetMethod = findMethod(methods, attName);
		if (targetMethod == null) {
			throw new RuntimeException("参数错误属性" + attName + "未定义");
		}
		Object keyValue;
		for (V item : list) {
			try {
				keyValue = targetMethod.invoke(item);
				dataMap.put((T) keyValue, item);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return dataMap;
	}

	/**
	 * 取得特定列的值
	 * 
	 * @param list
	 * @param attName
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, V> List<T> getValueList(final Collection<V> list, final String attName, final Class<T> clazz) {
		List<T> resList = new ArrayList<T>();
		if (EmptyTool.isEmpty(list)) {
			return resList;
		}
		// 取得第一个
		V obj = null;
		for (V e : list) {
			obj = e;
			break;
		}
		Method[] methods = obj.getClass().getMethods();
		Method targetMethod = findMethod(methods, attName);
		if (targetMethod == null) {
			throw new RuntimeException("参数错误属性" + attName + "未定义");
		}
		try {
			Object t;
			for (V e : list) {
				t = targetMethod.invoke(e);
				if (t != null) {
					resList.add((T) t);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return resList;
	}

	/**
	 * 查收指定方法
	 * 
	 * @param methods
	 * @param attName
	 * @return
	 */
	private static Method findMethod(Method[] methods, String attName) {
		if (methods == null) {
			return null;
		}
		for (Method m : methods) {
			String mName = m.getName();
			if (isMethodEquals(mName, attName)) {
				return m;
			}
		}
		return null;
	}

	/**
	 * 判断是否是指定的get方法
	 * 
	 * @param methodName
	 * @param name
	 * @return
	 */
	private static boolean isMethodEquals(String methodName, String name) {
		if (!methodName.startsWith("get")) {
			return false;
		}
		String mName = methodName.toLowerCase();
		String nameLow = name.toLowerCase();
		String nameLowGet = "get" + nameLow;
		if (mName.equals(nameLow) || mName.equals(nameLowGet)) {
			return true;
		}
		return false;
	}

	// ***************************************************************************************************************************
	/**
	 * 空处理
	 * 
	 * @param list
	 * @param flag
	 * @return
	 */
	public static <T> T nvl(T v, T defaultValue) {
		if (EmptyTool.empty(v)) {
			return defaultValue;
		}
		return null;
	}

	/**
	 * 空处理
	 * 
	 * @param vs
	 * @return
	 */
	public static String nvl(String... vs) {
		if (vs == null || vs.length == 0) {
			return null;
		}
		if (EmptyTool.empty(vs[0])) {
			return vs.length > 1 ? vs[1] : "";
		}
		return vs[0];
	}

	/**
	 * 连接
	 * 
	 * @param list
	 * @param flag
	 * @return
	 */
	public static <T> String join(Collection<T> list) {
		return join(list, ",");
	}

	/**
	 * 连接
	 * 
	 * @param list
	 * @param flag
	 * @return
	 */
	public static <T> String joinSQLString(Collection<T> list) {
		if (list == null) {
			return "";
		}
		String flag = ",";
		StringBuilder s = new StringBuilder();
		for (T l : list) {
			if (s.length() > 0) {
				s.append(flag);
			}
			s.append("'").append(l).append("'");
		}
		return s.toString();
	}

	/**
	 * 连接
	 * 
	 * @param list
	 * @param flag
	 * @return
	 */
	public static <T> String join(Collection<T> list, String flag) {
		if (list == null) {
			return "";
		}
		if (flag == null) {
			flag = ",";
		}
		StringBuilder s = new StringBuilder();
		for (T l : list) {
			if (s.length() > 0) {
				s.append(flag);
			}
			s.append(l);
		}
		return s.toString();
	}

	/**
	 * 删除多余字符，默认逗号
	 * 
	 * @param inStr
	 * @return
	 */
	public static String cut(String inStr) {
		return cut(inStr, ",");
	}

	/**
	 * 删除多余字符，默认逗号
	 * 
	 * @param inStr
	 * @param flag
	 * @return
	 */
	public static String cut(String inStr, String flag) {
		if (inStr == null || inStr.length() == 0) {
			return "";
		}
		if (flag == null) {
			flag = ",";
		}
		String t = inStr.trim();
		while (t.endsWith(flag)) {
			t = t.substring(0, t.length() - 1);
			t = t.trim();
		}
		while (t.startsWith(flag)) {
			t = t.substring(1);
			t = t.trim();
		}
		return t;
	}

	/**
	 * 数据（字符串）拆分，按照指定字符
	 * 
	 * @param value 数据
	 * @return
	 */
	public static String[] split(String value) {
		return split(value, ",");
	}

	/**
	 * 数据（字符串）拆分，按照指定字符
	 * 
	 * @param value     数据
	 * @param splitChar 分隔符
	 * @return
	 */
	public static String[] split(String value, String splitChar) {
		if (EmptyTool.isEmpty(value)) {
			return new String[0];
		}
		String newValue = cut(value, splitChar);
		if (EmptyTool.isEmpty(newValue)) {
			return new String[0];
		}
		String[] values = newValue.split(splitChar);
		List<String> vList = new ArrayList<String>();
		for (String v : values) {
			if (EmptyTool.isEmpty(v)) {
				continue;
			}
			vList.add(v);
		}
		return vList.toArray(new String[0]);
	}

	/**
	 * 数据（列表）拆分，按照指定大小
	 * 
	 * @param datas 数据
	 * @param size  等分每组大小
	 * @return 拆分后集合
	 */
	public static <T> List<List<T>> split(List<T> datas, int size) {
		return split(datas, size, size);
	}

	/**
	 * 数据（列表）拆分，按照指定大小
	 * 
	 * @param datas     数据
	 * @param firstSize 第一组大小
	 * @param size      以后各组大小
	 * @return 拆分后集合
	 */
	public static <T> List<List<T>> split(List<T> datas, int firstSize, int size) {
		List<List<T>> resList = new ArrayList<List<T>>();
		if (EmptyTool.isEmpty(datas)) {
			return resList;
		}
		boolean begin = true;
		List<T> rowList = new ArrayList<T>();
		for (int i = 0; i < datas.size(); i++) {
			if (begin) {
				if (rowList.size() == firstSize) {
					resList.add(rowList);
					rowList = new ArrayList<T>();
					begin = false;
				}
			} else {
				if (rowList.size() == size) {
					resList.add(rowList);
					rowList = new ArrayList<T>();
				}
			}
			rowList.add(datas.get(i));
		}
		if (rowList.size() > 0) {
			resList.add(rowList);
		}
		return resList;
	}

	/**
	 * 拆分固定个组
	 * 
	 * @param datas 数据
	 * @param size  组个数
	 * @return 拆分后集合
	 */
	public static <T> List<List<T>> group(List<T> datas, int size) {
		List<List<T>> resList = new ArrayList<List<T>>();
		if (EmptyTool.isEmpty(datas)) {
			return resList;
		}
		if (size <= 1) {
			resList.add(datas);
			return resList;
		}
		List<T> tempList;
		for (int i = 0; i < size; i++) {
			tempList = new ArrayList<T>();
			resList.set(i, tempList);
		}
		int seqGroupIndex = 0;
		for (int index = 0; index < datas.size(); index++) {
			resList.get(seqGroupIndex++).add(datas.get(index));
			if (seqGroupIndex >= size) {
				seqGroupIndex = 0;
			}
		}
		return resList;
	}
}
