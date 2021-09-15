package com.jc.system.dic;

import java.util.List;

import com.jc.foundation.exception.CustomException;
import com.jc.system.dic.domain.Dic;
import com.jc.system.dic.domain.DicType;

/**
 * 字典接口
 * @author Administrator
 * @date 2020-06-29
 */
public interface IDicManager {

	/**
	 * 初始化函数
	 */
	void init();

	/**
	 * 根据字典类型获得标志可以使用的字典(根类型)
	 * @param typeCode 字典类型码
	 */
	List<Dic> getDicsByTypeCode(String typeCode);
	
	/**
	 * 根据字典类型获得标志可以使用的字典
	 * @param typeCode 字典类型码
	 * @param typeCode 父类型码
	 */
	List<Dic> getDicsByTypeCode(String typeCode,String parentCode);

	/**
	 * 根据字典类型获得字典
	 * @param typeCode 字典类型吗
	 * @param typeCode 父类型码
	 * @param useFlag true:返回标志启用的字典 false:返回所有（包括标志不可用）的字典
	 */
	List<Dic> getAllDicsByTypeCode(String typeCode,String parentCode, Boolean useFlag);

	/**
	 * 获得字典
	 * @param typeCode
	 * @param parendCode
	 * @param dicCode
	 * @return
	 */
	Dic getDic(String typeCode,String parendCode, String dicCode);

	/**
	 * 获得字典类型类型
	 * @param code
	 * @param parentCode
	 * @return
	 */
	DicType getDicType(String code,String parentCode);
	
	/**
	 * 获得所有类型
	 * @return 树形结构（子节点放入到children中）
	 */
	List<DicType> getDicTypes();

	/**
	 * 新增字典
	 * @param dic 新字典bean
	 */
	Dic addNewDic(Dic dic) throws CustomException;
	
	/**
	 * 批量新增字典
	 * @param dicList 新字典bean集合
	 */
	Integer addNewDicList(List<Dic> dicList) throws CustomException;

	/**
	 * 新增字典类型
	 * @param dicType 字典类型
	 */
	DicType addNewDicType(DicType dicType) throws CustomException;

	/**
	 * 删除字典类型
	 * @param dicType 字典类型
	 */
	DicType removeDicType(DicType dicType) throws CustomException;

	/**
	 * 删除字典
	 * @param dic 字典
	 */
	Dic removeDic(Dic dic) throws CustomException;

	/**
	 * 更新字典
	 * @param dic
	 * @return
	 * @throws CustomException
	 */
	Dic updateDic(Dic dic) throws CustomException;

	/**
	 * 更新字典类型
	 * @param dicType 字典类型
	 */
	DicType updateType(DicType dicType) throws CustomException;

	/**
	 * 将字典转换为字典类型，以增加字典
	 * @param dic
	 */
	Dic changeDicToType(Dic dic) throws CustomException;
	
	/**
	 * 获得字典
	 * @param dicId 字典id
	 */
	Dic getDic(String dicId);
}
