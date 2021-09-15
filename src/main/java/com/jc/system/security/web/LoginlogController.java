package com.jc.system.security.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.jc.foundation.web.BaseController;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.util.menuUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.foundation.domain.PageManager;
import com.jc.system.applog.ActionLog;
import com.jc.system.security.domain.Loginlog;
import com.jc.system.security.domain.User;
import com.jc.system.security.domain.validator.LoginlogValidator;
import com.jc.system.security.service.ILoginlogService;

/**
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
@RequestMapping(value="/sys/loginlog")
public class LoginlogController extends BaseController {

	@Autowired
	private ILoginlogService loginlogService;

	@org.springframework.web.bind.annotation.InitBinder("loginlog")
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new LoginlogValidator());
	}

	public LoginlogController() {
	}

	@RequestMapping(value="manageList.action",method=RequestMethod.GET)
	@ResponseBody
	@ActionLog(operateModelNm="登录日志",operateFuncNm="manageList",operateDescribe="对登录日志进行查询")
	public PageManager manageList(Loginlog loginlog,PageManager page,HttpServletRequest request){
		//默认排序
		if(StringUtils.isEmpty(loginlog.getOrderBy())) {
			loginlog.addOrderByFieldDesc("(case when t.LOGIN_TIME is null then t.LOGOUT_TIME else t.LOGIN_TIME end)");
		}
		PageManager rePage = loginlogService.query(loginlog, page);
		return rePage;
	}

	@RequestMapping(value="manage.action",method=RequestMethod.GET)
	@ActionLog(operateModelNm="登录日志",operateFuncNm="manage",operateDescribe="对登录日志进行访问")
	public String manage(HttpServletRequest request) throws Exception{
		User userInfo = SystemSecurityUtils.getUser();
		if(userInfo != null){
			if(userInfo.getIsAdmin().equals(3) || userInfo.getIsSystemAdmin()){
				menuUtil.saveMenuID("/sys/loginlog/manage.action",request);
				return "sys/loginlog/loginlog";
			} else {
				return "error/unauthorized";
			}
		}
		return "error/unauthorized";
	}

	@RequestMapping(value="deleteByIds.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer deleteByIds(Loginlog loginlog,String ids) throws Exception{
		loginlog.setPrimaryKeys(ids.split(","));
		return loginlogService.delete(loginlog);
	}

	@RequestMapping(value = "save.action",method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> save(@Valid Loginlog loginlog,BindingResult result) throws Exception{

		Map<String,Object> resultMap = validateBean(result);
		if(!"false".equals(resultMap.get("success"))){
			loginlogService.save(loginlog);
			resultMap.put("success", "true");
		}
		return resultMap;
	}

	@RequestMapping(value="update.action",method=RequestMethod.POST)
	@ResponseBody
	public Integer update(Loginlog loginlog) throws Exception{
		Integer flag = loginlogService.update(loginlog);
		return flag;
	}

	@RequestMapping(value="get.action",method=RequestMethod.GET)
	@ResponseBody
	public Loginlog get(Loginlog loginlog) throws Exception{
		return loginlogService.get(loginlog);
	}

	@RequestMapping(value="manageloginlog.action",method=RequestMethod.GET)
	public String manageloginlog(Model model,Loginlog loginlog,HttpServletRequest request) throws Exception{
		request.getParameter("parameter");
		if(StringUtils.isEmpty(loginlog.getOrderBy())) {
			loginlog.addOrderByFieldDesc("(case when t.LOGIN_TIME is null then t.LOGOUT_TIME else t.LOGIN_TIME end)");
		}
		List<Loginlog> returnlist = loginlogService.queryAll(loginlog);
		model.addAttribute("loginlogList", returnlist);
		model.addAttribute("loginlogListSize", returnlist.size());
		return "sys/loginlog/loginlogPortlet";
	}

}