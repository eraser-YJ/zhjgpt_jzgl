package com.jc.common.kit;

import java.util.Collection;
import java.util.Map;

/**
 * 空判断
 * 
 * @author lc  liubq
 * @since 20170711
 */
public class EmptyTool {
	/**
	 * 判断是否为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(Collection<?> c) {
		if (c == null || c.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(Map<?, ?> c) {
		if (c == null || c.size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param c
	 * @return
	 */
	public static <T> boolean isEmpty(T[] datas) {
		if (datas == null || datas.length == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否为空
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isEmpty(CharSequence c) {
		if (c == null || c.length() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 判断数值是否为空
	 * 
	 * @param o 数值
	 * @return true 空，false 非空
	 */
	public static boolean empty(Object o) {
		if (o == null) {
			return true;
		} else if (o instanceof CharSequence) {
			String v = (String) o;
			return isEmpty(v);
		} else if (o instanceof Collection<?>) {
			Collection<?> v = (Collection<?>) o;
			return isEmpty(v);
		} else if (o instanceof Map<?, ?>) {
			Map<?, ?> v = (Map<?, ?>) o;
			return isEmpty(v);
		} else if (o.getClass().isArray()) {
			Object[] v = (Object[]) o;
			return isEmpty(v);
		}
		return false;
	}
}
