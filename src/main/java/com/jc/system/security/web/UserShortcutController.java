package com.jc.system.security.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
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
import com.jc.system.security.domain.UserShortcut;
import com.jc.system.security.domain.validator.UserShortcutValidator;
import com.jc.system.security.service.IUserShortcutService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/userShortcut")
public class UserShortcutController extends BaseController {
	
	@Autowired
	private IUserShortcutService userShortcutService;

	@org.springframework.web.bind.annotation.InitBinder("userShortcut")
    public void initBinder(WebDataBinder binder) {  
		binder.setValidator(new UserShortcutValidator());
    }
	
	public UserShortcutController() {
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="快捷方式与用户关联表",operateFuncNm="save",operateDescribe="对快捷方式与用户关联表进行新增操作")
	public Map<String,Object> save(String shortcutids,String sequence, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = userShortcutService.saveorupdate(shortcutids,sequence);
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="快捷方式与用户关联表",operateFuncNm="update",operateDescribe="对快捷方式与用户关联表进行更新操作")
	public Map<String, Object> update(UserShortcut userShortcut, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = userShortcutService.update(userShortcut);
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
	@ActionLog(operateModelNm="快捷方式与用户关联表",operateFuncNm="get",operateDescribe="对快捷方式与用户关联表进行单条查询操作")
	public UserShortcut get(UserShortcut userShortcut,HttpServletRequest request) throws Exception{
		return userShortcutService.get(userShortcut);
	}

	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/userShortcut/module/userShortcutForm"; 
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(UserShortcut userShortcut,PageManager page){
		//默认排序
		if(StringUtil.isEmpty(userShortcut.getOrderBy())) {
			userShortcut.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = userShortcutService.query(userShortcut, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="快捷方式与用户关联表",operateFuncNm="manage",operateDescribe="对快捷方式与用户关联表进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/userShortcut/userShortcutList"; 
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="快捷方式与用户关联表",operateFuncNm="deleteByIds",operateDescribe="对快捷方式与用户关联表进行删除")
	public  Map<String, Object> deleteByIds(UserShortcut userShortcut,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		userShortcut.setPrimaryKeys(ids.split(","));	
		if(userShortcutService.deleteByIds(userShortcut) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	@RequestMapping(value="getShortcutListByUserId.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="快捷方式与用户关联表",operateFuncNm="get",operateDescribe="对快捷方式与用户关联表进行单条查询操作")
	public List<Shortcut> getShortcutListByUserId(UserShortcut userShortcut,HttpServletRequest request) throws Exception{
		String userid = SystemSecurityUtils.getUser().getId();
		return userShortcutService.getShortcutListByUserId(userid);
	}

}