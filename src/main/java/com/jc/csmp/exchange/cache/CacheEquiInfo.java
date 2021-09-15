package com.jc.csmp.exchange.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheEquiInfo {

    private static Map<String, Map<String, Object>> cache = new HashMap<>();

    public static void put(String key, Map<String, Object> value) {
        cache.put(key, value);
    }

    public static Map<String, Object> get(String key) {
        return cache.get(key);
    }

}
