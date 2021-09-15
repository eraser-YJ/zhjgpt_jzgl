package com.jc.system.gateway.utils;

import com.jc.foundation.util.RsaUtil;
import com.jc.system.gateway.exception.RRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.Enumeration;

/**
 * 生成token
 * @author Administrator
 * @date 2020-07-01
 */
public class TokenGenerator {
    private static final Logger logger = LoggerFactory.getLogger(TokenGenerator.class);

    private final char[] hexCode = "0123456789abcdef".toCharArray();
    private final String SPLITTER = ",";
    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;
    private static TokenGenerator tokenGenerator;


    private TokenGenerator() {
        try {
            String keystoreFile = "jcap.jks";
            String keyPassword = "jiacheng1995";
            String storePassword = "jiacheng1995";
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(getClass().getClassLoader().getResourceAsStream(keystoreFile), storePassword.toCharArray());
            Enumeration<String> aliases = keyStore.aliases();
            String alias = null;
            while (aliases.hasMoreElements()) {
                alias = aliases.nextElement();
            }
            privateKey = (RSAPrivateKey) keyStore.getKey(alias, keyPassword.toCharArray());
            Certificate certificate = keyStore.getCertificate(alias);
            publicKey = (RSAPublicKey) certificate.getPublicKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TokenGenerator getInstance() {
        if (tokenGenerator == null) {
            tokenGenerator = new TokenGenerator();
        }
        return tokenGenerator;
    }

    public String toHexString(byte[] data) {
        if (data == null) {
            return null;
        }
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }

    public String generateToken(@NotNull String userId, @NotNull String clientType, @NotNull Date expired) {
        try {
            return RsaUtil.encryptByPublicKey(userId + SPLITTER + clientType + SPLITTER + expired.getTime(), publicKey);
        } catch (Exception e) {
            logger.error("generateToken failed: "+e.getMessage());
            throw new RRException("生成Token失败", e);
        }
    }

    public String[] resolveToken(@NotNull String token) {
        String[] result = null;
        try {
            String text = RsaUtil.decryptByPrivateKey(token, privateKey);
            result = text.split(SPLITTER);
        } catch (Exception e) {
            logger.error("resolveToken failed: "+e.getMessage());
        }
        return result;
    }
}
