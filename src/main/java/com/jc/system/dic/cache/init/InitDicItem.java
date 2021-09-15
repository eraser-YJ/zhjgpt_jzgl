package com.jc.system.dic.cache.init;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.util.JsonUtil;
import com.jc.system.dic.cache.CacheDate;
import com.jc.system.dic.domain.Dic;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class InitDicItem extends InitBase {
	@Override
	public void init(List<Dic> list, CacheDate cacheData) {
		String dicMapStr = CacheClient.getCache("dicMap");
		Map<String, Map<String, Dic>> dicMap = null;
		if(dicMapStr == null){
			dicMap = new LinkedHashMap<String, Map<String, Dic>>();
		}else{
			dicMap = (Map<String, Map<String, Dic>>) JsonUtil.json2Java(dicMapStr, Map.class);
		}
		for (Dic dic : list) {
			if (Dic.DIC_FLAG_TRUE.equals(dic.getDicFlag())) {
				LinkedHashMap<String, Dic> map = (LinkedHashMap<String, Dic>) dicMap.get(dic.getParentType());
				if (map == null) {
					logger.error("没有code为" + dic.getParentType() + "的字典类型");
					continue;
				}
				map.put(dic.getCode(), dic);
			}
		}
		CacheClient.putCache("dicMap",JsonUtil.java2Json(dicMap));
	}

}
