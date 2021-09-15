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

import com.jc.dlh.domain.DlhDataobjectField;
import com.jc.dlh.service.IDlhDataobjectFieldService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;

/**
 * @title 统一数据资源中心
 * @description controller类
 * @author lc 
 * @version 2020-03-10
 */

@Controller
@RequestMapping(value = "/dlh/dlhDataobjectField")
public class DlhDataobjectFieldController extends BaseController {

	@Autowired
	private IDlhDataobjectFieldService dlhDataobjectFieldService;

	public DlhDataobjectFieldController() {
	}

	/**
	 * @description 保存方法
	 * @param DlhDataobjectField dlhDataobjectField 实体类
	 * @param BindingResult      result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "save.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@Valid DlhDataobjectField dlhDataobjectField, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		try {
			if (resultMap.size() > 0) {
				return resultMap;
			}
			// 验证token
			resultMap = validateToken(request);
			if (resultMap.size() > 0) {
				return resultMap;
			}

			if (!"false".equals(resultMap.get("success"))) {
				dlhDataobjectFieldService.save(dlhDataobjectField);
				resultMap.put("success", "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return resultMap;

	}

	/**
	 * workflowParamTemp修改方法
	 * 
	 * @param DlhDataobjectField dlhDataobjectField 实体类
	 * @param BindingResult      result 校验结果
	 * @return Integer 更新的数目
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "update.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(DlhDataobjectField dlhDataobjectField, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		if (dlhDataobjectField.getFieldCondShow() == null) {
			dlhDataobjectField.setFieldCondShowNull("Y");
		}
		if (dlhDataobjectField.getFieldListShow() == null) {
			dlhDataobjectField.setFieldListShowNull("Y");
		}
		if (dlhDataobjectField.getFieldFormShow() == null) {
			dlhDataobjectField.setFieldFormShowNull("Y");
		}
		Integer flag = dlhDataobjectFieldService.update(dlhDataobjectField);
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
	 * @param DlhDataobjectField dlhDataobjectField 实体类
	 * @return DlhDataobjectField 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "get.action", method = RequestMethod.GET)
	@ResponseBody
	public DlhDataobjectField get(DlhDataobjectField dlhDataobjectField, HttpServletRequest request) throws Exception {
		return dlhDataobjectFieldService.get(dlhDataobjectField);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "loadForm.action", method = RequestMethod.GET)
	public String loadForm(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		String dbType = request.getParameter("dbType");
		return "dlh/dlhDataobjectField/" + dbType + "/dlhDataobjectFieldForm";
	}

	/**
	 * @description 分页查询方法
	 * @param DlhDataobjectField dlhDataobjectField 实体类
	 * @param PageManager        page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manageList.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(DlhDataobjectField dlhDataobjectField, PageManager page) {
		try {
			// 默认排序
			if (StringUtil.isEmpty(dlhDataobjectField.getOrderBy())) {
				dlhDataobjectField.addOrderByField("t.FIELD_SEQ");
				dlhDataobjectField.addOrderByFieldDesc("t.CREATE_DATE");
			}
			PageManager page_ = dlhDataobjectFieldService.query(dlhDataobjectField, page);
			return page_;
		} catch (Exception ex) {
			ex.printStackTrace();
			return page;
		}

	}

	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manage.action", method = RequestMethod.GET)
	public String manage(HttpServletRequest request) throws Exception {
		String modelId = request.getParameter("modelId");
		String objectId = request.getParameter("objectId");
		String dbType = request.getParameter("dbType");
		if (modelId != null) {
			request.setAttribute("objectId", objectId);
			request.setAttribute("modelId", modelId);
			request.setAttribute("dbType", dbType);
			return "dlh/dlhDataobjectField/" + dbType + "/dlhDataobjectFieldList";
		}
		return "";

	}

	/**
	 * @description 删除方法
	 * @param DlhDataobjectField dlhDataobjectField 实体类
	 * @param String             dlhDataobjectField 实体类
	 * @param String             ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "deleteByIds.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteByIds(DlhDataobjectField dlhDataobjectField, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dlhDataobjectField.setPrimaryKeys(ids.split(","));
		try {
			if (dlhDataobjectFieldService.deleteByIds(dlhDataobjectField) != 0) {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
			} else {
				resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
			}
		} catch (Exception ex) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, ex.getMessage());
		}
		return resultMap;
	}

}