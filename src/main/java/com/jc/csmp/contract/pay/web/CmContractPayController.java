package com.jc.csmp.contract.pay.web;

import com.jc.csmp.common.tool.AuthUtil;
import com.jc.csmp.contract.pay.domain.CmContractPay;
import com.jc.csmp.contract.pay.domain.validator.CmContractPayValidator;
import com.jc.csmp.contract.pay.service.ICmContractPayService;
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
 * 建设管理-合同支付管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/contract/pay")
public class CmContractPayController extends BaseController {
	@Autowired
	private IInstanceService instanceService;

	@Autowired
	private ICmContractPayService cmContractPayService;

	@org.springframework.web.bind.annotation.InitBinder("cmContractPay")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmContractPayValidator());
	}

	public CmContractPayController() {
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
	@ActionLog(operateModelNm="建设管理-合同管理",operateFuncNm="save",operateDescribe="建设管理-合同管理新增操作")
	public Map<String,Object> saveWorkflow(@Valid CmContractPay entity, BindingResult result, HttpServletRequest request) throws Exception{
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
			Integer flag = cmContractPayService.saveWorkflow(entity);
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
	@ActionLog(operateModelNm="建设管理-合同管理",operateFuncNm="update",operateDescribe="建设管理-合同管理更新操作")
	public Map<String, Object> updateWorkflow(CmContractPay entity, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		entity.setHandleUser(SystemSecurityUtils.getUser().getId());
		Integer flag = cmContractPayService.updateWorkflow(entity);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_WORKFLOW_001"));
		} else if (flag.intValue() == -100) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "已付总额不能大于合同金额");
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
	public String startWorkflow(CmContractPay entity, Model model, HttpServletRequest request) throws Exception{
		Map<String,Object> workflowBean = instanceService.getStartNodeInfo(entity.getWorkflowBean());
		model.addAttribute("workflowBean", workflowBean);
		menuUtil.saveMenuID("contract/pay/startWorkflow.action",request);
		String token = getToken(request);
		INumber number = new Number();
		String no = number.getNumber("CONTRACTPAY", 1, 2, null);
		model.addAttribute("applyCode", no.substring(1));
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/contract/pay/contractPay_workflow_form";
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
	public String loadWorkflow(CmContractPay entity, Model model, HttpServletRequest request) throws Exception {
		CmContractPay entity_temp = new CmContractPay();
		entity_temp.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		entity_temp = cmContractPayService.get(entity_temp);
		fillPageInfo(model, request);
		Map<String,Object> workflowBean = instanceService.getDefaultNodeInfo(entity.getWorkflowBean());
		model.addAttribute("projectPlan", entity_temp);
		model.addAttribute("businessJson", JsonUtil.java2Json(entity_temp));
		model.addAttribute("workflowBean", workflowBean);
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/contract/pay/contractPay_workflow_form";
	}

	/**
	 * @description 待办列表
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version  2020-07-09 
	 */
	@RequestMapping(value="todoList.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-合同管理",operateFuncNm="manageForApplyList",operateDescribe="对建设管理-合同管理进行申请列表跳转工作")
	public String todoList(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/project/projectPlan/todoList.action",request);
		fillPageInfo(model, request);
		return "csmp/contract/pay/contractPay_workflow_todoList";
	}

	/**
	 * 待办数据
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageTodoList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageTodoList(CmContractPay entity, PageManager page) {
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = null;
		entity.setCurUserId(SystemSecurityUtils.getUser().getId()+"");
		try {
			page_ = cmContractPayService.workFlowTodoQueryByPage(entity, page);
			GlobalUtil.setTableRowNo(page_, page.getPageRows());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page_;
	}

	/**
	 * 合同查询
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="search.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-合同管理",operateFuncNm="projectPlanMyList",operateDescribe="对建设管理-合同管理进行查询列表跳转工作")
	public String search(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("contract/pay/search.action",request);
		fillPageInfo(model, request);
		model.addAttribute("contractId", request.getParameter("contractId"));
		return "csmp/contract/pay/cmContractPayList";
	}

	@RequestMapping(value="paySearch.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-合同管理",operateFuncNm="projectPlanMyList",operateDescribe="对建设管理-合同管理进行查询列表跳转工作")
	public String paySearch(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("contract/pay/search.action",request);
		fillPageInfo(model, request);
		model.addAttribute("contractId", request.getParameter("contractId"));
		if (request.getParameter("fn") != null && request.getParameter("fn").equals("myApply")) {
			model.addAttribute("createUser", SystemSecurityUtils.getUser().getId());
		}
		return "csmp/contract/pay/cmContractPaySearchList";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="searchList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager searchList(CmContractPay entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.modify_date ");
		}
		PageManager page_ = cmContractPayService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	@RequestMapping(value="paySearchList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager paySearchList(CmContractPay entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.modify_date ");
		}
		entity.setDeptCodeCondition(AuthUtil.checkSuperAuth(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId())));
		PageManager page_ = cmContractPayService.selectAuthQuery(entity, page);
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
	public CmContractPay get(CmContractPay entity) throws Exception{
		return cmContractPayService.get(entity);
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
		return "csmp/contract/pay/cmContractPayForm";
	}
}

