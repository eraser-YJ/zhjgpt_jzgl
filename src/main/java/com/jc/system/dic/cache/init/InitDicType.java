package com.jc.system.dic.cache.init;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.dic.cache.CacheDate;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.domain.DicType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class InitDicType extends InitBase {
	@Override
	public void init(List<Dic> list, CacheDate cacheData) {
		String dicMapStr = CacheClient.getCache("dicMap");
		Map<String, Map<String, Dic>> dicMap;
		if(dicMapStr == null){
			dicMap = new LinkedHashMap<>();
		}else{
			dicMap = (Map<String, Map<String, Dic>>) JsonUtil.json2Java(dicMapStr, Map.class);
		}
		String typeMapStr = CacheClient.getCache("dicTypeMap");
		Map<String, DicType> typeMap;
		if(typeMapStr == null){
			typeMap = new LinkedHashMap<>();
		}else{
			typeMap = (Map<String, DicType>) JsonUtil.json2Java(typeMapStr, Map.class);
		}
		for (Dic dic : list) {
			if (dicMap.get(dic.getCode()) != null) {
				logger.error("已经存在相同code的类型,code值为:" + dic.getCode());
			}
			if (Dic.TYPE_FLAG_TRUE.equals(dic.getTypeFlag())) {
				String typeCode = dic.getCode()+"="+dic.getParentId();
				dicMap.put(typeCode,
						new LinkedHashMap<String, Dic>());
				typeMap.put(typeCode, new DicType(dic));
			}
		}
		CacheClient.putCache("dicMap", JsonUtil.java2Json(dicMap));
		CacheClient.putCache("dicTypeMap",JsonUtil.java2Json(typeMap));
		InitBase initItem = new InitDicItem();
		initItem.init(list, cacheData);
	}

}
