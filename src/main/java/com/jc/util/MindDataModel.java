package com.jc.util;

import com.jc.common.kit.StringUtil;

import javax.swing.text.StringContent;
import java.util.List;
import java.util.Map;

public class MindDataModel {
    private String id;
    private boolean isroot;
    private String topic;
    private String parentid;
    private String background;
    private String foreground;
    private boolean expanded;
    private List<MindDataModel> children;
    private List<Map<String,String>> data;
    private String objUrl;
    private String tableCode;



    public MindDataModel(String id, boolean isroot, String topic, String parentid, String background, String foreground, boolean expanded) {
        this.id = id;
        this.isroot = isroot;
        this.topic = topic;
        this.parentid = parentid;
        this.background = background;
        this.foreground = foreground;
        this.expanded = expanded;
    }

    public String getTableCode() {
        return tableCode;
    }

    public void setTableCode(String tableCode) {
        this.tableCode = tableCode;
    }

    public String getObjUrl() {
        return objUrl;
    }

    public void setObjUrl(String objUrl) {
        this.objUrl = objUrl;
    }

    public MindDataModel(String id) {
        this.id = id;
    }

    public MindDataModel() {

    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIsroot() {
        return isroot;
    }

    public void setIsroot(boolean isroot) {
        this.isroot = isroot;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getForeground() {
        return foreground;
    }

    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<MindDataModel> getChildren() {
        return children;
    }

    public void setChildren(List<MindDataModel> children) {
        this.children = children;
    }

    public String getValueByName(String name){
        for(Map<String,String> map:this.data){
            String itemName = map.get("itemName");
            if(!StringUtil.isEmpty(itemName)&&itemName.equalsIgnoreCase(name)){
              return map.get("value");
            }
        }
        return "";
    }
}
