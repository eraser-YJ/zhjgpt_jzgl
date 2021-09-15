package com.jc.system.group.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jc.foundation.web.BaseController;
import com.jc.system.security.SystemSecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.jc.system.applog.ActionLog;
import com.jc.system.group.domain.Group;
import com.jc.system.group.domain.validator.GroupValidator;
import com.jc.system.group.service.IGroupService;
import com.jc.system.security.domain.User;

/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Controller
@RequestMapping(value="/sys/group")
public class GroupController extends BaseController {
	
	@Autowired
	private IGroupService groupService;

	@org.springframework.web.bind.annotation.InitBinder("group")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new GroupValidator()); 
    }
	
	public GroupController() {
	}
	
	private GroupValidator gv = new GroupValidator();

	/**
	* 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author 都业广
	* @version  2014-07-24 
	*/
	@RequiresPermissions("group:select")
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="组别",operateFuncNm="manage",operateDescribe="对组别进行跳转操作")
	public String manage(HttpServletRequest request, HttpSession session) throws Exception{

		//获得组别
		String groupType = request.getParameter("groupType");
		
		//公共组别时进行权限控制
		if("2".equals(groupType)){
			//访问权限
			User userInfo = SystemSecurityUtils.getUser();
			if (userInfo != null) {
				if (userInfo.getIsAdmin() == 1 || userInfo.getIsSystemAdmin()) {
					session.setAttribute("groupType", groupType);
					return "sys/group/group";
				} else {
					return "error/unauthorized";
				}
			}
		//个人组别时不需要权限控制
		}else{
			session.setAttribute("groupType", groupType);
			return "sys/group/group";
		}
		
		//跳转到错误页面
		return "error/unauthorized"; 
	}
	
	/**
	 * 获取单条记录的方法
	 * @param  group 实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 都业广
	 * @version  2014-07-28
	 */
	@RequestMapping(value = "get.action",method=RequestMethod.GET)
	@ResponseBody
	public Group get(Group group) throws Exception {
		
		//获得组别的单条数据
		group = groupService.getAll(group);
		
		return group;
	}

	/**
	 * 获取单条记录的方法
	 * @param  group 实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 都业广
	 * @version  2014-07-28
	 */
	@RequestMapping(value = "getVal.action",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getVal(Group group) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		//获得组别的单条数据
		group = groupService.get(group);
		if (group == null){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
		}
		return resultMap;
	}
	
	@RequestMapping(value = "groupEdit.action",method=RequestMethod.GET)
	public String timerTaskEdit(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("token", getToken(request));
		return "sys/group/groupEdit";
	}
	
	/**
	 * 分页查询方法
	 * @param  group 实体类
	 * @param  page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 都业广
	 * @version  2014-07-24 
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="组别",operateFuncNm="manageList",operateDescribe="对组别进行查询操作")
	public PageManager manageList(Group group, PageManager page, 
			HttpServletRequest request, HttpSession session){

		//组别类型
		String groupType = (String) session.getAttribute("groupType");
		group.setGroupType(groupType);
		
		//根据功能进行不同的数据过滤
		if("1".equals(groupType)){
			//设置个人用户id
			group.setCreateUser(SystemSecurityUtils.getUser().getId());
		}else{
			//设置组织id
			group.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		}
		
		PageManager rePage = groupService.query(group, page);
		return rePage;
	}
	
	/**
	 * @description 保存方法
	 * @param  group 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author 都业广
	 * @version 2014-07-25
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="组别",operateFuncNm="save",operateDescribe="对组别进行插入操作")
	public Map<String, Object> save(Group group, BindingResult result,
			HttpServletRequest request, HttpSession session) throws Exception {
		
		//组别类型
		String groupType = (String) session.getAttribute("groupType");
		group.setGroupType(groupType);
		
		//验证bean的内容
		gv.validate(group, result);
		
		// 验证bean
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		// 验证token
		resultMap = validateToken(request,request.getParameter(GlobalContext.SESSION_TOKEN));
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		// 根据功能进行不同的数据过滤
		if ("1".equals(groupType)) {
			// 设置个人用户id
			group.setCreateUser(SystemSecurityUtils.getUser().getId());
		} else {
			// 设置组织id
			group.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		}
		
		//存在重名时的处理
		Group sameGroup = groupService.querySameGroupCount(group);
		if(null != sameGroup.getName() && sameGroup.getName().length() > 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_SYS_122"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			return resultMap;
		}
		
		// 保存组别
		if (groupService.add(group) > 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}else{
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_SYS_002"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		
		return resultMap;
	}
	
	/**
	 * @description 更新方法
	 * @param  group 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author 都业广
	 * @version 2014-07-25
	 */
	@RequestMapping(value = "update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="组别",operateFuncNm="update",operateDescribe="对组别进行更新操作")
	public Map<String, Object> update(Group group, BindingResult result,
			HttpServletRequest request, HttpSession session) throws Exception {
		// 组别类型
		String groupType = (String) session.getAttribute("groupType");
		group.setGroupType(groupType);
		
		//验证bean的内容
		gv.validate(group, result);
		
		// 验证bean
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		// 验证token
		resultMap = validateToken(request,request.getParameter(GlobalContext.SESSION_TOKEN));
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		// 根据功能进行不同的数据过滤
		if ("1".equals(groupType)) {
			// 设置个人用户id
			group.setCreateUser(SystemSecurityUtils.getUser().getId());
		} else {
			// 设置组织id
			group.setCreateUserOrg(SystemSecurityUtils.getUser().getOrgId());
		}
		
		//存在重名时的处理
		Group sameGroup = groupService.querySameGroupCount(group);
		if (null != sameGroup.getName() && 
				sameGroup.getName().length() > 0 && 
				!group.getId().equals(sameGroup.getId())) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_SYS_122"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			return resultMap;
		}
		
		// 保存组别
		if (groupService.updateAll(group) > 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}else{
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE,MessageUtils.getMessage("JC_SYS_002"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		
		return resultMap;
	}

/**
	 * 删除方法
	 * @param  group 实体类
	 * @param  ids 删除的多个主键
	 * @return Integer 成功删除
	 * @throws Exception
	 * @author 都业广
	 * @version  2014-07-24
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="",operateFuncNm="deleteByIds",operateDescribe="对进行删除")
	public Integer deleteByIds(Group group,String ids) throws Exception{
		group.setPrimaryKeys(ids.split(","));
		return groupService.remove(group);
	}

}