package com.jc.system.security.web;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jc.csmp.common.enums.SubSystemSignEnum;
import com.jc.foundation.cache.CacheClient;
import com.jc.foundation.util.*;
import com.jc.system.applog.ActionLog;
import com.jc.system.common.util.Encodes;
import com.jc.system.content.service.IAttachService;
import com.jc.system.security.SystemSecurityUtils;
import com.jc.system.security.domain.Logo;
import com.jc.system.security.domain.Principal;

import com.jc.system.security.service.ILogoService;
import com.jc.system.security.util.UserUtils;
import com.jc.system.security.utils.RSASetting;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.cas.CasProperties;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.jc.foundation.domain.Attach;
import com.jc.foundation.exception.CustomException;
import com.jc.foundation.web.BaseController;
import com.jc.system.security.domain.User;
import com.jc.system.security.exception.OnlineCountException;
import com.jc.system.security.exception.UserDisabledException;
import com.jc.system.security.exception.UserIpException;
import com.jc.system.security.exception.UserLockedException;
import com.jc.system.security.exception.UserPasswordException;
import com.jc.system.security.license.UKBean;
import com.jc.system.security.license.VerificationSession;
import com.jc.system.security.service.IDepartmentService;
import com.jc.system.security.service.ISystemService;
import com.jc.system.security.service.IUserService;
import com.jc.system.util.SettingUtils;

/**
 * 登录操作
 * @author Administrator
 * @date 2020-07-01
 */
@Controller
public class LoginController extends BaseController {
    protected Logger logger = Logger.getLogger(this.getClass());
    @Autowired
    private ISystemService systemService;
    @Autowired
    private IUserService userService;

    @Autowired
	private IAttachService attachService;
    
    @RequestMapping(value = "loginForDesktop", method = RequestMethod.GET)
    public String loginForDesktop(HttpServletRequest request, HttpServletResponse response, Model model) throws CustomException{
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) { 
            systemService.logoutCallBack(request);
            subject.logout();
        }
        return "sys/loginForDesktop";
    }

    @RequestMapping(value = "rsaKey.action",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> generateRSAKey(HttpServletRequest request,String username,String captcha) {
        // 将公钥传到前端
        Map<String,Object> map = new HashMap<>();
        String verifyCodeExpected = (String)request.getSession().getAttribute(com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY);
        String valCaptcha = GlobalContext.getProperty("sys.valCaptcha");
        if("yes".equals(valCaptcha)){
            if(captcha == null){
                map.put("valResule","false");
                return map;
            }
            if(captcha.toLowerCase().equals(verifyCodeExpected.toLowerCase())){
                map.put("valResule","true");
            } else {
                map.put("valResule","false");
                return map;
            }
        } else {
            map.put("valResule","true");
        }
        try {
            Map rasMap= RSASetting.getRSAModulusAndExponent();
            map.putAll(rasMap);
            return map;
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
        return map;
    }

    /**
     * 管理登录
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpServletRequest request, HttpServletResponse response, Model model) {
        String kickout = request.getParameter("kickout");
        String timeout = request.getParameter("timeout");
        if(StringUtils.isNotEmpty(kickout)){
            request.getSession().setAttribute("kickoutMes", true);
        } else {
            if(request.getSession().getAttribute("kickoutMes") == null){
                if(StringUtils.isNotEmpty(timeout)) {
                    model.addAttribute("timeout", true);
                } else{
                    User userInfo = SystemSecurityUtils.getUser();
                    if(userInfo != null){
                        return "redirect:"+"/";
                    }
                }
            }
        }
        model.addAttribute("loginBody", "classic");
        String url = "sys/login";
        return url;
    }

    @RequestMapping(value = "login-{loginBody}", method = RequestMethod.GET)
    public String getLoginBody(@PathVariable("loginBody")String loginBody, HttpServletRequest request, HttpServletResponse response, Model model) {
        String url = "";
        if(!StringUtil.isEmpty(loginBody)){
            url =  "sys/login-"+loginBody;
        }else{
           url =  "sys/login-classic";
        }
        return url;
    }

    /**
     * 登录失败，真正登录的POST请求由Filter完成
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    @ActionLog(operateModelNm="用户登录",operateFuncNm="login",operateDescribe="用户登录请求失败")
    public String login(Model model, HttpServletRequest request) {
        String username = request.getParameter("username");
        String exceptionClassName = (String)request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
        if(UserDisabledException.class.getName().equals(exceptionClassName)){
            model.addAttribute(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_098"));
        } else if(UserLockedException.class.getName().equals(exceptionClassName)){
            model.addAttribute(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_099"));
        } else if(UserIpException.class.getName().equals(exceptionClassName)){
            model.addAttribute(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_100"));
        } else if(UserPasswordException.class.getName().equals(exceptionClassName)){
            if(username != null){
                try {
                    User user = new User();
                    user.setLoginName(username);
                    user = userService.get(user);
                    if(user.getStatus().equals(GlobalContext.USER_STATUS_2)){
                        model.addAttribute(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_099"));
                    } else {
                        int count = Integer.parseInt(SettingUtils.getSetting("maxErrorCount").toString()) - user.getWrongCount();
                        model.addAttribute(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_101")+ count + MessageUtils.getMessage("JC_SYS_102"));
                    }
                } catch (CustomException e) {
                    logger.error(e);
                }
            }
        } else if(UnknownAccountException.class.getName().equals(exceptionClassName)){
            model.addAttribute(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_103"));
        } else if(OnlineCountException.class.getName().equals(exceptionClassName)){
            model.addAttribute(GlobalContext.RESULT_ERRORMESSAGE, MessageUtils.getMessage("JC_SYS_124"));
        } else {
            User userInfo = SystemSecurityUtils.getUser();
            if(userInfo != null){
                return "redirect:"+"/";
            }
        }
        model.addAttribute("username", username);
        ILogoService logoService  =SpringContextHolder.getBean(ILogoService.class);
        Logo logo = new Logo();
        logo.setLogoFlag(1);
        String loginBody = "classic";
        try {
            logo = logoService.get(logo);
            loginBody = logo.getLogoSign();
        } catch (CustomException e) { 
        	logger.error(e);
        }
        model.addAttribute("loginBody", loginBody);
        return "sys/login";
    }

    @RequestMapping(value="index",method=RequestMethod.GET)
    public String portal(Model model,HttpServletRequest requests) throws Exception{
        User user = SystemSecurityUtils.getUser();
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        user.setPhoto(principal.getPhoto());
        
        Attach attach = new Attach();
		attach.setBusinessIdArray(new String[] {user.getId()});
		attach.setBusinessTable("tty_sys_user");
        List<Attach> attachList = attachService.queryAttachByBusinessIds(attach);
		if (attachList != null && attachList.size() > 0) {
			user.setAttachId(attachList.get(0).getId());
		}
		model.addAttribute("user",user);
        return "platform/index";
    }

    @RequestMapping(value="loginForNull",method=RequestMethod.GET)
    public String loginForNull(Model model,HttpServletRequest requests) throws Exception{
        User user = SystemSecurityUtils.getUser();
        model.addAttribute("user",user);
        return "sys/loginForNull";
    }

    @RequiresUser
    @RequestMapping(value = "",method=RequestMethod.GET)
    public void index(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
        if(principal != null){
            if(principal.getLoginState() == 0){
                systemService.loginCallBack(request);
                principal.setLoginState(1);
            }
            if(StringUtils.isEmpty(principal.getTheme())){
                request.getSession().setAttribute("theme", "");
            } else {
                request.getSession().setAttribute("theme", principal.getTheme());
            }
            if(StringUtils.isEmpty(principal.getManageHostPath())){
                request.getSession().setAttribute("manageHostPath", "");
            } else {
                request.getSession().setAttribute("manageHostPath", principal.getManageHostPath());
            }
        } else {
            request.getSession().setAttribute("theme", "");
        }
        //清空用户引用字典集合
        UserUtils.clearUserDic();

        if (GlobalContext.getProperty("subsystem.id") != null && !"".equals(GlobalContext.getProperty("subsystem.id"))&& !GlobalContext.isSysCenter()) {
            if (!StringUtils.isEmpty(request.getParameter("openUrl")) || !StringUtils.isEmpty(request.getParameter("pageid"))) {
                request.getSession().setAttribute("pageid",request.getParameter("pageid"));
                request.getSession().setAttribute("openUrl",request.getParameter("openUrl"));
                response.sendRedirect(request.getContextPath() + "/");
            }else{
                request.getRequestDispatcher("/transitMenu/manage.action").forward(request, response);
            }
        }else {
            if (!StringUtils.isEmpty(request.getParameter("openUrl")) || !StringUtils.isEmpty(request.getParameter("pageid"))) {
                request.getSession().setAttribute("pageid",request.getParameter("pageid"));
                request.getSession().setAttribute("openUrl",request.getParameter("openUrl"));
                response.sendRedirect(request.getContextPath() + "/sys/menu/manage.action");
            }else if ("yes".equals(GlobalContext.getProperty("platform.load-on"))) {
                request.getRequestDispatcher("/index").forward(request, response);
            } else {
                String subSystemMenuId = SubSystemSignEnum.getByCode(request.getParameter("s")).getMenuId();
                request.getRequestDispatcher("/sys/menu/manage.action?subSystemMenuId=" + subSystemMenuId).forward(request, response);
            }
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request,HttpServletResponse response) throws CustomException {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            systemService.logoutCallBack(request);
            subject.logout();
        }
        String casStart = GlobalContext.getProperty("cas.start");
		if(casStart==null || !"true".equals(casStart)){
			return "redirect:" + "/sys/login";
		}else{
			try {
                CasProperties casProperties = CasProperties.getInstance();
                String logoutUrl = casProperties.getLogoutUrl();
				response.sendRedirect(logoutUrl);
			} catch (IOException e) {
				logger.error(e);
			}
			return null;
		}
    }

    @RequestMapping(value = "logoutlog",method=RequestMethod.POST)
    public Map<String,Object> logoutlog(HttpServletRequest request) throws Exception{
        Map<String,Object> resultMap = new HashMap<>();
        systemService.logoutCallBack(request);
        resultMap.put("success", "true");
        return resultMap;
    }

	@RequestMapping(value = "licenseMes", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> licenseMes(HttpServletRequest request) throws CustomException {
    	Principal principal = (Principal) SecurityUtils.getSubject().getPrincipal();
    	String currentLoginName = principal.getLoginName();
    	String loginname = GlobalContext.getProperty("linceseMes.loginname");
    	List<String> loginnameList = new ArrayList<String>();
        
        if (loginname != null && !"".endsWith(loginname)) {
        	String[] loginnames = loginname.split(",");
        	loginnameList = Arrays.asList(loginnames);
        }
        
        if ((loginnameList != null && loginnameList.size() > 0 && loginnameList.contains(currentLoginName))) {
        	
        	List licenseList = VerificationSession.a();
        	
        	if (licenseList != null && licenseList.size() > 0) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                UKBean ukbean = (UKBean) licenseList.get(0);
                long day = DateUtils.subtractionDays(DateUtils.parseDate(ukbean.f()), new Date()) + 1;
                String specdate = GlobalContext.getProperty("linceseMes.specdate");
                String predate = GlobalContext.getProperty("linceseMes.predate");
                List<String> specdateList = new ArrayList<String>();
                  
                if (specdate != null && !"".endsWith(specdate)) {
                  	String[] specdates = specdate.split(",");
                   	specdateList = Arrays.asList(specdates);
                }
                   
                if ((specdateList != null && specdateList.size() > 0 && specdateList.contains(String.valueOf(day))) 
                  		|| (predate != null && !"".endsWith(predate) && day <= Long.valueOf(predate))) {
                    resultMap.put("state", true);
                    resultMap.put("mes", MessageUtils.getMessage("JC_SYS_125")+day+MessageUtils.getMessage("JC_SYS_126"));
                } else {
                    resultMap.put("state", false);
                }
                
                return resultMap;
            }
        }
    	
        return null;
    }
    
    @RequestMapping(value = "license.action",method=RequestMethod.GET)
	public String duressPassword(Model model, HttpServletRequest request) throws Exception {
    	model.addAttribute("licenseMes", request.getParameter("licenseMes"));
		return "sys/license/license";
	}
}
