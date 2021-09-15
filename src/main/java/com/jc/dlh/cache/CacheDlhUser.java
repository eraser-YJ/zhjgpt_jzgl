package com.jc.dlh.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.jc.dlh.domain.DlhUser;
import com.jc.dlh.service.IDlhCommonService;
import com.jc.dlh.service.IDlhUserService;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;

@Service
public class CacheDlhUser {
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
		getRedis().delete(DlhCacheConst.REDIS_USER_KEY);
	}

	public static DlhUser queryByUserName(String userName) {
		if (userName == null || userName.trim().length() <= 0) {
			return null;
		}
		Object jsonStr = getRedis().opsForHash().get(DlhCacheConst.REDIS_USER_KEY, userName);
		if (jsonStr == null || jsonStr.toString().length() <= 0) {
			IDlhUserService dlhUserService = SpringContextHolder.getBean(IDlhUserService.class);
			DlhUser data = dlhUserService.queryByUserName(userName);
			if (data != null) {
				getRedis().opsForHash().put(DlhCacheConst.REDIS_USER_KEY, userName, JsonUtil.java2Json(data));
			}
			return data;
		} else {
			DlhUser data = (DlhUser) JsonUtil.json2Java(jsonStr.toString(), DlhUser.class);
			return data;
		}
	}

}
