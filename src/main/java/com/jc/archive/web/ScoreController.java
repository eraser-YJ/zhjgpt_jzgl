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
import com.jc.archive.domain.Score;
import com.jc.archive.domain.validator.ScoreValidator;
import com.jc.archive.service.IScoreService;
import com.jc.system.applog.ActionLog;


/**
 * @title  GOA2.0源代码
 * @description  controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 
 * @version  2014-06-05
 */
 
@Controller
@RequestMapping(value="/archive/score")
public class ScoreController extends BaseController{
	
	@Autowired
	private IScoreService scoreService;
	
	@org.springframework.web.bind.annotation.InitBinder("score")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new ScoreValidator()); 
    }
	
	public ScoreController() {
	}

	/**
	 * 分页查询方法
	 * @param Score score 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author
	 * @version  2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="manageList",operateDescribe="查询知识评分信息")
	public PageManager manageList(Score score,PageManager page, HttpServletRequest request){
		PageManager page_ = scoreService.query(score, page);
		return page_; 
	}

	/**
	* 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2014-06-05 
	*/
	@RequestMapping(value="manage.action")
	@ActionLog(operateModelNm="知识管理",operateFuncNm="manage",operateDescribe="知识评分跳转页面")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/score/score1"; 
	}

/**
	 * 删除方法
	 * @param Score score 实体类
	 * @param String ids 删除的多个主键
	 * @return Integer 成功删除
	 * @throws Exception
	 * @author
	 * @version  2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="deleteByIds",operateDescribe="删除知识评分信息")
	public Integer deleteByIds(Score score,String ids, HttpServletRequest request) throws Exception{
		score.setPrimaryKeys(ids.split(","));
		return scoreService.delete(score);
	}

	/**
	 * 保存方法
	 * @param Score score 实体类
	 * @param BindingResult result 校验结果
	 * @return success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author
	 * @version  2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="save",operateDescribe="新增知识评分信息")
	public Map<String,Object> save(@Valid Score score,BindingResult result,
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
			scoreService.save(score);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	* 修改方法
	* @param Score score 实体类
	* @return Integer 更新的数目
	* @author
	* @version  2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="update",operateDescribe="更新知识评分信息")
	public Map<String, Object> update(Score score, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = scoreService.update(score);
		if (flag == 1) {
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	 * 获取单条记录方法
	 * @param Score score 实体类
	 * @return Score 查询结果
	 * @throws Exception
	 * @author
	 * @version  2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识管理",operateFuncNm="get",operateDescribe="查询知识评分单条信息")
	public Score get(Score score, HttpServletRequest request) throws Exception{
		return scoreService.get(score);
	}

}