package com.jc.system.security.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.system.security.domain.Shortcut;
import com.jc.system.security.domain.validator.ShortcutValidator;
import com.jc.system.security.service.IShortcutService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/shortcut")
public class ShortcutController extends BaseController {
	
	@Autowired
	private IShortcutService shortcutService;
	
	@org.springframework.web.bind.annotation.InitBinder("shortcut")
    public void initBinder(WebDataBinder binder) {  
		binder.setValidator(new ShortcutValidator());
    }
	
	public ShortcutController() {
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="快捷方式表",operateFuncNm="save",operateDescribe="对快捷方式表进行新增操作")
	public Map<String,Object> save(@Valid Shortcut shortcut,BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			shortcutService.save(shortcut);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="快捷方式表",operateFuncNm="update",operateDescribe="对快捷方式表进行更新操作")
	public Map<String, Object> update(Shortcut shortcut, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = shortcutService.update(shortcut);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="快捷方式表",operateFuncNm="get",operateDescribe="对快捷方式表进行单条查询操作")
	public Shortcut get(Shortcut shortcut,HttpServletRequest request) throws Exception{
		return shortcutService.get(shortcut);
	}

	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/shortcut/shortcutEdit"; 
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(Shortcut shortcut,PageManager page){
		//默认排序
		if(StringUtil.isEmpty(shortcut.getOrderBy())) {
			shortcut.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = shortcutService.query(shortcut, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="快捷方式表",operateFuncNm="manage",operateDescribe="对快捷方式表进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/shortcut/shortcutList"; 
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="快捷方式表",operateFuncNm="deleteByIds",operateDescribe="对快捷方式表进行删除")
	public  Map<String, Object> deleteByIds(Shortcut shortcut,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		shortcut.setPrimaryKeys(ids.split(","));	
		if(shortcutService.deleteByIds(shortcut) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}
	
	/**
	 * @description 获取用户快捷方式选择范围
	 * @return String 对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2015-11-17
	 */
	@RequestMapping(value="getShortcutUserList.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="快捷方式表",operateFuncNm="getShortcutUserList",operateDescribe="对快捷方式表进行获取用户快捷方式选择范围操作")
	public String getShortcutUserList(Model model,HttpServletRequest request) throws Exception{
		Map<String,Object> portletMap = shortcutService.getShortcutUserList();
		model.mergeAttributes(portletMap);
		
		return "sys/shortcut/shortcutUserSet";
	}

}