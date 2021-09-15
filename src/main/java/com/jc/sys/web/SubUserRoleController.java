package com.jc.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
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
import com.jc.foundation.web.BaseController;
import com.jc.sys.domain.SubUser;
import com.jc.sys.domain.SubUserRole;
import com.jc.sys.domain.validator.SubUserRoleValidator;
import com.jc.sys.service.ISubUserRoleService;
import com.jc.sys.service.ISubUserService;
import com.jc.system.applog.ActionLog;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Controller
@RequestMapping(value = "/sys/subUserRole")
public class SubUserRoleController extends BaseController {

	@Autowired
	private ISubUserService subUserService;

	@Autowired
	private ISubUserRoleService subUserRoleService;

	@org.springframework.web.bind.annotation.InitBinder("subUserRole")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new SubUserRoleValidator());

	}

	public SubUserRoleController() {
	}

	/**
	 * @description 保存方法
	 * @param  subUserRole 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version 2018-04-20
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "子系统用户角色表", operateFuncNm = "save", operateDescribe = "对子系统用户角色表进行新增操作")
	public Map<String, Object> save(@Valid SubUserRole subUserRole, BindingResult result, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		if (!"false".equals(resultMap.get("success"))) {
			subUserRoleService.saveByRoleIds(subUserRole);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
		}
		return resultMap;
	}

	/**
	 * workflowParamTemp修改方法
	 * 
	 * @param  subUserRole 实体类
	 * @param  result 校验结果
	 * @return Integer 更新的数目
	 * @author
	 * @version 2018-04-20
	 */
	@RequestMapping(value = "update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "子系统用户角色表", operateFuncNm = "update", operateDescribe = "对子系统用户角色表进行更新操作")
	public Map<String, Object> update(SubUserRole subUserRole, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		Integer flag = subUserRoleService.update(subUserRole);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param  subUserRole 实体类
	 * @return SubUserRole 查询结果
	 * @throws Exception
	 * @author
	 * @version 2018-04-20
	 */
	@RequestMapping(value = "get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm = "子系统用户角色表", operateFuncNm = "get", operateDescribe = "对子系统用户角色表进行单条查询操作")
	public SubUserRole get(SubUserRole subUserRole, HttpServletRequest request) throws Exception {
		List<SubUserRole> subUserRoleList = this.subUserRoleService.getRolesByUserAndDeptId(subUserRole);
		if (subUserRoleList != null && subUserRoleList.size() > 0) {
			List<String> roleIdList = new ArrayList<String>();
			List<String> roleNameList = new ArrayList<String>();
			subUserRole = subUserRoleList.get(0);
			for (SubUserRole sur : subUserRoleList) {
				roleIdList.add(sur.getRoleId().toString());
				roleNameList.add(sur.getRoleName());
			}
			subUserRole.setRoleIds(StringUtils.join(roleIdList, ","));
			subUserRole.setRoleNames(StringUtils.join(roleNameList, ","));
		}
		return subUserRole;
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version 2018-04-20
	 */
	@RequestMapping(value = "loadForm.action",method=RequestMethod.GET)
	public String loadForm(SubUserRole subUserRole, Model model, HttpServletRequest request) throws Exception {
		SubUser subUser = null;
		Map<String, Object> map = new HashMap<String, Object>();
		if (subUserRole != null && subUserRole.getUserId() != null) {
			subUser = new SubUser();
			subUser.setId(subUserRole.getUserId());
			subUser.setDeptId(subUserRole.getDeptId());
			model.addAttribute("subUser", this.subUserService.get(subUser));
		}
		model.addAttribute("data", map);
		return "sys/subUserRole/module/subUserRoleForm";
	}

	/**
	 * @description 分页查询方法
	 * @param  subUserRole 实体类
	 * @param  page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author
	 * @version 2018-04-20
	 */
	@RequestMapping(value = "manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(SubUserRole subUserRole, PageManager page) {
		// 默认排序
		if (StringUtil.isEmpty(subUserRole.getOrderBy())) {
			subUserRole.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = subUserRoleService.query(subUserRole, page);
		return rePage;
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version 2018-04-20
	 */
	@RequestMapping(value = "manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm = "子系统用户角色表", operateFuncNm = "manage", operateDescribe = "对子系统用户角色表进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception {
		return "sys/subUserRole/subUserRoleList";
	}

	/**
	 * @description 删除方法
	 * @param  subUserRole 实体类
	 * @param  subUserRole 实体类
	 * @param  ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version 2018-04-20
	 */
	@RequestMapping(value = "deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "子系统用户角色表", operateFuncNm = "deleteByIds", operateDescribe = "对子系统用户角色表进行删除")
	public Map<String, Object> deleteByIds(SubUserRole subUserRole, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		subUserRole.setPrimaryKeys(ids.split(","));
		if (subUserRoleService.deleteByIds(subUserRole) != 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

}