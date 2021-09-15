package com.jc.system.dic.cache;

import java.util.List;

import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.domain.DicType;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public interface IDicCacheService {

	/**
	 * @description 初始化函数
	 */
	void init();

	/**
	 * @description 根据字典类型获得标志可以使用的字典
	 * @param typeCode
	 *            字典类型码
	 */
	List<Dic> getDicsByTypeCode(String typeCode,String parentCode);

	/**
	 * @description 根据字典类型获得（包括标志不可用）字典
	 * @param typeCode 字典类型码
	 * @param parentCode 父类型码
	 */
	List<Dic> getDicsByTypeCodeAll(String typeCode,String parentCode);

	/**
	 * @description 获得字典
	 * @param typeCode 字典类型code
	 * @param dicCode 字典code
	 * @param parentCode 父类型字典
	 */
	Dic getDic(String typeCode,String parentCode, String dicCode);

	/**
	 * @description 获得所有类型
	 * @return 树形结构（子节点放入到children中）
	 */
	List<DicType> getTypes();

	/**
	 * @description 获得字典类型
	 */
	DicType getType(String code,String parentcode);

	/**
	 * @description 刷新缓存（项目和类型）
	 */
	void refreshDicItem(Dic dic, Dic oldDic);
}
