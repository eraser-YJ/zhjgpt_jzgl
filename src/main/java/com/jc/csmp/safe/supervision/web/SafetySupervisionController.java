package com.jc.csmp.safe.supervision.web;

import com.jc.csmp.item.domain.ItemClassify;
import com.jc.csmp.item.service.IItemClassifyService;
import com.jc.csmp.safe.supervision.domain.SafetySupervision;
import com.jc.csmp.safe.supervision.domain.SafetyUnit;
import com.jc.csmp.safe.supervision.domain.validator.SafetySupervisionValidator;
import com.jc.csmp.safe.supervision.service.ISafetySupervisionService;
import com.jc.csmp.safe.supervision.service.ISafetyUnitService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.content.web.AttachController;
import com.jc.system.dic.IDicManager;
import com.jc.system.dic.domain.Dic;
import com.jc.system.util.menuUtil;
import com.jc.system.applog.ActionLog;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.exception.CustomException;
import com.jc.system.security.SystemSecurityUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.jc.workflow.instance.service.IInstanceService;
import com.jc.workflow.util.WorkflowContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/safe/supervision/")
public class SafetySupervisionController extends BaseController{
	private IDicManager dicManager = SpringContextHolder.getBean(IDicManager.class);
	private static final Logger logger = Logger.getLogger(AttachController.class);
	@Autowired
	IInstanceService instanceService;
	@Autowired
	private IItemClassifyService itemClassifyService;

	@Autowired
	private ISafetySupervisionService safetySupervisionService;
	@Autowired
	private ISafetyUnitService safetyUnitService;


	@org.springframework.web.bind.annotation.InitBinder("safetySupervision")


	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new SafetySupervisionValidator());
	}

	public SafetySupervisionController() {
	}

		/**
	 * @description ???????????? 
	 * @param cond ????????????
	 * @return success ??????????????? errorMessage????????????
	 * @throws Exception
	 * @author
	 * @version  2020-07-09
	 */
	@RequestMapping(value = "saveWorkflow.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="??????",operateFuncNm="save",operateDescribe="????????????")
	public Map<String,Object> saveWorkflow(@Valid SafetySupervision cond,BindingResult result,
			HttpServletRequest request) throws Exception{
		
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// ??????token
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		if(!"false".equals(resultMap.get("success"))){
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			Integer flag = safetySupervisionService.saveWorkflow(cond);
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
	* workflowParamTemp????????????
	* @param cond ?????????
	* @param result ????????????
	* @return Integer ???????????????
	* @author
	* @version  2020-07-09 
	*/
	@RequestMapping(value="updateWorkflow.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="??????",operateFuncNm="update",operateDescribe="????????????")
	public Map<String, Object> updateWorkflow(SafetySupervision cond, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		Integer flag = safetySupervisionService.updateWorkflow(cond);
		
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
	 * @description ???????????????????????????
	 * @param workflowParamTemp ?????????
	 * @return String ??????????????????
	 * @throws Exception
	 */
	@RequestMapping(value="startWorkflow.action",method=RequestMethod.GET)
	public String startWorkflow(SafetySupervision cond,Model model,HttpServletRequest request) throws Exception{
		Map<String,Object> workflowBean = instanceService.getStartNodeInfo(cond.getWorkflowBean());
		model.addAttribute("workflowBean",workflowBean);
		menuUtil.saveMenuID("/safe/supervision/startWorkflow.action",request);
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		List<Dic> dictList = dicManager.getAllDicsByTypeCode("company_type","csmp", true);
		model.addAttribute("dictList", dictList);
		ItemClassify itemClassify=new ItemClassify();
		itemClassify.setItemCode(cond.getItemCode());
		itemClassify  =itemClassifyService.get(itemClassify);
		model.addAttribute("itemClassify", itemClassify);
		return "csmp/safe/supervision/safetySupervision_form";
	}
	/**
	 * @description ???????????????????????????
	 * @param workflowParamTemp ?????????
	 * @return String ??????????????????
	 * @throws Exception
	 */
	@RequestMapping(value="loadWorkflow.action",method=RequestMethod.GET)
	public String loadWorkflow(SafetySupervision cond,Model model,HttpServletRequest request) throws Exception{

		SafetySupervision cond_temp = new SafetySupervision();
		cond_temp.setPiId(cond.getWorkflowBean().getBusiness_Key_());
		cond_temp = safetySupervisionService.get(cond_temp);
		/**??????????????????*/
		SafetyUnit safetyUnit =new SafetyUnit();

		safetyUnit.addOrderByField("t.EXT_NUM1");
		safetyUnit.setProjectId(cond_temp.getId());

		List<SafetyUnit>safetyUnitList= safetyUnitService.queryAll(safetyUnit);
		cond_temp.setSafetyUnitList(safetyUnitList);
		ItemClassify itemClassify=new ItemClassify();
		itemClassify.setItemCode(cond_temp.getItemCode());
		itemClassify=itemClassifyService.get(itemClassify);
		cond_temp.setItemClassify(itemClassify);
		fillPageInfo(model, request);
		Map<String,Object> workflowBean = instanceService.getDefaultNodeInfo(cond.getWorkflowBean());
		model.addAttribute("cond",cond_temp);
		List<Dic> dictList = dicManager.getAllDicsByTypeCode("company_type","csmp", true);
		model.addAttribute("dictList", dictList);
		model.addAttribute("itemClassify",itemClassify);

		String json = JsonUtil.java2Json(cond_temp);
		System.out.println(json);
		model.addAttribute("businessJson",json);
		model.addAttribute("workflowBean",workflowBean);
		String token = getToken(request);
		model.addAttribute(GlobalContext.SESSION_TOKEN, token);
		return "csmp/safe/supervision/safetySupervision_form";
	}
	/**
	* @description ????????????
	* @return String ???????????????
	* @throws Exception
	* @author
	* @version  2020-07-09 
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="??????",operateFuncNm="manage",operateDescribe="??????")
	public String manage(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/safe/supervision/manage.action",request);
		fillPageInfo(model, request);
		return "csmp/safe/supervision/safetySupervision_queryList";
	}

	/**
	 * @description ??????/????????????????????????
	 * @param cond ?????????
	 * @param PageManager page ???????????????
	 * @return PagingBean ????????????
	 * @throws Exception
	 * @author
	 * @version  2020-07-09 
	 */
	@RequestMapping(value="manageWorkflowList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageWorkflowList(SafetySupervision cond,PageManager page){
		//????????????
		if(StringUtil.isEmpty(cond.getOrderBy())) {
			cond.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = safetySupervisionService.workFlowInstanceQueryByPage(cond, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_; 
	}
	/**
	 * @description ????????????
	 * @return String ???????????????
	 * @throws Exception
	 * @author
	 * @version  2020-07-09
	 */
	@RequestMapping(value="workflowList.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="??????",operateFuncNm="manageForApplyList",operateDescribe="????????????")
	public String workflowList(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/safe/supervision/workflowList.action",request);
		fillPageInfo(model, request);
		return "csmp/safe/supervision/safetySupervision_workflowList";
	}
	/**
	* @description ????????????
	* @return String ???????????????
	* @throws Exception
	* @author
	* @version  2020-07-09 
	*/
	@RequestMapping(value="todoList.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="??????",operateFuncNm="manageForApplyList",operateDescribe="????????????")
	public String todoList(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/safe/supervision/todoList.action",request);
		fillPageInfo(model, request);
		return "csmp/safe/supervision/safetySupervision_todoList";
	}

	/**
	 * @description ????????????
	 * @param cond ?????????
	 * @param PageManager page ???????????????
	 * @return PagingBean ????????????
	 * @throws Exception
	 * @author
	 * @version  2020-07-09 
	 */
	@RequestMapping(value="manageTodoList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageTodoList(SafetySupervision cond,PageManager page){
		//????????????
		if(StringUtil.isEmpty(cond.getOrderBy())) {
			cond.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = null;
		cond.setCurUserId(SystemSecurityUtils.getUser().getId()+"");

		try {
			page_ = safetySupervisionService.workFlowTodoQueryByPage(cond,page);
			GlobalUtil.setTableRowNo(page_, page.getPageRows());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page_;
	}

	/**
	* @description ????????????
	* @return String ???????????????
	* @throws Exception
	* @author
	* @version  2020-07-09 
	*/
	@RequestMapping(value="doneList.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="??????",operateFuncNm="condSearch",operateDescribe="????????????")
	public String doneList(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/safe/supervision/doneList.action",request);
		fillPageInfo(model, request);
		return "csmp/safe/supervision/safetySupervision_doneList";
	}

	/**
	 * @description ????????????
	 * @param cond ?????????
	 * @param PageManager page ???????????????
	 * @return PagingBean ????????????
	 * @throws Exception
	 * @author
	 * @version  2020-07-09 
	 */
	@RequestMapping(value="manageDoneList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageDoneList(SafetySupervision cond,PageManager page, HttpServletRequest request) throws CustomException{
		//????????????
		if(StringUtil.isEmpty(cond.getOrderBy())) {
			cond.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = null;
		cond.setCurUserId(SystemSecurityUtils.getUser().getId()+"");
		try {
			page_ = safetySupervisionService.workFlowDoneQueryByPage(cond,page);
			GlobalUtil.setTableRowNo(page_, page.getPageRows());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page_;
	}

	/**
	* @description ????????????
	* @return String ???????????????
	* @throws Exception
	* @author
	* @version  2020-07-09 
	*/
	@RequestMapping(value="myList.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="??????",operateFuncNm="condMyList",operateDescribe="????????????")
	public String myList(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/safe/supervision/myList.action",request);
		fillPageInfo(model, request);
		return "csmp/safe/supervision/safetySupervision_myList";
	}

	/**
	 * @description ??????????????????
	 * @param cond ?????????
	 * @return PagingBean ????????????
	 * @throws Exception
	 * @author
	 * @version  2020-07-09 
	 */
	@RequestMapping(value="manageMyList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageMyList(SafetySupervision cond,PageManager page, HttpServletRequest request) throws CustomException{
		//????????????
		if(StringUtil.isEmpty(cond.getOrderBy())) {
			cond.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = null;
		cond.setCreateUser(SystemSecurityUtils.getUser().getId());
		try {
			page_ = safetySupervisionService.workFlowInstanceQueryByPage(cond,page);
			GlobalUtil.setTableRowNo(page_, page.getPageRows());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page_;
	}
	/**
	 * ????????????????????????
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="???????????????????????????")
	public SafetySupervision get(SafetySupervision entity) throws Exception{
		return safetySupervisionService.get(entity);
	}


	@RequestMapping(value="search.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="????????????-  ",operateFuncNm="projectPlanMyList",operateDescribe="???????????????-??????????????????????????????????????????")
	public String search(Model model,HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("safe/supervision/search.action",request);
		fillPageInfo(model, request);
		model.addAttribute("contractId", request.getParameter("contractId"));
		return "csmp/contract/pay/cmContractPayList";
	}


	@RequestMapping(value="createAdviceNote.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="????????????-???????????????",operateFuncNm="createAdviceNote",operateDescribe="????????????-??????word?????????")
	@ResponseBody
	public Map<String, Object> createAdviceNote(SafetySupervision cond, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		boolean result = safetySupervisionService.createAdviceNote(cond);
		if (result) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
		}
		return resultMap;
	}

	@RequestMapping(value="downAdviceNote.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="????????????-???????????????",operateFuncNm="createAdviceNote",operateDescribe="????????????-??????word?????????")
	public void downAdviceNote(SafetySupervision cond, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
		safetySupervisionService.downAdviceNote(cond,response,request);
	} catch (CustomException e) {
		logger.error(e);
	}

	}

	@RequestMapping(value="exportExcelAdviceNote.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="????????????-??????excel",operateFuncNm="createAdviceNote",operateDescribe="????????????-??????excel?????????")
	public void exportExcelAdviceNote(SafetySupervision cond, HttpServletResponse response, HttpServletRequest request) throws Exception {
		try {
			safetySupervisionService.exportExcelAdviceNote(cond,response,request);
		} catch (CustomException e) {
			logger.error(e);
		}

	}
}

