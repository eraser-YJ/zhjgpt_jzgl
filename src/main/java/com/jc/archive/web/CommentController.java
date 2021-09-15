package com.jc.archive.web;

import com.jc.foundation.domain.PageManager;
import com.jc.foundation.web.BaseController;
import com.jc.archive.domain.Comment;
import com.jc.archive.domain.validator.CommentValidator;
import com.jc.archive.service.ICommentService;
import com.jc.system.applog.ActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;


/**
 * @title  嘉诚智能政务办公平台
 * @description  知识评论controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version 2014-06-05
 */
 
@Controller
@RequestMapping(value="/archive/comment")
public class CommentController extends BaseController{
	
	@Autowired
	private ICommentService commentService;
	
	@org.springframework.web.bind.annotation.InitBinder("comment")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new CommentValidator()); 
    }
	
	public CommentController() {
	}

	/**
	 * @description 分页查询方法
	 * @param Comment comment
	 * @param PageManager page
	 * @param HttpServletRequest request
	 * @return PagingBean
	 * @author 盖旭
	 * @version 2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识评论",operateFuncNm="manageList",operateDescribe="查询知识评论")
	public PageManager manageList(Comment comment,PageManager page, HttpServletRequest request){
		PageManager page_ = commentService.query(comment, page);
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
	@ActionLog(operateModelNm="知识评论",operateFuncNm="manage",operateDescribe="跳转知识评论")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/comment/comment1"; 
	}

    /**
	 * @description 删除方法
	 * @param Comment comment
	 * @param String ids
	 * @return Integer
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识评论",operateFuncNm="deleteByIds",operateDescribe="删除知识评论")
	public Integer deleteByIds(Comment comment,String ids, HttpServletRequest request) throws Exception{
		comment.setPrimaryKeys(ids.split(","));
		return commentService.delete(comment);
	}

	/**
	 * @description 保存方法
	 * @param Comment comment
	 * @param BindingResult result
	 * @param HttpServletRequest request
	 * @return Map<String,Object>
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识评论",operateFuncNm="save",operateDescribe="新增知识评论")
	public Map<String,Object> save(@Valid Comment comment,BindingResult result,HttpServletRequest request) throws Exception{
		
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
			commentService.save(comment);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

   /**
	* @description 修改方法
	* @param Comment comment
	* @param BindingResult result
	* @param HttpServletRequest request
	* @return Map<String, Object>
	* @author 盖旭
	* @version 2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识评论",operateFuncNm="update",operateDescribe="更新知识评论")
	public Map<String, Object> update(Comment comment, BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = commentService.update(comment);
		if (flag == 1) {
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param Comment comment
	 * @param HttpServletRequest request
	 * @return Comment
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="知识评论",operateFuncNm="get",operateDescribe="查询单条知识评论")
	public Comment get(Comment comment, HttpServletRequest request) throws Exception{
		return commentService.get(comment);
	}

}