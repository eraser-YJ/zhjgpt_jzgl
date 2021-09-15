package com.jc.common.kit;

import com.jc.foundation.util.JsonUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 克隆工具
 * CloneUtil.cloneByJson(bean, 5000);
 * CloneUtil.cloneByStream(bean, 5000);
 * 测试结果
 * Json序列化耗时:3120ms
 * Java序列化耗时:500ms
 * 
 * @author lc  liubq
 * @since 2017年12月21日
 */
public class CloneUtil {

	/**
	 * 克隆对象，采用序列化方式进行克隆
	 * 
	 * @param t
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> cloneByStream(T t, int size) throws Exception {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(t);
		List<T> list = new ArrayList<T>();
		byte[] datas = byteOut.toByteArray();
		for (int i = 0; i < size; i++) {
			ByteArrayInputStream byteIn = new ByteArrayInputStream(datas);
			ObjectInputStream in = new ObjectInputStream(byteIn);
			list.add((T) in.readObject());
		}
		return list;
	}

	/**
	 * 克隆对象，采用序列化方式进行克隆
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public static <T> T cloneByStream(T t) throws Exception {
		return cloneByStream(t, 1).get(0);
	}

	/**
	 * 克隆对象，采用Json方式进行克隆
	 * 强烈建议的方法，性能提高很多，如果一个一个的clone性能会成为瓶颈
	 * 
	 * @param t
	 * @param size
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> cloneByJson(T t, int size) throws Exception {
		String json = JsonUtil.java2Json(t);
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < size; i++) {
			list.add((T) JsonUtil.json2Java(json, t.getClass()));
		}
		return list;
	}

	/**
	 * 克隆对象，采用Json方式进行克隆
	 * 
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public static <T> T cloneByJson(T t) throws Exception {
		return cloneByJson(t, 1).get(0);
	}

}
