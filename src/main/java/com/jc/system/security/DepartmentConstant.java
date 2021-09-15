package com.jc.system.security;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class DepartmentConstant {

    private DepartmentConstant() {
        throw new IllegalStateException("DepartmentConstant class");
    }
    /**
     * 部门类型-机构
     */
    public static final Integer  DEPARTMENT_TYPE_ORG =  1;
    /**
     * 部门类型-部门
     */
    public static final Integer  DEPARTMENT_TYPE_DEPT= 0;

    public static final String DEPARTMENT_ROOT_ID ="100001";

    public static final String DEPARTMENT_ROOT_ID_OLD ="1";//后期会去掉

    public static final String  DEPARTMENT_ROOT_PARENT_ID = "0";
}
