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

import com.jc.common.kit.vo.ResVO;
import com.jc.dlh.domain.DlhDatamodel;
import com.jc.dlh.domain.DlhDataobject;
import com.jc.dlh.service.IDlhDatamodelService;
import com.jc.dlh.service.IDlhDataobjectService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;

/**
 * @title 统一数据资源中心
 * @description controller类 Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved Company 长春奕迅
 * @author lc 
 * @version 2020-03-10
 */

@Controller
@RequestMapping(value = "/dlh/dlhDataobject")
public class DlhDataobjectController extends BaseController {

	@Autowired
	private IDlhDataobjectService dlhDataobjectService;

	@Autowired
	private IDlhDatamodelService dlhDatamodelService;

	public DlhDataobjectController() {
	}

	/**
	 * @description 保存方法
	 * @param DlhDataobject dlhDataobject 实体类
	 * @param BindingResult result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "save.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@Valid DlhDataobject dlhDataobject, BindingResult result, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		try {
			// 验证token
			resultMap = validateToken(request);
			if (resultMap.size() > 0) {
				return resultMap;
			}
			if (dlhDataobject.getModelId() == null || dlhDataobject.getModelId().trim().length() <= 0) {
				resultMap.put("success", "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "数据模型不能为空");
				return resultMap;
			}
			DlhDatamodel mCond = new DlhDatamodel();
			mCond.setId(dlhDataobject.getModelId());
			DlhDatamodel model = dlhDatamodelService.get(mCond);
			if (model == null) {
				resultMap.put("success", "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "数据模型不正确");
				return resultMap;
			}
			dlhDataobject.setDbSource(model.getDbSource());
			if (!"false".equals(resultMap.get("success"))) {
				dlhDataobjectService.save(dlhDataobject);
				resultMap.put("success", "true");
				resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
				resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
			}
			return resultMap;
		} catch (Exception e) {
			e.printStackTrace();
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, e.getMessage());
			return resultMap;
		}
	}

	/**
	 * workflowParamTemp修改方法
	 * 
	 * @param DlhDataobject dlhDataobject 实体类
	 * @param BindingResult result 校验结果
	 * @return Integer 更新的数目
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "update.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(DlhDataobject dlhDataobject, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		Integer flag = dlhDataobjectService.update(dlhDataobject);
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
	 * @param DlhDataobject dlhDataobject 实体类
	 * @return DlhDataobject 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "get.action", method = RequestMethod.GET)
	@ResponseBody
	public DlhDataobject get(DlhDataobject dlhDataobject, HttpServletRequest request) throws Exception {
		return dlhDataobjectService.get(dlhDataobject);
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
		return "dlh/dlhDataobject/module/dlhDataobjectForm";
	}

	/**
	 * @description 分页查询方法
	 * @param DlhDataobject dlhDataobject 实体类
	 * @param PageManager   page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manageList.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(DlhDataobject dlhDataobject, PageManager page) {
		// 默认排序
		if (StringUtil.isEmpty(dlhDataobject.getOrderBy())) {
			dlhDataobject.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = dlhDataobjectService.query(dlhDataobject, page);
		return page_;
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
		return "dlh/dlhDataobject/dlhDataobjectList";
	}

	/**
	 * @description 删除方法
	 * @param DlhDataobject dlhDataobject 实体类
	 * @param String        dlhDataobject 实体类
	 * @param String        ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "deleteByIds.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteByIds(DlhDataobject dlhDataobject, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dlhDataobject.setPrimaryKeys(ids.split(","));
		try {
			if (dlhDataobjectService.deleteByIds(dlhDataobject) != 0) {
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

	@RequestMapping(value = "init.action", method = RequestMethod.POST)
	@ResponseBody
	public ResVO init(DlhDataobject dlhDataobject) throws Exception {
		try {
			// 初始化
			dlhDataobjectService.init(dlhDataobject);
			return ResVO.buildSuccess();
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResVO.buildFail(ex.getMessage());
		}
	}

}