package com.jc.csmp.project.plan.web;

import com.jc.csmp.project.domain.CmProjectInfo;
import com.jc.csmp.project.plan.domain.CmProjectPlanStage;
import com.jc.csmp.project.plan.domain.validator.CmProjectPlanStageValidator;
import com.jc.csmp.project.plan.service.ICmProjectPlanStageService;
import com.jc.csmp.project.service.ICmProjectInfoService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.*;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Department;
import net.bytebuddy.asm.Advice;
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
 * 建设管理-项目计划阶段管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/plan/stage")
public class CmProjectPlanStageController extends BaseController{

	@Autowired
	private ICmProjectPlanStageService cmProjectPlanStageService;
	@Autowired
	private ICmProjectInfoService cmProjectInfoService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectPlanStage")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectPlanStageValidator());
    }

	public CmProjectPlanStageController() {
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
	@ActionLog(operateModelNm="",operateFuncNm="save",operateDescribe="对进行新增操作")
	public Map<String,Object> save(@Valid CmProjectPlanStage entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(cmProjectPlanStageService.saveEntity(entity), resultMap, getToken(request));
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
	@ActionLog(operateModelNm="",operateFuncNm="update",operateDescribe="对进行更新操作")
	public Map<String, Object> update(CmProjectPlanStage entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmProjectPlanStageService.updateEntity(entity), resultMap, getToken(request));
		return resultMap;
	}

	@RequestMapping(value="move.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="update",operateDescribe="对进行更新操作")
	public Map<String, Object> move(HttpServletRequest request) throws Exception{
		String parentId = request.getParameter("parentId");
		String id = request.getParameter("id");
		Map<String, Object> resultMap = new HashMap<>(2);
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(parentId)) {
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "参数异常，刷新后再试!");
		}
		if (parentId.equals(id)) {
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "非法参数");
		}
		CmProjectPlanStage entity = new CmProjectPlanStage();
		entity.setParentId(parentId);
		entity.setId(id);
		GlobalUtil.resultToMap(cmProjectPlanStageService.updateEntity(entity), resultMap, getToken(request));
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
	@ActionLog(operateModelNm="",operateFuncNm="get",operateDescribe="对进行单条查询操作")
	public CmProjectPlanStage get(CmProjectPlanStage entity) throws Exception{
		return cmProjectPlanStageService.get(entity);
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
		return "csmp/project/plan/stage/cmProjectPlanStageForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmProjectPlanStage entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = cmProjectPlanStageService.query(entity, page);
		return page_;
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
	@ActionLog(operateModelNm="",operateFuncNm="deleteByIds",operateDescribe="对进行删除")
	public  Map<String, Object> deleteByIds(CmProjectPlanStage entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(cmProjectPlanStageService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	@RequestMapping(value="treeList.action")
	@ResponseBody
	public List<Department> treeList(HttpServletRequest request) throws CustomException {
		CmProjectPlanStage param = new CmProjectPlanStage();
		String projectId = request.getParameter("projectId");
		projectId = StringUtil.isEmpty(projectId) ? "-1" : projectId;
		String rootName = "阶段名称";
		if (!projectId.equals("-1")) {
			CmProjectInfo project = this.cmProjectInfoService.getById(projectId);
			if (project != null) {
				rootName = project.getProjectName();
			}
		}
		param.setProjectId(projectId);
		param.addOrderByField(" queue ");
		List<CmProjectPlanStage> dataList = this.cmProjectPlanStageService.queryAll(param);
		List<Department> resultList = new ArrayList<>();
		resultList.add(Department.createTreeData("0", "-1", rootName));
		if (dataList != null) {
			for (CmProjectPlanStage entity : dataList) {
				resultList.add(Department.createTreeData(entity.getId(), entity.getParentId(), entity.getStageName()));
			}
		}
		return resultList;
	}

	@RequestMapping(value="checkPlan.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="项目计划管理",operateFuncNm="deleteByIds",operateDescribe="验证是否设置了计划")
	public Result checkPlan(String projectId) throws Exception{
		CmProjectPlanStage entity = new CmProjectPlanStage();
		entity.setProjectId(projectId);
		List<CmProjectPlanStage> planList = this.cmProjectPlanStageService.queryAll(entity);
		if (planList == null || planList.size() == 0) {
			return Result.failure(1, "未设置计划");
		}
		return Result.success();
	}
}
