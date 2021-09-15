package com.jc.system.security.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Logo;
import com.jc.system.security.domain.validator.LogoValidator;
import com.jc.system.security.service.ILogoService;
import com.jc.system.util.menuUtil;
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
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/logo")
public class LogoController extends BaseController {
	
	@Autowired
	private ILogoService logoService;
	
	@org.springframework.web.bind.annotation.InitBinder("logo")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new LogoValidator()); 

    }
	
	public LogoController() {
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="系统登录页样式表",operateFuncNm="save",operateDescribe="对系统登录页样式表进行新增操作")
	public Map<String,Object> save(@Valid Logo logo,BindingResult result, HttpServletRequest request) throws Exception{
		
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// 验证token
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		if(!"false".equals(resultMap.get("success"))){
			logoService.save(logo);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="系统登录页样式表",operateFuncNm="update",operateDescribe="对系统登录页样式表进行更新操作")
	public Map<String, Object> update(Logo logo, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = logoService.update(logo);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			String token = getToken(request);
			resultMap.put(GlobalContext.SESSION_TOKEN, token);
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		return resultMap;
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="系统登录页样式表",operateFuncNm="get",operateDescribe="对系统登录页样式表进行单条查询操作")
	public Logo get(Logo logo,HttpServletRequest request) throws Exception{
		return logoService.get(logo);
	}

	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/logo/logoform";
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(Logo logo,PageManager page){
		//默认排序
		if(StringUtil.isEmpty(logo.getOrderBy())) {
			logo.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = logoService.query(logo, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="系统登录页样式表",operateFuncNm="manage",operateDescribe="对系统登录页样式表进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/sys/logo/manage.action",request);
		return "sys/logo/logoList";
	}

	@RequestMapping(value="manageSet.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="系统登录页样式表",operateFuncNm="manage",operateDescribe="对系统登录页样式表进行跳转操作")
	public String manageSet(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Logo logo = new Logo();
		List<Logo> logoList = logoService.queryAll(logo);
		resultMap.put("logoList",logoList);
		resultMap.put("logoJson", JsonUtil.java2Json(logoList));
		model.mergeAttributes(resultMap);
		return "sys/logo/logoSet";
	}

	@RequestMapping(value="updateSet.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="系统登录页样式表",operateFuncNm="updateSet",operateDescribe="对系统登录页样式表进行设置操作")
	public Map<String, Object> updateSet(Logo logo,String id,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<>();
		Integer flag = logoService.updateSet(id);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		return resultMap;
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="系统登录页样式表",operateFuncNm="deleteByIds",operateDescribe="对系统登录页样式表进行删除")
	public  Map<String, Object> deleteByIds(Logo logo,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		logo.setPrimaryKeys(ids.split(","));
		if(logoService.deleteByIds(logo) == -1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "登录页样式使用中不允许删除");
		} else if(logoService.deleteByIds(logo) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		}else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

}