package com.jc.system.dic.domain;

/***
 * @author Administrator
 * @date 2020-06-30
 */
public class DicTree {
    private String id;
    private String parentId;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId(){
        return parentId;
    }

    public void setParentId(String parentId){
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
