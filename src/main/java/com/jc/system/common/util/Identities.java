package com.jc.system.common.util;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class Identities {

	private Identities() {
		throw new IllegalStateException("Identities class");
	}

	private static SecureRandom random = new SecureRandom();

	/**
	 * @description 封装JDK自带的UUID, 通过Random数字生成
	 * @return 生成的UUID,中间有-分割
	 */
	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @description 封装JDK自带的UUID, 通过Random数字生成
	 * @return 生成的UUID,中间无-分割
	 */
	public static String uuid2() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	/**
	 * @description 使用SecureRandom随机生成Long.
	 * @return 生成的随机长整形
	 */
	public static long randomLong() {
		long value = random.nextLong();
		if(value ==Long.MIN_VALUE){
			return randomLong();
		}
		else{
			return Math.abs(value);
		}
	}
}
