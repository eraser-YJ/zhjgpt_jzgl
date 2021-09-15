package com.jc.csmp.plan.domain;

import com.jc.system.dic.domain.Dic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class XyDic implements Serializable {
    private String code;
    private String value;
    private String parentId;
    private String leaf="N";
    private String treePathName;
    private Object subInfo;
    private List<XyDic> nextDic;

    public String getLeaf() {
        return leaf;
    }

    public void setLeaf(String leaf) {
        this.leaf = leaf;
    }

    public String getCode() {
        return code;
    }

    public XyDic setCode(String code) {
        this.code = code;
        return this;
    }

    public String getValue() {
        return value;
    }

    public XyDic setValue(String value) {
        this.value = value;
        return this;
    }

    public String getParentId() {
        return parentId;
    }

    public XyDic setParentId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    public List<XyDic> getNextDic() {
        return nextDic;
    }

    public void setNextDic(List<XyDic> nextDic) {
        this.nextDic = nextDic;
    }

    public XyDic clone() {
        XyDic vo = new XyDic();
        vo.setCode(this.getCode());
        vo.setValue(this.getValue());
        vo.setParentId(this.getParentId());
        vo.setLeaf(this.getLeaf());
        return vo;
    }

    public XyDic deepClone() {
        XyDic vo = new XyDic();
        vo.setCode(this.getCode());
        vo.setValue(this.getValue());
        vo.setParentId(this.getParentId());
        vo.setLeaf(this.getLeaf());
        List<XyDic> resList = new ArrayList<XyDic>();
        if(this.nextDic!=null){
            for(XyDic subDic:nextDic){
                resList.add(subDic.deepClone());
            }
        }
        vo.setNextDic(resList);
        return vo;
    }

    public int hashCode() {
        return this.getCode().hashCode();
    }

    public boolean equals(Object value) {
        if (value == null) {
            return false;
        }
        if (!(value instanceof XyDic)) {
            return false;
        }
        return this.getCode().equalsIgnoreCase(((XyDic) value).getCode());
    }

    public Object getSubInfo() {
        return subInfo;
    }

    public void setSubInfo(Object subInfo) {
        this.subInfo = subInfo;
    }

    public String getTreePathName() {
        return treePathName;
    }

    public void setTreePathName(String treePathName) {
        this.treePathName = treePathName;
    }
}
