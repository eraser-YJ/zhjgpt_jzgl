package com.jc.system.session.web;

import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.MessageUtils;
import com.jc.foundation.web.BaseController;
import com.jc.system.applog.ActionLog;
import com.jc.system.session.domain.SessionService;
import com.jc.system.session.service.ISessionServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 在线人员
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/sessionService")
public class SessionServiceController extends BaseController{
	
	@Autowired
	private ISessionServiceService sessionServiceService;
	
	public SessionServiceController() {
	}

	/**
	 * @description
	 * @return 在线人员数组
	 * @throws Exception
	 * @author
	 * @version  2019-11-13 
	 */
	@RequestMapping(value="sessionList.action",method=RequestMethod.POST)
	@ResponseBody
	public void manageList(HttpServletRequest request,HttpServletResponse response)  throws Exception {
		String jsonArray = sessionServiceService.getDeptAndUserByOnLine();
		sendJson(response, jsonArray);
	}

	protected void sendJson(HttpServletResponse response, String content) throws IOException {
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().print(content);
		response.getWriter().flush();
		response.getWriter().close();
	}

	/**
	* @description 跳转方法
	* @return String 跳转的路径
	* @throws Exception
	* @author
	* @version  2019-11-13 
	*/
	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="TTY_SYS_SESSION_SERVICE",operateFuncNm="manage",operateDescribe="对会话管理进行访问")
	public String manage(HttpServletRequest request) throws Exception{
		return "sys/sessionService/sessionServiceList"; 
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	@ActionLog(operateModelNm="TTY_SYS_SESSION_SERVICE",operateFuncNm="deleteByIds",operateDescribe="对用户进行踢出操作")
	public  Map<String, Object> deleteByIds(SessionService sessionService,String ids, HttpServletRequest request) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		sessionService.setPrimaryKeys(ids.split(","));
		sessionService.setUserId(ids);
		if(sessionServiceService.deleteByIds(sessionService) != 0){
			resultMap.put(GlobalContext.RESULT_SUCCESS, "true");
			resultMap.put(GlobalContext.RESULT_SUCCESSMESSAGE, MessageUtils.getMessage("JC_SYS_161"));
		} else {
			resultMap.put(GlobalContext.RESULT_SUCCESS, "false");
			resultMap.put(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_162"));
		}
		return resultMap;
	}

}