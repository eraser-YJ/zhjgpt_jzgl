package com.jc.dlh.cache;

import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;

public class DlhCacheConst {

	public static final String REDIS_DIC_KEY = "dlh_cache:dic";

	public static final String REDIS_USER_KEY = "dlh_cache:user";

	public static final String REDIS_DB_KEY = "dlh_cache:db";

	public static void deleteByPrex(RedisTemplate<Object, Object> redisTemplate, String prex) {
		Set<Object> keys = redisTemplate.keys(prex);
		if (keys != null && keys.size() > 0) {
			redisTemplate.delete(keys);
		}
	}
}
