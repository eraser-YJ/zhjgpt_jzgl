package com.jc.supervise.point.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.supervise.point.service.ICmSupervisionPointColumnService;

/**
 * 建设管理-监察点数据来源管理
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
public class CmSupervisionPointColumn extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmSupervisionPointColumn(){}
    /**所属检查点*/
    private String supervisionId;
    /**数据来源（sql或程序）*/
    private String dataSource;
    /**程序时所在对象*/
    private String dataClass;
    /**sql或对象属性*/
    private String dataValue;
    /**排序*/
    private String queue;
    /**描述*/
    private String dataMeta;

    private String newSupervisionId;

    public String getSupervisionId() {
        return supervisionId;
    }

    public void setSupervisionId(String supervisionId) {
        this.supervisionId = supervisionId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataClass() {
        return dataClass;
    }

    public void setDataClass(String dataClass) {
        this.dataClass = dataClass;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getNewSupervisionId() {
        return newSupervisionId;
    }

    public void setNewSupervisionId(String newSupervisionId) {
        this.newSupervisionId = newSupervisionId;
    }

    public String getDataSourceValue() {
        if (StringUtil.isEmpty(this.getDataSource())) {
            return "";
        }
        if (this.getDataSource().equals("sql")) {
            return "SQL语句";
        } else if (this.getDataSource().equals("bean")) {
            return "程序数据源";
        }
        return "";
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getDataMeta() {
        return dataMeta;
    }

    public void setDataMeta(String dataMeta) {
        this.dataMeta = dataMeta;
    }

    public String getDataValueFile() {
        String result = "";
        if (this.getDataSource() != null && this.getDataSource().equals(ICmSupervisionPointColumnService.SQL)) {
            result = GlobalUtil.getFileContent(this.getId(), this.getCreateDate());
            if (result == null) {
                result = "";
            }
        }
        return result;
    }
}
