package com.jc.system.security.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.UserRecycle;
import com.jc.system.security.domain.validator.UserRecycleValidator;
import com.jc.system.security.service.IUserRecycleService;
import com.jc.system.security.service.IUserService;
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
import java.util.Map;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/userRecycle")
public class UserRecycleController extends BaseController {

	@Autowired
	private IUserRecycleService userRecycleService;
	@Autowired
	private IUserService userService;
	
	@org.springframework.web.bind.annotation.InitBinder("userRecycle")
    public void initBinder(WebDataBinder binder) {  
		binder.setValidator(new UserRecycleValidator());
    }
	
	public UserRecycleController() {
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="回收站",operateFuncNm="save",operateDescribe="对回收站进行新增操作")
	public Map<String,Object> save(@Valid UserRecycle userRecycle, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if(!"false".equals(resultMap.get("success"))){
			userRecycleService.save(userRecycle);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="回收站",operateFuncNm="update",operateDescribe="对回收站进行更新操作")
	public Map<String, Object> update(UserRecycle userRecycle, BindingResult result, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		Integer flag = userRecycleService.update(userRecycle);
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
	@ActionLog(operateModelNm="回收站",operateFuncNm="get",operateDescribe="对回收站进行单条查询操作")
	public UserRecycle get(UserRecycle userRecycle,HttpServletRequest request) throws Exception{
		return userRecycleService.get(userRecycle);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2018-03-26
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<>(1);
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/userRecycle/module/userRecycleForm"; 
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="回收站",operateFuncNm="manageList",operateDescribe="对回收站进行查询操作")
	public PageManager manageList(UserRecycle userRecycle,PageManager page,HttpServletRequest request){
		if(StringUtil.isEmpty(userRecycle.getOrderBy())) {
			userRecycle.addOrderByFieldDesc("t.CREATE_DATE");
		}
		userRecycle.setDeleteFlag(1);
		PageManager rePage = userRecycleService.query(userRecycle, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="回收站",operateFuncNm="manage",operateDescribe="对回收站进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/userRecycle/userRecycleList"; 
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="回收站",operateFuncNm="deleteByIds",operateDescribe="对回收站进行清理")
	public  Map<String, Object> deleteByIds(UserRecycle userRecycle,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<>(2);
		userRecycle.setPrimaryKeys(ids.split(","));	
		if(userRecycleService.deleteByIds(userRecycle) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	/**
	 * 恢复删除用户方法
	 * @param userRecycle
	 * @param user
	 * @param ids
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "deleteBackByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="回收站",operateFuncNm="deleteBackByIds",operateDescribe="对回收站用户进行数据恢复")
	public Map<String, Object> deleteBackByIds(UserRecycle userRecycle,User user, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<>(1);
		if(ids.length() > 0){
			user.setPrimaryKeys(ids.split(","));
			user.setStatus("status_0");
			if(userService.deleteBack(user) >= 1){
				userRecycle.setPrimaryKeys(ids.split(","));
				userRecycleService.deleteByIds(userRecycle);
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, "用户恢复成功");
				return resultMap;
			}
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "用户恢复失败");
			return resultMap;
		}
		return null;
	}
}