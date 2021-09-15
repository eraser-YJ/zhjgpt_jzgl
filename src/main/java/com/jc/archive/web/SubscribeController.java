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
import com.jc.foundation.web.BaseController;
import com.jc.archive.domain.Subscribe;
import com.jc.archive.domain.validator.SubscribeValidator;
import com.jc.archive.service.ISubscribeService;
import com.jc.system.applog.ActionLog;


/**
 * @title  GOA2.5源代码
 * @description  controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version  2014-06-05
 */
 
@Controller
@RequestMapping(value="/archive/subscribe")
public class SubscribeController extends BaseController{
	
	@Autowired
	private ISubscribeService subscribeService;
	
	@org.springframework.web.bind.annotation.InitBinder("subscribe")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new SubscribeValidator()); 
    }
	
	public SubscribeController() {
	}

	/**
	 * @deprecated 分页查询方法
	 * @param Subscribe subscribe 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="manageList",operateDescribe="查询知识订阅信息")
	public PageManager manageList(Subscribe subscribe,PageManager page, HttpServletRequest request){
		PageManager page_ = subscribeService.query(subscribe, page);
		return page_; 
	}

	/**
	* @deprecated 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author 盖旭
	* @version  2014-06-05 
	*/
	@RequestMapping(value="manage.action")
	@ActionLog(operateModelNm="知识管理",operateFuncNm="manage",operateDescribe="知识订阅跳转页面")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/subscribe/subscribe1"; 
	}

/**
	 * @deprecated 删除方法
	 * @param Subscribe subscribe 实体类
	 * @param String ids 删除的多个主键
	 * @return Integer 删除 结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="deleteByIds",operateDescribe="删除知识订阅信息")
	public Integer deleteByIds(Subscribe subscribe,String ids, HttpServletRequest request) throws Exception{
		subscribe.setPrimaryKeys(ids.split(","));
		return subscribeService.delete(subscribe);
	}

	/**
	 * @deprecated 保存方法
	 * @param Subscribe subscribe 实体类
	 * @param BindingResult result 校验结果
	 * @return Map success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="save",operateDescribe="新增知识订阅信息")
	public Map<String,Object> save(@Valid Subscribe subscribe,BindingResult result,
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
			subscribeService.save(subscribe);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	* @deprecated 修改方法
	* @param Subscribe subscribe 实体类
	* @return Map 更新结果
	* @author 盖旭
	* @version  2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="update",operateDescribe="更新知识订阅信息")
	public Map<String, Object> update(Subscribe subscribe, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = subscribeService.update(subscribe);
		if (flag == 1) {
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	 * @deprecated 获取单条记录方法
	 * @param Subscribe subscribe 实体类
	 * @return Subscribe 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="get",operateDescribe="查询知识订阅单条信息")
	public Subscribe get(Subscribe subscribe, HttpServletRequest request) throws Exception{
		return subscribeService.get(subscribe);
	}

}