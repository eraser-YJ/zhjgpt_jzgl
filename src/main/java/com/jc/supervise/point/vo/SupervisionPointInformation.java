package com.jc.supervise.point.vo;

import com.jc.supervise.point.domain.CmSupervisionPointColumn;

import java.util.List;

/**
 * 监测点详情，包含参数数据来源
 * @Author 常鹏
 * @Date 2020/8/12 8:54
 * @Version 1.0
 */
public class SupervisionPointInformation {
    /**主键id*/
    private String id;
    /**检查点*/
    private String pointName;
    /**描述*/
    private String pointDesc;
    /**所属机构*/
    private String deptId;
    /**脚本方法名*/
    private String functionName;
    /**监测结果错误描述*/
    private String errorText;
    /**脚本内容*/
    private String jsContent;
    /**数据来源*/
    private List<CmSupervisionPointColumn> columnList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getPointDesc() {
        return pointDesc;
    }

    public void setPointDesc(String pointDesc) {
        this.pointDesc = pointDesc;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }

    public String getJsContent() {
        return jsContent;
    }

    public void setJsContent(String jsContent) {
        this.jsContent = jsContent;
    }

    public List<CmSupervisionPointColumn> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<CmSupervisionPointColumn> columnList) {
        this.columnList = columnList;
    }

    public static SupervisionPointInformation create(String id, String pointName, String pointDesc, String deptId, String functionName, String errorText, String jsContent) {
        SupervisionPointInformation entity = new SupervisionPointInformation();
        entity.setId(id);
        entity.setPointDesc(pointDesc);
        entity.setPointName(pointName);
        entity.setDeptId(deptId);
        entity.setFunctionName(functionName);
        entity.setErrorText(errorText);
        entity.setJsContent(jsContent);
        return entity;
    }
}
