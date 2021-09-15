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
import com.jc.archive.domain.Recommend;
import com.jc.archive.domain.validator.RecommendValidator;
import com.jc.archive.service.IRecommendService;
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
@RequestMapping(value="/archive/recommend")
public class RecommendController extends BaseController{
	
	@Autowired
	private IRecommendService recommendService;
	
	@org.springframework.web.bind.annotation.InitBinder("recommend")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new RecommendValidator()); 
    }
	
	public RecommendController() {
	}

	/**
	 * @deprecated 分页查询方法
	 * @param Recommend recommend 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识推荐",operateFuncNm="manageList",operateDescribe="查询知识推荐信息")
	public PageManager manageList(Recommend recommend,PageManager page,HttpServletRequest request){
		PageManager page_ = recommendService.query(recommend, page);
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
	@ActionLog(operateModelNm="知识推荐",operateFuncNm="manage",operateDescribe="知识推荐跳转页面")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/recommend/recommend1"; 
	}

/**
	 * @deprecated 删除方法
	 * @param Recommend recommend 实体类
	 * @param String ids 删除的多个主键
	 * @return Integer 删除结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识推荐",operateFuncNm="deleteByIds",operateDescribe="删除知识推荐信息")
	public Integer deleteByIds(Recommend recommend,String ids,HttpServletRequest request) throws Exception{
		recommend.setPrimaryKeys(ids.split(","));
		return recommendService.delete(recommend);
	}

	/**
	 * @deprecated 保存方法
	 * @param Recommend recommend 实体类
	 * @param BindingResult result 校验结果
	 * @return Map success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识推荐",operateFuncNm="save",operateDescribe="新增知识推荐信息")
	public Map<String,Object> save(@Valid Recommend recommend,BindingResult result,
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
			recommendService.save(recommend);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	* @deprecated 修改方法
	* @param Recommend recommend 实体类
	* @return Map 更新结果
	* @author 盖旭
	* @version  2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识推荐",operateFuncNm="update",operateDescribe="更新知识推荐信息")
	public Map<String, Object> update(Recommend recommend, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = recommendService.update(recommend);
		if (flag == 1) {
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	 * @deprecated 获取单条记录方法
	 * @param Recommend recommend 实体类
	 * @return Recommend 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识推荐",operateFuncNm="get",operateDescribe="查询知识推荐单条信息")
	public Recommend get(Recommend recommend,HttpServletRequest request) throws Exception{
		return recommendService.get(recommend);
	}
}