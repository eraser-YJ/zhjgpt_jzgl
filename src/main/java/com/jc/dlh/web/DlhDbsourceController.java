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

import com.jc.dlh.domain.DlhDbsource;
import com.jc.dlh.service.IDlhDbsourceService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.StringUtil;
import com.jc.foundation.web.BaseController;


/**
 * @title 统一数据资源中心
 * @description  controller类
 * Copyright (c) 2020 Jiachengnet.com Inc. All Rights Reserved
 * Company 长春奕迅
 * @author lc  
 * @version  2020-04-07
 */
 
@Controller
@RequestMapping(value="/dlh/dlhDbsource")
public class DlhDbsourceController extends BaseController{

	
	@Autowired
	private IDlhDbsourceService dlhDbsourceService;
 
	
	public DlhDbsourceController() {
	}

	/**
	 * @description 保存方法
	 * @param DlhDbsource dlhDbsource 实体类
	 * @param BindingResult result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version  2020-04-07
	 */
	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> save(@Valid DlhDbsource dlhDbsource,BindingResult result,
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
			dlhDbsourceService.save(dlhDbsource);
			resultMap.put("success", "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
			resultMap.put(GlobalContext.SESSION_TOKEN, getToken(request));
		}
		return resultMap;
	}

	/**
	* workflowParamTemp修改方法
	* @param DlhDbsource dlhDbsource 实体类
	* @param BindingResult result 校验结果
	* @return Integer 更新的数目
	* @author
	* @version  2020-04-07 
	*/
	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> update(DlhDbsource dlhDbsource, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = dlhDbsourceService.update(dlhDbsource);
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
	 * @param DlhDbsource dlhDbsource 实体类
	 * @return DlhDbsource 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version  2020-04-07
	 */
	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public DlhDbsource get(DlhDbsource dlhDbsource,HttpServletRequest request) throws Exception{
		return dlhDbsourceService.get(dlhDbsource);
	}

	/**
	 * @description 弹出对话框方法
	 * @return String form对话框所在位置
	 * @throws Exception
	 * @author lc 
	 * @version  2020-04-07
	 */
	@RequestMapping(value="loadForm.action",method=RequestMethod.GET)
	public String loadForm(Model model,HttpServletRequest request) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String token = getToken(request);
		map.put(GlobalContext.SESSION_TOKEN, token);
		model.addAttribute("data", map);
		return "dlh/dlhDbsource/module/dlhDbsourceForm"; 
	}

	/**
	 * @description 分页查询方法
	 * @param DlhDbsource dlhDbsource 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author lc 
	 * @version  2020-04-07 
	 */
	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	public PageManager manageList(DlhDbsource dlhDbsource,PageManager page){
		//默认排序
		if(StringUtil.isEmpty(dlhDbsource.getOrderBy())) {
			dlhDbsource.addOrderByFieldDesc("t.CREATE_DATE");
		}
		PageManager page_ = dlhDbsourceService.query(dlhDbsource, page);
		return page_; 
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2020-04-07 
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	public String manage(HttpServletRequest request) throws Exception{
		return "dlh/dlhDbsource/dlhDbsourceList"; 
	}

/**
	 * @description 删除方法
	 * @param DlhDbsource dlhDbsource 实体类
	 * @param String dlhDbsource 实体类
	 * @param String ids 删除的多个主键
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author lc 
	 * @version  2020-04-07
	 */
	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	public  Map<String, Object> deleteByIds(DlhDbsource dlhDbsource,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		dlhDbsource.setPrimaryKeys(ids.split(","));	
		if(dlhDbsourceService.deleteByIds(dlhDbsource) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_006"));
		}
		return resultMap;
	}

}