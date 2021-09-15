package com.jc.dlh.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.domain.DlhUserRole;
import com.jc.dlh.domain.DlhUserRoleTreeVo;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.dlh.service.IDlhUserRoleService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;

/**
 * @title 统一数据资源中心
 * @description controller类 
 * @author lc 
 * @version 2020-03-18
 */

@Controller
@RequestMapping(value = "/dlh/dlhUserRole")
public class DlhUserRoleController extends BaseController {

	@Autowired
	private IDlhUserRoleService dlhUserRoleService;

	@Autowired
	private IDlhDataobjectService dlhDataobjectService;

	public DlhUserRoleController() {
	}

	/**
	 * @description 保存方法
	 * @param DlhUserRole   dlhUserRole 实体类
	 * @param BindingResult result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-18
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	public Map<String, Object> save(@Valid DlhUserRole dlhUserRole, BindingResult result, HttpServletRequest request) throws Exception {

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
			Integer flag = dlhUserRoleService.save(dlhUserRole);
			if (flag != -1) {
				resultMap.put("success", "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			} else {
				resultMap.put("success", "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_002"));
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			}
		}
		return resultMap;
	}

	/**
	 * workflowParamTemp修改方法
	 * 
	 * @param DlhUserRole   dlhUserRole 实体类
	 * @param BindingResult result 校验结果
	 * @return Integer 更新的数目
	 * @author lc 
	 * @version 2020-03-18
	 */
	@RequestMapping(value = "update.action")
	@ResponseBody
	public Map<String, Object> update(DlhUserRole dlhUserRole, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		Integer flag = dlhUserRoleService.update(dlhUserRole);
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
	 * @param DlhUserRole dlhUserRole 实体类
	 * @return DlhUserRole 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-18
	 */
	@RequestMapping(value = "get.action")
	@ResponseBody
	public DlhUserRole get(DlhUserRole dlhUserRole, HttpServletRequest request) throws Exception {
		return dlhUserRoleService.get(dlhUserRole);
	}

	@RequestMapping(value = "getList.action")
	@ResponseBody
	public List<DlhUserRole> getList(DlhUserRole dlhUserRole, HttpServletRequest request) throws Exception {
		return dlhUserRoleService.queryAll(dlhUserRole);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-18
	 */
	@RequestMapping(value = "loadForm.action")
	public String loadForm(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "dlh/dlhUserRole/module/dlhUserRoleForm";
	}

	/**
	 * @description 分页查询方法
	 * @param DlhUserRole dlhUserRole 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-18
	 */
	@RequestMapping(value = "manageList.action")
	@ResponseBody
	public PageManager manageList(DlhUserRole dlhUserRole, PageManager page) {
		// 默认排序
		if (StringUtil.isEmpty(dlhUserRole.getOrderBy())) {
			dlhUserRole.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = dlhUserRoleService.query(dlhUserRole, page);
		return page_;
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-18
	 */
	@RequestMapping(value = "manage.action")
	public String manage(HttpServletRequest request) throws Exception {
		return "dlh/dlhUserRole/dlhUserRoleList";
	}

	/**
	 * @description 删除方法
	 * @param DlhUserRole dlhUserRole 实体类
	 * @param String      dlhUserRole 实体类
	 * @param String      ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-18
	 */
	@RequestMapping(value = "deleteByIds.action")
	@ResponseBody
	public Map<String, Object> deleteByIds(DlhUserRole dlhUserRole, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dlhUserRole.setPrimaryKeys(ids.split(","));
		if (dlhUserRoleService.deleteByIds(dlhUserRole) != 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	@RequestMapping(value = "treeList.action")
	@ResponseBody
	public List<DlhUserRoleTreeVo> treeList(DlhDataobject dlhUserRole) throws CustomException {
		// 默认排序
		if (StringUtil.isEmpty(dlhUserRole.getOrderBy())) {
			dlhUserRole.addOrderByFieldDesc("t.CREATE_DATE");
		}
		List<DlhDataobject> page_ = dlhDataobjectService.queryAll(dlhUserRole);
		List<DlhUserRoleTreeVo> treeList = new ArrayList<>();
		for (DlhDataobject item : page_) {
			DlhUserRoleTreeVo dlhUserRoleTree = new DlhUserRoleTreeVo();
			dlhUserRoleTree.setCode(item.getId());
			dlhUserRoleTree.setText(item.getObjName());
			treeList.add(dlhUserRoleTree);
		}
		return treeList;
	}

}