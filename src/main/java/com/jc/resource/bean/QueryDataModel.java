package com.jc.resource.bean;

import java.util.List;

/**
 * @Author 常鹏
 * @Date 2020/7/27 10:27
 * @Version 1.0
 */
public class QueryDataModel {
    /**
     * operationAction操作符
     * operationType 字段类型
     * operationKey 字段名
     * value 值区间值用，好隔开，表示>=?1 and <=?2
     * 数组：[{"operationAction": like,"operationKey":"person_name","value":"1111"},
     *       {"operationAction":like,"operationKey":"person_cert_num","operationType":"varchar","value":"2222,333"}
     *       ]*/
    private List<QueryDataParam> condJson ;
    private int page = 0;
    private int pageRows = 10;
    private int totalCount = 0;

    private String objUrl ;

    public List<QueryDataParam> getCondJson() {
        return condJson;
    }

    public void setCondJson(List<QueryDataParam> condJson) {
        this.condJson = condJson;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageRows() {
        return pageRows;
    }

    public void setPageRows(int pageRows) {
        this.pageRows = pageRows;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getObjUrl() {
        return objUrl;
    }

    public void setObjUrl(String objUrl) {
        this.objUrl = objUrl;
    }
}
