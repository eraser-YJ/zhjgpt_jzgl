package com.jc.system.security.utils;

import com.jc.foundation.util.RsaUtil;
import com.jc.system.util.SettingUtils;
import org.apache.log4j.Logger;

import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class RSASetting {

    private transient static final Logger log = Logger.getLogger(RSASetting.class);

    public static void init() {
        log.debug("初始化系统参数开始");
        // 获取公钥和私钥sa
        try {
            initRSAKey();
        } catch (NoSuchAlgorithmException e) {
            log.error(e, e);
        }
        log.debug("初始化系统参数结束");
    }

    private static void initRSAKey() throws NoSuchAlgorithmException {
        HashMap<String, Object> keys = RsaUtil.getKeys();
        RSAPublicKey publicKey = (RSAPublicKey) keys.get("public");
        RSAPrivateKey privateKey = (RSAPrivateKey) keys.get("private");
        RsaUtil.setRasMap("privateKey-ras", privateKey);
        SettingUtils.setSetting("modulus", publicKey.getModulus().toString(16));
        SettingUtils.setSetting("exponent", publicKey.getPublicExponent().toString(16));
    }

    public static Map<String, Object> getRSAModulusAndExponent() throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<>();
        if (RsaUtil.getRasMap("privateKey-ras") == null) {
            // 获取公钥和私钥sa
            initRSAKey();
        }

        map.put("modulus", SettingUtils.getSetting("modulus"));
        map.put("exponent", SettingUtils.getSetting("exponent"));
        return map;
    }

    public static String decryptByPrivateKey(String data) throws Exception {
        return RsaUtil.decryptByPrivateKey(data, RsaUtil.getRasMap("privateKey-ras"));
    }
}
