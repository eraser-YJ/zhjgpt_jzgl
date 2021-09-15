package com.jc.dlh.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.busi.standard.util.RSAUtil;
import com.jc.common.kit.vo.ResVO;
import com.jc.dlh.domain.DlhUser;
import com.jc.dlh.service.IDlhUserService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;

/**
 * @description 临时项目
 * @author lc  Administrator
 */
@Controller
@RequestMapping(value = "/dlh/dlhUser")
public class DlhUserController extends BaseController {

	@Autowired
	private IDlhUserService dlhUserService;

	public DlhUserController() {
	}

	/**
	 * @description 保存方法
	 * @param DlhUser       dlhUser 实体类
	 * @param BindingResult result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-13
	 */
	@RequestMapping(value = "save.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@Valid DlhUser dlhUser, BindingResult result, HttpServletRequest request) throws Exception {

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
			dlhUserService.save(dlhUser);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	/**
	 * workflowParamTemp修改方法
	 * 
	 * @param DlhUser       dlhUser 实体类
	 * @param BindingResult result 校验结果
	 * @return Integer 更新的数目
	 * @author lc 
	 * @version 2020-03-13
	 */
	@RequestMapping(value = "update.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(DlhUser dlhUser, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		Integer flag = dlhUserService.update(dlhUser);
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
	 * @param DlhUser dlhUser 实体类
	 * @return DlhUser 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-13
	 */
	@RequestMapping(value = "get.action", method = RequestMethod.GET)
	@ResponseBody
	public DlhUser get(DlhUser dlhUser, HttpServletRequest request) throws Exception {
		return dlhUserService.get(dlhUser);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-13
	 */
	@RequestMapping(value = "loadForm.action", method = RequestMethod.GET)
	public String loadForm(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "dlh/dlhUser/module/dlhUserForm";
	}

	/**
	 * @description 分页查询方法
	 * @param DlhUser     dlhUser 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-13
	 */
	@RequestMapping(value = "manageList.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(DlhUser dlhUser, PageManager page) {
		// 默认排序
		if (StringUtil.isEmpty(dlhUser.getOrderBy())) {
			dlhUser.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = dlhUserService.query(dlhUser, page);
		return page_;
	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-13
	 */
	@RequestMapping(value = "manage.action", method = RequestMethod.GET)
	public String manage(HttpServletRequest request) throws Exception {
		return "dlh/dlhUser/dlhUserList";
	}

	/**
	 * @description 删除方法
	 * @param DlhUser dlhUser 实体类
	 * @param String  dlhUser 实体类
	 * @param String  ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-13
	 */
	@RequestMapping(value = "deleteByIds.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteByIds(DlhUser dlhUser, String ids, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		dlhUser.setPrimaryKeys(ids.split(","));
		if (dlhUserService.deleteByIds(dlhUser) != 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

	@RequestMapping(value = "generatorRSA.action", method = RequestMethod.POST)
	@ResponseBody
	public ResVO generatorRSA() throws Exception {
		Map<String, String> resultMap = new HashMap<String, String>();
		String[] keys = RSAUtil.generatorRSA();
		resultMap.put("public", keys[0]);
		resultMap.put("private", keys[1]);
		return ResVO.buildSuccess(resultMap);
	}

}