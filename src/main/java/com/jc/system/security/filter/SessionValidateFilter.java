package com.jc.system.security.filter;

import com.jc.digitalchina.util.DigitalContext;
import com.jc.foundation.util.GlobalContext;
import com.jc.foundation.util.StringUtil;
import com.jc.system.security.domain.Principal;
import com.jc.system.session.util.sessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.cas.CasProperties;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class SessionValidateFilter extends AccessControlFilter {

	protected Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		if (isLoginRequest(request, response)) {
			return true;
		} else {
			Subject subject = getSubject(request, response);
			Session session = subject.getSession();
			if (session.getAttribute("kickout") != null && subject.getPrincipal() != null) {
				return false;
			}

			if(subject.getPrincipal() != null){
				Principal principal = (Principal) subject.getPrincipal();

				if(sessionUtil.getLogoutUser(principal.getLoginName()) > 0) {
					String casStart = GlobalContext.getProperty("cas.start");
					if(casStart !=null && "true".equals(casStart)){
						try {
							HttpServletResponse hpresponse = (HttpServletResponse)response;
							CasProperties casProperties = CasProperties.getInstance();
							String relogoutUrl = casProperties.getLogoutUrl();
							hpresponse.sendRedirect(relogoutUrl);
						} catch (IOException e) {
							logger.error(e,e);
						}
					}
				}

				if(httpRequest.getSession().getAttribute("theme") == null){
					if (StringUtils.isNotEmpty(principal.getTheme())){
						httpRequest.getSession().setAttribute("theme",principal.getTheme());
					} else {
						httpRequest.getSession().setAttribute("theme","classics");
					}
				}
				if(httpRequest.getSession().getAttribute("color") == null) {
					if (StringUtils.isNotEmpty(principal.getColor())) {
						httpRequest.getSession().setAttribute("color", principal.getColor());
					} else {
						httpRequest.getSession().setAttribute("color", "blue");
					}
				}
				if(httpRequest.getSession().getAttribute("font") == null) {
					if (StringUtils.isNotEmpty(principal.getFont())) {
						httpRequest.getSession().setAttribute("font", principal.getFont());
					} else {
						httpRequest.getSession().setAttribute("font", "standard");
					}
				}
			}
			return subject.getPrincipal() != null;
		}
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
			Subject subject = getSubject(request, response);
		Session session = subject.getSession();
		if ("true".equals(request.getParameter("login-at"))) {
			saveRequestAndRedirectToLogin(request, response);
			return false;
		}
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String requestType = req.getHeader("X-Requested-With");
		if ("XMLHttpRequest".equals(requestType)) {
			resp.setHeader("sessionstatus", "timeout");
			session.setAttribute("timeout", "true");
		} else {
			CasProperties casProperties = CasProperties.getInstance();
			if (session.getAttribute("kickout") != null && subject.getPrincipal() != null) {
				if(!"true".equals(GlobalContext.getProperty("cas.start"))) {
					try {
						subject.logout();
						session.stop();
					} catch (Exception e) {
						logger.error(e);
					}
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.sendRedirect(httpRequest.getContextPath() + "/login?kickout=true");
					return false;
		        }else {
					String kickoutUrl = casProperties.getKickoutUrl();
					WebUtils.issueRedirect(request, response, kickoutUrl);
					return false;
		        }
			}else if (!StringUtil.isEmpty((String) session.getAttribute("timeout"))) {
				session.removeAttribute("timeout");
				saveRequest(request);
				String logoutUrl = casProperties.getTimeoutUrl();
				WebUtils.issueRedirect(request, response, logoutUrl);
			}else if(!"true".equals(GlobalContext.getProperty("cas.start"))){
				if (GlobalContext.getProperty("run_mode") != null && GlobalContext.getProperty("run_mode").equals("dev")) {
					saveRequestAndRedirectToLogin(request, response);
					WebUtils.saveRequest(request);
				} else {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse.sendRedirect(DigitalContext.systemUrl + "/auth/login?clientId=" +DigitalContext.clientId + "&backUrl=" + DigitalContext.myUrl + "/csmp/third/login.action");
				}
				return false;
			}else{
				if (!StringUtil.isEmpty(request.getParameter("ticket"))) {
					saveRequest(request);
					WebUtils.issueRedirect(request, response, casProperties.getLocalUrl()+"/login?ticket="+request.getParameter("ticket"));
				} else {
					if("true".equals(GlobalContext.getProperty("cas.start")) && !httpRequest.getRequestURI().equals(httpRequest.getContextPath()+"/")){
						String loginUrl = casProperties.getLoginUrl();
						WebUtils.issueRedirect(request, response, loginUrl);
					}else{
						saveRequestAndRedirectToLogin(request, response);
					}
				}
			}
		}
		
		return false;
	}

	@Override
    public String getLoginUrl() {
		CasProperties casProperties = CasProperties.getInstance();
		super.setLoginUrl(casProperties.getLoginUrl());
		return super.getLoginUrl();
	}

}
