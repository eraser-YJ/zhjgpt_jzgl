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
import com.jc.sys.domain.SubRoleGroup;
import com.jc.sys.domain.SubRoleGroupMenu;
import com.jc.sys.domain.validator.SubRoleGroupMenuValidator;
import com.jc.sys.service.ISubRoleGroupMenuService;
import com.jc.sys.service.ISubRoleGroupService;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Subsystem;
import com.jc.system.security.service.ISubsystemService;
/**
 *@ClassName
 *@Description
 *@author Administrator -Technical Management Architecture
 *@date 2019-04-22 15:15
 *@Version 3.0
 *
 **/
@Controller
@RequestMapping(value = "/sys/subRoleGroupMenu")
public class SubRoleGroupMenuController extends BaseController {

	@Autowired
	private ISubsystemService subsystemService;

	@Autowired
	private ISubRoleGroupService subRoleGroupService;

	@Autowired
	private ISubRoleGroupMenuService subRoleGroupMenuService;

	@org.springframework.web.bind.annotation.InitBinder("subRoleGroupMenu")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new SubRoleGroupMenuValidator());
	}

	public SubRoleGroupMenuController() {
	}

	/**
	 * @description ????????????
	 * @param  subRoleGroupMenu ?????????
	 * @param  result ????????????
	 * @return success ??????????????? errorMessage????????????
	 * @throws Exception
	 * @author
	 * @version 2018-04-12
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "???????????????????????????", operateFuncNm = "save", operateDescribe = "????????????????????????????????????????????????")
	public Map<String, Object> save(@Valid SubRoleGroupMenu subRoleGroupMenu, BindingResult result, HttpServletRequest request)
			throws Exception {

		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		if (!"false".equals(resultMap.get("success"))) {
			subRoleGroupMenuService.saveByMenuIds(subRoleGroupMenu);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
		}
		return resultMap;
	}

	/**
	 * workflowParamTemp????????????
	 * 
	 * @param  subRoleGroupMenu ?????????
	 * @param  result ????????????
	 * @return Integer ???????????????
	 * @author
	 * @version 2018-04-12
	 */
	@RequestMapping(value = "update.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "???????????????????????????", operateFuncNm = "update", operateDescribe = "????????????????????????????????????????????????")
	public Map<String, Object> update(SubRoleGroupMenu subRoleGroupMenu, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		Integer flag = subRoleGroupMenuService.update(subRoleGroupMenu);
		if (flag == 1) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_003"));
		}
		return resultMap;
	}

	/**
	 * @description ????????????????????????
	 * @param  subRoleGroupMenu ?????????
	 * @return SubRoleGroupMenu ????????????
	 * @throws Exception
	 * @author
	 * @version 2018-04-12
	 */
	@RequestMapping(value = "get.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm = "???????????????????????????", operateFuncNm = "get", operateDescribe = "??????????????????????????????????????????????????????")
	public SubRoleGroupMenu get(SubRoleGroupMenu subRoleGroupMenu, HttpServletRequest request) throws Exception {
		List<SubRoleGroupMenu> subRoleGroupMenuList = subRoleGroupMenuService.queryAll(subRoleGroupMenu);
		if (subRoleGroupMenuList != null && subRoleGroupMenuList.size() > 0) {
			List<String> menuIdList = new ArrayList<String>();
			List<String> menuNameList = new ArrayList<String>();
			subRoleGroupMenu = subRoleGroupMenuList.get(0);
			for (SubRoleGroupMenu srgm : subRoleGroupMenuList) {
				menuIdList.add(srgm.getMenuId().toString());
				menuNameList.add(srgm.getMenuName());
			}
			subRoleGroupMenu.setMenuIds(StringUtils.join(menuIdList, ","));
			subRoleGroupMenu.setMenuNames(StringUtils.join(menuNameList, ","));
		}
		return subRoleGroupMenu;
	}

	/**
	 * @description ?????????????????????
	 * @return String form?????????????????????
	 * @throws Exception
	 * @author
	 * @version 2018-04-12
	 */
	@RequestMapping(value = "loadForm.action",method=RequestMethod.GET)
	public String loadForm(SubRoleGroupMenu subRoleGroupMenu, Model model, HttpServletRequest request) throws Exception {
		Subsystem subsystem = null;
		SubRoleGroup subRoleGroup = null;
		List<Subsystem> subsystemList = null;
		String subSystemId = GlobalContext.getProperty("subsystem.id");
		if (subRoleGroupMenu.getRoleGroupId() != null) {
			subRoleGroup = new SubRoleGroup();
			subRoleGroup.setId(subRoleGroupMenu.getRoleGroupId());
			subRoleGroup = this.subRoleGroupService.get(subRoleGroup);
		}
		if (!StringUtil.isEmpty(subSystemId)) {
			subsystem = new Subsystem();
			subsystem.setPermission(subSystemId);
			subsystemList = this.subsystemService.queryAll(subsystem);
			if (subsystemList != null && subsystemList.size() > 0) {
				model.addAttribute("parentMenuId", subsystemList.get(0).getMenuid());
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		model.addAttribute("data", map);
		model.addAttribute("subRoleGroup", subRoleGroup);
		return "sys/subRoleGroupMenu/module/subRoleGroupMenuForm";
	}

	/**
	 * @description ??????????????????
	 * @param  subRoleGroupMenu ?????????
	 * @param  page ???????????????
	 * @return PagingBean ????????????
	 * @throws Exception
	 * @author
	 * @version 2018-04-12
	 */
	@RequestMapping(value = "manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(SubRoleGroupMenu subRoleGroupMenu, PageManager page) {
		// ????????????
		if (StringUtil.isEmpty(subRoleGroupMenu.getOrderBy())) {
			subRoleGroupMenu.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager rePage = subRoleGroupMenuService.query(subRoleGroupMenu, page);
		return rePage;
	}

	/**
	 * @description ????????????
	 * @return String ???????????????
	 * @throws Exception
	 * @author
	 * @version 2018-04-12
	 */
	@RequestMapping(value = "manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm = "???????????????????????????", operateFuncNm = "manage", operateDescribe = "????????????????????????????????????????????????")
	public String manage(HttpServletRequest request) throws Exception {
		return "sys/subRoleGroupMenu/subRoleGroupMenuList";
	}

	/**
	 * @description ????????????
	 * @param  subRoleGroupMenu ?????????
	 * @param  subRoleGroupMenu ?????????
	 * @param  ids ?????????????????????
	 * @return success ??????????????? errorMessage????????????
	 * @throws Exception
	 * @author
	 * @version 2018-04-12
	 */
	@RequestMapping(value = "deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm = "???????????????????????????", operateFuncNm = "deleteByIds", operateDescribe = "??????????????????????????????????????????")
	public Map<String, Object> deleteByIds(SubRoleGroupMenu subRoleGroupMenu, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		subRoleGroupMenu.setPrimaryKeys(ids.split(","));
		if (subRoleGroupMenuService.deleteByIds(subRoleGroupMenu) != 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

}