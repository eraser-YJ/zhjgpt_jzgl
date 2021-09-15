package com.jc.system.security.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.jc.system.security.SystemSecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.DateUtils;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Setting;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.validator.SettingValidator;
import com.jc.system.security.service.ISettingService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/setting")
public class SettingController extends BaseController{
	
	@Autowired
	private ISettingService settingService;

	@org.springframework.web.bind.annotation.InitBinder("setting")
    public void initBinder(WebDataBinder binder) {  
		binder.setValidator(new SettingValidator());
    }
	
	public SettingController() {
	}
	
	private SettingValidator sv = new SettingValidator();

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(Setting setting,PageManager page){
		PageManager rePage = settingService.query(setting, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage(Model model) throws Exception{
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			if(userInfo.getIsAdmin() == 1 || userInfo.getIsSystemAdmin()){
				Setting setting = new Setting();
				model.addAttribute("settingList",settingService.queryAll(setting));
				return "sys/setting/setting";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer deleteByIds(Setting setting,String ids) throws Exception{
		setting.setPrimaryKeys(ids.split(","));
		return settingService.delete(setting);
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="参数设置",operateFuncNm="save",operateDescribe="对参数设置进行添加") 
	public Map<String,Object> save(@Valid Setting setting,BindingResult result,HttpServletRequest request) throws Exception {
		sv.validate(setting, result);
		Map<String,Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			setting.setCreateDate(DateUtils.getSysDate());
			setting.setModifyDate(DateUtils.getSysDate());
			settingService.save(setting);
			resultMap.put("success", "true");
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="参数设置",operateFuncNm="update",operateDescribe="对参数设置进行修改") 
	public Map<String, Object> update(@RequestBody Map<String, Object> mapData,BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		settingService.updateByMap(mapData);
		resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public Setting get(Setting setting) throws Exception{
		return settingService.get(setting);
	}

	@RequestMapping(value="getOne.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getOne(Setting setting, HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put("settingvos", settingService.getSettings(setting));
		map.put(GlobalContext.SESSION_TOKEN, token);
		return map;
	}

}