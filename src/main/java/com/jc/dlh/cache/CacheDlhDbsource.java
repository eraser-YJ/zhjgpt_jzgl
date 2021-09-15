package com.jc.dlh.cache;

import org.springframework.data.redis.core.RedisTemplate;

import com.jc.dlh.domain.DlhDbsource;
import com.jc.dlh.service.IDlhCommonService;
import com.jc.dlh.service.IDlhDbsourceService;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;

public class CacheDlhDbsource {
	// redis
	private static RedisTemplate<Object, Object> redisTemplateTemp;

	private static RedisTemplate<Object, Object> getRedis() {
		if (redisTemplateTemp == null) {
			IDlhCommonService common = SpringContextHolder.getBean(IDlhCommonService.class);
			redisTemplateTemp = common.getRedis();
		}
		return redisTemplateTemp;

	}

	public static void clear() {
		getRedis().delete(DlhCacheConst.REDIS_DB_KEY);
	}

	public static DlhDbsource queryByCode(String dbCode) {
		if (dbCode == null || dbCode.trim().length() <= 0) {
			return null;
		}
		Object jsonStr = getRedis().opsForHash().get(DlhCacheConst.REDIS_DB_KEY, dbCode);
		if (jsonStr == null || jsonStr.toString().length() <= 0) {
			IDlhDbsourceService dlhDbsourceService = SpringContextHolder.getBean(IDlhDbsourceService.class);
			DlhDbsource data = dlhDbsourceService.queryByCode(dbCode);
			if (data != null) {
				getRedis().opsForHash().put(DlhCacheConst.REDIS_DB_KEY, dbCode, JsonUtil.java2Json(data));
			}
			return data;
		} else {
			DlhDbsource data = (DlhDbsource) JsonUtil.json2Java(jsonStr.toString(), DlhDbsource.class);
			return data;
		}
	}

}
