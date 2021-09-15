package com.jc.csmp.common.enums;

import com.jc.foundation.util.StringUtil;

/**
 * 企业类型字典对应着机构的编码
 * @Author 常鹏
 * @Date 2020/7/16 15:51
 * @Version 1.0
 */
public enum CompanyTypeConvertDepartmentEnums {
    /***/
    empty("", "", ""),
    design("100028", "000000030002", "勘察设计单位"),
    survey("100029", "000000030003", "招标代理机构"),
    supervisor("100030", "000000030004", "工程监理单位"),
    build("100031", "000000030005", "建筑施工单位"),
    company_type_sg("100032", "000000030006", "施工图审查机构"),
    company_type_gczj("100033", "000000030007", "工程造价咨询企业");

    private String deptId;
    private String deptCode;
    private String deptName;

    CompanyTypeConvertDepartmentEnums(String deptId, String deptCode, String deptName) {
        this.deptId = deptId;
        this.deptCode = deptCode;
        this.deptName = deptName;
    }

    /**
     * 根据编号获取枚举对象
     * @param code
     * @return
     */
    public static CompanyTypeConvertDepartmentEnums getByCode(String code) {
        CompanyTypeConvertDepartmentEnums result = CompanyTypeConvertDepartmentEnums.empty;
        if (!StringUtil.isEmpty(code)) {
            CompanyTypeConvertDepartmentEnums[] array = values();
            for (CompanyTypeConvertDepartmentEnums e : array) {
                if (e.toString().equals(code)) {
                    result = e;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 根据部门id获取枚举对象
     * @param deptId
     * @return
     */
    public static CompanyTypeConvertDepartmentEnums getByDeptId(String deptId) {
        CompanyTypeConvertDepartmentEnums result = CompanyTypeConvertDepartmentEnums.empty;
        if (!StringUtil.isEmpty(deptId)) {
            CompanyTypeConvertDepartmentEnums[] array = values();
            for (CompanyTypeConvertDepartmentEnums e : array) {
                if (e.getDeptId().equals(deptId)) {
                    result = e;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 根据部门code获取枚举对象
     * @param deptCode
     * @return
     */
    public static CompanyTypeConvertDepartmentEnums getByDeptCode(String deptCode) {
        CompanyTypeConvertDepartmentEnums result = CompanyTypeConvertDepartmentEnums.empty;
        if (!StringUtil.isEmpty(deptCode)) {
            CompanyTypeConvertDepartmentEnums[] array = values();
            for (CompanyTypeConvertDepartmentEnums e : array) {
                if (!StringUtil.isEmpty(e.getDeptCode()) && deptCode.indexOf(e.getDeptCode()) > -1) {
                    result = e;
                    break;
                }
            }
        }
        return result;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
}
