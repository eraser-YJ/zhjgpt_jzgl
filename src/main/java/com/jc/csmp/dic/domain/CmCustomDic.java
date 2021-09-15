package com.jc.csmp.dic.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.StringUtil;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 建设管理-自定义字典项
 * @Author 常鹏
 * @Date 2020/7/7 10:02
 * @Version 1.0
 */
public class CmCustomDic extends BaseBean {
    private static final long serialVersionUID = 1L;
    public CmCustomDic(){}
    /**数据类型*/
    private String dataType;
    /**类别代码*/
    private String code;
    /**父id*/
    private String parentId;
    /**类别名称*/
    private String name;
    /**范围*/
    private String scope;
    /**排序*/
    private String queue;
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataTypeValue() {
        if (StringUtil.isEmpty(this.getDataType())) {
            return "";
        }
        if (this.getDataType().equals("landNature")) {
            return "所属用地性质";
        } else if (this.getDataType().equals("projectTrade")) {
            return "所属国标行业";
        }
        return "";
    }
}
