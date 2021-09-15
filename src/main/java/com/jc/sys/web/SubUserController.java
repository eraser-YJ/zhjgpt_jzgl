package com.jc.sys.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.sys.domain.SubUser;
import com.jc.sys.domain.validator.SubUserValidator;
import com.jc.sys.service.ISubUserService;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.UserUtils;
import org.apache.log4j.Logger;
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
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Controller
@RequestMapping(value="/sys/subUser")
public class SubUserController extends BaseController{

	private static Logger logger = Logger.getLogger(SubUserController.class);
	
	@Autowired
	private ISubUserService subUserService;
	
	@org.springframework.web.bind.annotation.InitBinder("subUser")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new SubUserValidator()); 

    }
	
	public SubUserController() {
	}

	/**
	 * @description 保存方法
	 * @param  subUser 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户表（公共）",operateFuncNm="save",operateDescribe="对用户表（公共）进行新增操作")
	public Map<String,Object> save(@Valid SubUser subUser,BindingResult result,
			HttpServletRequest request) throws Exception{
		
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
			User userInfo = null;
			if (subUser.getDisplayName() != null){
				userInfo = UserUtils.getNewUser(subUser.getDisplayName());
				if (userInfo != null){
					subUser.setCode(userInfo.getCode());
				} else {
					resultMap.put("errorMessage", "此用户已不存在");
					resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
					return resultMap;
				}
			}
			SubUser validUser = new SubUser();
			validUser.setCode(subUser.getCode());
			validUser.setDeptId(subUser.getDeptId());
			validUser = subUserService.get(validUser);
			if (validUser != null){
				resultMap.put("errorMessage", "部门已存在此用户");
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
				return resultMap;
			}

			//User userInfo = UserUtils.getNewUserForCode(subUser.getCode());;// 获取登录用户信息
			if (userInfo != null){
				subUser.setCode(userInfo.getCode());
				subUser.setLoginName(userInfo.getLoginName());
				subUser.setDisplayName(userInfo.getDisplayName());
				subUser.setSex(userInfo.getSex());
			} else {
				resultMap.put("errorMessage", "此用户已不存在");
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
				return resultMap;
			}

			subUserService.save(subUser);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	/**
	* workflowParamTemp修改方法
	* @param  subUser 实体类
	* @param  result 校验结果
	* @return Integer 更新的数目
	* @author
	* @version  2018-04-04 
	*/
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户表（公共）",operateFuncNm="update",operateDescribe="对用户表（公共）进行更新操作")
	public Map<String, Object> update(SubUser subUser, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		User userInfo = UserUtils.getNewUserForCode(subUser.getCode());// 获取登录用户信息
		if (userInfo != null){
			subUser.setDisplayName(userInfo.getDisplayName());
			subUser.setLoginName(userInfo.getLoginName());
		} else {
			resultMap.put("errorMessage", "此用户已不存在");
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			return resultMap;
		}
		Integer flag = subUserService.update(subUser);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * 排序修改方法
	 * @param  subUser 实体类
	 * @param  result 校验结果
	 * @return Integer 更新的数目
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="changeOrder.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户表（公共）",operateFuncNm="changeOrder",operateDescribe="对用户表（公共）进行排序修改操作")
	public Map<String, Object> changeOrder(SubUser subUser, BindingResult result,
									  HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		User userInfo = SystemSecurityUtils.getUser();
		SubUser newUser = new SubUser();
		newUser.setCode(userInfo.getCode());
		newUser.setLoginName(userInfo.getLoginName());
		newUser.setDeptId(subUser.getDeptId());
		newUser = subUserService.get(newUser);
		newUser.setTheme("sel");
		subUserService.reTheme(newUser);
		Integer flag = subUserService.update(newUser);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		}
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param  subUser 实体类
	 * @return SubUser 查询结果
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="用户表（公共）",operateFuncNm="get",operateDescribe="对用户表（公共）进行单条查询操作")
	public SubUser get(SubUser subUser,HttpServletRequest request) throws Exception{
		return subUserService.get(subUser);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/subUser/module/subUserForm"; 
	}

	/**
	 * @description 分页查询方法
	 * @param  subUser 实体类
	 * @param  page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author
	 * @version  2018-04-04 
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(SubUser subUser,PageManager page,
								  HttpServletRequest request){
		//默认排序
		if(StringUtil.isEmpty(subUser.getOrderBy())) {
			subUser.addOrderByFieldDesc("t.ORDER_NO");
		}
		PageManager rePage = null;
		try{
			rePage = subUserService.query(subUser, page);
		}catch (Exception e){
			logger.error(e.getMessage());
		}
		return rePage;
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2018-04-04 
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="用户表（公共）",operateFuncNm="manage",operateDescribe="对用户表（公共）进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/subUser/subUserList"; 
	}

/**
	 * @description 删除方法
	 * @param  subUser 实体类
	 * @param  subUser 实体类
	 * @param  ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2018-04-04
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="用户表（公共）",operateFuncNm="deleteByIds",operateDescribe="对用户表（公共）进行删除")
	public  Map<String, Object> deleteByIds(SubUser subUser,String ids,String deptId, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		subUser.setPrimaryKeys(ids.split(","));
		if (deptId != null && !"".equals(deptId)){
			subUser.setDeptId(deptId);
			if(subUserService.deleteByIds(subUser) != 0){
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
			}
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}

		return resultMap;
	}

}