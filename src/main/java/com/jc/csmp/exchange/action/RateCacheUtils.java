package com.jc.csmp.exchange.action;

import com.jc.foundation.util.SpringContextHolder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

public class RateCacheUtils {
    private static CacheManager cacheManager = (CacheManager) SpringContextHolder.getBean("cacheManagerSystem");
    private static final String SYS_CACHE = "reqRateCache";

    public RateCacheUtils() {
    }

    public static Object get(String key) {
        SimpleValueWrapper wrapper = (SimpleValueWrapper)get(SYS_CACHE, key);
        return wrapper != null ? wrapper.get() : null;
    }

    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    public static Object get(String cacheName, String key) {
        if (getCache(cacheName) != null) {
            try {
                return getCache(cacheName).get(key);
            } catch (Throwable var3) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void put(String cacheName, String key, Object value) {
        try {
            Cache tempCache = getCache(cacheName);
            tempCache.put(key, value);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

    }

    public static void remove(String cacheName, String key) {
        getCache(cacheName).evict(key);
    }

    private static Cache getCache(String cacheName) {
        try {
            return cacheManager.getCache(cacheName);
        } catch (Throwable var2) {
            return null;
        }
    }
}
