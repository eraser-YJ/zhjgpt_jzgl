package com.jc.common.def;

import java.util.ArrayList;
import java.util.List;

public class PageAttributeRow {
    private List<DefItemVO> rowList = new ArrayList<>();

    public List<DefItemVO> getRowList() {
        return rowList;
    }

    public void setRowList(List<DefItemVO> rowList) {
        this.rowList = rowList;
    }
}
