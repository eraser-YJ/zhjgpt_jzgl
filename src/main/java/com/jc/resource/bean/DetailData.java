package com.jc.resource.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * 资源数据
 * @Author 常鹏
 * @Date 2020/7/27 16:32
 * @Version 1.0
 */
public class DetailData implements Serializable {
    private static final long serialVersionUID = -1;
    public DetailData(){}

    /**属性名*/
    private String itemName;
    /**属性类型*/
    private String itemType;
    /**表单是否显示*/
    private String formShow;
    /**搜索条件是否显示*/
    private String condShow;
    /**显示文字*/
    private String title;
    /**字典的值*/
    private String dicCode;
    /**值*/
    private String value;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getFormShow() {
        return formShow;
    }

    public void setFormShow(String formShow) {
        this.formShow = formShow;
    }

    public String getCondShow() {
        return condShow;
    }

    public void setCondShow(String condShow) {
        this.condShow = condShow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public static DetailData create(Map<String, String> map) {
        DetailData entity = new DetailData();
        entity.setItemName(map.get("itemName"));
        entity.setItemType(map.get("itemType"));
        entity.setFormShow(map.get("formShow"));
        entity.setCondShow(map.get("condShow"));
        entity.setTitle(map.get("title"));
        entity.setValue(map.get("value"));
        return entity;
    }

    public static DetailData create(String itemName, String itemType, String formShow, String condShow, String title, String value) {
        DetailData entity = new DetailData();
        entity.setItemName(itemName);
        entity.setItemType(itemType);
        entity.setFormShow(formShow);
        entity.setCondShow(condShow);
        entity.setTitle(title);
        entity.setValue(value);
        return entity;
    }
}
