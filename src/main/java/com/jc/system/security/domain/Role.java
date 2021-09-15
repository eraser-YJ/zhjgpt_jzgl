package com.jc.system.security.domain;

import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.util.GlobalContext;

import java.util.List;

/**
 * 角色
 * @author Administrator
 * @date 2020-06-30
 */
public class Role extends BaseBean{
    private static final long serialVersionUID = 1L;
    /** 名称 */
    private String name;
    /** 描述 */
    private String description;
    /** 部门id */
    private String deptId;
    /** 部门名称 */
    private String deptName;
    /** 人员数量 */
    private String personNum;
    private List<RoleMenus> roleMenus;
    private List<RoleBlocks> roleBlocks;
    private List<RoleExts> roleExts;
    private List<SysUserRole> sysUserRoles;
    private String menuIds;
    private String nameOld;
    private String userId;
    private String remark1;
    /**权重系数*/
    private Integer weight;
    /**跨机构权重系数*/
    private Integer bestrideWeight;
    /**权重类型*/
    private Integer weightType;
    /**部门查询条件*/
    private String deptIds;
    private Integer deptWeight;
    /**机构的id转code查询条件*/
    private String deptIdToCode;
    public String getDeptIdToCode() {
        return deptIdToCode;
    }
    public void setDeptIdToCode(String deptIdToCode) {
        this.deptIdToCode = deptIdToCode;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getMenuIds() {
        return menuIds;
    }
    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }
    public List<RoleMenus> getRoleMenus() {
        return roleMenus;
    }
    public void setRoleMenus(List<RoleMenus> roleMenus) {
        this.roleMenus = roleMenus;
    }
    public List<RoleBlocks> getRoleBlocks() {
        return roleBlocks;
    }
    public void setRoleBlocks(List<RoleBlocks> roleBlocks) {
        this.roleBlocks = roleBlocks;
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
    public String getDeptIds() {
        return deptIds;
    }
    public void setDeptIds(String deptIds) {
        this.deptIds = deptIds;
    }
    public String getPersonNum() {
        return personNum;
    }
    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }
    public List<SysUserRole> getSysUserRoles() {
        return sysUserRoles;
    }
    public void setSysUserRoles(List<SysUserRole> sysUserRoles) {
        this.sysUserRoles = sysUserRoles;
    }
    public List<RoleExts> getRoleExts() {
        return roleExts;
    }
    public void setRoleExts(List<RoleExts> roleExts) {
        this.roleExts = roleExts;
    }
    public String getNameOld() {
        return nameOld;
    }
    public void setNameOld(String nameOld) {
        this.nameOld = nameOld;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getRemark1() {
        return remark1;
    }
    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }
    @Override
    public Integer getWeight() {
        return weight;
    }
    @Override
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
    public Integer getBestrideWeight() {
        return bestrideWeight;
    }
    public void setBestrideWeight(Integer bestrideWeight) {
        this.bestrideWeight = bestrideWeight;
    }
    public Integer getWeightType() {
        return weightType;
    }
    public void setWeightType(Integer weightType) {
        this.weightType = weightType;
    }
    public Integer getDeptWeight() {
        return deptWeight;
    }
    public void setDeptWeight(Integer deptWeight) {
        this.deptWeight = deptWeight;
    }
    /**判断是否是系统管理员*/
    public boolean getIsManager(){
        if(GlobalContext.MANAGER_NAME.equals(this.remark1)){
            return true;
        } else {
            return false;
        }
    }
    /**判断是否是安全保密员*/
    public boolean getIsSecurity(){
        if(GlobalContext.SECURITY_NAME.equals(this.remark1)){
            return true;
        } else {
            return false;
        }
    }
    /**判断是否是安全审计员*/
    public boolean getIsAudit(){
        if(GlobalContext.AUDIT_NAME.equals(this.remark1)){
            return true;
        } else {
            return false;
        }
    }
}