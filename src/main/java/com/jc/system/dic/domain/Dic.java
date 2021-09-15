package com.jc.system.dic.domain;

import java.util.HashMap;
import java.util.Map;

import com.jc.foundation.domain.BaseBean;

/***
 * @author Administrator
 * @date 2020-06-30
 */
public class Dic extends BaseBean implements Cloneable {
	public static final Integer TYPE_FLAG_TRUE = 1;
	public static final Integer TYPE_FLAG_FALSE = 0;
	public static final Integer DIC_FLAG_TRUE = 1;
	public static final Integer DIC_FLAG_FALSE = 0;
	public static final Integer USE_FLAG_TRUE = 1;
	public static final Integer USER_FLAG_FALSE = 0;

	private static final long serialVersionUID = 1L;
	/** 字典码 */
	private String code;
	/** 字典值 */
	private String value;
	/** 父项目id */
	private String parentId;
	/** 启用标志 */
	private Integer useFlag;
	/** 字典类型标志 */
	private Integer typeFlag;
	/** 字典标志 */
	private Integer dicFlag;
	/** 字典类型默认值 */
	private Integer defaultValue;
	/** 字典排序标示 */
	private Integer orderFlag;
	/**字典类型*/
	private String dicType;
	/**父字典类型*/
	private String parentType;

	public Dic() {
	}

	public Dic(DicType dicType) {
		setId(dicType.getId());
		this.code = dicType.getCode();
		this.value = dicType.getValue();
		this.parentId = dicType.getParentId();
		this.parentType = dicType.getCode()+"="+dicType.getParentId();
		this.defaultValue = dicType.getDefaultValue();
		setTypeFlag(Dic.TYPE_FLAG_TRUE);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getName() {return value;}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getUseFlag() {
		return useFlag;
	}

	public void setUseFlag(Integer useFlag) {
		this.useFlag = useFlag;
	}

	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	public Integer getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Integer defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getDicFlag() {
		return dicFlag;
	}

	public void setDicFlag(Integer dicFlag) {
		this.dicFlag = dicFlag;
	}

	public void setOrderFlag(Integer orderFlag) {
		this.orderFlag = orderFlag;
	}

	public Integer getOrderFlag() {
		return orderFlag;
	}
	
	public String getDicType(){
		return dicType;
	}
	
	public void setDicType(String dicType){
		this.dicType = dicType;
	}
	
	public String getParentType(){
		return parentType;
	}
	
	public void setParentType(String parentType){
		this.parentType = parentType;
	}

	@Override
	public Dic clone() {
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public String toString() {
		return "字典:类型id:" + getParentId() + ",code:" + getCode();
	}

	public static Dic convertByMap(Map<String,Object> map){
		Dic result = new Dic();
		if(map == null){
			return null;
		}
		if(map.get("code")!=null){
			result.setCode(map.get("code").toString());
		}
		if(map.get("value")!=null){
			result.setValue(map.get("value").toString());
		}
		if(map.get("parentId")!=null){
			result.setParentId(map.get("parentId").toString());
		}
		if(map.get("useFlag")!=null){
			result.setUseFlag((Integer) map.get("useFlag"));
		}
		if(map.get("typeFlag")!=null){
			result.setTypeFlag((Integer) map.get("typeFlag"));
		}
		if(map.get("dicFlag")!=null){
			result.setDicFlag((Integer) map.get("dicFlag"));
		}
		if(map.get("defaultValue")!=null){
			result.setDefaultValue((Integer) map.get("defaultValue"));
		}
		if(map.get("orderFlag")!=null){
			result.setOrderFlag((Integer) map.get("orderFlag"));
		}
		if(map.get("dicType")!=null){
			result.setDicType(map.get("dicType").toString());
		}
		if(map.get("parentType")!=null){
			result.setParentType(map.get("parentType").toString());
		}
		return result;
	}
	
	public Map<String,Object> convertToMap(){
		Map<String,Object> item = new HashMap<String, Object>();
		item.put("code", code);
		item.put("value", value);
		item.put("parentId", parentId);
		item.put("useFlag", useFlag);
		item.put("typeFlag", typeFlag);
		item.put("dicFlag", dicFlag);
		item.put("defaultValue", defaultValue);
		item.put("orderFlag", orderFlag);
		item.put("dicType", dicType);
		item.put("parentType", parentType);
		return item;
	}

}