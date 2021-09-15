package com.jc.system.dic.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.jc.foundation.domain.BaseBean;

/****
 * @author Administrator
 * @date 2020-06-30
 */
public class DicType extends BaseBean implements Cloneable {
	public static final Integer TYPE_FLAG_TRUE = 1;
	public static final Integer TYPE_FLAG_FALSE = 0;
	public static final Integer USE_FLAG_TRUE = 1;
	public static final Integer USER_FLAG_FALSE = 0;

	private static final long serialVersionUID = 1L;
	/** 字典码 */
	private String code;
	/** 字典值 */
	private String value;
	/** 父项目id */
	private String parentId;
	/** code与父类型id的组合，方便查询*/
	private String parentType;
	/** 字典类型默认值 */
	private Integer defaultValue;
	/** 字典标识 */
	private String dicFlag;
	/** 子类型 */
	private List<DicType> children = null;
	/** 预留字段1 */
	private String str1;
	/** 预留字段2 */
	private String str2;
	/** 预留字段3 */
	private String str3;
	/** 预留字段4 */
	private String str4;
	/** 预留字段5 */
	private String str5;
	
	public DicType() {
	}

	public DicType(Dic dic) {
		setId(dic.getId());
		this.code = dic.getCode();
		this.value = dic.getValue();
		this.parentId = dic.getParentId();
		this.defaultValue = dic.getDefaultValue();
	}

	public List<DicType> getChildren() {
		return children;
	}

	public void addChildren(DicType type) {
		if (children == null) {
			children = new ArrayList<DicType>();
		}
		children.add(type);
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public void setParentType(String parentType) {
		this.parentType = parentType;
	}
	
	public String getParentType() {
		return parentType;
	}

	public Integer getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(Integer defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
    public DicType clone() {
		throw new java.lang.UnsupportedOperationException();
	}

	public void setDicFlag(String dicFlag) {
		this.dicFlag = dicFlag;
	}

	public String getDicFlag() {
		return dicFlag;
	}

	public String getStr1() {
		return str1;
	}

	public void setStr1(String str1) {
		this.str1 = str1;
	}

	public String getStr2() {
		return str2;
	}

	public void setStr2(String str2) {
		this.str2 = str2;
	}

	public String getStr3() {
		return str3;
	}

	public void setStr3(String str3) {
		this.str3 = str3;
	}

	public String getStr4() {
		return str4;
	}

	public void setStr4(String str4) {
		this.str4 = str4;
	}

	public String getStr5() {
		return str5;
	}

	public void setStr5(String str5) {
		this.str5 = str5;
	}
	     
     /***
      * @description: 根据Map转换为Dic对象
      * @author: sunsr
      * @param map
      * @return
      * @created: 2015年11月23日 上午9:51:04 
      */
     public static DicType convertByMap(Map<String,Object> map){
         DicType result = new DicType();
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
         if(map.get("defaultValue")!=null){
             result.setDefaultValue((Integer) map.get("defaultValue"));
         }
         if(map.get("parentType")!=null){
             result.setParentType(map.get("parentType").toString());
         }
         if(map.get("dicFlag")!=null){
             result.setDicFlag(map.get("dicFlag").toString());
         }
         return result;
     }

}