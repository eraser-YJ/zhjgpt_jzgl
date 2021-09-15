package com.jc.system.dic.cache;

import java.util.HashMap;
import java.util.Map;

import com.jc.system.dic.domain.BaseDic;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.domain.DicType;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class CacheDate {
	Map<String, DicType> dicTypeMap = null;
	/** 数据缓存 格式：// <typyCode,<code,dic>>*/
	Map<String, Map<String, Dic>> dicMap = null;
	/**自定义字典<flag,parent,<code,dic>>*/
	Map<String, Map<String, Map<String, BaseDic>>> otherDicMap = null;
	/**自定义字典<flag,parent,<code,dic>>*/
	Map<String, Map<String, BaseDic>> otherDicTypeMap = null;

	public CacheDate() {
		dicTypeMap = new HashMap<String, DicType>();
		dicMap = new HashMap<String, Map<String, Dic>>();
		otherDicMap = new HashMap<String, Map<String, Map<String, BaseDic>>>();
		otherDicTypeMap = new HashMap<String, Map<String, BaseDic>>();
	}
}
