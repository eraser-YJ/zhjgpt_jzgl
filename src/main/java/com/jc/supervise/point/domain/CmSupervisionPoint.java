package com.jc.supervise.point.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;

import java.util.List;

/**
 * 建设管理-检查点
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmSupervisionPoint extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmSupervisionPoint(){}
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
    /**保存时使用的临时id，用来更新子表的supervisionId*/
    private String tempId;
    /**脚本内容*/
    private String jsContent;
    private String supervisionId;
    private String projectNumber;

    public String getSupervisionId() {
        return supervisionId;
    }

    public void setSupervisionId(String supervisionId) {
        this.supervisionId = supervisionId;
    }

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getJsContent() {
        if (StringUtil.isEmpty(jsContent)) {
            return GlobalUtil.getFileContent(this.getId(), this.getCreateDate());
        }
        return jsContent;
    }

    public void setJsContent(String jsContent) {
        this.jsContent = jsContent;
    }

    public String getPointDesc() {
        return pointDesc;
    }

    public void setPointDesc(String pointDesc) {
        this.pointDesc = pointDesc;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
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
}
