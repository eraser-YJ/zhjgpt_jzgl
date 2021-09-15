package com.jc.busi.standard.util;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.apache.commons.codec.binary.Base64;

public class ThreeDESUtil {

	public static String generatorThreeDES() throws Exception {
		// 1.生成KEY
		KeyGenerator keyGenerator = KeyGenerator.getInstance("DESEde");
		keyGenerator.init(new SecureRandom());
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] bytesKey = secretKey.getEncoded();
		return Base64.encodeBase64String(bytesKey);
	}

	public static String encrypt(String base64Key, String value) throws Exception {
		DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(Base64.decodeBase64(base64Key));
		SecretKeyFactory factory = SecretKeyFactory.getInstance("DESEde");
		SecretKey convertKey = factory.generateSecret(deSedeKeySpec);
		// 3.加密
		Cipher cipher = Cipher.getInstance("DESEde/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, convertKey);
		byte[] result = cipher.doFinal(value.getBytes());
		String resStr = Base64.encodeBase64String(result);
		return resStr;
	}

	public static String decrypt(String base64Key, String value) throws Exception {
		DESedeKeySpec deSedeKeySpec = new DESedeKeySpec(Base64.decodeBase64(base64Key));
		SecretKeyFactory factory = SecretKeyFactory.getInstance("DESEde");
		SecretKey convertKey = factory.generateSecret(deSedeKeySpec);
		Cipher cipher = Cipher.getInstance("DESEde/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, convertKey);
		byte[] result = cipher.doFinal(Base64.decodeBase64(value));
		String resStr = new String(result);
		return resStr;

	}

}
