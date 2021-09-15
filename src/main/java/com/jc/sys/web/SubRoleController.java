package com.jc.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;
import com.jc.sys.domain.SubRole;
import com.jc.sys.domain.SubUserRole;
import com.jc.sys.domain.validator.SubRoleValidator;
import com.jc.sys.service.ISubRoleService;
import com.jc.sys.service.ISubUserRoleService;
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
@RequestMapping(value = "/sys/subRole")
public class SubRoleController extends BaseController {

	@Autowired
	private ISubRoleService subRoleService;

	@Autowired
	private ISubUserRoleService subUserRoleService;

	@org.springframework.web.bind.annotation.InitBinder("subRole")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new SubRoleValidator());

	}

	public SubRoleController() {
	}

	/**
	 * @description 保存方法
	 * @param  subRole 实体类
	 * @param  result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version 2018-04-18
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "子系统角色表", operateFuncNm = "save", operateDescribe = "对子系统角色表进行新增操作")
	public Map<String, Object> save(@Valid SubRole subRole, BindingResult result, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		// 验证token
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		if (!"false".equals(resultMap.get("success"))) {
			subRoleService.save(subRole);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	/**
	 * workflowParamTemp修改方法
	 * 
	 * @param  subRole 实体类
	 * @param  result 校验结果
	 * @return Integer 更新的数目
	 * @author
	 * @version 2018-04-18
	 */
	@RequestMapping(value = "update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "子系统角色表", operateFuncNm = "update", operateDescribe = "对子系统角色表进行更新操作")
	public Map<String, Object> update(SubRole subRole, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		Integer flag = subRoleService.update(subRole);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		String token = getToken(request);
		resultMap.put(GlobalContext.SESSION_TOKEN, token);
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param  subRole 实体类
	 * @return SubRole 查询结果
	 * @throws Exception
	 * @author
	 * @version 2018-04-18
	 */
	@RequestMapping(value = "get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm = "子系统角色表", operateFuncNm = "get", operateDescribe = "对子系统角色表进行单条查询操作")
	public SubRole get(SubRole subRole, HttpServletRequest request) throws Exception {
		return subRoleService.get(subRole);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author
	 * @version 2018-04-18
	 */
	@RequestMapping(value = "loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "sys/subRole/module/subRoleForm";
	}

	/**
	 * @description 分页查询方法
	 * @param  subRole 实体类
	 * @param  page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author
	 * @version 2018-04-18
	 */
	@RequestMapping(value = "manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(SubRole subRole, PageManager page) {
		// 默认排序
		if (StringUtil.isEmpty(subRole.getOrderBy())) {
			subRole.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = subRoleService.query(subRole, page);
		return rePage;
	}

	/**
	 * 查询全部角色
	 * 
	 * @param subRole
	 * @return
	 * @throws CustomException
	 */
	@RequestMapping(value = "queryAll.action",method=RequestMethod.GET)
	@ResponseBody
	public List<SubRole> queryAll(SubRole subRole) throws CustomException {
		// 默认排序
		if (StringUtil.isEmpty(subRole.getOrderBy())) {
			subRole.addOrderByFieldDesc("t.CREATE_DATE");
		}
		return this.subRoleService.queryAll(subRole);
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author
	 * @version 2018-04-18
	 */
	@RequestMapping(value = "manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm = "子系统角色表", operateFuncNm = "manage", operateDescribe = "对子系统角色表进行跳转操作")
	public String manage(HttpServletRequest request) throws Exception {
		return "sys/subRole/subRoleList";
	}

	/**
	 * @description 删除方法
	 * @param  subRole 实体类
	 * @param  subRole 实体类
	 * @param  ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version 2018-04-18
	 */
	@RequestMapping(value = "deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "子系统角色表", operateFuncNm = "deleteByIds", operateDescribe = "对子系统角色表进行删除")
	public Map<String, Object> deleteByIds(SubRole subRole, String ids, HttpServletRequest request) throws Exception {
		List<SubUserRole> subUserRoleList = null;
		SubUserRole subUserRole = new SubUserRole();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		subUserRole.setRoleIds(ids);
		subUserRoleList = this.subUserRoleService.queryAll(subUserRole);

		if (subUserRoleList != null && subUserRoleList.size() > 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "角色已使用，无法删除");
		} else {
			subRole.setPrimaryKeys(ids.split(","));
			if (subRoleService.deleteByIds(subRole) != 0) {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
			}
		}
		return resultMap;
	}

}