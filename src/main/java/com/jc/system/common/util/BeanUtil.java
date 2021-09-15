package com.jc.system.common.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class BeanUtil {

	private BeanUtil() {
		throw new IllegalStateException("BeanUtil class");
	}

	public static <T> T map2Object(Map<String, Object> map, Class<T> t){
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
		objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
		return objectMapper.convertValue(map, t);
	}
}
