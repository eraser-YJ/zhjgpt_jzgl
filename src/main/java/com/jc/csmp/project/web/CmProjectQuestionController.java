package com.jc.csmp.project.web;

import com.jc.csmp.common.enums.ProjectQuestionEnum;
import com.jc.csmp.common.tool.AuthUtil;
import com.jc.csmp.project.domain.CmProjectQuestion;
import com.jc.csmp.project.domain.validator.CmProjectQuestionValidator;
import com.jc.csmp.project.service.ICmProjectQuestionService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建设管理-工程问题管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/question")
public class CmProjectQuestionController extends BaseController{

	@Autowired
	IInstanceService instanceService;

	@Autowired
	private ICmProjectQuestionService projectQuestionService;

	@org.springframework.web.bind.annotation.InitBinder("CmProjectQuestion")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectQuestionValidator());
	}

	public CmProjectQuestionController() {
	}

	@RequestMapping(value = "saveWorkflow.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="管理",operateFuncNm="save",operateDescribe="新增操作")
	public Map<String,Object> saveWorkflow(@Valid CmProjectQuestion entity, BindingResult result, HttpServletRequest request) throws Exception{
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
			Map<String, String> workflowAuditUser = new HashMap<>(2);
			workflowAuditUser.put("partaUnitLeaderId", entity.getPartaUnitLeaderId());
			workflowAuditUser.put("superviseLeaderId", entity.getSuperviseLeaderId());
			entity.setExtStr1(JsonUtil.java2Json(workflowAuditUser));
			Integer flag = projectQuestionService.saveWorkflow(entity);
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

	@RequestMapping(value="updateWorkflow.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="管理",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> updateWorkflow(CmProjectQuestion entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		Map<String, String> workflowAuditUser = new HashMap<>(2);
		workflowAuditUser.put("partaUnitLeaderId", entity.getPartaUnitLeaderId());
		workflowAuditUser.put("superviseLeaderId", entity.getSuperviseLeaderId());
		entity.setExtStr1(JsonUtil.java2Json(workflowAuditUser));
		Integer flag = projectQuestionService.updateWorkflow(entity);
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

	@RequestMapping(value="startWorkflow.action",method=RequestMethod.GET)
	public String startWorkflow(CmProjectQuestion entity, Model model, HttpServletRequest request) throws Exception {
		Map<String,Object> workflowBean = instanceService.getStartNodeInfo(entity.getWorkflowBean());
		String definitionId = (String) workflowBean.get("definitionId_");
		model.addAttribute("workflowBean",workflowBean);
		menuUtil.saveMenuID("/project/question/startWorkflow.action",request);
		String token = getToken(request);
		if (definitionId == null) {
			definitionId = "";
		}
		ProjectQuestionEnum questionEnum = ProjectQuestionEnum.getIndexOfDefinitionId(definitionId);
		INumber number = new Number();
		String no = number.getNumber(questionEnum.getNumberKey(), 1, 2, null);
		model.addAttribute("applyCode", no.substring(1));
		model.addAttribute("queryType", questionEnum.toString());
		model.addAttribute("attchBusinessTable", questionEnum.getBusinessTable());
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/project/question/projectQuestion_form";
	}

	@RequestMapping(value="loadWorkflow.action",method=RequestMethod.GET)
	public String loadWorkflow(CmProjectQuestion entity, Model model, HttpServletRequest request) throws Exception {
		CmProjectQuestion cond_temp = new CmProjectQuestion();
		cond_temp.setPiId(entity.getWorkflowBean().getBusiness_Key_());
		cond_temp = projectQuestionService.get(cond_temp);
		fillPageInfo(model, request);
		Map<String,Object> workflowBean = instanceService.getDefaultNodeInfo(entity.getWorkflowBean());
		model.addAttribute("cond",cond_temp);
		model.addAttribute("businessJson",JsonUtil.java2Json(cond_temp));
		model.addAttribute("workflowBean",workflowBean);
		model.addAttribute("attchBusinessTable", ProjectQuestionEnum.getByCode(cond_temp.getQuestionType()).getBusinessTable());
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/project/question/projectQuestion_form";
	}

	/**
	* @description 待办列表
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2020-07-09 
	*/
	@RequestMapping(value="todoList.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="管理",operateFuncNm="manageForApplyList",operateDescribe="跳转工作")
	public String todoList(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/project/question/todoList.action",request);
		fillPageInfo(model, request);
		String questionType = request.getParameter("questionType");
		model.addAttribute("questionType", (StringUtil.isEmpty(questionType) ? "1" : questionType));
		return "csmp/project/question/projectQuestion_todoList";
	}

	@RequestMapping(value="manageTodoList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageTodoList(CmProjectQuestion cond, PageManager page){
		if(StringUtil.isEmpty(cond.getOrderBy())) {
			cond.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = null;
		cond.setCurUserId(SystemSecurityUtils.getUser().getId()+"");
		try {
			page_ = projectQuestionService.workFlowTodoQueryByPage(cond,page);
			GlobalUtil.setTableRowNo(page_, page.getPageRows());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page_;
	}

	/**
	 * 问题
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="search.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="建设管理-问题",operateFuncNm="projectPlanMyList",operateDescribe="对建设管理-问题进行查询列表跳转工作")
	public String search(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("project/question/search.action",request);
		fillPageInfo(model, request);
		String questionType = request.getParameter("questionType");
		questionType = StringUtil.isEmpty(questionType) ? "1" : questionType;
		model.addAttribute("questionType", questionType);
		if (request.getParameter("fn") != null && request.getParameter("fn").equals("myApply")) {
			model.addAttribute("createUser", SystemSecurityUtils.getUser().getId());
		}
		return "csmp/project/question/projectQuestionList";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="searchList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager searchList(CmProjectQuestion entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc(" t.modify_date ");
		}
		entity.setDeptCodeCondition(AuthUtil.checkSuperAuth(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId())));
		PageManager page_ = projectQuestionService.query(entity, page);
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
	public CmProjectQuestion get(CmProjectQuestion entity) throws Exception{
		return projectQuestionService.get(entity);
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
		return "csmp/project/question/projectQuestionForm";
	}

	@RequestMapping(value="look.action",method=RequestMethod.GET)
	public String look(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		String id = request.getParameter("id");
		String questionType = request.getParameter("questionType");
		ProjectQuestionEnum questionEnum = ProjectQuestionEnum.getByCode(questionType);
		String title = "<span>" + questionEnum.getTitle() + "查询 > </span><span>" + questionEnum.getTitle() + "详细信息</span>";
		map.put("id", id);
		map.put("questionType", questionType);
		map.put("title", title);
		map.put("businessTable", questionEnum.getBusinessTable());
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "csmp/project/question/projectQuestionLook";
	}

	@RequestMapping(value="loadModify.action",method=RequestMethod.GET)
	public String loadModify(Model model,HttpServletRequest request) throws Exception{
		String questionType = request.getParameter("questionType");
		ProjectQuestionEnum questionEnum = ProjectQuestionEnum.getByCode(questionType);
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		map.put("businessTable", questionEnum.getBusinessTable());
		model.addAttribute("data", map);
		return "csmp/project/question/projectQuestionModify";
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="update",operateDescribe="对进行更新操作")
	public Map<String, Object> update(CmProjectQuestion entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(this.projectQuestionService.updateEntity(entity), resultMap, getToken(request));
		return resultMap;
	}

	/**
	 * 删除方法
	 * @param entity
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="deleteByIds",operateDescribe="对进行删除")
	public  Map<String, Object> deleteByIds(CmProjectQuestion entity, String id) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(new String[]{id});
		if(projectQuestionService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	/**
	 * 根据项目编号和问题类型获取问题
	 * @param projectNumber
	 * @param questionType
	 * @return
	 */
	@RequestMapping(value="getListByProjectNumber.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="deleteByIds",operateDescribe="对进行删除")
	public Result deleteByIds(String projectNumber, String questionType) {
		if (StringUtil.isEmpty(projectNumber) || StringUtil.isEmpty(questionType)) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		CmProjectQuestion param = new CmProjectQuestion();
		param.addOrderByFieldDesc(" t.create_date ");
		param.setProjectNumber(projectNumber);
		param.setQuestionType(questionType);
		try {
			List<CmProjectQuestion> dbList = this.projectQuestionService.queryAll(param);
			if (dbList == null) {
				dbList = new ArrayList<>();
			}
			return Result.success(dbList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Result.failure(ResultCode.INTERFACE_INNER_INVOKE_ERROR);
	}
}

