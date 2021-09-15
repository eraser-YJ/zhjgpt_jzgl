package com.jc.busi.standard.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RSAUtil {

	/**
	 * @Document 公钥加密
	 * @param base64Key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPublic(String base64Key, String value) throws Exception {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64Key));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] result = cipher.doFinal(value.getBytes());
		String resStr = Base64.encodeBase64String(result);
		return resStr;
	}

	/**
	 * @Document 公钥加密
	 * @param base64Key
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPrivate(String base64Key, String value) throws Exception {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64Key));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] result = cipher.doFinal(Base64.decodeBase64(value));
		String resStr = new String(result);
		return resStr;
	}

	public static String encryptByPrivate(String base64Key, String value) throws Exception {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(base64Key));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] result = cipher.doFinal(value.getBytes());
		String resStr = Base64.encodeBase64String(result);
		return resStr;
	}

	public static String decryptByPublic(String base64Key, String value) throws Exception {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decodeBase64(base64Key));
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] result = cipher.doFinal(Base64.decodeBase64(value));
		String resStr = new String(result);
		return resStr;
	}

	public static String[] generatorRSA() throws Exception {
		// 1.初始化密钥
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(512);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey rsaPrivate = (RSAPrivateKey) keyPair.getPrivate();
		// 打印出来的公钥是比私钥短较多的,公钥公开，较短较容易保存
		String publicCode = Base64.encodeBase64String(rsaPublicKey.getEncoded());
		String privateCode = Base64.encodeBase64String(rsaPrivate.getEncoded());
		String[] code = new String[2];
		code[0] = publicCode;
		code[1] = privateCode;
		return code;
	}

}