package com.jc.common.def;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class DefItemVO {
    private String itemCode;
    private String targetCode;
    private String itemName="";
    private String itemType = "String";
    private String dicCode;
    private String dicParentId;
    private String dateFormat;
    private Integer condSeq;
    private Integer listSeq;
    private Integer formSeq;
    private Integer condLen = 1;
    private Integer listLen;
    private Integer formLen = 1;
    private Object itemValue;
    private String placeholder= "N";

    public String getTargetCode() {
        return targetCode;
    }

    public void setTargetCode(String targetCode) {
        this.targetCode = targetCode;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public String getDicParentId() {
        return dicParentId;
    }

    public void setDicParentId(String dicParentId) {
        this.dicParentId = dicParentId;
    }

    public Integer getCondSeq() {
        return condSeq;
    }

    public void setCondSeq(Integer condSeq) {
        this.condSeq = condSeq;
    }

    public Integer getListSeq() {
        return listSeq;
    }

    public void setListSeq(Integer listSeq) {
        this.listSeq = listSeq;
    }

    public Integer getFormSeq() {
        return formSeq;
    }

    public void setFormSeq(Integer formSeq) {
        this.formSeq = formSeq;
    }

    public Integer getCondLen() {
        return condLen;
    }

    public Integer getCondShowLen() {
        return condLen * DefUtil.PUB_ATT_LEN - 1;
    }

    public void setCondLen(Integer condLen) {
        this.condLen = condLen;
    }

    public Integer getListLen() {
        return listLen;
    }

    public void setListLen(Integer listLen) {
        this.listLen = listLen;
    }

    public Integer getFormLen() {
        return formLen;
    }

    public void setFormLen(Integer formLen) {
        this.formLen = formLen;
    }

    public Integer getFormShowLen() {
        return formLen * DefUtil.PUB_ATT_LEN - 1;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }


    public Object getItemValue() {
        return itemValue;
    }

    public Object getItemShowValue() {
        return itemValue;
    }



    public void setItemValue(Object itemValue) {
        this.itemValue = itemValue;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }
}
