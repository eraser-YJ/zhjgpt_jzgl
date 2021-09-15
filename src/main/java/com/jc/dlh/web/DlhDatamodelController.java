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
import com.jc.dlh.domain.DlhDbsource;
import com.jc.dlh.service.IDlhDatamodelService;
import com.jc.dlh.service.IDlhDbsourceService;
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
@RequestMapping(value = "/dlh/dlhDatamodel")
public class DlhDatamodelController extends BaseController {

	@Autowired
	private IDlhDatamodelService dlhDatamodelService;

	@Autowired
	private IDlhDbsourceService dlhDbsourceService;

	public DlhDatamodelController() {
	}

	/**
	 * @description 保存方法
	 * @param DlhDatamodel  dlhDatamodel 实体类
	 * @param BindingResult result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "save.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(@Valid DlhDatamodel dlhDatamodel, BindingResult result, HttpServletRequest request) throws Exception {
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
			if (dlhDatamodel.getDbSource() == null || dlhDatamodel.getDbSource().trim().length() <= 0) {
				resultMap.put("success", "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "数据源编码不能为空");
				return resultMap;
			}
			DlhDbsource sCond = new DlhDbsource();
			sCond.setDbCode(dlhDatamodel.getDbSource());
			DlhDbsource db = dlhDbsourceService.get(sCond);
			if (db == null) {
				resultMap.put("success", "false");
				resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, "数据源编码不正确");
				return resultMap;
			}
			if (!"false".equals(resultMap.get("success"))) {
				dlhDatamodelService.save(dlhDatamodel);
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
	 * @param DlhDatamodel  dlhDatamodel 实体类
	 * @param BindingResult result 校验结果
	 * @return Integer 更新的数目
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "update.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(DlhDatamodel dlhDatamodel, BindingResult result, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}

		Integer flag = dlhDatamodelService.update(dlhDatamodel);
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
	 * @param DlhDatamodel dlhDatamodel 实体类
	 * @return DlhDatamodel 查询结果
	 * @throws Exception
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "get.action", method = RequestMethod.GET)
	@ResponseBody
	public DlhDatamodel get(DlhDatamodel dlhDatamodel, HttpServletRequest request) throws Exception {
		return dlhDatamodelService.get(dlhDatamodel);
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
		return "dlh/dlhDatamodel/module/dlhDatamodelForm";
	}

	/**
	 * @description 分页查询方法
	 * @param DlhDatamodel dlhDatamodel 实体类
	 * @param PageManager  page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "manageList.action", method = RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(DlhDatamodel dlhDatamodel, PageManager page) {
		try {
			// 默认排序
			if (StringUtil.isEmpty(dlhDatamodel.getOrderBy())) {
				dlhDatamodel.addOrderByFieldDesc("t.CREATE_DATE");
			}
			PageManager page_ = dlhDatamodelService.query(dlhDatamodel, page);
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
		return "dlh/dlhDatamodel/dlhDatamodelList";
	}

	/**
	 * @description 删除方法
	 * @param DlhDatamodel dlhDatamodel 实体类
	 * @param String       dlhDatamodel 实体类
	 * @param String       ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc
	 * @version 2020-03-10
	 */
	@RequestMapping(value = "deleteByIds.action", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> deleteByIds(DlhDatamodel dlhDatamodel, String ids, HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dlhDatamodel.setPrimaryKeys(ids.split(","));
		try {
			if (dlhDatamodelService.deleteByIds(dlhDatamodel) != 0) {
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

	@RequestMapping(value = "publish.action", method = RequestMethod.POST)
	@ResponseBody
	public ResVO publish(DlhDatamodel dlhDatamodel, HttpServletRequest request) {
		try {
			dlhDatamodelService.publish(dlhDatamodel);
			return ResVO.buildSuccess();
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResVO.buildFail(ex.getMessage());
		}
	}

}