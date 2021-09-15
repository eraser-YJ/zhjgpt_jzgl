package com.jc.resource.enums.vo;

import com.jc.foundation.domain.Attach;
import com.jc.foundation.util.GlobalUtil;
import com.jc.system.content.service.IAttachService;
import com.jc.workflow.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资源附件信息
 * @Author 常鹏
 * @Date 2020/8/18 10:25
 * @Version 1.0
 */
public class ResourceAttachInfo {
    /**上传人*/
    private String attach_creater;
    /**业务的objurl*/
    private String attach_objUrl;
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

    public String getAttach_objUrl() {
        return attach_objUrl;
    }

    public void setAttach_objUrl(String attach_objUrl) {
        this.attach_objUrl = attach_objUrl;
    }

    public static ResourceAttachInfo create(IAttachService attachService, String addFileIds, String delFileIds, String userName, String businessId, String businessTable) {
        if (StringUtil.isEmpty(addFileIds)) {
            return null;
        }
        ResourceAttachInfo entity = new ResourceAttachInfo();
        entity.setAttach_creater(userName);
        entity.setAttach_key(businessId);
        entity.setDelFileIds(delFileIds);
        entity.setAttach_objUrl(businessTable);
        if (!StringUtil.isEmpty(addFileIds)) {
            Attach attach = new Attach();
            attach.setPrimaryKeys(GlobalUtil.splitStr(addFileIds, ','));
            try {
                List<Attach> attachList = attachService.queryAttachByBusinessIds(attach);
                if (attachList != null && attachList.size() > 0) {
                    List<Map<String, Object>> fileList = new ArrayList<>();
                    for (Attach file : attachList) {
                        Map<String, Object> result = new HashMap<>(3);
                        result.put("id", file.getId());
                        result.put("name", file.getName());
                        result.put("path", "/content/attach/download.action?attachId=" + file.getId());
                        fileList.add(result);
                    }
                    entity.setAddFile(fileList);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return entity;
    }
}
