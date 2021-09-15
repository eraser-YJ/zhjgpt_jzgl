package com.jc.archive.web;

import java.util.Date;
import java.util.HashMap;
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
import com.jc.archive.domain.Document;
import com.jc.archive.domain.Relate;
import com.jc.archive.domain.validator.RelateValidator;
import com.jc.archive.service.IAudithisService;
import com.jc.archive.service.IDocumentService;
import com.jc.archive.service.IRelateService;
import com.jc.system.applog.ActionLog;
import com.jc.system.common.util.Constants;
import com.jc.system.security.SystemSecurityUtils;


/**
 * @title  GOA2.5源代码
 * @description  controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version  2014-06-05
 */
 
@Controller
@RequestMapping(value="/archive/relate")
public class RelateController extends BaseController{
	
	@Autowired
	private IRelateService relateService;

	@Autowired
	private IAudithisService audithisService;

	@Autowired
	private IDocumentService documentService;
	
	@org.springframework.web.bind.annotation.InitBinder("relate")
    public void initBinder(WebDataBinder binder) {  
        	binder.setValidator(new RelateValidator()); 
    }
	
	public RelateController() {
	}

	/**
	 * @deprecated 分页查询方法
	 * @param Relate relate 实体类
	 * @param PageManager page 分页实体类
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="manageList",operateDescribe="文档关联查询")
	public PageManager manageList(Relate relate,PageManager page,HttpServletRequest request){
		PageManager page_ = null;
		try{
			relate.setOrderBy("t.CREATE_DATE desc");
			page_ = relateService.query(relate, page);
		}catch(Exception e){
			System.out.print(e);
		}
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
	@ActionLog(operateModelNm="文档管理",operateFuncNm="manage",operateDescribe="文档关联跳转页面")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/relate/relate1"; 
	}

/**
	 * @deprecated 删除方法
	 * @param Relate relate 实体类
	 * @param String ids 删除的多个主键
	 * @return Map 删除结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="deleteByIds",operateDescribe="删除文档关联信息")
	public Map<String, Object> deleteByIds(Relate relate,String ids, HttpServletRequest request) throws Exception{

		Map<String, Object> resultMap = new HashMap<String, Object>();
		relate.setPrimaryKeys(ids.split(","));
		relate.setModifyDate(new Date());
		relate.setModifyUser(SystemSecurityUtils.getUser().getId());
		Relate rel = new Relate();
		rel.setId(ids);
		rel = relateService.get(rel);
		if(relateService.delete(relate) > 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_005"));
		}
		audithisService.audithis(request, rel.getId(),
				rel.getDmName(),
				Constants.ARC_AUDITHIS_DATATYPE_DOC,
				Constants.ARC_AUDITHIS_RELATE, "删除关联文档");
		return resultMap;
	}

	/**
	 * @deprecated 保存方法
	 * @param Relate relate 实体类
	 * @param BindingResult result 校验结果
	 * @return Map success 是否成功， errorMessage错误信息
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="save",operateDescribe="新增文档关联信息")
	public Map<String,Object> save(@Valid Relate relate,BindingResult result,
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
			relateService.save(relate);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	/**
	* @deprecated 修改方法
	* @param Relate relate 实体类
	* @return Map 更新结果
	* @author 盖旭
	* @version  2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="update",operateDescribe="更新文档关联信息")
	public Map<String, Object> update(Relate relate, BindingResult result,
			HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = relateService.update(relate);
		if (flag == 1) {
			resultMap.put("success", "true");
		} else {
			resultMap.put("success", "false");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE,
					MessageUtils.getMessage("JC_OA_COMMON_014"));
		}
		return resultMap;
	}

	/**
	 * @deprecated 获取单条记录方法
	 * @param Relate relate 实体类
	 * @return Relate 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="get",operateDescribe="查询文档关联单条信息")
	public Relate get(Relate relate,HttpServletRequest request) throws Exception{
		return relateService.get(relate);
	}
	
	/**
	 * @deprecated 根据选择的文档批量保存关联关系方法
	 * @param request
	 * @return Map 保存结果
	 * @throws Exception
	 * @author 盖旭
	 * @version  2014-07-01
	 */
	@RequestMapping(value="saveByDocumentIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="文档管理",operateFuncNm="saveByDocumentIds",operateDescribe="批量保存关联关系信息")
	public Map<String, Object> saveByDocumentIds(HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String documentId = request.getParameter("documentId");//文档id
		String documentIds = request.getParameter("documentIds");//关联的文档id
		// 保存元素
		if (relateService.saveByDocumentIds(documentId,documentIds) > 0) {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_001"));
		}
		Document doc = new Document();
		doc.setId(documentId);
		doc = documentService.get(doc);
		audithisService.audithis(request, documentId,
				doc.getDmName(),
				Constants.ARC_AUDITHIS_DATATYPE_DOC,
				Constants.ARC_AUDITHIS_RELATE, "添加关联文档");
		return resultMap;
	}
}