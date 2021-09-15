package com.jc.system.dic.cache.refresh;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.cache.CacheDate;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.domain.DicType;
import com.jc.system.dic.service.IDicService;
import com.jc.system.dic.service.IDicTypeService;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class DicTypeRefresh extends RefreshBase {

	IDicService dicService = SpringContextHolder.getBean(IDicService.class);
	IDicTypeService typeService = SpringContextHolder.getBean(IDicTypeService.class);

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Dic dic, Dic oldDic, CacheDate cacheData) {
		
		String dicMapStr = CacheClient.getCache("dicMap");
		Map<String, Map<String, Dic>> dicMap = null;
		if(dicMapStr == null){
			dicMap = new HashMap<String, Map<String, Dic>>();
		}else{
			dicMap = (Map<String, Map<String, Dic>>) JsonUtil.json2Java(dicMapStr, Map.class);
		}
		
		
		String typeMapStr = CacheClient.getCache("dicTypeMap");
		Map<String, DicType> typeMap = null;
		if(typeMapStr == null){
			typeMap = new HashMap<String, DicType>();
		}else{
			typeMap = (Map<String, DicType>) JsonUtil.json2Java(typeMapStr, Map.class);
		}
		if (oldDic != null && oldDic.getTypeFlag().equals(Dic.TYPE_FLAG_TRUE)) {
			dicMap.remove(dic.getParentType());
			typeMap.remove(dic.getParentType());
		}
		
		
		DicType dicType = new DicType(dic);
		dicType = typeService.get(dicType);
		if (dicType != null) {
			Dic itemDic = new Dic();
			itemDic.setParentId(dicType.getCode());
			itemDic.setParentType(dicType.getCode()+"="+dicType.getParentId());
			List<Dic> list = dicService.query(itemDic);
			Map<String, Dic> itemMap = new LinkedHashMap<String, Dic>();
			for (Dic item : list) {
				itemMap.put(item.getCode(), item);
			}
			dicMap.put(dicType.getCode()+"="+dicType.getParentId(), itemMap);
			typeMap.put(dicType.getCode()+"="+dicType.getParentId(), dicType);
		}
		CacheClient.putCache("dicMap",JsonUtil.java2Json(dicMap));
		CacheClient.putCache("dicTypeMap",JsonUtil.java2Json(typeMap));
	}
}
