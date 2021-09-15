package com.jc.system.security.domain;

import java.util.List;

import com.jc.foundation.domain.BaseBean;

/**
 * 菜单
 * @author Administrator
 * @date 2020-06-30
 */
public class Menu extends BaseBean{
    private static final long serialVersionUID = 1L;
    /**菜单名称*/
    private String name;
    /**上级菜单ID*/
    private String parentId;
    /**图标类型名称*/
    private String icon;
    /**0功能菜单  1权限控制菜单*/
    private Integer menuType;
    /**对应action名称*/
    private String actionName;
    /**排序*/
    private Integer queue;
    /**按钮认证字段*/
    private String permission;
    /**是否显示 null全部数据 0显示 1不显示*/
    private Integer isShow;
    /**父菜单名称字段*/
    private String parentname;
    /**是否存在下级节点*/
    private String isNextNode;
    /**父节点的上级节点*/
    private String rootNode;
    /**登录用户id*/
    private String userId;
    private List<Menu> childmenus;
    private Integer childmenussize;
    private Integer isChecked;
    /**权重系数*/
    private Integer weight;

    /**显示哪个菜单下的子菜单*/
    private String subSystemMenuId;

    public String getSubSystemMenuId() {
        return subSystemMenuId;
    }

    public void setSubSystemMenuId(String subSystemMenuId) {
        this.subSystemMenuId = subSystemMenuId;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getParentId(){
        return parentId;
    }
    public void setParentId(String parentId){
        this.parentId = parentId;
    }
    public String getIcon(){
        return icon;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }
    public Integer getMenuType(){
        return menuType;
    }
    public void setMenuType(Integer menuType){
        this.menuType = menuType;
    }
    public String getActionName(){
        return actionName;
    }
    public void setActionName(String actionName){
        this.actionName = actionName;
    }
    public Integer getQueue(){
        return queue;
    }
    public void setQueue(Integer queue){
        this.queue = queue;
    }
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission) {
        this.permission = permission;
    }
    public Integer getIsShow() {
        return isShow;
    }
    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }
    public String getParentname() {
        return parentname;
    }
    public void setParentname(String parentname) {
        this.parentname = parentname;
    }
    public String getIsNextNode() {
        return isNextNode;
    }
    public void setIsNextNode(String isNextNode) {
        this.isNextNode = isNextNode;
    }
    public String getRootNode() {
        return rootNode;
    }
    public void setRootNode(String rootNode) {
        this.rootNode = rootNode;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public List<Menu> getChildmenus() {
        return childmenus;
    }
    public void setChildmenus(List<Menu> childmenus) {
        this.childmenus = childmenus;
    }
    public Integer getChildmenussize() {
        return childmenussize;
    }
    public void setChildmenussize(Integer childmenussize) {
        this.childmenussize = childmenussize;
    }
    public Integer getIsChecked() {
        return isChecked;
    }
    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }
    @Override
    public Integer getWeight() {
        return weight;
    }
    @Override
    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}