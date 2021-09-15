package com.jc.system.security.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.cas.CasProperties;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jc.foundation.util.GlobalContext;

/**
 * @author Administrator
 * @date 2020-06-30
 */
public class JCLogoutFilter extends LogoutFilter{
	
	private static final Logger log = LoggerFactory.getLogger(LogoutFilter.class);

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        try {
            subject.logout();
        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        if(!"true".equals(GlobalContext.getProperty("cas.start"))) {
            issueRedirect(request, response, redirectUrl);
            return false;
        }else {
        	CasProperties casProperties = CasProperties.getInstance();
			String kickoutUrl = casProperties.getLogoutUrl();//getKickoutUrl();
			WebUtils.issueRedirect(request, response, kickoutUrl);
			return false;
        }
    }

}
