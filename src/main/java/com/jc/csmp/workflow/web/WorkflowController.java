package com.jc.csmp.workflow.web;

import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.message.domain.CmMessageInfo;
import com.jc.csmp.message.service.ICmMessageInfoService;
import com.jc.csmp.workflow.domain.TodoExtendBean;
import com.jc.csmp.workflow.enums.WorkflowContentEnum;
import com.jc.csmp.workflow.enums.service.IWorkflowTodoService;
import com.jc.foundation.domain.BaseBean;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.util.DeptCacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 工作流待办
 * @Author 常鹏
 * @Date 2020/7/9 10:37
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/workflow")
public class WorkflowController extends BaseController {

    @Autowired
    private ICmMessageInfoService cmMessageInfoService;

    @RequestMapping(value="todoList.action",method= RequestMethod.GET)
    @ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
    public String todoList() {
        return "csmp/workflow/workflowTodoList";
    }

    @RequestMapping(value="manageTodoList.action",method=RequestMethod.GET)
    @ResponseBody
    public PageManager manageTodoList(CmMessageInfo entity, PageManager page) {
        if(StringUtil.isEmpty(entity.getOrderBy())) {
            entity.addOrderByFieldDesc("t.CREATE_DATE");
        }
        PageManager page_ = null;
        entity.setCurUserId(SystemSecurityUtils.getUser().getId()+"");
        try {
            page_ = cmMessageInfoService.workflowTodoQuery(entity, page);
            GlobalUtil.setTableRowNo(page_, page.getPageRows());
            GlobalUtil.setExtendInfomation(page_);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return page_;
    }
}
