package com.jc.system.security.filter;

import com.jc.foundation.util.StringUtil;
import org.apache.shiro.cas.CasProperties;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.cas.client.session.HashMapBackedSessionMappingStorage;
import org.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class KickoutSessionControlFilter extends AccessControlFilter {
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
		return false;
	}

	@SuppressWarnings("static-access")
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		if ("POST".equals(httpRequest.getMethod()) && !StringUtil.isEmpty(httpRequest.getParameter("kickoutRequest"))) {
			String logoutMessage = httpRequest.getParameter("kickoutRequest");
			String token = XmlUtils.getTextForElement(logoutMessage, "SessionIndex");
			if (CommonUtils.isNotBlank(token)) {
				HashMapBackedSessionMappingStorage sessionStorage = (HashMapBackedSessionMappingStorage) SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
				HttpSession session = sessionStorage.removeSessionByMappingId(token);

				if (session != null) {
					session.setAttribute("kickout", "true");
				}
			}
		}else if("GET".equals(httpRequest.getMethod()) && "true".equals(httpRequest.getParameter("kickout")) ){
			CasProperties casProperties = CasProperties.getInstance();
			String kickoutUrl = casProperties.getKickoutUrl();
			saveRequest(request);
			WebUtils.issueRedirect(request, response, kickoutUrl);
			return false;
		}
		return true;
	}
}
