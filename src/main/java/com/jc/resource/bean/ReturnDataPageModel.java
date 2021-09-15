package com.jc.resource.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author 常鹏
 * @Date 2020/7/27 10:28
 * @Version 1.0
 */
public class ReturnDataPageModel {

    public ReturnDataPageModel(){}
    /**0000成功*/
    private String code;
    /**消息*/
    private String message;
    /** 检索区域*/
    List<DlhDataobjectField> condList;
    /** 列标题*/
    List<Map<String, Object>> headBody;
    /**分页信息*/
    private int page = 0;
    private int pageRows = 10;
    private int totalCount = 0;
    /**返回数据Map*/
    List<Map<String,Object>> data = new ArrayList<>();
    /**其他返回值*/
    Map<String,Object> otherParam;

    /**关联表
     * tableName 表名
     * columnNameSelf 本表列名
     * culumnNameOther 关联表列名*/
    private List<Map<String,Object>> relationTable = new ArrayList<>();

    private String objUrl ;

    public ReturnDataPageModel(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DlhDataobjectField> getCondList() {
        return condList;
    }

    public void setCondList(List<DlhDataobjectField> condList) {
        this.condList = condList;
    }

    public List<Map<String, Object>> getHeadBody() {
        return headBody;
    }

    public void setHeadBody(List<Map<String, Object>> headBody) {
        this.headBody = headBody;
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

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }

    public Map<String, Object> getOtherParam() {
        return otherParam;
    }

    public void setOtherParam(Map<String, Object> otherParam) {
        this.otherParam = otherParam;
    }

    public String getObjUrl() {
        return objUrl;
    }

    public void setObjUrl(String objUrl) {
        this.objUrl = objUrl;
    }
}
