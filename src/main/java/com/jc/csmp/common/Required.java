package com.jc.csmp.common;

/**
 * 流程必填属性
 */
public class Required{
	
    public String id;//唯一标识
    private String name;//流程设计页面显示的名称
    private String value;//自定义属性的值，默认可以为空，然后在流程设计页面上维护
    private String type;//自定义属性支持的控件类型，具体每个值对应的类型含义详见 fieldCustomProperty.getType 方法中的说明
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
