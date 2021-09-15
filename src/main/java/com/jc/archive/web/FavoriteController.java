package com.jc.archive.web;

import com.jc.archive.domain.Favorite;
import com.jc.archive.domain.validator.FavoriteValidator;
import com.jc.archive.service.IFavoriteService;
import com.jc.foundation.domain.PageManager;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.web.BaseController;
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
 * @description  收藏信息 controller类
 * Copyright (c) 2014 yixunnet.com Inc. All Rights Reserved
 * Company 长春嘉诚网络工程有限公司
 * @author 盖旭
 * @version 2014-06-05
 */
 
@Controller
@RequestMapping(value="/archive/favorite")
public class FavoriteController extends BaseController{
	
	@Autowired
	private IFavoriteService favoriteService;
	
	@org.springframework.web.bind.annotation.InitBinder("favorite")
    public void initBinder(WebDataBinder binder) {  
        binder.setValidator(new FavoriteValidator()); 
    }
	
	public FavoriteController() {
	}

	/**
	 * @description 分页查询方法
	 * @param Favorite favorite
	 * @param PageManager page
	 * @param HttpServletRequest request
	 * @return PagingBean 查询结果
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05 
	 */
	@RequestMapping(value="manageList.action")
	@ResponseBody
	@ActionLog(operateModelNm="收藏信息",operateFuncNm="manageList",operateDescribe="查询收藏信息")
	public PageManager manageList(Favorite favorite,PageManager page,HttpServletRequest request){
		PageManager page_ = favoriteService.query(favorite, page);
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
	@ActionLog(operateModelNm="收藏信息",operateFuncNm="manage",operateDescribe="跳转收藏信息")
	public String manage(HttpServletRequest request) throws Exception{
		return "archive/favorite/favorite1"; 
	}

    /**
	 * @description 删除方法
	 * @param Favorite favorite
	 * @param String ids
	 * @param HttpServletRequest request
	 * @return Integer
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value="deleteByIds.action")
	@ResponseBody
	@ActionLog(operateModelNm="收藏信息",operateFuncNm="deleteByIds",operateDescribe="删除收藏信息")
	public Integer deleteByIds(Favorite favorite,String ids,HttpServletRequest request) throws Exception{
		favorite.setPrimaryKeys(ids.split(","));
		return favoriteService.delete(favorite);
	}

	/**
	 * @description 保存方法
	 * @param Favorite favorite
	 * @param BindingResult result
	 * @param HttpServletRequest request
	 * @return Map<String,Object>
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value = "save.action")
	@ResponseBody
	@ActionLog(operateModelNm="收藏信息",operateFuncNm="save",operateDescribe="新增收藏信息")
	public Map<String,Object> save(@Valid Favorite favorite,BindingResult result,HttpServletRequest request) throws Exception{
		
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
			favoriteService.save(favorite);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

   /**
	* @description 修改方法
	* @param Favorite favorite
	* @param BindingResult result
	* @param HttpServletRequest request
	* @return Map<String, Object>
	* @author 盖旭
	* @version 2014-06-05 
	*/
	@RequestMapping(value="update.action")
	@ResponseBody
	@ActionLog(operateModelNm="收藏信息",operateFuncNm="update",operateDescribe="更新收藏信息")
	public Map<String, Object> update(Favorite favorite, BindingResult result,HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = validateBean(result);
		if (resultMap.size() > 0) {
			return resultMap;
		}
		
		Integer flag = favoriteService.update(favorite);
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
	 * @param Favorite favorite
	 * @param HttpServletRequest request
	 * @return Favorite
	 * @throws Exception
	 * @author 盖旭
	 * @version 2014-06-05
	 */
	@RequestMapping(value="get.action")
	@ResponseBody
	@ActionLog(operateModelNm="收藏信息",operateFuncNm="get",operateDescribe="收藏信息单条查询")
	public Favorite get(Favorite favorite,HttpServletRequest request) throws Exception{
		return favoriteService.get(favorite);
	}

}