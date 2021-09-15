package com.jc.system.dic.domain;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
public interface BaseDic extends Cloneable {
	/**
	 * @description 获得字典码
	 */
	String getCode();

	/**
	 * @description 获得对应显示值
	 */
	String getValue();

	/**
	 * @description 获得父Id
	 */
	String getParentId();

	/**
	 * @description 是否是根节点
	 */
	boolean isRoot();

	void addChildren(BaseDic dic);
}
