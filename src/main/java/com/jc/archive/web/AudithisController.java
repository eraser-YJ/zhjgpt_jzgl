package com.jc.archive.web;

import com.jc.archive.domain.Audithis;
import com.jc.archive.domain.validator.AudithisValidator;
import com.jc.archive.service.IAudithisService;
import com.jc.archive.util.PermissionUtil;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.web.BaseController;
import com.jc.oa.click.ActionClick;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.User;
import com.jc.system.security.util.UserUtils;
import com.jc.system.util.menuUtil;
import org.apache.commons.lang3.StringUtils;
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
 * @title 嘉诚智能政务办公平台
 * @description  文档审计controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version 2014-06-05
 */
 
@Controller
@RequestMapping(value="/archive/audithis")
public class AudithisController extends BaseController{
	
	@Autowired
	private IAudithisService audithisService;
	
	@org.springframework.web.bind.annotation.InitBinder("audithis")
    public void initBinder(WebDataBinder binder) {  
        binder.setValidator(new AudithisValidator()); 
    }
	
	public AudithisController() {
	}

	/**
	 * @description 分页查询方法
	 * @param HttpServletRequest request
	 * @param Audithis audithis
	 * @param PageManager page
	 * @return PagingBean
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档审计",operateFuncNm="manageList",operateDescribe="查询文档审计历史")
	public PageManager manageList(HttpServletRequest request,Audithis audithis,PageManager page){
		//默认排序
		if(StringUtils.isEmpty(audithis.getOrderBy())) {
			audithis.addOrderByFieldDesc("t.CREATE_DATE");
		}
		if(audithis.getEndDate() !=null){
			audithis.setEndDate(audithis.getEndDate()+" 23:59:59");
		}
		PageManager page_ = null;
		if(audithis.getDataId() == null) {
			//文档审计菜单调用
			page_ = audithisService.queryByPermission(audithis, page);
		} else {
			// 公共文档中文档审计调用
			page_ = audithisService.query(audithis, page);
		}
		if(page_ ==null || page_.getAaData() == null || page_.getAaData().size()<1){
			return page;
		}
		for(int i=0;i<page_.getAaData().size();i++){
			Audithis rec = (Audithis) page_.getAaData().get(i);
			User user = UserUtils.getUser(rec.getUserId());
			if(user  == null) {
				//由于操作的人可能被删除，再通过id查询将查不到操作人，所以在保存操作记录时已将操作人保存到扩展字段extstr1中
				rec.setUserName(rec.getExtStr1());
			} else {
				 rec.setUserName(UserUtils.getUser(rec.getUserId()).getDisplayName());
			}
			rec.setAuditTypeValue(PermissionUtil.permissionType(rec.getAuditType()));
		}
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
    @ActionClick(menuAction = "/archive/audithis/manage.action")
	@RequestMapping(value="manage.action")
	@ActionLog(operateModelNm="文档审计",operateFuncNm="manage",operateDescribe="跳转文档审计历史")
	public String manage(HttpServletRequest request) throws Exception{
		menuUtil.saveMenuID("/archive/audithis/manage.action",request);
		return "archive/audithis"; 
	}
	
	/**
	 * @description 跳转方法
	 * @return String 跳转的路径
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05 
	 */
	@RequestMapping(value="manageTest.action")
//	@ActionLog(operateModelNm="文档审计",operateFuncNm="manage",operateDescribe="文档审计历史跳转页面")
	public String manageTest(HttpServletRequest request) throws Exception{
		return "archive/MyJsp"; 
	}

    /**
	 * @description 删除方法
	 * @param HttpServletRequest request
	 * @param Audithis audithis
	 * @param String ids
	 * @return Integer
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档审计",operateFuncNm="deleteByIds",operateDescribe="删除文档审计历史")
	public Integer deleteByIds(HttpServletRequest request,Audithis audithis,String ids) throws Exception{
		audithis.setPrimaryKeys(ids.split(","));
		return audithisService.delete(audithis);
	}

	/**
	 * @description 保存方法
	 * @param Audithis audithis
	 * @param BindingResult result
	 * @param HttpServletRequest request
	 * @return Map success
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档审计",operateFuncNm="save",operateDescribe="新增文档审计历史")
	public Map<String,Object> save(@Valid Audithis audithis,BindingResult result,HttpServletRequest request) throws Exception{
		
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
			audithisService.save(audithis);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

   /**
	* @description 修改方法
	* @param Audithis audithis
	* @param BindingResult result
	* @param HttpServletRequest request
	* @return Map<String, Object>
	* @author 盖旭
	* @version 2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档审计",operateFuncNm="update",operateDescribe="更新文档审计历史")
	public Map<String, Object> update(Audithis audithis, BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = audithisService.update(audithis);
		if (flag == 1) {
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	 * @description 获取单条记录方法
	 * @param Audithis audithis
	 * @param HttpServletRequest request
	 * @return Audithis
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档审计",operateFuncNm="get",operateDescribe="查询单条文档审计历史")
	public Audithis get(HttpServletRequest request,Audithis audithis) throws Exception{
		return audithisService.get(audithis);
	}

	/**
	 * @description 分页查询方法
	 * @param HttpServletRequest request
	 * @param Audithis audithis
	 * @param PageManager page
	 * @return PagingBean
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05 
	 */
	@RequestMapping(value="myOperationList.action")
	@ResponseBody
	@ActionLog(operateModelNm="我的操作",operateFuncNm="myOperationList",operateDescribe="查询我的操作历史")
	public PageManager myOperationList(HttpServletRequest request,Audithis audithis,PageManager page){
		//默认排序
		if(StringUtils.isEmpty(audithis.getOrderBy())) {
			audithis.addOrderByFieldDesc("t.CREATE_DATE");
		}
		audithis.setUserId(SystemSecurityUtils.getUser().getId());
		if(audithis.getEndDate() !=null){
			audithis.setEndDate(audithis.getEndDate()+" 23:59:59");
		}
		PageManager page_ = audithisService.query(audithis, page);
		if(page_ ==null || page_.getAaData() == null || page_.getAaData().size()<1){
			return page;
		}
		for(int i=0;i<page_.getAaData().size();i++){
			Audithis rec = (Audithis) page_.getAaData().get(i);
			rec.setUserName(UserUtils.getUser(rec.getUserId()).getDisplayName());
			rec.setAuditType(PermissionUtil.permissionType(rec.getAuditType()));
		}
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
   @ActionClick(menuAction = "/archive/audithis/myOperation.action")
	@RequestMapping(value="myOperation.action")
	@ActionLog(operateModelNm="我的操作",operateFuncNm="myOperation",operateDescribe="跳转我的操作历史")
	public String myOperation(HttpServletRequest request) throws Exception{
	   menuUtil.saveMenuID("/archive/audithis/myOperation.action",request);
		return "archive/myOperation"; 
	}

}