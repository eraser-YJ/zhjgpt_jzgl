package com.jc.common.kit;

import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

/**
 * @description 分布式锁
 * @author Administrator
 */
public class RedisDistributedLock {
	// redis对象
	private RedisTemplate<Object, Object> redisTemplate;
	// 锁名称
	private String lockKey = null;
	// 谁加锁成功
	private String uuid;

	/**
	 * @Document 分布式加锁初始化
	 * @description
	 * @param redis
	 * @param lockKey
	 */
	public RedisDistributedLock(RedisTemplate<Object, Object> redis, String lockKey) {
		this.redisTemplate = redis;
		this.lockKey = "RedisDistributedLock:" + lockKey;
		this.uuid = UUID.randomUUID().toString();
	}

	/**
	 * @document 最终加强分布式锁
	 *
	 * @param time 单位 毫秒
	 * @return 是否获取到
	 */
	@SuppressWarnings("unchecked")
	public boolean lock(int time) {
		return (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				RedisSerializer<Object> valueSerializer = (RedisSerializer<Object>) redisTemplate.getValueSerializer();
				RedisSerializer<String> keySerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
				Object obj = connection.execute("set", keySerializer.serialize(lockKey), valueSerializer.serialize(uuid), SafeEncoder.encode("NX"), SafeEncoder.encode("PX"), Protocol.toByteArray(time));
				return obj != null;
			}
		});
	}

	/**
	 * @document 释放自己的锁
	 *
	 * @param key
	 */
	public void releaseLock() {
		Object uuidObj = redisTemplate.opsForValue().get(lockKey);
		if (uuidObj != null && uuid.equals(uuidObj.toString())) {
			redisTemplate.delete(lockKey);
		}
	}

}
