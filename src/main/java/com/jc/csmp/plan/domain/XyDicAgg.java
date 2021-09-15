package com.jc.csmp.plan.domain;

import java.io.Serializable;
import java.util.List;

public class XyDicAgg implements Serializable {
    private List<XyDic> dicTree;
    private List<XyDic> xsDicList;
    public List<XyDic> getDicTree() {
        return dicTree;
    }

    public void setDicTree(List<XyDic> dicTree) {
        this.dicTree = dicTree;
    }

    public List<XyDic> getXsDicList() {
        return xsDicList;
    }

    public void setXsDicList(List<XyDic> xsDicList) {
        this.xsDicList = xsDicList;
    }
}
