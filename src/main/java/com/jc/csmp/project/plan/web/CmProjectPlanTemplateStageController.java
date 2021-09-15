package com.jc.csmp.project.plan.web;

import com.jc.csmp.project.plan.domain.CmProjectPlanTemplateStage;
import com.jc.csmp.project.plan.domain.validator.CmProjectPlanTemplateStageValidator;
import com.jc.csmp.project.plan.service.ICmProjectPlanTemplateStageService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Department;
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
 * 建设管理-项目计划模板阶段管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/plan/templatestage")
public class CmProjectPlanTemplateStageController extends BaseController{

	@Autowired
	private ICmProjectPlanTemplateStageService cmProjectPlanTemplateStageService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectPlanTemplateStage")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectPlanTemplateStageValidator());
    }

	public CmProjectPlanTemplateStageController() {
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
	public Map<String,Object> save(@Valid CmProjectPlanTemplateStage entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			GlobalUtil.resultToMap(cmProjectPlanTemplateStageService.saveEntity(entity), resultMap, getToken(request));
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
	public Map<String, Object> update(CmProjectPlanTemplateStage entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmProjectPlanTemplateStageService.updateEntity(entity), resultMap, getToken(request));
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
		CmProjectPlanTemplateStage entity = new CmProjectPlanTemplateStage();
		entity.setParentId(parentId);
		entity.setId(id);
		GlobalUtil.resultToMap(cmProjectPlanTemplateStageService.updateEntity(entity), resultMap, getToken(request));
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
	public CmProjectPlanTemplateStage get(CmProjectPlanTemplateStage entity) throws Exception{
		return cmProjectPlanTemplateStageService.get(entity);
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
		return "csmp/project/plan/template/cmProjectPlanTemplateStageForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmProjectPlanTemplateStage entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = cmProjectPlanTemplateStageService.query(entity, page);
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
	public  Map<String, Object> deleteByIds(CmProjectPlanTemplateStage entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(cmProjectPlanTemplateStageService.deleteByIds(entity) != 0){
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
		CmProjectPlanTemplateStage param = new CmProjectPlanTemplateStage();
		String templateId = request.getParameter("templateId");
		templateId = StringUtil.isEmpty(templateId) ? "-1" : templateId;
		param.setTemplateId(templateId);
		param.addOrderByField(" queue ");
		List<CmProjectPlanTemplateStage> dataList = this.cmProjectPlanTemplateStageService.queryAll(param);
		List<Department> resultList = new ArrayList<>();
		resultList.add(Department.createTreeData("0", "-1", "阶段名称"));
		if (dataList != null) {
			for (CmProjectPlanTemplateStage entity : dataList) {
				resultList.add(Department.createTreeData(entity.getId(), entity.getParentId(), entity.getStageName()));
			}
		}
		return resultList;
	}
}
