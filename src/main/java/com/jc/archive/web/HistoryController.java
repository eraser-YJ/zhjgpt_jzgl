package com.jc.archive.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.web.BaseController;
import com.jc.archive.domain.History;
import com.jc.archive.domain.validator.HistoryValidator;
import com.jc.archive.service.IHistoryService;
import com.jc.system.applog.ActionLog;


/**
 * @title 嘉诚智能政务办公平台
 * @description  文档历史 controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version 2014-06-05
 */
 
@Controller
@RequestMapping(value="/archive/history")
public class HistoryController extends BaseController{
	
	@Autowired
	private IHistoryService historyService;
	
	@org.springframework.web.bind.annotation.InitBinder("history")
    public void initBinder(WebDataBinder binder) {  
        binder.setValidator(new HistoryValidator()); 
    }
	
	public HistoryController() {
	}

	/**
	 * @description 分页查询方法
	 * @param History history
	 * @param PageManager page
	 * @param HttpServletRequest request
	 * @return PagingBean
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档历史",operateFuncNm="manageList",operateDescribe="查询文档历史")
	public PageManager manageList(History history,PageManager page,HttpServletRequest request){
		PageManager page_ = historyService.query(history, page);
		return page_; 
	}

	/**
	* @description 跳转方法
	* @param HttpServletRequest request
	* @return String
	* @throws Exception
	* @author 盖旭
	* @version 2014-06-05 
	*/
	@RequestMapping(value="manage.action")
	@ActionLog(operateModelNm="文档历史",operateFuncNm="manage",operateDescribe="跳转文档历史")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/history/history1"; 
	}

    /**
	 * @description 删除方法
	 * @param History history
	 * @param String ids
	 * @param HttpServletRequest request
	 * @return Integer
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档历史",operateFuncNm="deleteByIds",operateDescribe="删除文档历史")
	public Integer deleteByIds(History history,String ids,HttpServletRequest request) throws Exception{
		history.setPrimaryKeys(ids.split(","));
		return historyService.delete(history);
	}

	/**
	 * @description 保存方法
	 * @param History history
	 * @param BindingResult result
	 * @param HttpServletRequest result
	 * @return Map<String,Object>
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档历史",operateFuncNm="save",operateDescribe="新增文档历史")
	public Map<String,Object> save(@Valid History history,BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		//验证token
		resultMap = validateToken(request);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		if(!"false".equals(resultMap.get("success"))){
			historyService.save(history);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

   /**
	* @description 修改方法
	* @param History history
	* @param BindingResult result
	* @param HttpServletRequest request
	* @return Map<String, Object>
	* @author 盖旭
	* @version 2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档历史",operateFuncNm="update",operateDescribe="更新文档历史信息")
	public Map<String, Object> update(History history, BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = historyService.update(history);
		if (flag == 1) {
			resultMap.put("success", "true");
		} else {
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,MessageUtils.getMessage("JC_OA_COMMON_014"));
		}
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param History history
	 * @param HttpServletRequest request
	 * @return History
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档历史",operateFuncNm="get",operateDescribe="查询单条文档历史")
	public History get(History history,HttpServletRequest request) throws Exception{
		return historyService.get(history);
	}
}