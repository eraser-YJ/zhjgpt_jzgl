package com.jc.dlh;

import java.util.*;

/**
 * 资源附件信息
 * @Author 常鹏
 * @Date 2020/8/18 10:25
 * @Version 1.0
 */
public class ResourceAttachInfo {
    /**上传人*/
    private String attach_creater;
    /**业务主键*/
    private String attach_key;
    /**资源名*/
    private String objUrl;
    /**删除的附件id*/
    private String delFileIds;
    /***
     * id: '附件id' ,
     * name: '附件名',
     * path: '附件地址'
     */
    private List<Map<String, Object>> addFile;

    public String getAttach_creater() {
        return attach_creater;
    }

    public void setAttach_creater(String attach_creater) {
        this.attach_creater = attach_creater;
    }

    public String getAttach_key() {
        return attach_key;
    }

    public void setAttach_key(String attach_key) {
        this.attach_key = attach_key;
    }

    public String getObjUrl() {
        return objUrl;
    }

    public void setObjUrl(String objUrl) {
        this.objUrl = objUrl;
    }

    public String getDelFileIds() {
        return delFileIds;
    }

    public void setDelFileIds(String delFileIds) {
        this.delFileIds = delFileIds;
    }

    public List<Map<String, Object>> getAddFile() {
        return addFile;
    }

    public void setAddFile(List<Map<String, Object>> addFile) {
        this.addFile = addFile;
    }

    public List<Map> getConMapList(){
        String[] ids = this.getDelFileIds().split(",");
        List<Map> condMapList = new ArrayList<>();
        Map map = new HashMap();
        map.put("operationType","varchar");
        map.put("operationAction","in");
        map.put("operationKey","attach_file_id");

        StringBuffer conid = new StringBuffer("\'");
        int count =0;
        for(String id:ids){
            conid.append(id);
            if(count!=ids.length-1){
                conid.append("\',\'");
            }else{
                conid.append("\'");
            }
        }
        map.put("value",conid);
        return null;
    }
    public List<Map<String, Object>>  getDataList(){
        List<Map<String, Object>> dataList = new ArrayList<>();
        if(this.getAddFile()==null||this.getAddFile().size()<=0) {
           return dataList;
        }
        for(Map formap:this.getAddFile()){
            Map<String,Object> map = new HashMap<>();
            map.put("attach_title",formap.get("name"));
            map.put("attach_objUrl",this.getObjUrl());
            map.put("attach_key",this.getAttach_key());
            map.put("attach_creater",this.getAttach_creater());
            map.put("attach_path",formap.get("path"));
            map.put("attach_file_id",formap.get("id"));
            map.put("id", UUID.randomUUID().toString().replaceAll("-", ""));
            dataList.add(map);
        }
        return dataList;

    }
}
