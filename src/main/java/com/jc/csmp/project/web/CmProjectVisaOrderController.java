package com.jc.csmp.project.web;

import com.jc.csmp.common.tool.AuthUtil;
import com.jc.csmp.project.domain.CmProjectVisaOrder;
import com.jc.csmp.project.domain.validator.CmProjectVisaOrderValidator;
import com.jc.csmp.project.service.ICmProjectVisaOrderService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.number.service.Number;
import com.jc.system.number.service.interfaces.INumber;
import com.jc.system.security.SystemSecurityUtils;
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
 * 工程过滤合同，采用合同的数据权限进行整体的数据权限处理
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/visa")
public class CmProjectVisaOrderController extends BaseController{

	@Autowired
	IInstanceService instanceService;

	@Autowired
	private ICmProjectVisaOrderService projectVisaOrderService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectVisaOrder")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectVisaOrderValidator());
	}

	public CmProjectVisaOrderController() {
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
	public Map<String,Object> saveWorkflow(@Valid CmProjectVisaOrder entity, BindingResult result, HttpServletRequest request) throws Exception{
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
			Integer flag = projectVisaOrderService.saveWorkflow(entity);
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
	@RequestMapping(value="updateWorkflow.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="建设管理-工程变更单",operateFuncNm="update",operateDescribe="建设管理-工程变更单更新操作")
	public Map<String, Object> updateWorkflow(CmProjectVisaOrder entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		Integer flag = projectVisaOrderService.updateWorkflow(entity);
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
		return resultMap;
	}

	/**
	 * 发起流程进入的方法
	 * @param cond
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="startWorkflow.action",method=RequestMethod.GET)
	public String startWorkflow(CmProjectVisaOrder cond, Model model, HttpServletRequest request) throws Exception{
		Map<String,Object> workflowBean = instanceService.getStartNodeInfo(cond.getWorkflowBean());
		model.addAttribute("workflowBean", workflowBean);
		menuUtil.saveMenuID("/project/visa/startWorkflow.action",request);
		String token = getToken(request);
		INumber number = new Number();
		String no = number.getNumber("PROVISA", 1, 2, null);
		model.addAttribute("applyCode", no.substring(1));
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/project/visa/projectVisaOrder_form";
	}

	/**
	 * 打开流程进入的方法
	 * @param entity
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception	 *
	 */
	@RequestMapping(value="loadWorkflow.action",method=RequestMethod.GET)
	public String loadWorkflow(CmProjectVisaOrder entity, Model model, HttpServletRequest request) throws Exception {
		CmProjectVisaOrder cond_temp = new CmProjectVisaOrder();
		cond_temp.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		cond_temp = projectVisaOrderService.get(cond_temp);
		fillPageInfo(model, request);
		Map<String,Object> workflowBean = instanceService.getDefaultNodeInfo(entity.getWorkflowBean());
		model.addAttribute("cond", cond_temp);
		model.addAttribute("businessJson",JsonUtil.java2Json(cond_temp));
		model.addAttribute("workflowBean",workflowBean);
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/project/visa/projectVisaOrder_form";
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
		menuUtil.saveMenuID("/project/visa/todoList.action",request);
		fillPageInfo(model, request);
		return "csmp/project/visa/projectVisaOrder_todoList";
	}

	/**
	 * 待办数据
	 * @param cond
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageTodoList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageTodoList(CmProjectVisaOrder cond, PageManager page){
		if(StringUtil.isEmpty(cond.getOrderBy())) {
			cond.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = null;
		cond.setCurUserId(SystemSecurityUtils.getUser().getId()+"");
		try {
			page_ = projectVisaOrderService.workFlowTodoQueryByPage(cond,page);
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
		menuUtil.saveMenuID("project/visa/search.action",request);
		fillPageInfo(model, request);
		model.addAttribute("contractId", request.getParameter("contractId"));
		if (request.getParameter("fn") != null && request.getParameter("fn").equals("myApply")) {
			model.addAttribute("createUser", SystemSecurityUtils.getUser().getId());
		}
		return "csmp/project/visa/projectVisaOrderList";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="searchList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager searchList(CmProjectVisaOrder entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.modify_date ");
		}
		entity.setDeptCodeCondition(AuthUtil.checkSuperAuth(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId())));
		PageManager page_ = projectVisaOrderService.query(entity, page);
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
	public CmProjectVisaOrder get(CmProjectVisaOrder entity) throws Exception{
		return projectVisaOrderService.get(entity);
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
		return "csmp/project/visa/projectVisaOrderForm";
	}
}

