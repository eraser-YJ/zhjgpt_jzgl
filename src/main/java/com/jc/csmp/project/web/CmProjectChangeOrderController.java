package com.jc.csmp.project.web;

import com.jc.csmp.common.tool.AuthUtil;
import com.jc.csmp.project.domain.CmProjectChangeOrder;
import com.jc.csmp.project.domain.validator.CmProjectChangeOrderValidator;
import com.jc.csmp.project.service.ICmProjectChangeOrderService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.number.service.Number;
import com.jc.system.number.service.interfaces.INumber;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.util.menuUtil;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.util.WorkflowContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 建设管理-工程变更单管理Controller
 * 项目过滤条件为参与项目的用户
 * 合同为根据项目及当前登录人过滤
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/change")
public class CmProjectChangeOrderController extends BaseController{
	@Autowired
	private IInstanceService instanceService;

	@Autowired
	private ICmProjectChangeOrderService cmProjectChangeOrderService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectChangeOrder")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectChangeOrderValidator());
	}

	public CmProjectChangeOrderController() {
	}

	/**
	 * 保存方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "saveWorkflow.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="建设管理-工程变更单",operateFuncNm="save",operateDescribe="建设管理-工程变更单新增操作")
	public Map<String,Object> saveWorkflow(@Valid CmProjectChangeOrder entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			Integer flag = cmProjectChangeOrderService.saveWorkflow(entity);
			if (flag == 1) {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_001"));
			} else {
				String workflowMessage = WorkflowContext.getErrMsg(flag);
				if(StringUtil.isEmpty(workflowMessage)){
					resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_002"));
				}else{
					resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
					resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, workflowMessage);
				}
			}
		}
		return resultMap;
	}

	/**
	 * 修改方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="updateWorkflow.action", method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="建设管理-工程变更单",operateFuncNm="update",operateDescribe="建设管理-工程变更单更新操作")
	public Map<String, Object> updateWorkflow(CmProjectChangeOrder entity, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		Integer flag = cmProjectChangeOrderService.updateWorkflow(entity);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_001"));
		} else if (flag == GlobalContext.CUSTOM_SIGN_ERROR.intValue()) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "修改后合同金额小于0，无法修改");
		} else {
			String workflowMessage = WorkflowContext.getErrMsg(flag);
			if(StringUtil.isEmpty(workflowMessage)){
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_002"));
			}else{
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, workflowMessage);
			}
		}

		return resultMap;
	}

	/**
	 * 发起流程进入的方法
	 * @param entity
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="startWorkflow.action", method=RequestMethod.GET)
	public String startWorkflow(CmProjectChangeOrder entity, Model model, HttpServletRequest request) throws Exception{
		Map<String,Object> workflowBean = instanceService.getStartNodeInfo(entity.getWorkflowBean());
		String definitionId = (String) workflowBean.get("definitionId_");
		model.addAttribute("workflowBean", workflowBean);
		menuUtil.saveMenuID("project/change/startWorkflow.action",request);
		String token = getToken(request);
		Department department = DeptCacheUtil.getDeptById(SystemSecurityUtils.getUser().getDeptId());
		if (department != null) {
			model.addAttribute("defaultApplyDept", department.getId() + "," + department.getName());
		}
		INumber number = new Number();
		String no = number.getNumber("PROCHA", 1, 2, null);
		model.addAttribute("applyCode", no.substring(1));
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		if (definitionId.indexOf("projectChangeOrder") > -1) {
			model.addAttribute("changeType", "1");
			return "csmp/project/change/projectChangeOrder_workflow_form";
		} else {
			model.addAttribute("changeType", "2");
			return "csmp/project/change/contractChangeOrder_workflow_form";
		}
	}

	/**
	 * 打开流程进入的方法
	 * @param entity
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception	 *
	 */
	@RequestMapping(value="loadWorkflow.action", method=RequestMethod.GET)
	public String loadWorkflow(CmProjectChangeOrder entity, Model model, HttpServletRequest request) throws Exception {
		CmProjectChangeOrder entity_temp = new CmProjectChangeOrder();
		entity_temp.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		entity_temp = cmProjectChangeOrderService.get(entity_temp);
		fillPageInfo(model, request);
		Map<String,Object> workflowBean = instanceService.getDefaultNodeInfo(entity.getWorkflowBean());
		String definitionId = (String) workflowBean.get("definitionId_");
		model.addAttribute("projectPlan", entity_temp);
		model.addAttribute("businessJson", JsonUtil.java2Json(entity_temp));
		model.addAttribute("workflowBean", workflowBean);
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		if (definitionId.indexOf("projectChangeOrder") > -1) {
			return "csmp/project/change/projectChangeOrder_workflow_form";
		} else {
			return "csmp/project/change/contractChangeOrder_workflow_form";
		}
	}

	/**
	 * @description 待办列表
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version  2020-07-09 
	 */
	@RequestMapping(value="todoList.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-工程变更单",operateFuncNm="manageForApplyList",operateDescribe="对建设管理-工程变更单进行申请列表跳转工作")
	public String todoList(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/project/projectPlan/todoList.action",request);
		fillPageInfo(model, request);
		return "csmp/project/change/projectChangeOrder_workflow_todoList";
	}

	/**
	 * 待办数据
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageTodoList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageTodoList(CmProjectChangeOrder entity, PageManager page) {
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = null;
		entity.setCurUserId(SystemSecurityUtils.getUser().getId()+"");
		try {
			page_ = cmProjectChangeOrderService.workFlowTodoQueryByPage(entity, page);
			GlobalUtil.setTableRowNo(page_, page.getPageRows());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page_;
	}

	/**
	 * 工程变更单
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="search.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-工程变更单",operateFuncNm="projectPlanMyList",operateDescribe="对建设管理-工程变更单进行查询列表跳转工作")
	public String search(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("project/change/search.action",request);
		fillPageInfo(model, request);
		model.addAttribute("contractId", request.getParameter("contractId"));
		return "csmp/project/change/projectChangeOrderList";
	}

	/**
	 * 我的申请
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="myProjectSearch.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-工程变更单",operateFuncNm="projectPlanMyList",operateDescribe="对建设管理-工程变更单进行查询列表跳转工作")
	public String myProjectSearch(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("project/change/search.action",request);
		fillPageInfo(model, request);
		model.addAttribute("contractId", request.getParameter("contractId"));
		model.addAttribute("createUser", SystemSecurityUtils.getUser().getId());
		return "csmp/project/change/myProjectChangeOrderList";
	}

	/**
	 * 我的申请
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="myContractSearch.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-工程变更单",operateFuncNm="projectPlanMyList",operateDescribe="对建设管理-工程变更单进行查询列表跳转工作")
	public String myContractSearch(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("project/change/search.action",request);
		fillPageInfo(model, request);
		model.addAttribute("contractId", request.getParameter("contractId"));
		model.addAttribute("createUser", SystemSecurityUtils.getUser().getId());
		return "csmp/project/change/myContractChangeOrderList";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="searchList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager searchList(CmProjectChangeOrder entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.create_date ");
		}
		entity.setDeptCodeCondition(AuthUtil.checkSuperAuth(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId())));
		PageManager page_ = cmProjectChangeOrderService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	/**
	 * 获取单条记录方法
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public CmProjectChangeOrder get(CmProjectChangeOrder entity) throws Exception{
		return cmProjectChangeOrderService.get(entity);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		String changeType = request.getParameter("changeType");
		if (changeType != null && changeType.equals("1")) {
			return "csmp/project/change/projectChangeOrderForm";
		} else {
			return "csmp/project/change/contractChangeOrderForm";
		}
	}
}

