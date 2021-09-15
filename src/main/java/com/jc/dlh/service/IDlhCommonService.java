package com.jc.dlh.service;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;

import com.jc.common.kit.vo.DicVO;

/**
 * @title 统一数据资源中心
 * @description 业务接口类
 * @author lc
 * @version 2020-03-13
 */

public interface IDlhCommonService {
	public List<DicVO> getDic(String typeCode) throws Exception;

	public RedisTemplate<Object, Object> getRedis();
}