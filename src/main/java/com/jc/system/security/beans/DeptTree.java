package com.jc.system.security.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @date 2020-07-01
 */
public class DeptTree {
    private String id;
    private String name;
    /**上级节点Id*/
    private String parentId;
    private Integer deptType;
    private String isChecked = "1";
    private List<UserTree> users = new ArrayList<>();
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public Integer getDeptType() {
        return deptType;
    }
    public void setDeptType(Integer deptType) {
        this.deptType = deptType;
    }
    public String getIsChecked() {
        return isChecked;
    }
    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }
    public List<UserTree> getUsers() {
        return users;
    }
    public void setUsers(List<UserTree> users) {
        this.users = users;
    }
    public void addUser(UserTree user) {
        users.add(user);
    }
}
