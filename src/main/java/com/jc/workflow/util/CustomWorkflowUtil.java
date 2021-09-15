package com.jc.workflow.util;

import com.jc.workflow.definition.bean.Definition;
import com.jc.workflow.definition.bean.Node;
import com.jc.workflow.definition.bean.RuntimeInfo;
import com.jc.workflow.definition.service.IDefinitionService;
import com.jc.workflow.definition.service.IRuntimeInfoService;
import com.jc.workflow.external.WorkflowBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author 常鹏
 * @Date 2020/8/6 10:07
 * @Version 1.0
 */
public class CustomWorkflowUtil {
    public static WorkflowBean ssemblyStartBean(String workflowCode, String busiKey, String nowUserId, String auditUser, IDefinitionService definitionService, IRuntimeInfoService runtimeInfoService) throws Exception {
        if (workflowCode == null || workflowCode.trim().length() == 0) {
            throw new Exception("流程编码不能为空");
        }
        if (nowUserId == null) {
            throw new Exception("当前用户不能为空");
        }
        String currentUserId = nowUserId;
        Definition definition = definitionService.getDefinitionByFLowId(workflowCode, currentUserId);
        if (definition == null) {
            throw new Exception("没有找到编码为" + workflowCode + "的流程定义");
        }
        String definitionId = definition.getDefinitionId();
        RuntimeInfo startNode = runtimeInfoService.getStartInfo(definition.getDefinitionId(), currentUserId);
        if (startNode == null) {
            throw new Exception("编码为" + workflowCode + "的流程没有找到开始节点");
        }
        Map<String, Object> varMap = new HashMap<>();
        String curNodeId = startNode.getCurNode().getComponentId();
        RuntimeInfo nextInfo = runtimeInfoService.getSubmitInfo(definitionId, curNodeId, null, varMap, currentUserId);
        if (nextInfo == null) {
            throw new Exception("编码为" + workflowCode + "的流程没有找到下级节点信息");
        }
        List<Node> nextNodeList = nextInfo.getNextNodes();
        if (nextNodeList == null) {
            throw new Exception("编码为" + workflowCode + "的流程没有找到下级节点信息");
        }
        if (nextNodeList.size() > 1) {
            throw new Exception("编码为" + workflowCode + "的流程启动后的下级节点为多个，流程定义异常");
        }
        Node nextNode = nextNodeList.get(0);
        WorkflowBean bean = new WorkflowBean();
        bean.setCurNodeId_(curNodeId);
        bean.setDefinitionId_(definitionId);
        // 分配人员
        StringBuilder userStr = new StringBuilder();
        if (nextNode.getAssignees() != null && nextNode.getAssignees().size() > 0) {
            for (Map<String, Object> userMap : nextNode.getAssignees()) {
                if (userStr.length() > 0) {
                    userStr.append(",");
                }
                userStr.append(userMap.get("id"));
            }
        } else if (auditUser != null) {
            userStr.append(auditUser);
        }
        if (userStr.length() < 1) {
            throw new Exception("编码为" + workflowCode + "的流程启动后的下级节点没有处理人，流程定义异常");
        }
        bean.setConfirmUserId_(userStr.toString());
        bean.setConfirmNodeId_(nextNode.getComponentId());
        // 路由路线
        StringBuilder route = new StringBuilder();
        for (Map<String, String> lineMap : nextNode.getChannel()) {
            if (route.length() > 0) {
                route.append("&");
            }
            route.append(lineMap.get("sequenceId"));
        }
        bean.setConfirmRouteId_(route.toString());
        bean.setSubmitType_("SUBMIT");
        bean.setMobileUserId(nowUserId + "");
        bean.setBusiness_Key_(busiKey);
        return bean;
    }
}
