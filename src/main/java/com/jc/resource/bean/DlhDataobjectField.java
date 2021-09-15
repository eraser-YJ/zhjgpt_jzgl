package com.jc.resource.bean;

import com.jc.foundation.domain.BaseBean;
import com.jc.system.dic.domain.Dic;

import java.util.List;

/**
 * @title 统一数据资源中心
 * @description 数据对象属性表 实体类
 * @author lc
 * @version 2020-03-10
 */
public class DlhDataobjectField extends BaseBean {

    private static final long serialVersionUID = 1L;

    public DlhDataobjectField() {
    }

    /** 对象主键 */
    private String objectId;
    /** 模型主键 */
    private String modelId;
    /** 属性 */
    private String fieldCode;
    /** 属性名 */
    private String fieldName;
    /** 列名 */
    private String itemId;
    /** 列名 */
    private String itemName;
    /** 列类型 */
    private String itemType;
    /** 列长度 */
    private String itemLen;
    /** 字典编码 */
    private String itemDicCode;
    /** 是否主键 */
    private String itemKey;
    /** 排序 */
    private Integer fieldSeq;
    /** 检查 */
    private String fieldCheck;
    /** 是否条件显示，显示顺序 */
    private Integer fieldCondShow;
    private String fieldCondShowNull;
    private Integer fieldCondShowBegin;
    private Integer fieldCondShowEnd;
    /** 是否列表显示，显示顺序 */
    private Integer fieldListShow;
    private String fieldListShowNull;
    private Integer fieldListShowBegin;
    private Integer fieldListShowEnd;
    /** 是否表单显示，显示顺序 */
    private Integer fieldFormShow;
    private String fieldFormShowNull;
    private Integer fieldFormShowBegin;
    private Integer fieldFormShowEnd;
    /** 检查 */
    private Object objValue;
    private Object dicList;
    /**数据字典对应下拉选*/
    private List<Dic> optionList;
    /**数据字典对应下拉选*/
    private String dicCode;
    /**是否为字典项*/
    private Boolean isDic;


    public List<Dic> getOptionList() {
        return optionList;
    }

    public void setOptionList(List<Dic> optionList) {
        this.optionList = optionList;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public Boolean getIsDic() {
        return isDic;
    }

    public void setIsDic(Boolean isDic) {
        isDic = isDic;
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemDicCode() {
        return itemDicCode;
    }

    public void setItemDicCode(String itemDicCode) {
        this.itemDicCode = itemDicCode;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

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

    public String getItemLen() {
        return itemLen;
    }

    public void setItemLen(String itemLen) {
        this.itemLen = itemLen;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public Integer getFieldSeq() {
        return fieldSeq;
    }

    public void setFieldSeq(Integer fieldSeq) {
        this.fieldSeq = fieldSeq;
    }

    public String getFieldCheck() {
        return fieldCheck;
    }

    public void setFieldCheck(String fieldCheck) {
        this.fieldCheck = fieldCheck;
    }

    public Integer getFieldCondShow() {
        return fieldCondShow;
    }

    public void setFieldCondShow(Integer fieldCondShow) {
        this.fieldCondShow = fieldCondShow;
    }

    public Integer getFieldCondShowBegin() {
        return fieldCondShowBegin;
    }

    public void setFieldCondShowBegin(Integer fieldCondShowBegin) {
        this.fieldCondShowBegin = fieldCondShowBegin;
    }

    public Integer getFieldCondShowEnd() {
        return fieldCondShowEnd;
    }

    public void setFieldCondShowEnd(Integer fieldCondShowEnd) {
        this.fieldCondShowEnd = fieldCondShowEnd;
    }

    public Integer getFieldListShowBegin() {
        return fieldListShowBegin;
    }

    public void setFieldListShowBegin(Integer fieldListShowBegin) {
        this.fieldListShowBegin = fieldListShowBegin;
    }

    public Integer getFieldListShowEnd() {
        return fieldListShowEnd;
    }

    public void setFieldListShowEnd(Integer fieldListShowEnd) {
        this.fieldListShowEnd = fieldListShowEnd;
    }

    public Integer getFieldFormShowBegin() {
        return fieldFormShowBegin;
    }

    public void setFieldFormShowBegin(Integer fieldFormShowBegin) {
        this.fieldFormShowBegin = fieldFormShowBegin;
    }

    public Integer getFieldFormShowEnd() {
        return fieldFormShowEnd;
    }

    public void setFieldFormShowEnd(Integer fieldFormShowEnd) {
        this.fieldFormShowEnd = fieldFormShowEnd;
    }

    public Integer getFieldListShow() {
        return fieldListShow;
    }

    public void setFieldListShow(Integer fieldListShow) {
        this.fieldListShow = fieldListShow;
    }

    public Integer getFieldFormShow() {
        return fieldFormShow;
    }

    public void setFieldFormShow(Integer fieldFormShow) {
        this.fieldFormShow = fieldFormShow;
    }

    public String getFieldCondShowNull() {
        return fieldCondShowNull;
    }

    public void setFieldCondShowNull(String fieldCondShowNull) {
        this.fieldCondShowNull = fieldCondShowNull;
    }

    public String getFieldListShowNull() {
        return fieldListShowNull;
    }

    public void setFieldListShowNull(String fieldListShowNull) {
        this.fieldListShowNull = fieldListShowNull;
    }

    public String getFieldFormShowNull() {
        return fieldFormShowNull;
    }

    public void setFieldFormShowNull(String fieldFormShowNull) {
        this.fieldFormShowNull = fieldFormShowNull;
    }

    public Object getObjValue() {
        return objValue;
    }

    public void setObjValue(Object objValue) {
        this.objValue = objValue;
    }

    public Object getDicList() {
        return dicList;
    }

    public void setDicList(Object docList) {
        this.dicList = docList;
    }

    public Boolean getDic() {
        return isDic;
    }

    public void setDic(Boolean dic) {
        isDic = dic;
    }
}
