package com.jc.csmp.project.plan.web;

import com.jc.csmp.project.plan.domain.CmProjectPlanTemplate;
import com.jc.csmp.project.plan.domain.validator.CmProjectPlanTemplateValidator;
import com.jc.csmp.project.plan.service.ICmProjectPlanTemplateService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.GlobalUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.dic.IDicManager;
import com.jc.system.security.SystemSecurityUtils;
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
 * 建设管理-项目计划模板管理Controller
 * @Author 常鹏
 * @Date 2020/7/6 14:29
 * @Version 1.0
 */
@Controller
@RequestMapping(value="/project/plan/template")
public class CmProjectPlanTemplateController extends BaseController{

	@Autowired
	private ICmProjectPlanTemplateService cmProjectPlanTemplateService;

	@org.springframework.web.bind.annotation.InitBinder("cmProjectPlanTemplate")
    public void initBinder(WebDataBinder binder) {
		binder.setValidator(new CmProjectPlanTemplateValidator());
    }

	public CmProjectPlanTemplateController() {
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
	public Map<String,Object> save(@Valid CmProjectPlanTemplate entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			entity.setDeptId(SystemSecurityUtils.getUser().getDeptId());
			GlobalUtil.resultToMap(cmProjectPlanTemplateService.saveEntity(entity), resultMap, getToken(request));
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
	public Map<String, Object> update(CmProjectPlanTemplate entity, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		GlobalUtil.resultToMap(cmProjectPlanTemplateService.updateEntity(entity), resultMap, getToken(request));
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
	public CmProjectPlanTemplate get(CmProjectPlanTemplate entity) throws Exception{
		return cmProjectPlanTemplateService.get(entity);
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
		return "csmp/project/plan/template/cmProjectPlanTemplateForm";
	}

	/**
	 * 分页查询方法
	 * @param entity
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(CmProjectPlanTemplate entity, PageManager page){
		if(StringUtil.isEmpty(entity.getOrderBy())) {
			entity.addOrderByFieldDesc("t.CREATE_DATE");
		}
		entity.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		PageManager page_ = cmProjectPlanTemplateService.query(entity, page);
		return page_;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2020-04-10
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String manage() throws Exception{
		return "csmp/project/plan/template/cmProjectPlanTemplateList";
	}

	@RequestMapping(value="choose.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String choose(HttpServletRequest request, String projectId) throws Exception{
		request.setAttribute("projectId", projectId);
		CmProjectPlanTemplate template = new CmProjectPlanTemplate();
		template.setDeptId(SystemSecurityUtils.getUser().getDeptId());
		List<CmProjectPlanTemplate> templateList = this.cmProjectPlanTemplateService.queryAll(template);
		if (templateList == null) {
			templateList = new ArrayList<>();
		}
		request.setAttribute("templateList", templateList);
		return "csmp/project/plan/template/cmProjectPlanChooseTemplate";
	}

	@RequestMapping(value="templateContent.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="",operateFuncNm="manage",operateDescribe="对进行跳转操作")
	public String templateContent(HttpServletRequest request) throws Exception{
		String templateId = request.getParameter("templateId");
		request.setAttribute("templateId", templateId);
		CmProjectPlanTemplate template = this.cmProjectPlanTemplateService.getById(templateId);
		if (template != null) {
			request.setAttribute("templateName", template.getTemplateName());
		}
		return "csmp/project/plan/template/cmProjectPlanTemplateContentList";
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
	public  Map<String, Object> deleteByIds(CmProjectPlanTemplate entity, String ids) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		entity.setPrimaryKeys(ids.split(","));
		if(cmProjectPlanTemplateService.deleteByIds(entity) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}
}
