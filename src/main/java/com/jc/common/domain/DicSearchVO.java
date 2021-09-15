package com.jc.common.domain;

import java.util.List;
import java.util.Map;

public class DicSearchVO {
    private String id;
    private String dictName;
    private String parentCode;
    private List<Map<String, String>> dicList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public List<Map<String, String>> getDicList() {
        return dicList;
    }

    public void setDicList(List<Map<String, String>> dicList) {
        this.dicList = dicList;
    }
}
