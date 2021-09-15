package com.jc.system.security.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.jc.foundation.web.BaseController;
import com.jc.system.common.util.Encodes;
import com.jc.system.util.menuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Subsystem;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.validator.SubsystemValidator;
import com.jc.system.security.service.ISubsystemService;

/**
 * 子系统维护控制器
 * @author Administrator
 * @date 2020-06-29
 */
@Controller
@RequestMapping(value="/sys/subsystem")
public class SubsystemController extends BaseController {

	@Autowired
	private ISubsystemService subsystemService;

	@org.springframework.web.bind.annotation.InitBinder("subsystem")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new SubsystemValidator());
	}

	public SubsystemController() {
	}

	/**
	 * 保存方法
	 * @param subsystem
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="子系统维护表",operateFuncNm="save",operateDescribe="对子系统维护表进行新增操作")
	public Map<String,Object> save(@Valid Subsystem subsystem,BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			subsystem.setPermission(subsystem.getPermission().toLowerCase());
			subsystemService.save(subsystem);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	/**
	 * 修改方法
	 * @param subsystem
	 * @param result
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="子系统维护表",operateFuncNm="update",operateDescribe="对子系统维护表进行更新操作")
	public Map<String, Object> update(Subsystem subsystem, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		subsystem.setPermission(subsystem.getPermission().toLowerCase());
		Integer flag = subsystemService.update(subsystem);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param subsystem
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="子系统维护表",operateFuncNm="get",operateDescribe="对子系统维护表进行单条查询操作")
	public Subsystem get(Subsystem subsystem,HttpServletRequest request) throws Exception{
		return subsystemService.get(subsystem);
	}

	/**
	 * 弹出对话框方法
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/subsystem/module/subsystemForm";
	}

	/**
	 * 分页查询方法
	 * @param subsystem
	 * @param page
	 * @return
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(Subsystem subsystem,PageManager page){
		if(StringUtil.isEmpty(subsystem.getOrderBy())) {
			subsystem.addOrderByField("t.QUEUE");
		}
		User user = SystemSecurityUtils.getUser();
		if(user.getIsSystemAdmin() || user.getIsSystemManager()) {
            subsystem.setCurLoginUserId("-99");
        } else {
            subsystem.setCurLoginUserId(user.getId());
        }
		PageManager rePage = subsystemService.query(subsystem, page);
		return rePage;
	}

	/**
	 * 跳转方法
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="子系统维护表",operateFuncNm="manage",operateDescribe="对子系统维护表进行访问")
	public String manage(HttpServletRequest request) throws Exception{
		User userInfo = SystemSecurityUtils.getUser();
		if (userInfo != null) {
			if (userInfo.getIsSystemManager() || userInfo.getIsSystemAdmin()) {
				menuUtil.saveMenuID("/sys/subsystem/manage.action",request);
				return "sys/subsystem/subsystemList";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	/**
	 * 跳转方法
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="application.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="子系统维护表",operateFuncNm="manage",operateDescribe="对子系统维护表进行跳转操作")
	public String application(HttpServletRequest request) throws Exception{
		User userInfo = SystemSecurityUtils.getUser();
		if (userInfo != null) {
			if (userInfo.getIsSystemManager() || userInfo.getIsSystemAdmin()) {
				menuUtil.saveMenuID("/sys/subsystem/application.action",request);
				return "sys/subsystem/subsystemList";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	/**
	 * 删除方法
	 * @param subsystem
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="子系统维护表",operateFuncNm="deleteByIds",operateDescribe="对子系统维护表进行删除")
	public  Map<String, Object> deleteByIds(Subsystem subsystem,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		subsystem.setPrimaryKeys(ids.split(","));
		if(subsystemService.deleteByIds(subsystem) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

}