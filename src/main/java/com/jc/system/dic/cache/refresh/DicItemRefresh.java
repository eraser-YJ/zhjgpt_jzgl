package com.jc.system.dic.cache.refresh;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.dic.cache.CacheDate;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.service.IDicService;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class DicItemRefresh extends RefreshBase {

	IDicService dicService = SpringContextHolder.getBean(IDicService.class);

	@SuppressWarnings("unchecked")
	@Override
	public void refresh(Dic dic, Dic oldDic, CacheDate cacheData) {
		String dicMapStr = CacheClient.getCache("dicMap");
		Map<String, Map<String, Object>> dicMap ;
		if(dicMapStr == null){
			dicMap = new HashMap<>();
		}else{
			dicMap = (Map<String, Map<String, Object>>) JsonUtil.json2Java(dicMapStr, Map.class);
		}
		
		// 去掉缓存里面的该项目
		Iterator<Map<String, Object>> itor = dicMap.values().iterator();
		out: while (itor.hasNext()) {
			Map<String, Object> map = itor.next();
			Iterator<Object> itemIt = map.values().iterator();
			while (itemIt.hasNext()) {
				Dic item = Dic.convertByMap((Map<String, Object>) itemIt.next());
				if (item.getCode().equals(dic.getCode())&&item.getParentType().equals(dic.getParentType())) {
					map.remove(item.getCode());
					break out;
				}
			}
		}

		Dic newDic = dicService.get(dic);
		if (newDic != null && newDic.getDicFlag().equals(Dic.DIC_FLAG_TRUE)) {
			// 不更新排序
			if (oldDic != null
					&& (newDic.getOrderFlag() == null || newDic.getOrderFlag()
							.equals(oldDic.getOrderFlag()))) {
				if (newDic != null) {
					String typeCode = newDic.getCode()+"="+newDic.getParentId();
					dicMap.get(typeCode).put(
							newDic.getCode(), newDic);
				}
			} else {
				// 更新排序
				LinkedHashMap<String, Object> newOrderMap = new LinkedHashMap<String, Object>();
				Dic dicTemp = new Dic();
				dicTemp.setParentId(newDic.getParentId());
				dicTemp.setParentType(newDic.getParentType());
				List<Dic> list = dicService.query(dicTemp);
				for (Dic item : list) {
					newOrderMap.put(item.getCode(), item);
				}
				dicMap.put(dic.getParentType(), newOrderMap);
			}
		}
		CacheClient.putCache("dicMap",JsonUtil.java2Json(dicMap));
	}

}
