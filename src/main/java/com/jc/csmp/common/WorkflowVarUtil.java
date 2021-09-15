package com.jc.csmp.common;

import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.JsonUtil;
import com.jc.workflow.util.SpringContextHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowVarUtil {
    /**
     * 构建节点配置数据值
     *
     * @param workflowBean
     * @return
     */
    /**
     * 构建节点配置数据值
     *
     * @param workflowBean
     * @return
     */

    public static Map<String, Object> buildItemCtrl(Map<String, Object> workflowBean, String openType) {
        //工作参数对照
        Map<String, Object> reMap = new HashMap<>();


        // 字段属性值
        String json = (String) workflowBean.get("formItemPrivJsonStr");
        if (json == null || json.trim().length() <= 0) {
            return reMap;
        }

        // 结构化
        WorkflowVarAgg agg = (WorkflowVarAgg) JsonUtil.json2Java(json, WorkflowVarAgg.class);
        if (agg != null && agg.getTodo() != null) {
            for (WorkflowVarVO vo : agg.getTodo()) {
                String key = vo.getColumnName();
                if (key == null || key.trim().length() == 0) {
                    key = vo.getItemName().trim();
                }
                if ("DONE".equalsIgnoreCase(openType)) {
                    reMap.put(key, "readonly");
                } else {
                    if (vo.getCustomProperty() != null && vo.getCustomProperty().getRequired() != null) {
                        reMap.put(key, "must");
                    } else {
                        reMap.put(key, vo.getAttributeAccessCtrl());
                    }
                }
            }
        }
        return reMap;
    }
}
