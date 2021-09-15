package com.jc.system.api.web;

import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.JsonUtil;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.util.SpringContextHolder;
import com.jc.system.api.service.IUserSync;
import com.jc.system.api.util.ApiServiceUtil;
import com.jc.system.api.util.UserSyncUtil;
import com.jc.system.content.service.IUploadService;
import com.jc.system.security.domain.User;

import com.jc.system.security.service.IUserService;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value = "/api/user")
public class ApiUserSyncController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiUserSyncController.class);

    @Autowired(required=false)
    private IUserSync apiUserSync;

	@Autowired
	private IUserService userService;
    
    @Autowired
	private IUploadService uploadService;

    public ApiUserSyncController() {
    }

	/**
	 * 单一用户推送接口
	 * @param userid
	 */
	@RequestMapping(value = "pushuser.action",method=RequestMethod.GET)
	@ResponseBody
	public void pushUser(String userid){
		IUserService userService = SpringContextHolder.getBean(IUserService.class);
		if(GlobalContext.isSysCenter()) {
			try {
				User user = userService.getUser(userid);
				UserSyncUtil.push(user);
			} catch (Exception e) {
				LOGGER.error("用户信息推送失败！");
			}
		}
	}

	/**
	 * 用户同步接口
	 * @param jsonparams
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "sync",method=RequestMethod.GET)
    @ResponseBody
    public String sync(String jsonparams) throws Exception {
		String returnJson = "";
		if(apiUserSync == null){
			Map<String,String> errorMap = new HashMap<>(2);
	    	errorMap.put("status", "1");
	    	errorMap.put("msg", "接口未实现");
	    	return JsonUtil.java2Json(errorMap);
		}
		try{
			User user = (User) JsonUtil.json2Java(jsonparams, User.class);
			boolean result = apiUserSync.sync(user);
			if(result == true){
				Map<String,String> successMap = new HashMap<>(2);
				successMap.put("status", "0");
				successMap.put("msg", "接口调用成功");
    	    	returnJson = JsonUtil.java2Json(successMap);
			}else{
				Map<String,String> errorMap = new HashMap<>(2);
    	    	errorMap.put("status", "2");
    	    	errorMap.put("msg", "接口调用失败");
    	    	returnJson = JsonUtil.java2Json(errorMap);
			}
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			Map<String,String> errorMap = new HashMap<>(2);
	    	errorMap.put("status", "2");
	    	errorMap.put("msg", "接口调用失败");
	    	returnJson = JsonUtil.java2Json(errorMap);
		}
		return returnJson;
	}

	/**
	 * 用户相片同步接口
	 * @param jsonparams
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "syncphoto",method=RequestMethod.GET)
    @ResponseBody
    public String syncPhoto(String jsonparams,HttpServletRequest request) throws Exception {
		String returnJson = "";
		User user = (User) JsonUtil.json2Java(jsonparams, User.class);
		try{
			Attach attach = uploadService.uploadFile(request, "userPhotoImg", user);
			Map<String,String> successMap = new HashMap<>(2);
			successMap.put("status", "0");
			successMap.put("data", String.valueOf(attach.getId()));
			successMap.put("msg", "接口调用成功");
	    	returnJson = JsonUtil.java2Json(successMap);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			Map<String,String> errorMap = new HashMap<>(2);
	    	errorMap.put("status", "2");
	    	errorMap.put("msg", "接口调用失败");
	    	returnJson = JsonUtil.java2Json(errorMap);
		}
		return returnJson;
	}

	/**
	 * 根据登录名称获取用户详细信息
	 * @param request
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "getUserByLoginName.action",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getUserByLoginName(HttpServletRequest request, User user) {
		Map<String, Object> result = new HashMap<>(3);
		if(user == null || "".equals(user.getLoginName())){
			result.put("code", "200000");
			result.put("errormsg", MessageUtils.getMessage("JC_SYS_131"));
			result.put("body", null);
			return result;
		}
		User body;
		try {
			body = userService.getUserByLoginName(user.getLoginName());
			if(body != null){
				body.setPhoto(ApiServiceUtil.getUserPhotoAbsolutePath(request, body.getPhoto()));
			}
		} catch (CustomException e) {
			result.put("code", "200000");
			result.put("errormsg", e.getMessage());
			result.put("body", null);
			return result;
		}
		result.put("code", "000000");
		result.put("errormsg", "");
		result.put("body", body);
		return result;
	}

	/**
	 * 根据关键字模糊查询用户集合
	 * @param request
	 * @param keyword
	 * @return
	 */
	@RequestMapping(value = "queryByKeyword.action",method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryByKeyword(HttpServletRequest request, String keyword) {
		Map<String, Object> result = new HashMap<>(2);
		if(keyword == null || "".equals(keyword)){
			result.put("code", "000000");
			result.put("errormsg", "");
			result.put("body", null);
			return result;
		}
		try {
			User user = new User();
			user.setDisplayName(keyword);
			if(StringUtils.isEmpty(user.getOrderBy())) {
				user.addOrderByField("t.ORDER_NO");
			}
			if(user.getStatus() != null && user.getStatus().equals(GlobalContext.USER_STATUS_3)){
				user.setDeleteFlag(1);
			} else {
				user.setDeleteFlag(null);
			}
			List<User> body = userService.queryAll(user);
			for (User u : body) {
        		u.setPhoto(ApiServiceUtil.getUserPhotoAbsolutePath(request, u.getPhoto()));
			}
			result.put("code", "000000");
			result.put("errormsg", "");
			result.put("body", body);
		} catch (CustomException e) {
			result.put("code", "200000");
			result.put("errormsg", e.getMessage());
			result.put("body", null);
		}
		return result;
	}
}