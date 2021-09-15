package com.jc.digitalchina.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 类说明 AES算法加密解密工具类
 *
 * @author lenovo
 * @version V1.0 创建时间：2015年4月27日 下午2:02:28
 * Copyright 2015 by DigitalChina
 */
public class AesUtils {
    private static String Key = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAK12rAV9T4cpzFrQ"
            + "7vlAc2n80wmBxl5WhwSNMcSLfqhbBCBwW7y6rOV+3scxGXNXLOToNP9Wn8mmnJml"
            + "ZzaQDmApUx3+Lfo/+V3scRTXOER/Jc0JnaopX5veoyxq7s2DD2TJSh3RLwJUWVwo"
            + "fLVO2CStwj+XUZ41oj31234567lpAgMBAAECgYAg5lcuG1xQGH9n4iTRJYY+XfNw"
            + "5CpJjkPBez4rNwHo0Ipx1J2xp/5O8Mc0JH3dQHWFXP1gSH+58V3WTmbLFHuFNXQY"
            + "kRVZC2t1kq0zwBd8fzALJUZr/oSa6GP9ef+FkdioPu4P8p/XK5x0LVsj+Hu+LrBY"
            + "FTpH+HGcsxpG6qpTmQJBAOE/2GDO5Vou/hnnGlYFgg7H+zoU0+1Eh8c92FLfdH6Z"
            + "8ib6SvSj2nYPNqSZl6ayVGiu89t9dc5FIh6I/b99zbcCQQDFJPvG2igeK5BXnC0P"
            + "Z3ixmfN6PNBdyZLwKe8tVcBguKC8IcuuIEUqipK3LwC/rGkzrpwljsSgAmyWOLcz"
            + "SJHfAkEAnPFeMz2VAjwh4ixq5iraTrA6NGLUssoADOvjcQKu6ZMb7NqSJViRW5cI"
            + "DLHTv1sbgwD7eiA1V+5Fcp8ipiziiwJBAJMhkeKLiCtUGeq4vQxlDbeGjSbqYhvM"
            + "x6QDt+Qevm+w+wzNMCSpItWoC+044Bcsxmry4shI78f3ZzEJ7PgSLgMCQQCDkAG6"
            + "a5JVjDPPnnD3vzRZ2JeKxEDThxmF4DoyUizJCOC92TMei5AX6Qc2q5Kb0VLbpaBY" + "GvFj1q6pYOLZUHxm";
    /**
     * 加密强度
     */
    private static int ENCRYPT_STRENGTH = 128;

    private static boolean isEmpty(String msg) {
        if (msg == null || "".equals(msg)) {
            return true;
        }
        return false;
    }

    /**
     * 获取byte[]的md5值
     *
     * @param bytes byte[]
     * @return md5
     */
    public static byte[] md5(byte[] bytes) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {

        }
        md.update(bytes);
        return md.digest();
    }

    /**
     * 获取字符串md5值
     *
     * @param msg
     * @return md5
     */
    public static byte[] md5(String msg) {
        return isEmpty(msg) ? null : md5(msg.getBytes());
    }

    /**
     * AES加密为base 64 code
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的base 64 code
     * @throws Exception
     */
    public static String aesEncrypt(String content, String encryptKey) throws Exception {
        return new String(Base64.getEncoder().encode((aesEncryptToBytes(content, encryptKey))));
    }

    /**
     * ES加密为base 64 code
     *
     * @param content
     * @return
     * @author zuoyue 2015年7月21日
     */
    public static String aesEncrypt(String content) {
        String encryptStr = "";
        byte[] byt = null;
        try {
            byt = aesEncryptToBytes(content, Key);
            encryptStr = new String(Base64.getEncoder().encode(byt));
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    /**
     * 加上自己盐值进行加密
     *
     * @param content 加密内容
     * @param salt    加密盐值
     * @return
     * @author xutaog 2015年8月3日
     */
    public static String aecEncryptBySalt(String content, String salt) {
        String encryptStr = "";
        byte[] byt = null;
        try {
            String keyStr = Key + salt;
            byt = aesEncryptToBytes(content, keyStr);
            encryptStr = new String(Base64.getEncoder().encode(byt));
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptStr;
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 加密密钥
     * @return 加密后的byte[]
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static byte[] aesEncryptToBytes(String content, String encryptKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException,
            UnsupportedEncodingException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(encryptKey.getBytes());
        kgen.init(ENCRYPT_STRENGTH, secureRandom);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        return cipher.doFinal(content.getBytes("utf-8"));
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code 默认key
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr) throws Exception {
        return aesDecrypt(encryptStr, Key);
    }

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        KeyGenerator kgen = null;
        Cipher cipher = null;
        SecureRandom secureRandom = null;
        try {
            kgen = KeyGenerator.getInstance("AES");
            cipher = Cipher.getInstance("AES");
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(decryptKey.getBytes());
        } catch (NoSuchAlgorithmException e) {
            // No need to catch
        }
        kgen.init(ENCRYPT_STRENGTH, secureRandom);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kgen.generateKey().getEncoded(), "AES"));
        byte[] decryptBytes = cipher.doFinal(encryptBytes);
        return new String(decryptBytes);
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
        return isEmpty(encryptStr) ? null : aesDecryptByBytes(Base64.getDecoder().decode(encryptStr),
                decryptKey);
    }

    /**
     * 带盐值的解密算法
     *
     * @param encryptStr 带解密字符串
     * @param salt       加密盐
     * @return
     * @author xutaog 2015年8月3日
     */
    public static String aesDecryptBySalt(String encryptStr, String salt) {
        String decryptStr = "";
        try {
            String decryptKey = Key + salt;
            decryptStr = isEmpty(encryptStr) ? null : aesDecryptByBytes(
                    Base64.getDecoder().decode(encryptStr), decryptKey);
        } catch (InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return decryptStr;
    }

    public static void main(String[] args) throws Exception {
        /*String content = "888888";
        System.out.println("加密前：" + content);
        String result = AesUtils.aecEncryptBySalt(content, "cityadmin09e4a024-5e39-457d-b609-9cc887ded5f9");
        System.out.println("加密后：" + result);
        result = AesUtils.aesDecryptBySalt("eyJhbGciOiJIUzI1NiIsInppcCI6IkRFRiJ9.eJxVzMsOAiEMheF3YS1JoS3QeRsuRTEGTWYWJhPfXTYu3H75zznNmP1pNgOilMETKwpxbCWASK0pxaaNu5iLGfkwm2OR6DEALtj3tWzjOo78qLcx86rux1joSYPPlaw4dJa4qy2J2DKq6wDcuscVz9L_L_X9-gET4OcLs0QqVw.f5Iv1w2w2BnM9DqsyZqTBnTFs6aal9zQHFVF0cN4VeY", "cityadmin09e4a024-5e39-457d-b609-9cc887ded5f9");
        //result = AesUtils.aesDecryptBySalt(result, "cityadmin09e4a024-5e39-457d-b609-9cc887ded5f9");
        System.out.println("解密后：" + result);*/

        String content = "/ZkM3RgBTJfKx+Pj00lGtUI2abW7fb03MY1/kHGCTeNK3XIgslnoG299JM50ZOztK5RARZD5Wxt9myfCRqhKxdJ7OiAmeaX/zSe8sIFqtl6lLJePTXgRkhrrUbtzWYmNGPAuICOavagDH6AGWjxp+lw3931qmdzQ6IoxLLbobF1Z6bVbnPw27+Rw2YRJnop4hHnONQ6PUsVPk331dwiGrm6VROWlQZt2XjDFGGIueeNbr5gEMlEBMx+ggTStMTIKT3J2TFfOairDTVFby0auXzhbs7KP73fMDOOxr05F/KNG1jEV1fERdOTvHrPccrdPsycmaiisM2Ppc7LF09J4IwuXFYjZufyLA/21HDgDCtWa4TmycEOyjWzmbv31mW+o";
        System.out.println(AesUtils.aesDecryptBySalt(content, "qIY0HdNJytu4cxbLtNPZo/yc1mzY4v/UrrXYt6TKQpKpUrkuO2ztAn/dSEmSbYKa"));


    }
}
