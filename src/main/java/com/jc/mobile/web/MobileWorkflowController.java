package com.jc.mobile.web;

import com.jc.common.kit.StringUtil;
import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.csmp.message.service.ICmMessageInfoService;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.mobile.basic.web.MobileController;
import com.jc.mobile.util.MobileApiResponse;
import com.jc.workflow.external.WorkflowBean;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.suggest.service.IWorkflowSuggestService;
import com.jc.workflow.task.service.ITaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 移动端工作流
 * @Author 常鹏
 * @Date 2020/8/21 12:10
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/mobile/workflow")
public class MobileWorkflowController extends MobileController {

    @Autowired
    private ICmMessageInfoService cmMessageInfoService;
    @Autowired
    private IInstanceService instanceService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private IWorkflowSuggestService suggestService;
    @Autowired
    private ITaskService taskService;

    /**
     * 我的待办
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="todoList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse todoList(PageManager page, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmMessageInfo entity = new CmMessageInfo();
        entity.setCurUserId(apiResponse.getUserId());
        String wt = request.getParameter("wt");
        if (!StringUtil.isEmpty(wt)) {
            entity.setWorkflowTitle(wt);
        }
        entity.setIsMobile("true");
        PageManager page_ = this.cmMessageInfoService.workflowTodoQuery(entity, page);
        GlobalUtil.setTableRowNo(page_, page.getPageRows());
        GlobalUtil.setExtendInfomation(page_);
        return MobileApiResponse.ok(page_);
    }

    /**
     * 我的已办
     * @param page
     * @param request
     * @return
     */
    @RequestMapping(value="doneList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse doneList(PageManager page, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        CmMessageInfo entity = new CmMessageInfo();
        entity.setCurUserId(apiResponse.getUserId());
        entity.setIsMobile("true");
        String wt = request.getParameter("wt");
        if (!StringUtil.isEmpty(wt)) {
            entity.setWorkflowTitle(wt);
        }
        PageManager page_ = this.cmMessageInfoService.workflowDoneQuery(entity, page);
        GlobalUtil.setTableRowNo(page_, page.getPageRows());
        GlobalUtil.setExtendInfomation(page_);
        return MobileApiResponse.ok(page_);
    }

    /**
     * 加载流程信息
     * @param entity
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="loadWorkflow.action",method=RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse loadWorkflow(CmMessageInfo entity, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        Map<String, Object> workflowBean = instanceService.getDefaultNodeInfo(entity.getWorkflowBean());
        Map<String, Object> pageData = new HashMap<>(2);
        pageData.put("workflowBean", workflowBean);
        IWorkflowTodoService service = WorkflowContentEnum.getByCode(entity.getWorkflowTitle()).getService();
        pageData.put("businessData", service.getEntityByPid(entity.getWorkflowBean().getBusiness_Key_()));
        pageData.put("attachBusinessTable", service.getAttachTableName());
        pageData.put("flowVariable", service.getFlowVariable(workflowBean, pageData.get("businessData")));
        return MobileApiResponse.ok(pageData);
    }

    /**
     * 工作流流转
     * @param param
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value="updateWorkflow.action", method = RequestMethod.POST)
    @ResponseBody
    public MobileApiResponse updateWorkflow(@RequestBody Map<String, Object> param, HttpServletRequest request) throws Exception{
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        WorkflowBean workflowBean = new WorkflowBean();
        workflowBean.setConfirmUserId_((String) param.get("workflowBean.confirmUserId_"));
        workflowBean.setDefinitionId_((String) param.get("workflowBean.definitionId_"));
        workflowBean.setTaskId_((String) param.get("workflowBean.taskId_"));
        workflowBean.setSubmitType_((String) param.get("workflowBean.submitType_"));
        workflowBean.setCurNodeId_((String) param.get("workflowBean.curNodeId_"));
        workflowBean.setConfirmRouteId_((String) param.get("workflowBean.confirmRouteId_"));
        workflowBean.setConfirmNodeId_((String) param.get("workflowBean.confirmNodeId_"));
        if (param != null && param.size() > 0) {
            Map<String, Object> workflowVar = new HashMap<>(1);
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                if (entry.getKey().indexOf("workflowVar_[") > -1) {
                    String key = GlobalUtil.replaceStr(entry.getKey(), "workflowVar_[", "");
                    key = GlobalUtil.replaceStr(key, "]", "");
                    workflowVar.put(key, entry.getValue());
                }
            }
            workflowBean.setWorkflowVar_(workflowVar);
        }
        workflowBean.setInstanceId_((String) param.get("workflowBean.instanceId_"));
        workflowBean.setMessage_((String) param.get("workflowBean.message_"));
        workflowBean.setSignInfo_("");
        workflowBean.setSuggestId_((String) param.get("workflowBean.suggestId_"));
        workflowBean.setSignContainerId_((String) param.get("workflowBean.signContainerId_"));
        workflowBean.setBusiness_Key_((String) param.get("workflowBean.business_Key_"));
        workflowBean.setMobileUserId(apiResponse.getUserId());
        String workflowTitle = (String) param.get("flowType");
        Map<String, Object> extendData = (Map<String, Object>) param.get("extendData");
        if (workflowTitle.equals(WorkflowContentEnum.htzf.toString())) {
            //合同支付审批需要设置办理人
            extendData.put("handleUser", apiResponse.getUserId());
        }
        Integer flag = WorkflowContentEnum.getByCode(workflowTitle).getService().updateWorkflow(workflowBean, extendData);
        if (flag == 1) {
            return MobileApiResponse.ok(MessageUtils.getMessage("JC_WORKFLOW_001"));
        } else {
            return MobileApiResponse.error(MessageUtils.getMessage("JC_WORKFLOW_002"));
        }
    }

    /**
     * 获取意见域
     * @param pid
     * @param itemId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "suggestList.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse suggestList(String pid, String itemId) {
        List<Map<String, Object>> dbList = jdbcTemplate.queryForList("select proc_inst_id from workflow_instance where business_key_ = ?", new Object[]{pid});
        if (dbList != null && dbList.size() > 0) {
            String instanceId = (String) dbList.get(0).get("proc_inst_id");
            if (!GlobalUtil.isEmpty(instanceId)) {
                return MobileApiResponse.ok(this.suggestService.querySuggestList(instanceId, itemId, "false", "createTime"));
            }
        }
        return MobileApiResponse.ok(Collections.EMPTY_LIST);
    }

    /**
     * 获取节点
     * @param param
     * @return
     */
    @RequestMapping(value = "getNextNodes.action", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getNextNodes(@RequestBody Map<String, Object> param) {
        WorkflowBean workflowBean = new WorkflowBean();
        workflowBean.setCurNodeId_((String) param.get("curNodeId_"));
        workflowBean.setDefinitionId_((String) param.get("definitionId_"));
        workflowBean.setInstanceId_((String) param.get("instanceId_"));
        if (param != null && param.size() > 0) {
            Map<String, Object> workflowVar = new HashMap<>(1);
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                if (entry.getKey().indexOf("workflowVar_[") > -1) {
                    String key = GlobalUtil.replaceStr(entry.getKey(), "workflowVar_[", "");
                    key = GlobalUtil.replaceStr(key, "]", "");
                    workflowVar.put(key, entry.getValue());
                }
            }
            workflowBean.setWorkflowVar_(workflowVar);
        }
        Map<String, Object> result = this.taskService.getNextNodes(workflowBean);
        return MobileApiResponse.ok(result);
    }

    /**
     * 根据pid获取详细信息
     * @param pid
     * @param flowType
     * @param request
     * @return
     */
    @RequestMapping(value = "loadInformation.action", method = RequestMethod.GET)
    @ResponseBody
    public MobileApiResponse loadInformation(@RequestParam("pid") String pid, @RequestParam("flowType") String flowType, HttpServletRequest request) {
        MobileApiResponse apiResponse = validateToken(request);
        if (!apiResponse.isSuccess()) {
            return apiResponse;
        }
        return MobileApiResponse.ok(WorkflowContentEnum.getByCode(flowType).getService().getEntityByPid(pid));
    }
}
