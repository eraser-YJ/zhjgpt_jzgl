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
import com.jc.archive.domain.Readinfo;
import com.jc.archive.domain.validator.ReadinfoValidator;
import com.jc.archive.service.IReadinfoService;
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
@RequestMapping(value="/archive/readinfo")
public class ReadinfoController extends BaseController{
	
	@Autowired
	private IReadinfoService readinfoService;
	
	@org.springframework.web.bind.annotation.InitBinder("readinfo")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new ReadinfoValidator()); 
    }
	
	public ReadinfoController() {
	}

	/**
	 * @deprecated 分页查询方法
	 * @param ExchangeReadinfo readinfo 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="manageList",operateDescribe="查询知识阅读信息")
	public PageManager manageList(Readinfo readinfo,PageManager page,HttpServletRequest request){
		PageManager page_ = readinfoService.query(readinfo, page);
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
	@ActionLog(operateModelNm="知识管理",operateFuncNm="manage",operateDescribe="知识阅读跳转页面")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/readinfo/readinfo1"; 
	}

/**
	 * @deprecated 删除方法
	 * @param Readinfo readinfo 实体类
	 * @param String ids 删除的多个主键
	 * @return Integer 删除结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="deleteByIds",operateDescribe="删除知识阅读信息")
	public Integer deleteByIds(Readinfo readinfo,String ids,HttpServletRequest request) throws Exception{
		readinfo.setPrimaryKeys(ids.split(","));
		return readinfoService.delete(readinfo);
	}

	/**
	 * @deprecated 保存方法
	 * @param Readinfo readinfo 实体类
	 * @param BindingResult result 校验结果
	 * @return Map success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="save",operateDescribe="新增知识阅读信息")
	public Map<String,Object> save(@Valid Readinfo readinfo,BindingResult result,
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
			readinfoService.save(readinfo);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	* @deprecated 修改方法
	* @param Readinfo readinfo 实体类
	* @param result
	* @param request
	* @return Map 更新结果
	* @author 盖旭
	* @version  2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="update",operateDescribe="更新知识阅读信息")
	public Map<String, Object> update(Readinfo readinfo, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = readinfoService.update(readinfo);
		if (flag == 1) {
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	 * @deprecated 获取单条记录方法
	 * @param Readinfo readinfo 实体类
	 * @return Readinfo 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="get",operateDescribe="查询知识阅读单条信息")
	public Readinfo get(Readinfo readinfo,HttpServletRequest request) throws Exception{
		return readinfoService.get(readinfo);
	}
}