package com.jc.csmp.contract.info.web;

import com.jc.csmp.common.enums.WorkflowAuditStatusEnum;
import com.jc.csmp.common.tool.AuthUtil;
import com.jc.csmp.contract.info.domain.CmContractInfo;
import com.jc.csmp.contract.info.domain.validator.CmContractInfoValidator;
import com.jc.csmp.contract.info.service.ICmContractInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建设管理-合同管理Controller
 * 数据权限，项目的建设单位、监管单位。合同的发包单位、中标单位可以查看
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/contract/info")
public class CmContractInfoController extends BaseController {
	@Autowired
	private IInstanceService instanceService;

	@Autowired
	private ICmContractInfoService cmContractInfoService;

	@org.springframework.web.bind.annotation.InitBinder("cmContractInfo")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmContractInfoValidator());
	}

	public CmContractInfoController() {
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
	public Map<String,Object> saveWorkflow(@Valid CmContractInfo entity, BindingResult result, HttpServletRequest request) throws Exception{
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
			Integer flag = cmContractInfoService.saveWorkflow(entity);
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
	public Map<String, Object> updateWorkflow(CmContractInfo entity, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		Integer flag = cmContractInfoService.updateWorkflow(entity);
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
	 * @param entity
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="startWorkflow.action", method=RequestMethod.GET)
	public String startWorkflow(CmContractInfo entity, Model model, HttpServletRequest request) throws Exception{
		Map<String,Object> workflowBean = instanceService.getStartNodeInfo(entity.getWorkflowBean());
		model.addAttribute("workflowBean", workflowBean);
		menuUtil.saveMenuID("contract/info/startWorkflow.action",request);
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/contract/info/contractInfo_workflow_form";
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
	public String loadWorkflow(CmContractInfo entity, Model model, HttpServletRequest request) throws Exception {
		CmContractInfo entity_temp = new CmContractInfo();
		entity_temp.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		entity_temp = cmContractInfoService.get(entity_temp);
		fillPageInfo(model, request);
		Map<String,Object> workflowBean = instanceService.getDefaultNodeInfo(entity.getWorkflowBean());
		model.addAttribute("projectPlan", entity_temp);
		model.addAttribute("businessJson", JsonUtil.java2Json(entity_temp));
		model.addAttribute("workflowBean", workflowBean);
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/contract/info/contractInfo_workflow_form";
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="update",operateDescribe="对进行更新操作")
	public Map<String, Object> update(CmContractInfo entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmContractInfoService.updateEntity(entity), resultMap, getToken(request));
		return resultMap;
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
		return "csmp/contract/info/contractInfo_workflow_todoList";
	}

	/**
	 * 待办数据
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageTodoList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageTodoList(CmContractInfo entity, PageManager page) {
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = null;
		entity.setCurUserId(SystemSecurityUtils.getUser().getId()+"");
		try {
			page_ = cmContractInfoService.workFlowTodoQueryByPage(entity, page);
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
		menuUtil.saveMenuID("contract/info/search.action",request);
		fillPageInfo(model, request);
		if (request.getParameter("fn") != null && request.getParameter("fn").equals("myApply")) {
			model.addAttribute("createUser", SystemSecurityUtils.getUser().getId());
		}
		return "csmp/contract/info/cmContractInfoList";
	}

	/**
	 * 合同支付总览
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="paySearch.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-合同管理",operateFuncNm="projectPlanMyList",operateDescribe="对建设管理-合同管理进行查询列表跳转工作")
	public String paySearch(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("contract/info/paySearch.action",request);
		fillPageInfo(model, request);
		return "csmp/contract/info/cmContractSearchPayList";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="searchList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager searchList(CmContractInfo entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}

		System.out.println("xxxxxx1212213");

		entity.setDeptCodeCondition(AuthUtil.checkSuperAuth(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId())));
		PageManager page_ = cmContractInfoService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	@RequestMapping(value="searchListByProjectNumber.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager searchListByProjectNumber(CmContractInfo entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = cmContractInfoService.query(entity, page);
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
	public CmContractInfo get(CmContractInfo entity) throws Exception{
		return cmContractInfoService.get(entity);
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
		return "csmp/contract/info/cmContractInfoForm";
	}

	@RequestMapping(value="look.action",method=RequestMethod.GET)
	public String look(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String id = request.getParameter("id");
		map.put("id", id);
		model.addAttribute("data", map);
		return "csmp/contract/info/cmContractInfoLook";
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="change.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String change() throws Exception{
		return "csmp/contract/info/cmContractInfoChange";
	}

	/**
	 * 查询有权限查看的合同列表
	 * @param entity
	 * @return
	 */
	@RequestMapping(value="contractList.action",method=RequestMethod.GET)
	@ResponseBody
	public List<CmContractInfo> contractList(CmContractInfo entity){
		entity.setDeptCodeCondition(AuthUtil.checkSuperAuth(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId())));
		try {
			if (StringUtil.isEmpty(entity.getAuditStatus())) {
				entity.setAuditStatus(WorkflowAuditStatusEnum.finish.toString());
			}
			List<CmContractInfo> dataList = this.cmContractInfoService.queryAll(entity);
			return dataList;
		} catch (Exception ex) {
			return Collections.emptyList();
		}
	}

	@RequestMapping(value="getADeptLeaderByContractId.action",method=RequestMethod.GET)
	@ResponseBody
	public Result getADeptLeaderByContractId(String id){
		CmContractInfo entity = this.cmContractInfoService.getById(id);
		if (entity == null || entity.getPartyaUnit() == null) {
			return Result.failure(ResultCode.RESULE_DATA_NONE);
		}
		try {
			Department department = DeptCacheUtil.getDeptById(entity.getPartyaUnit());
			if (department == null || StringUtil.isEmpty(department.getLeaderId())) {
				return Result.failure(ResultCode.RESULE_DATA_NONE);
			}
			Map<String, String> dataMap = new HashMap<>(2);
			dataMap.put("partyaUnitLeaderId", department.getLeaderId());
			User user = UserUtils.getUser(department.getLeaderId());
			if (user != null) {
				dataMap.put("leaderName", user.getDisplayName());
				dataMap.put("leaderMobile", user.getMobile());
			}
			dataMap.put("partybUnit", entity.getPartybUnit());
			dataMap.put("partybUnitValue", entity.getPartybUnitValue());
			return Result.success("", dataMap);
		} catch (CustomException e) {
			e.printStackTrace();
			return Result.failure(1, e.getMessage());
		}
	}
}

