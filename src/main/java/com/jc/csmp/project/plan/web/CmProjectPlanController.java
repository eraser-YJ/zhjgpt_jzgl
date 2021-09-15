package com.jc.csmp.project.plan.web;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.plan.domain.CmProjectPlan;
import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.domain.validator.CmProjectPlanValidator;
import com.jc.csmp.project.plan.service.ICmProjectPlanService;
import com.jc.csmp.project.plan.service.ICmProjectPlanStageService;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.mobile.log.enums.MobileReportLogEnum;
import com.jc.mobile.log.service.IMobileReportLogService;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Department;
import com.jc.system.security.util.DeptCacheUtil;
import com.jc.system.security.util.UserUtils;
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
import java.util.*;

/**
 * 建设管理-项目计划管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/plan/info")
public class CmProjectPlanController extends BaseController{

	@Autowired
	private ICmProjectPlanService projectPlanService;
	@Autowired
	private ICmProjectPlanStageService projectPlanStageService;
	@Autowired
	private ICmProjectInfoService cmProjectInfoService;
	@Autowired
	private IMobileReportLogService mobileReportLogService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectPlan")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectPlanValidator());
	}

	public CmProjectPlanController() {
	}

	/**
	 * 保存方法
	 * @param entity
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目计划管理",operateFuncNm="save",operateDescribe="新增操作")
	public Map<String,Object> save(@Valid CmProjectPlan entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(projectPlanService.saveEntity(entity), resultMap, getToken(request));
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
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目计划管理",operateFuncNm="update",operateDescribe="更新操作")
	public Map<String, Object> update(CmProjectPlan entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(projectPlanService.updateEntity(entity), resultMap, getToken(request));
		return resultMap;
	}

	@RequestMapping(value = "modifyProgress.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> modifyProgress(CmProjectPlan entity, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>(3);
		Result res = projectPlanService.modifyProgress(entity.getId(), entity.getCompletionRatio(), entity.getCompletionMoney(),
		entity.getActualStartDate(), entity.getActualEndDate());

		//记录移动端所需的上报日志
		mobileReportLogService.saveLog(MobileReportLogEnum.progress, res.getData(), SystemSecurityUtils.getUser());
		GlobalUtil.resultToMap(res, resultMap, getToken(request));
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public CmProjectPlan get(CmProjectPlan entity) throws Exception{
		return projectPlanService.get(entity);
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
		return "csmp/project/plan/info/cmProjectPlanInfoForm";
	}

	@RequestMapping(value="loadLook.action",method=RequestMethod.GET)
	public String loadLook() {
		return "csmp/project/plan/info/cmProjectPlanInfoLook";
	}

	/**
	 * @description 弹出进度对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2020-04-10
	 */
	@RequestMapping(value="loadProgressForm.action",method=RequestMethod.GET)
	public String loadProgressForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "csmp/project/plan/info/cmProjectPlanProgressForm";
	}
	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmProjectPlan entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByField(" stage.queue, t.queue ");
		}
		entity.setStageIds(projectPlanStageService.getChildIdById(entity.getStageId(), entity.getProjectId()));
		entity.setStageId(null);
		PageManager page_ = projectPlanService.query(entity, page);
		GlobalUtil.setTableRowNo(page_, page.getPageRows());
		return page_;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage(HttpServletRequest request, String projectId) throws Exception{
		request.setAttribute("projectId", projectId);
		return "csmp/project/plan/info/cmProjectPlanInfoList";
	}

	@RequestMapping(value="project.action",method=RequestMethod.GET)
	public String project() {
		return "csmp/project/plan/info/planProjectInfo";
	}

    @RequestMapping(value="progress.action",method=RequestMethod.GET)
    public String progress() {
        return "csmp/project/plan/info/projectPlanProgressList";
    }
	/**
	 * 删除方法
	 * @param entity
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目计划管理",operateFuncNm="deleteByIds",operateDescribe="删除操作")
	public  Map<String, Object> deleteByIds(CmProjectPlan entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(projectPlanService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	@RequestMapping(value="checkPlan.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目计划管理",operateFuncNm="deleteByIds",operateDescribe="验证是否设置了计划")
	public Result checkPlan(String projectId) throws Exception{
		CmProjectPlan entity = new CmProjectPlan();
		entity.setProjectId(projectId);
		List<CmProjectPlan> planList = this.projectPlanService.queryAll(entity);
		if (planList == null || planList.size() == 0) {
			return Result.failure(1, "未设置计划");
		}
		return Result.success();
	}

	@RequestMapping(value="chooseTemplate.action")
	@ResponseBody
	@ActionLog(operateModelNm="项目计划管理",operateFuncNm="deleteByIds",operateDescribe="选择模板默认保存")
	public Result chooseTemplate(String projectId, String templateId) throws Exception {
		return this.projectPlanService.saveEntityByTemplateId(projectId, templateId);
	}

	/**
	 * 根据条件获取项目计划
	 * @param projectId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="projectPlanProgress.action")
	@ResponseBody
	public List<CmProjectPlan> projectPlanProgress(String projectId) throws Exception {
		CmProjectPlan param = new CmProjectPlan();
		param.setProjectId(projectId);
		return this.projectPlanService.queryAll(param);
	}

	/**
	 * 获取项目及阶段树
	 * @return
	 */
	@RequestMapping(value = "projectStageTree.action")
	@ResponseBody
	public List<Department> projectStageTree() throws Exception {
		CmProjectInfo project = new CmProjectInfo();
		project.setDeptCodeCondition(DeptCacheUtil.getCodeById(SystemSecurityUtils.getUser().getDeptId()));
		project.addOrderByFieldDesc(" t.create_date ");
		List<CmProjectInfo> projectList = this.cmProjectInfoService.queryAll(project);
		List<Department> treeList = new ArrayList<>();
		treeList.add(Department.createTreeData("0", "-1", "项目名称"));
		if (projectList != null) {
			for (CmProjectInfo p : projectList) {
				treeList.add(Department.createTreeData("project_id|" + p.getId(), "0", p.getProjectName()));
				CmProjectPlanStage stageParam = new CmProjectPlanStage();
				stageParam.setProjectId(p.getId());
				stageParam.addOrderByField(" t.queue ");
				List<CmProjectPlanStage> stageList = this.projectPlanStageService.queryAll(stageParam);
				if (stageList != null) {
					for (CmProjectPlanStage stage : stageList) {
						treeList.add(Department.createTreeData("stage|" + p.getId() + "|" + stage.getId(), "project_id|" + p.getId(), stage.getStageName()));
					}
				}
			}
		}
		return treeList;
	}

	/**
	 * 根据项目获取所有计划
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "getPlanByProjectId.action",method=RequestMethod.GET)
	@ResponseBody
	public Result getPlanByProjectId(String projectId) {
		if (StringUtil.isEmpty(projectId)) {
			return Result.failure(ResultCode.PARAM_IS_BLANK);
		}
		CmProjectPlan entity = new CmProjectPlan();
		entity.setProjectId(projectId);
		entity.addOrderByField(" stage.queue, t.queue ");
		try {
			List<CmProjectPlan> dbList = this.projectPlanService.queryAll(entity);
			if (dbList == null) {
				dbList = Collections.emptyList();
			}
			return Result.success(dbList);
		} catch (CustomException e) {
			e.printStackTrace();
		}
		return Result.failure(ResultCode.INTERFACE_INNER_INVOKE_ERROR);
	}
}

