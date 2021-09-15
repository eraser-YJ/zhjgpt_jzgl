package com.jc.system.security.beans;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class UserTree {
    private String id;
    private String displayName;
    private String isCheck;
    private String deptId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String getIsCheck() {
        return isCheck;
    }
    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }
    public String getDeptId() {
        return deptId;
    }
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
